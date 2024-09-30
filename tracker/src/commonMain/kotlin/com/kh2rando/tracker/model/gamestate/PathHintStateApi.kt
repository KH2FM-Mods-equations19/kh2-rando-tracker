package com.kh2rando.tracker.model.gamestate

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.hints.LocationAuxiliaryHintInfo
import com.kh2rando.tracker.model.hints.PathHintSystem
import com.kh2rando.tracker.model.hints.checkAcquiredAllItems
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.locationsMap
import com.kh2rando.tracker.model.stateFlowOf
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * [HintStateApi] for [PathHintSystem].
 */
class PathHintStateApi(
  private val baseGameState: BaseGameStateApi,
  private val hintSystem: PathHintSystem,
) : HintStateApi {

  private val enabledLocations: Set<Location> = baseGameState.seed.settings.enabledLocations

  // TODO: This is basically identical to JSmartee
  private val revealedStatusByLocation: ImmutableMap<Location, Flow<Boolean>> = run {
    val hintsByHintedLocation = hintSystem.hintsByHintedLocation
    val progressionSettings = hintSystem.progressionSettings
    if (progressionSettings == null) {
      val acquiredReportSets = baseGameState.acquiredReportSets
      enabledLocations.locationsMap { location ->
        val relevantHint = hintsByHintedLocation[location]
        if (relevantHint == null) {
          stateFlowOf(false)
        } else {
          val report = AnsemReport.ofReportNumber(relevantHint.hintOrReportNumber)
          acquiredReportSets.map { acquiredReports -> report in acquiredReports }
        }
      }
    } else {
      val progressionSummaries = progressionSettings.basicSettings.progressionSummaries(baseGameState)
      enabledLocations.locationsMap { location ->
        val relevantHint = hintsByHintedLocation[location]
        if (relevantHint == null) {
          stateFlowOf(false)
        } else {
          progressionSummaries.map { progressionSummary ->
            relevantHint.hintOrReportNumber <= progressionSummary.totalEarnedHints
          }
        }
      }
    }
  }

  override val counterStates: ImmutableMap<Location, Flow<LocationCounterState>> = run {
    val allItemsByLocation = hintSystem.allItemsByLocation

    enabledLocations.locationsMap { location ->
      if (location == Location.GardenOfAssemblage) {
        return@locationsMap stateFlowOf(LocationCounterState.None)
      }

      val allLocationItems = allItemsByLocation.getOrElse(location) { emptyList() }
      if (allLocationItems.isEmpty()) {
        stateFlowOf(LocationCounterState.Completed)
      } else {
        baseGameState.stateForLocation(location).acquiredItems.map { acquiredItems ->
          val acquiredAll = checkAcquiredAllItems(acquiredItems = acquiredItems, allLocationItems = allLocationItems)
          if (acquiredAll) LocationCounterState.Completed else LocationCounterState.None
        }
      }
    }
  }

  override val auxiliaryHintInfos: ImmutableMap<Location, Flow<LocationAuxiliaryHintInfo>> = run {
    val hintsByHintedLocation = hintSystem.hintsByHintedLocation
    enabledLocations.locationsMap { location ->
      val hint = hintsByHintedLocation[location]
      if (hint == null) {
        stateFlowOf(LocationAuxiliaryHintInfo.Blank)
      } else {
        revealedStatusByLocation.getValue(location).map { revealed ->
          if (revealed) hint else LocationAuxiliaryHintInfo.Blank
        }
      }
    }
  }

  override val revealedReportHintSets: Flow<ImmutableSet<HintInfo>> = run {
    baseGameState.acquiredReportSets.map { acquiredReports ->
      acquiredReports.asSequence()
        .mapNotNull { hintSystem.hintInfoForHintOrReport(it.reportNumber) }
        .toImmutableSet()
    }
  }

  override val revealedProgressionHintSets: Flow<ImmutableSet<HintInfo>> = run {
    val progressionSettings = hintSystem.progressionSettings
    if (progressionSettings == null) {
      stateFlowOf(persistentSetOf())
    } else {
      val hintsInRevealOrder = hintSystem.hintInfoByHintOrReportNumber.values.sortedBy { it.hintOrReportNumber }
      progressionSettings.basicSettings.progressionSummaries(baseGameState).map { progressionSummary ->
        hintsInRevealOrder.asSequence().take(progressionSummary.totalEarnedHints).toImmutableSet()
      }
    }
  }

}
