package com.kh2rando.tracker.model.gamestate

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.hints.ShananasHintSystem
import com.kh2rando.tracker.model.hints.checkAcquiredAllItems
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
 * [HintStateApi] for [ShananasHintSystem].
 */
class ShananasHintStateApi(
  private val baseGameState: BaseGameStateApi,
  private val hintSystem: ShananasHintSystem,
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

  override val counterStates: ImmutableMap<Location, Flow<LocationCounterState>> = run {
    val allItemsByLocation = hintSystem.allItemsByLocation

    enabledLocations.locationsMap { location ->
      if (location == Location.GardenOfAssemblage) {
        return@locationsMap stateFlowOf(LocationCounterState.None)
      }

      val allLocationItems = allItemsByLocation.getOrElse(location) { emptyList() }

      combine(
        revealedStatusByLocation.getValue(location),
        baseGameState.stateForLocation(location).acquiredItems,
      ) { revealed, acquiredItems ->
        if (!revealed) {
          LocationCounterState.Unrevealed
        } else if (checkAcquiredAllItems(acquiredItems, allLocationItems)) {
          LocationCounterState.Completed
        } else {
          LocationCounterState.None
        }
      }
    }
  }

  override val revealedReportHintSets: Flow<ImmutableSet<HintInfo>> = run {
    baseGameState.acquiredReportSets.map { acquiredReports ->
      acquiredReports.asSequence()
        .mapNotNull { hintSystem.hintInfoForAcquiredReport(report = it) }
        .toImmutableSet()
    }
  }

  override val revealedProgressionHintSets: Flow<ImmutableSet<HintInfo>> = run {
    val progressionSettings = hintSystem.progressionSettings
    if (progressionSettings == null) {
      stateFlowOf(persistentSetOf())
    } else {
      val hintsInRevealOrder = progressionSettings.locationRevealOrder.mapIndexed { index, location ->
        HintInfo.GeneralRevealed(hintOrReportNumber = index + 1, location = location)
      }
      progressionSettings.basicSettings.progressionSummaries(baseGameState).map { progressionSummary ->
        hintsInRevealOrder.asSequence().take(progressionSummary.totalEarnedHints).toImmutableSet()
      }
    }
  }

}
