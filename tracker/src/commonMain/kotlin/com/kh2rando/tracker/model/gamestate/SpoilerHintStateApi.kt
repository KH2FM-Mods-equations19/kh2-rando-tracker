package com.kh2rando.tracker.model.gamestate

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.hints.SpoilerHintSystem
import com.kh2rando.tracker.model.hints.checkAcquiredAllItems
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.locationsMap
import com.kh2rando.tracker.model.stateFlowOf
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

/**
 * [HintStateApi] for [SpoilerHintSystem].
 */
class SpoilerHintStateApi(
  private val baseGameState: BaseGameStateApi,
  private val hintSystem: SpoilerHintSystem,
) : HintStateApi {

  private val enabledLocations: Set<Location> = baseGameState.seed.settings.enabledLocations

  private val revealedStatusByLocation: ImmutableMap<Location, Flow<Boolean>> = run {
    when (hintSystem.revealMode) {
      SpoilerHintSystem.RevealMode.Always -> {
        enabledLocations.locationsMap { stateFlowOf(true) }
      }

      SpoilerHintSystem.RevealMode.Gradual -> {
        // TODO: Pretty much identical to JSmartee again
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
    }
  }

  override val revealedItemLists: ImmutableMap<Location, Flow<ImmutableList<ItemPrototype>>> = run {
    val allItemsByLocation = hintSystem.allItemsByLocation
    val revealedItemTypes = hintSystem.revealedItemTypes
    enabledLocations.locationsMap { location ->
      revealedStatusByLocation.getValue(location).map { revealed ->
        if (revealed) {
          allItemsByLocation[location].orEmpty().asSequence().filter { it in revealedItemTypes }.toImmutableList()
        } else {
          persistentListOf()
        }
      }
    }
  }

  override val counterStates: ImmutableMap<Location, Flow<LocationCounterState>> = run {
    val allItemsByLocation = hintSystem.allItemsByLocation
    val revealWorldCompletion = hintSystem.revealWorldCompletion

    enabledLocations.locationsMap { location ->
      if (location == Location.GardenOfAssemblage) {
        return@locationsMap stateFlowOf(LocationCounterState.None)
      }

      combine(
        revealedStatusByLocation.getValue(location),
        baseGameState.stateForLocation(location).acquiredItems,
      ) { revealed, locationAcquired ->
        if (revealed) {
          if (revealWorldCompletion) {
            val acquiredAll = checkAcquiredAllItems(
              acquiredItems = locationAcquired,
              allLocationItems = allItemsByLocation[location].orEmpty()
            )
            if (acquiredAll) LocationCounterState.Completed else LocationCounterState.None
          } else {
            LocationCounterState.None
          }
        } else {
          LocationCounterState.Unrevealed
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
    when (hintSystem.revealMode) {
      SpoilerHintSystem.RevealMode.Always -> {
        stateFlowOf(persistentSetOf())
      }

      SpoilerHintSystem.RevealMode.Gradual -> {
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
  }

}
