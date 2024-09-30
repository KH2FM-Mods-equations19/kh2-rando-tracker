package com.kh2rando.tracker.model.gamestate

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.hints.JSmarteeHintSystem
import com.kh2rando.tracker.model.hints.LocationAuxiliaryHintInfo
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.locationsMap
import com.kh2rando.tracker.model.stateFlowOf
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

/**
 * [HintStateApi] for [JSmarteeHintSystem].
 */
class JSmarteeHintStateApi(
  private val baseGameState: BaseGameStateApi,
  private val hintSystem: JSmarteeHintSystem,
) : HintStateApi {

  private val enabledLocations: Set<Location> = baseGameState.seed.settings.enabledLocations

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
    val hintsByHintedLocation = hintSystem.hintsByHintedLocation

    enabledLocations.locationsMap { location ->
      if (location == Location.GardenOfAssemblage) {
        return@locationsMap stateFlowOf(LocationCounterState.None)
      }

      val hint = hintsByHintedLocation[location]
      if (hint == null) {
        stateFlowOf(LocationCounterState.Unrevealed)
      } else {
        combine(
          revealedStatusByLocation.getValue(location),
          baseGameState.stateForLocation(location).acquiredItems,
        ) { revealed, acquiredItems ->
          if (revealed) {
            val remainingCount = hint.count - acquiredItems.size
            if (remainingCount == 0) {
              // JSmartee hints don't include the locations of all the items (yet!).
              // We just assume if we have the exact number found then we're done.
              LocationCounterState.Completed
            } else {
              LocationCounterState.Revealed(remainingCount, adjustedByRevealedItems = false)
            }
          } else {
            LocationCounterState.Unrevealed
          }
        }
      }
    }
  }

  override val auxiliaryHintInfos: ImmutableMap<Location, Flow<LocationAuxiliaryHintInfo>> = run {
    val reportLocationsByReport = hintSystem.reportLocationsByReport
    if (reportLocationsByReport == null) {
      enabledLocations.locationsMap { stateFlowOf(LocationAuxiliaryHintInfo.NotApplicableToHintSystem) }
    } else {
      val hintsByHintedLocation = hintSystem.hintsByHintedLocation
      enabledLocations.locationsMap { location ->
        // Determine which report hints `location`
        val hintThatHintsLocation = hintsByHintedLocation[location] ?: run {
          return@locationsMap stateFlowOf(LocationAuxiliaryHintInfo.Blank)
        }
        val reportThatHintsLocation = AnsemReport.ofReportNumber(hintThatHintsLocation.hintOrReportNumber)

        // Determine the location of the report that hints `location`
        val reportLocation = reportLocationsByReport[reportThatHintsLocation] ?: run {
          return@locationsMap stateFlowOf(LocationAuxiliaryHintInfo.Blank)
        }

        combine(
          revealedStatusByLocation.getValue(location),
          revealedStatusByLocation.getValue(reportLocation)
        ) { locationRevealed, reportLocationRevealed ->
          if (locationRevealed && reportLocationRevealed) {
            LocationAuxiliaryHintInfo.HintedHint
          } else {
            LocationAuxiliaryHintInfo.Blank
          }
        }
      }
    }
  }

  override val revealedReportHintSets: Flow<ImmutableSet<HintInfo>> = run {
    baseGameState.acquiredReportSets.map { acquiredReports ->
      acquiredReports
        .asSequence()
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
