package com.kh2rando.tracker.model.gamestate

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.combineMany
import com.kh2rando.tracker.model.hints.DisabledHintSystem
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.hints.JSmarteeHintSystem
import com.kh2rando.tracker.model.hints.PathHintSystem
import com.kh2rando.tracker.model.hints.PointsHintSystem
import com.kh2rando.tracker.model.hints.ShananasHintSystem
import com.kh2rando.tracker.model.hints.SpoilerHintSystem
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.VisitUnlock
import com.kh2rando.tracker.model.item.removeAcquired
import com.kh2rando.tracker.model.locationsMap
import com.kh2rando.tracker.model.preferences.TrackerPreferences
import com.kh2rando.tracker.serialization.GameStateSerializedForm
import com.kh2rando.tracker.ui.LocationUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

/**
 * Gives access to all of the game state represented by the tracker.
 */
interface FullGameStateApi : BaseGameStateApi, HintStateApi {

  /**
   * Each location's ongoing UI state.
   */
  val locationUiStates: ImmutableMap<Location, StateFlow<LocationUiState>>

  /**
   * Hints that have been revealed, in order of reveal.
   *
   * Should not include [HintInfo.JournalTextOnly] values.
   */
  val revealedPrimaryHintsInOrder: StateFlow<ImmutableList<HintInfo>>

  /**
   * Takes a snapshot of the game state as a [GameStateSerializedForm].
   */
  fun toGameStateSerializedForm(): GameStateSerializedForm

}

/**
 * The most recently-revealed [HintInfo], if any.
 */
val FullGameStateApi.mostRecentRevealedPrimaryHint: Flow<HintInfo?>
  get() = revealedPrimaryHintsInOrder.map { it.lastOrNull() }

/**
 * All items that have been revealed by hints, but not yet acquired.
 */
val FullGameStateApi.revealedButNotAcquiredItems: Flow<ImmutableList<ItemPrototype>>
  get() {
    return combine(allRevealedItems, acquiredItems) { revealed, acquired -> revealed.removeAcquired(acquired) }
  }

/**
 * Gives read and write access (as needed) to all of the game state represented by the tracker.
 */
class FullGameState(
  private val baseGameState: BaseGameState,
  private val hintState: HintStateApi,
  previouslyRevealedHints: ImmutableList<HintInfo>,
  scope: CoroutineScope,
  backgroundDispatcher: CoroutineDispatcher,
) : BaseGameStateUpdateApi by baseGameState, HintStateApi by hintState, FullGameStateApi {

  override val locationUiStates: ImmutableMap<Location, StateFlow<LocationUiState>> = run {
    val gameAcquiredItemsSets = acquiredItems
    val userSelectedLocations = userSelectedLocations
    val autoDetectedLocations = detectedLocations

    val unlockItemsByLocation = VisitUnlock.entries.associateBy { it.associatedLocation }

    seed.settings.enabledLocations.locationsMap { location ->
      val locationState = stateForLocation(location)
      val visitCount = location.visitCount

      combineMany(
        gameAcquiredItemsSets,
        userSelectedLocations,
        autoDetectedLocations,
        locationState.acquiredItems,
        revealedItemLists.getValue(location),
        counterStates.getValue(location),
        locationState.completedProgressCheckpoints,
        locationState.userProofMarks,
        locationState.userMarkCounts,
        auxiliaryHintInfos.getValue(location),
      ) {
          gameAcquiredItems,
          userSelectedLocation,
          autoDetectedLocation,
          locationAcquiredItems,
          locationRevealedItems,
          counterState,
          completedProgressCheckpoints,
          userProofMarks,
          userMarkCount,
          auxiliaryHintInfo,
        ->
        LocationUiState(
          gameStateUpdater = this,
          location = location,
          isUserSelectedLocation = location == userSelectedLocation,
          isAutoDetectedLocation = location == autoDetectedLocation,
          acquiredItems = locationAcquiredItems,
          revealedItems = locationRevealedItems,
          lockedVisitCount = if (visitCount == 0) {
            0
          } else {
            val unlockItem = unlockItemsByLocation[location]
            val acquiredUnlocks = if (unlockItem == null) {
              0
            } else {
              gameAcquiredItems.count { it.prototype == unlockItem }
            }
            visitCount - acquiredUnlocks
          },
          counterState = counterState,
          completedProgressCheckpoints = completedProgressCheckpoints,
          userProofMarks = userProofMarks,
          userMarkCount = userMarkCount,
          auxiliaryHintInfo = auxiliaryHintInfo
        )
      }
        .flowOn(backgroundDispatcher)
        .stateIn(scope, SharingStarted.Eagerly, LocationUiState(gameStateUpdater = this, location))
    }
  }

  // TODO: Would like some more testing of this, both automated and just overall
  override val revealedPrimaryHintsInOrder: StateFlow<ImmutableList<HintInfo>> = run {
    combine(revealedReportHintSets, revealedProgressionHintSets) { reports, progression ->
      persistentSetOf<HintInfo>().mutate { builder ->
        reports.filterNotTo(builder) { it is HintInfo.JournalTextOnly }
        progression.filterNotTo(builder) { it is HintInfo.JournalTextOnly }
      }.toImmutableSet()
    }
      .onStart { emit(previouslyRevealedHints.toImmutableSet()) } // Emit any hints we've already seen first
      .scan(persistentSetOf<HintInfo>()) { acc, value -> acc + value } // Use Sets here to make sure things are only added once
      .drop(1) // Skip the first emission where we won't have a previous set to compare to
      .map { it.toImmutableList() } // Now present it as a list since order explicitly matters here
      .stateIn(scope, SharingStarted.Eagerly, previouslyRevealedHints)
  }

  override fun toGameStateSerializedForm(): GameStateSerializedForm {
    return GameStateSerializedForm(
      seed = seed,
      locations = Location.entries.map { location ->
        val locationState = stateForLocation(location)
        GameStateSerializedForm.LocationStateSerializedForm(
          location = location,
          items = locationState.acquiredItems.value.map { it.prototype },
          progress = locationState.completedProgressCheckpoints.value.map { it.index },
          manuallyRejectedItems = locationState.manuallyRejectedItems,
          userProofMarks = locationState.userProofMarks.value,
          userMark = locationState.userMarkCounts.value,
        )
      },
      revealedHints = revealedPrimaryHintsInOrder.value,
      ansemReportStrikes = ansemReportStrikes.value,
      deaths = deaths.value,
      manuallyCompletedObjectives = manuallyCompletedObjectives.value,
      objectivesMarkedSecondary = objectivesMarkedSecondary.value,
    )
  }

}

/**
 * Attempts to simplify the effort needed to build a [FullGameState] object.
 */
class GameStateFactory(
  private val preferences: TrackerPreferences,
  private val scope: CoroutineScope,
  private val backgroundDispatcher: CoroutineDispatcher,
) {

  /**
   * Creates a [FullGameState] from a [BaseGameState] and other information.
   */
  fun create(
    baseGameState: BaseGameState,
    previouslyRevealedHints: ImmutableList<HintInfo> = persistentListOf(),
  ) : FullGameState {
    val hintStateApi = when (val hintSystem = baseGameState.seed.settings.hintSystem) {
      is DisabledHintSystem -> {
        DisabledHintStateApi(baseGameState, hintSystem)
      }

      is JSmarteeHintSystem -> {
        JSmarteeHintStateApi(baseGameState, hintSystem)
      }

      is PathHintSystem -> {
        PathHintStateApi(baseGameState, hintSystem)
      }

      is PointsHintSystem -> {
        PointsHintStateApi(baseGameState, hintSystem, preferences)
      }

      is ShananasHintSystem -> {
        ShananasHintStateApi(baseGameState, hintSystem)
      }

      is SpoilerHintSystem -> {
        SpoilerHintStateApi(baseGameState, hintSystem)
      }
    }
    return FullGameState(
      baseGameState = baseGameState,
      hintState = hintStateApi,
      previouslyRevealedHints = previouslyRevealedHints,
      scope = scope,
      backgroundDispatcher = backgroundDispatcher
    )
  }

}
