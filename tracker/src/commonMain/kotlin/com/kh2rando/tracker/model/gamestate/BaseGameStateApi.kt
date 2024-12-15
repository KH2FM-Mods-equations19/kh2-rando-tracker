package com.kh2rando.tracker.model.gamestate

import com.kh2rando.tracker.model.AutoTrackerSnapshot
import com.kh2rando.tracker.model.DriveFormsState
import com.kh2rando.tracker.model.GameId
import com.kh2rando.tracker.model.GrowthState
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.MusicState
import com.kh2rando.tracker.model.SoraState
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.item.UniqueItem
import com.kh2rando.tracker.model.objective.Objective
import com.kh2rando.tracker.model.progress.ProgressCheckpoint
import com.kh2rando.tracker.model.seed.RandomizerSeed
import com.kh2rando.tracker.model.stateFlowOf
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

/**
 * Read-only information about the base state of the game.
 */
interface BaseGameStateApi {

  /**
   * The currently loaded seed.
   */
  val seed: RandomizerSeed

  /**
   * All items that are trackable per the current seed's settings.
   */
  val allTrackableItems: ImmutableSet<UniqueItem>

  /**
   * All trackable items that have not yet been acquired.
   */
  val availableItems: StateFlow<ImmutableSet<UniqueItem>>

  /**
   * All trackable items that have been acquired.
   */
  val acquiredItems: StateFlow<ImmutableSet<UniqueItem>>

  /**
   * Sora's current state.
   */
  val soraStates: StateFlow<SoraState>

  /**
   * Drive forms' current state.
   */
  val driveFormsStates: StateFlow<DriveFormsState>

  /**
   * Growth abilities' current state.
   */
  val growthStates: StateFlow<GrowthState>

  /**
   * Game music's current state.
   */
  val musicStates: StateFlow<MusicState>

  /**
   * Number of deaths taken so far.
   */
  val deaths: StateFlow<Int>

  /**
   * The number of Lucky Emblems / Objective Completion Marks that have been acquired.
   */
  val acquiredEmblemCounts: StateFlow<Int>

  /**
   * The current location as detected by the auto-tracker.
   */
  val detectedLocations: StateFlow<Location?>

  /**
   * The currently selected location in the tracker UI by the user.
   */
  val userSelectedLocations: StateFlow<Location?>

  /**
   * The counts of incorrect placements ("strikes") of Ansem Reports.
   *
   * Note that index 0 in each list is report 1, index 1 is report 2, and so on.
   */
  val ansemReportStrikes: StateFlow<ImmutableList<Int>>

  /**
   * Objectives that have been manually marked as completed.
   */
  val manuallyCompletedObjectives: StateFlow<ImmutableSet<Objective>>

  /**
   * Objectives that have been marked with their secondary indication.
   */
  val objectivesMarkedSecondary: StateFlow<ImmutableSet<Objective>>

  /**
   * Returns a [LocationStateApi] giving access to some of the state of the given [location].
   */
  fun stateForLocation(location: Location): LocationStateApi

  /**
   * Takes a snapshot of the base game state for use by the auto-tracker.
   */
  fun takeAutoTrackerSnapshot(): AutoTrackerSnapshot

}

/**
 * Sets of all of the acquired [AnsemReport]s.
 */
val BaseGameStateApi.acquiredReportSets: Flow<ImmutableSet<AnsemReport>>
  get() {
    return acquiredItems.map { acquiredItems ->
      acquiredItems.asSequence()
        .mapNotNull { item -> item.prototype as? AnsemReport }
        .toImmutableSet()
    }
  }

/**
 * All completed [ProgressCheckpoint]s across all locations.
 */
val BaseGameStateApi.allCompletedProgressCheckpoints: Flow<ImmutableSet<ProgressCheckpoint>>
  get() {
    val flows = Location.entries.map { stateForLocation(it).completedProgressCheckpoints }
    return combine(flows) { completedCheckpointSets ->
      completedCheckpointSets.asSequence().flatten().toImmutableSet()
    }
  }

/**
 * The total number of progression points that have been earned, based on settings and the current game state.
 */
val BaseGameStateApi.totalProgressionPointsEarned: Flow<Int>
  get() {
    val hintSystem = seed.settings.hintSystem
    val basicSettings = hintSystem.basicProgressionSettings ?: return stateFlowOf(0)
    val pointsToAwardByCheckpoint = basicSettings.pointsToAwardByCheckpoint

    val progressCheckpointPoints = allCompletedProgressCheckpoints.map { completedCheckpoints ->
      completedCheckpoints.sumOf { checkpoint -> pointsToAwardByCheckpoint.getOrElse(checkpoint) { 0 } }
    }

    val reportBonus = basicSettings.reportBonus
    val reportBonusPoints: Flow<Int> = if (reportBonus > 0) {
      acquiredItems.map { acquiredItems ->
        val acquiredReportCount = acquiredItems.count { it.prototype is AnsemReport }
        acquiredReportCount * reportBonus
      }
    } else {
      stateFlowOf(0)
    }

    // TODO: Need to add world complete bonus. This will be difficult since it relies on knowing
    //   - what progress has been made _after_ a world is complete
    //   - that the world has been revealed
    //   - has to be able to "bank" the bonuses to award then once a reveal does happen

    return combine(progressCheckpointPoints, reportBonusPoints) { progressPoints, reportPoints ->
      progressPoints + reportPoints
    }
  }

/**
 * Allows for updating the base game state being tracked.
 */
interface BaseGameStateUpdateApi : BaseGameStateApi {

  /**
   * Attempts to add the item represented by [prototype] to the given [location]. Returns true if successful or false if
   * not.
   *
   * This variant differs from [acquireItemManually] in that it will not re-add an item that had been previously removed
   * by the user from [location].
   */
  fun acquireItemFromAutoTracking(prototype: ItemPrototype, location: Location): Boolean

  /**
   * Attempts to add the item represented by [prototype] to the given [location]. Returns true if successful or false if
   * not.
   */
  fun acquireItemManually(prototype: ItemPrototype, location: Location): Boolean

  /**
   * Attempts to add the item whose [GameId] is represented by [gameId] to the given [location]. Returns true if
   * successful or false if not.
   */
  fun tryAcquireItemManuallyByGameId(gameId: GameId, location: Location): Boolean

  /**
   * Returns the item from [location] back to the collection of available items.
   */
  fun rejectItemManually(item: UniqueItem, location: Location)

  /**
   * Marks all of the items in [prototypes] as having been manually rejected for the given [location].
   */
  fun addManualRejectionsForLocation(location: Location, prototypes: Collection<ItemPrototype>)

  /**
   * Records [checkpoint] as progress that has been completed.
   */
  fun recordProgress(checkpoint: ProgressCheckpoint)

  /**
   * Removes [checkpoint] as progress that has been completed.
   */
  fun removeProgress(checkpoint: ProgressCheckpoint)

  /**
   * Updates the selected location as needed in response to the location being manually toggled.
   */
  fun manuallyToggleLocation(location: Location)

  /**
   * Marks [proof] as definitely possible for [location].
   */
  fun markProofPossible(location: Location, proof: Proof)

  /**
   * Marks [proof] as definitely impossible for [location].
   */
  fun markProofImpossible(location: Location, proof: Proof)

  /**
   * Marks [proof] as unknown possibility for [location].
   */
  fun markProofUnknown(location: Location, proof: Proof)

  /**
   * Adjusts the user mark for [proof] forward or backward based on the [delta].
   */
  fun adjustUserProofMark(location: Location, proof: Proof, delta: Int)

  /**
   * Sets the user mark count for the given [location] to [userMark], unconditionally.
   */
  fun setUserMarkForLocation(location: Location, userMark: Int)

  /**
   * Adjusts the user mark count for the given [location] by a delta of [userMarkDelta].
   */
  fun adjustUserMarkForLocation(location: Location, userMarkDelta: Int)

  /**
   * Manually marks the objective as completed or not. If auto-tracking is on and an objective has already been tracked,
   * the objective will still display as completed. This is primarily in place for manually marking objectives when not
   * auto-tracking.
   */
  fun manuallyToggleObjective(objective: Objective)

  /**
   * Toggles the secondary indication for [objective].
   */
  fun toggleObjectiveSecondary(objective: Objective)

  /**
   * Determines what changes need to be applied based on two [AutoTrackerSnapshot]s, and applies those changes to this
   * game state.
   */
  fun applyAutoTrackerSnapshot(previousSnapshot: AutoTrackerSnapshot, newSnapshot: AutoTrackerSnapshot)

}
