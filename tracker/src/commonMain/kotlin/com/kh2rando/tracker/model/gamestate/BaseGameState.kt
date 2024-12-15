package com.kh2rando.tracker.model.gamestate

import com.kh2rando.tracker.model.AutoTrackerSnapshot
import com.kh2rando.tracker.model.DriveFormsState
import com.kh2rando.tracker.model.GameId
import com.kh2rando.tracker.model.GrowthState
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.MusicState
import com.kh2rando.tracker.model.SoraState
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.DriveForm
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.item.UniqueItem
import com.kh2rando.tracker.model.locationsMap
import com.kh2rando.tracker.model.objective.Objective
import com.kh2rando.tracker.model.progress.ProgressCheckpoint
import com.kh2rando.tracker.model.seed.LevelChecks.Companion.checksBetween
import com.kh2rando.tracker.model.seed.RandomizerSeed
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.minus
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Source of truth for the base game state.
 */
class BaseGameState(
  override val seed: RandomizerSeed,
  ansemReportStrikes: List<Int> = List(13) { 0 },
  deaths: Int = 0,
  manuallyCompletedObjectives: Set<Objective> = emptySet(),
  objectivesMarkedSecondary: Set<Objective> = emptySet(),
) : BaseGameStateApi, BaseGameStateUpdateApi {

  private val _soraState = MutableStateFlow(SoraState.Unspecified)
  override val soraStates: StateFlow<SoraState>
    get() = _soraState

  private val _driveFormsState = MutableStateFlow(DriveFormsState.Unspecified)
  override val driveFormsStates: StateFlow<DriveFormsState>
    get() = _driveFormsState

  private val _growthState = MutableStateFlow(GrowthState.Unspecified)
  override val growthStates: StateFlow<GrowthState>
    get() = _growthState

  private val _musicState = MutableStateFlow(MusicState.Unspecified)
  override val musicStates: StateFlow<MusicState>
    get() = _musicState

  private val _deaths = MutableStateFlow(deaths)
  override val deaths: StateFlow<Int>
    get() = _deaths

  private val _acquiredEmblemCount = MutableStateFlow(0)
  override val acquiredEmblemCounts: StateFlow<Int>
    get() = _acquiredEmblemCount

  private val _detectedLocation = MutableStateFlow<Location?>(null)
  override val detectedLocations: StateFlow<Location?>
    get() = _detectedLocation

  private val _userSelectedLocation = MutableStateFlow<Location?>(null)
  override val userSelectedLocations: StateFlow<Location?>
    get() = _userSelectedLocation

  private val locationStates: ImmutableMap<Location, LocationState> =
    Location.entries.locationsMap { LocationState(it) }
  private inline val Location.readableState: LocationStateApi
    get() = locationStates.getValue(this)
  private inline val Location.writableState: LocationState
    get() = locationStates.getValue(this)

  override val allTrackableItems: ImmutableSet<UniqueItem> = run {
    ItemPrototype.fullList.asSequence().mapNotNull { item ->
      if (item in seed.settings.trackableItems) UniqueItem(item) else null
    }.toImmutableSet()
  }

  private val _availableItems: MutableStateFlow<PersistentSet<UniqueItem>> =
    MutableStateFlow(allTrackableItems.toPersistentSet())
  override val availableItems: StateFlow<ImmutableSet<UniqueItem>>
    get() = _availableItems

  private val _acquiredItems: MutableStateFlow<PersistentSet<UniqueItem>> = MutableStateFlow(persistentSetOf())
  override val acquiredItems: StateFlow<ImmutableSet<UniqueItem>>
    get() = _acquiredItems

  private val _manuallyCompletedObjectives: MutableStateFlow<PersistentSet<Objective>> =
    MutableStateFlow(manuallyCompletedObjectives.toPersistentSet())
  override val manuallyCompletedObjectives: StateFlow<ImmutableSet<Objective>>
    get() = _manuallyCompletedObjectives

  private val _objectivesMarkedSecondary: MutableStateFlow<PersistentSet<Objective>> =
    MutableStateFlow(objectivesMarkedSecondary.toPersistentSet())
  override val objectivesMarkedSecondary: StateFlow<ImmutableSet<Objective>>
    get() = _objectivesMarkedSecondary

  private val _ansemReportStrikes: MutableStateFlow<PersistentList<Int>> =
    MutableStateFlow(ansemReportStrikes.toPersistentList())
  override val ansemReportStrikes: StateFlow<ImmutableList<Int>>
    get() = _ansemReportStrikes

  private var isSoraCurrentlyDead = false

  override fun stateForLocation(location: Location): LocationStateApi {
    return location.readableState
  }

  override fun takeAutoTrackerSnapshot(): AutoTrackerSnapshot {
    return AutoTrackerSnapshot(
      currentLocation = detectedLocations.value,
      soraState = soraStates.value,
      driveFormsState = driveFormsStates.value,
      growthState = growthStates.value,
      musicState = musicStates.value,
      inventory = acquiredItems.value.map { it.prototype },
      completedProgress = locationStates.values.flatMapTo(mutableSetOf()) { it.completedProgressCheckpoints.value },
      dead = isSoraCurrentlyDead,
      emblemCount = acquiredEmblemCounts.value,
      inCreations = false, // Shouldn't matter here, we don't compare against the previous value for this
    )
  }

  private fun tryAcquireItem(
    location: Location,
    skipIfManuallyRejected: Boolean,
    predicate: (UniqueItem) -> Boolean,
  ): Boolean {
    val availableSnapshot = availableItems.value
    val availableItem = availableSnapshot.firstOrNull(predicate)
    return if (availableItem == null) {
      false
    } else {
      val prototype = availableItem.prototype

      if (skipIfManuallyRejected && prototype in location.readableState.manuallyRejectedItems) {
        return false
      }

      if (prototype is AnsemReport) {
        val reportIndex = prototype.ordinal

        if (ansemReportStrikes.value[reportIndex] >= 3) {
          return false
        }

        if (!seed.settings.hintSystem.isValidReportLocation(location, prototype)) {
          _ansemReportStrikes.update { previous ->
            previous.mutate { builder ->
              builder[reportIndex] = builder[reportIndex] + 1
            }
          }
          return false
        }
      }

      _availableItems.update { previous -> previous - availableItem }

      location.writableState.acquireItem(availableItem)
      _acquiredItems.update { previous -> previous + availableItem }

      true
    }
  }

  override fun acquireItemFromAutoTracking(prototype: ItemPrototype, location: Location): Boolean {
    return tryAcquireItem(location, skipIfManuallyRejected = true) { it.prototype == prototype }
  }

  override fun acquireItemManually(prototype: ItemPrototype, location: Location): Boolean {
    return tryAcquireItem(location, skipIfManuallyRejected = false) { it.prototype == prototype }
  }

  override fun tryAcquireItemManuallyByGameId(gameId: GameId, location: Location): Boolean {
    return tryAcquireItem(location, skipIfManuallyRejected = false) { it.prototype.gameId == gameId }
  }

  override fun rejectItemManually(item: UniqueItem, location: Location) {
    location.writableState.rejectItemManually(item)
    _availableItems.update { previous -> previous + item }
    _acquiredItems.update { previous -> previous - item }
  }

  override fun addManualRejectionsForLocation(location: Location, prototypes: Collection<ItemPrototype>) {
    location.writableState.addManualRejections(prototypes)
  }

  override fun recordProgress(checkpoint: ProgressCheckpoint) {
    checkpoint.location.writableState.recordProgress(checkpoint)
  }

  override fun removeProgress(checkpoint: ProgressCheckpoint) {
    checkpoint.location.writableState.removeProgress(checkpoint)
  }

  override fun manuallyToggleLocation(location: Location) {
    _userSelectedLocation.update { previous ->
      if (previous == location) null else location
    }
  }

  override fun markProofPossible(location: Location, proof: Proof) {
    location.writableState.markProofPossible(proof)
  }

  override fun markProofImpossible(location: Location, proof: Proof) {
    location.writableState.markProofImpossible(proof)
  }

  override fun markProofUnknown(location: Location, proof: Proof) {
    location.writableState.markProofUnknown(proof)
  }

  override fun adjustUserProofMark(location: Location, proof: Proof, delta: Int) {
    location.writableState.adjustUserProofMark(proof, delta)
  }

  override fun setUserMarkForLocation(location: Location, userMark: Int) {
    location.writableState.setUserMark(userMark)
  }

  override fun adjustUserMarkForLocation(location: Location, userMarkDelta: Int) {
    location.writableState.adjustUserMark(userMarkDelta)
  }

  override fun manuallyToggleObjective(objective: Objective) {
    _manuallyCompletedObjectives.update { previous ->
      if (objective in previous) {
        previous - objective
      } else {
        previous + objective
      }
    }
  }

  override fun toggleObjectiveSecondary(objective: Objective) {
    _objectivesMarkedSecondary.update { previous ->
      if (objective in previous) {
        previous - objective
      } else {
        previous + objective
      }
    }
  }

  override fun applyAutoTrackerSnapshot(previousSnapshot: AutoTrackerSnapshot, newSnapshot: AutoTrackerSnapshot) {
    _detectedLocation.value = newSnapshot.currentLocation
    _soraState.value = newSnapshot.soraState
    _driveFormsState.value = newSnapshot.driveFormsState
    _growthState.value = newSnapshot.growthState
    _musicState.value = newSnapshot.musicState
    _acquiredEmblemCount.value = newSnapshot.emblemCount
    acquireNewItems(previousSnapshot, newSnapshot)
    recordProgress(newSnapshot)
    handleDeath(previousSnapshot, newSnapshot)
  }

  private fun acquireNewItems(previousSnapshot: AutoTrackerSnapshot, newSnapshot: AutoTrackerSnapshot) {
    val itemsThatAreNew = newSnapshot.inventory.toMutableList()
    for (previousItem in previousSnapshot.inventory) {
      itemsThatAreNew.remove(previousItem)
    }
    if (itemsThatAreNew.isEmpty()) {
      return
    }

    val levelChecks = seed.settings.levelChecks
    val newSoraState = newSnapshot.soraState
    val newFormState = newSnapshot.driveFormsState

    fun processLevelItems(
      previousLevel: Int,
      newLevel: Int,
      getLevelChecks: () -> List<List<ItemPrototype>>,
      locationForAcquiredItems: Location,
    ) {
      if (previousLevel == newLevel) {
        return
      }

      val expectedLevelChecks = getLevelChecks().checksBetween(previousLevel, newLevel).toMutableList()
      if (expectedLevelChecks.isEmpty()) {
        return
      }

      val newItemsIterator = itemsThatAreNew.listIterator()
      while (newItemsIterator.hasNext()) {
        val newItem = newItemsIterator.next()
        if (expectedLevelChecks.remove(newItem)) {
          acquireItemFromAutoTracking(newItem, locationForAcquiredItems)
          newItemsIterator.remove()
        }
      }
    }

    val previousSoraState = previousSnapshot.soraState
    if (previousSoraState != SoraState.Unspecified) {
      processLevelItems(
        previousLevel = previousSoraState.currentLevel,
        newLevel = newSoraState.currentLevel,
        getLevelChecks = { levelChecks.soraLevels.getValue(newSoraState.dreamWeapon) },
        locationForAcquiredItems = Location.SoraLevels,
      )
    }

    val previousFormState = previousSnapshot.driveFormsState
    if (previousFormState != DriveFormsState.Unspecified) {
      val formLevelChecks = levelChecks.formLevels
      processLevelItems(
        previousLevel = previousFormState.valorLevel,
        newLevel = newFormState.valorLevel,
        getLevelChecks = { formLevelChecks.getValue(DriveForm.ValorFormDummy) },
        locationForAcquiredItems = Location.DriveForms,
      )
      processLevelItems(
        previousLevel = previousFormState.wisdomLevel,
        newLevel = newFormState.wisdomLevel,
        getLevelChecks = { formLevelChecks.getValue(DriveForm.WisdomForm) },
        locationForAcquiredItems = Location.DriveForms,
      )
      processLevelItems(
        previousLevel = previousFormState.limitLevel,
        newLevel = newFormState.limitLevel,
        getLevelChecks = { formLevelChecks.getValue(DriveForm.LimitForm) },
        locationForAcquiredItems = Location.DriveForms,
      )
      processLevelItems(
        previousLevel = previousFormState.masterLevel,
        newLevel = newFormState.masterLevel,
        getLevelChecks = { formLevelChecks.getValue(DriveForm.MasterForm) },
        locationForAcquiredItems = Location.DriveForms,
      )
      processLevelItems(
        previousLevel = previousFormState.finalLevel,
        newLevel = newFormState.finalLevel,
        getLevelChecks = { formLevelChecks.getValue(DriveForm.FinalFormDummy) },
        locationForAcquiredItems = Location.DriveForms,
      )
    }

    // Adjust location to Creations if needed
    val locationForAcquiredItems = if (newSnapshot.inCreations) {
      Location.Creations
    } else {
      newSnapshot.currentLocation
    }

    if (locationForAcquiredItems != null) {
      for (newItem in itemsThatAreNew) {
        acquireItemFromAutoTracking(newItem, locationForAcquiredItems)
      }
    }
  }

  private fun recordProgress(newSnapshot: AutoTrackerSnapshot) {
    for (checkpoint in newSnapshot.completedProgress) {
      recordProgress(checkpoint)
    }
  }

  private fun handleDeath(previousSnapshot: AutoTrackerSnapshot, newSnapshot: AutoTrackerSnapshot) {
    val isDead = newSnapshot.dead
    if (previousSnapshot.dead == isDead) {
      // State hasn't changed - we're either still dead or still not dead
      return
    }

    // We only increment if the state has changed and the new state is dead
    if (isDead) {
      _deaths.update { previous -> previous + 1 }
    }

    // Record our new state for next time
    isSoraCurrentlyDead = isDead
  }

}
