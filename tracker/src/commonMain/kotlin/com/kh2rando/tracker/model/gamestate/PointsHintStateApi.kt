package com.kh2rando.tracker.model.gamestate

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.hints.LocationAuxiliaryHintInfo
import com.kh2rando.tracker.model.hints.PointsHintSystem
import com.kh2rando.tracker.model.hints.checkAcquiredAllItems
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.removeAcquired
import com.kh2rando.tracker.model.locationsMap
import com.kh2rando.tracker.model.preferences.TrackerPreferences
import com.kh2rando.tracker.model.stateFlowOf
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

/**
 * [HintStateApi] for [PointsHintSystem].
 */
class PointsHintStateApi(
  private val baseGameState: BaseGameStateApi,
  private val hintSystem: PointsHintSystem,
  private val preferences: TrackerPreferences,
) : HintStateApi {

  private val enabledLocations: Set<Location> = baseGameState.seed.settings.enabledLocations

  private val revealedStatusByLocation: ImmutableMap<Location, Flow<Boolean>> = run {
    val progressionSettings = hintSystem.progressionSettings
    if (progressionSettings == null) {
      enabledLocations.locationsMap { stateFlowOf(true) }
    } else {
      enabledLocations.locationsMap { location ->
        val locationIndex = progressionSettings.locationRevealOrder.indexOf(location)
        if (locationIndex == -1) {
          stateFlowOf(false)
        } else {
          progressionSettings.basicSettings.progressionSummaries(baseGameState).map { progressionSummary ->
            locationIndex + 1 <= progressionSummary.totalEarnedHints
          }
        }
      }
    }
  }

  override val revealedItemLists: ImmutableMap<Location, Flow<ImmutableList<ItemPrototype>>> = run {
    val acquiredReportSets = baseGameState.acquiredReportSets
    enabledLocations.locationsMap { location ->
      val relevantHints = hintSystem.hintsByHintedLocation[location].orEmpty()
      if (relevantHints.isEmpty()) {
        stateFlowOf(persistentListOf())
      } else {
        acquiredReportSets.map { acquiredReports ->
          relevantHints.asSequence().mapNotNull { hint ->
            val report = AnsemReport.ofReportNumber(hint.hintOrReportNumber)
            if (report in acquiredReports) hint.item else null
          }.toImmutableList()
        }
      }
    }
  }

  override val counterStates: ImmutableMap<Location, Flow<LocationCounterState>> = run {
    val allItemsByLocation = hintSystem.allItemsByLocation
    val pointValuesByPrototype = hintSystem.pointValuesByPrototype
    val totalPointsByLocation = hintSystem.totalPointsByLocation

    enabledLocations.locationsMap { location ->
      if (location == Location.GardenOfAssemblage) {
        return@locationsMap stateFlowOf(LocationCounterState.None)
      }

      val allLocationItems = allItemsByLocation.getOrElse(location) { emptyList() }
      val locationTotalPoints = totalPointsByLocation.getOrElse(location) { 0 }

      combine(
        revealedStatusByLocation.getValue(location),
        baseGameState.stateForLocation(location).acquiredItems,
        revealedItemLists.getValue(location),
        preferences.pointsAutoMath.values
      ) { revealed, acquiredItems, revealedItems, pointsAutoMath ->
        if (!revealed) {
          return@combine LocationCounterState.Unrevealed
        }

        val acquiredAll = checkAcquiredAllItems(acquiredItems, allLocationItems)
        if (acquiredAll) {
          return@combine LocationCounterState.Completed
        }

        val locationAcquiredPoints = acquiredItems.sumOf { item ->
          pointValuesByPrototype.getOrElse(item.prototype) { 0 }
        }

        if (pointsAutoMath) {
          val revealedButNotAcquiredPoints = revealedItems.removeAcquired(acquiredItems).sumOf { item ->
            pointValuesByPrototype.getOrElse(item) { 0 }
          }
          LocationCounterState.Revealed(
            value = locationTotalPoints - locationAcquiredPoints - revealedButNotAcquiredPoints,
            adjustedByRevealedItems = revealedButNotAcquiredPoints > 0
          )
        } else {
          val remainingPoints = locationTotalPoints - locationAcquiredPoints
          LocationCounterState.Revealed(value = remainingPoints, adjustedByRevealedItems = false)
        }
      }
    }
  }

  override val auxiliaryHintInfos: ImmutableMap<Location, Flow<LocationAuxiliaryHintInfo>> = run {
    counterStates.mapValues { (_, counterStates) ->
      counterStates.map { counterState ->
        when (counterState) {
          LocationCounterState.None, LocationCounterState.Completed, LocationCounterState.Unrevealed -> {
            LocationAuxiliaryHintInfo.Blank
          }

          is LocationCounterState.Revealed -> {
            if (counterState.adjustedByRevealedItems) {
              LocationAuxiliaryHintInfo.CountAdjustedByRevealedItems
            } else {
              LocationAuxiliaryHintInfo.Blank
            }
          }
        }
      }
    }.toImmutableMap()
  }

  override val revealedReportHintSets: Flow<ImmutableSet<HintInfo>> = run {
    baseGameState.acquiredReportSets.map { acquiredReports ->
      acquiredReports.asSequence()
        .mapNotNull { hintSystem.hintInfoForReport(it.reportNumber) }
        .toImmutableSet()
    }
  }

  override val revealedProgressionHintSets: Flow<ImmutableSet<HintInfo>> = run {
    val progressionSettings = hintSystem.progressionSettings
    if (progressionSettings == null) {
      stateFlowOf(persistentSetOf())
    } else {
      val totalPointsByLocation = hintSystem.totalPointsByLocation
      val pointCountsInRevealOrder = progressionSettings.locationRevealOrder.mapIndexed { index, location ->
        HintInfo.PointsCount(
          hintOrReportNumber = index + 1,
          location = location,
          points = totalPointsByLocation.getOrElse(location) { 0 }
        )
      }
      progressionSettings.basicSettings.progressionSummaries(baseGameState).map { progressionSummary ->
        pointCountsInRevealOrder.asSequence().take(progressionSummary.totalEarnedHints).toImmutableSet()
      }
    }
  }

}
