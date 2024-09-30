package com.kh2rando.tracker.model.hints

import androidx.compose.runtime.Immutable
import com.kh2rando.tracker.model.gamestate.BaseGameStateApi
import com.kh2rando.tracker.model.gamestate.totalProgressionPointsEarned
import com.kh2rando.tracker.model.progress.ProgressCheckpoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

/**
 * Summarizes the current status of progression points and hints based on game progression.
 */
@Immutable
data class ProgressionSummary(
  val totalEarnedPoints: Int,
  val totalEarnedHints: Int,
  val nextHintEarnedPoints: Int,
  val nextHintRequiredPoints: Int,
) {

  companion object {

    val Unspecified = ProgressionSummary(
      totalEarnedPoints = 0,
      totalEarnedHints = 0,
      nextHintEarnedPoints = 0,
      nextHintRequiredPoints = 0
    )

  }

}

/**
 * Settings common to all [HintSystem]s that can use progression.
 */
@Serializable
class BasicProgressionSettings(
  /**
   * The number of points that should be awarded for each of the game's [ProgressCheckpoint]s.
   */
  val pointsToAwardByCheckpoint: Map<ProgressCheckpoint, Int>,
  /**
   * How many progression points it takes to reveal each respective progression hint.
   */
  val hintCosts: List<Int>,
  val worldCompleteBonus: Int,
  /**
   * Progression points to award for each Ansem Report acquired.
   */
  val reportBonus: Int,
  /**
   * Whether or not all remaining progression hints should be revealed when the game is finished.
   */
  // TODO: Need to consider doing this or not
  val revealAllWhenDone: Boolean,
) {

  /**
   * [ProgressionSummary] values based on settings and the current game state.
   */
  fun progressionSummaries(baseGameState: BaseGameStateApi): Flow<ProgressionSummary> {
    return baseGameState.totalProgressionPointsEarned.map(::summary)
  }

  private fun summary(totalEarnedPoints: Int): ProgressionSummary {
    var remainingPoints = totalEarnedPoints
    var hintsEarned = 0
    var nextHintCost = 0
    for (hintCost in hintCosts) {
      if (remainingPoints < hintCost) {
        // Can't afford this one
        nextHintCost = hintCost
        break
      } else {
        hintsEarned++
        remainingPoints -= hintCost
      }
    }
    return ProgressionSummary(
      totalEarnedPoints = totalEarnedPoints,
      totalEarnedHints = hintsEarned,
      nextHintEarnedPoints = remainingPoints,
      nextHintRequiredPoints = nextHintCost,
    )
  }

}
