package com.kh2rando.tracker.model.gamestate

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.hints.LocationAuxiliaryHintInfo
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.locationsMap
import com.kh2rando.tracker.model.stateFlowOf
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Information about the state of hints.
 */
interface HintStateApi {

  /**
   * Lists of items in each location that have been revealed.
   */
  val revealedItemLists: ImmutableMap<Location, Flow<ImmutableList<ItemPrototype>>>
    get() = Location.entries.locationsMap { stateFlowOf(persistentListOf()) }

  /**
   * The [LocationCounterState] for each location.
   */
  val counterStates: ImmutableMap<Location, Flow<LocationCounterState>>
    get() = Location.entries.locationsMap { stateFlowOf(LocationCounterState.None) }

  /**
   * Any [LocationAuxiliaryHintInfo] for each location.
   */
  val auxiliaryHintInfos: ImmutableMap<Location, Flow<LocationAuxiliaryHintInfo>>
    get() = Location.entries.locationsMap { stateFlowOf(LocationAuxiliaryHintInfo.NotApplicableToHintSystem) }

  /**
   * Hints that have been revealed by Ansem Reports.
   */
  val revealedReportHintSets: Flow<ImmutableSet<HintInfo>>
    get() = stateFlowOf(persistentSetOf())

  /**
   * Hints that have been revealed by game progression.
   */
  val revealedProgressionHintSets: Flow<ImmutableSet<HintInfo>>
    get() = stateFlowOf(persistentSetOf())

}

/**
 * All items revealed across all locations.
 */
val HintStateApi.allRevealedItems: Flow<ImmutableList<ItemPrototype>>
  get() {
    return combine(revealedItemLists.values) { lists ->
      lists.asSequence().flatten().toImmutableList()
    }
  }
