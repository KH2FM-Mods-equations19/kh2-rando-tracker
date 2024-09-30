package com.kh2rando.tracker.model.gamestate

import com.kh2rando.tracker.model.hints.DisabledHintSystem
import com.kh2rando.tracker.model.hints.HintInfo
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * [HintStateApi] for [DisabledHintSystem].
 */
class DisabledHintStateApi(
  private val baseGameState: BaseGameStateApi,
  private val hintSystem: DisabledHintSystem,
) : HintStateApi {

  override val revealedReportHintSets: Flow<ImmutableSet<HintInfo>> = run {
    baseGameState.acquiredReportSets.map { acquiredReports ->
      acquiredReports
        .asSequence()
        .mapNotNull { hintSystem.hintInfoForAcquiredReport(report = it) }
        .toImmutableSet()
    }
  }

}
