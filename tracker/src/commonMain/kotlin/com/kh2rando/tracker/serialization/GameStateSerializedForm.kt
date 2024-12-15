package com.kh2rando.tracker.serialization

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.gamestate.BaseGameState
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.objective.Objective
import com.kh2rando.tracker.model.progress.progressCheckpoints
import com.kh2rando.tracker.model.seed.RandomizerSeed
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.serialization.Serializable

@Serializable
data class GameStateSerializedForm(
  val seed: RandomizerSeed,
  val locations: List<LocationStateSerializedForm>,
  val revealedHints: List<HintInfo>,
  val ansemReportStrikes: List<Int>,
  val deaths: Int,
  val manuallyCompletedObjectives: Set<Objective>,
  val objectivesMarkedSecondary: Set<Objective>,
) {

  fun toBaseGameState(): BaseGameState {
    val gameState = BaseGameState(
      seed = seed,
      ansemReportStrikes = ansemReportStrikes.toImmutableList(),
      deaths = deaths,
      manuallyCompletedObjectives = manuallyCompletedObjectives.toImmutableSet(),
      objectivesMarkedSecondary = objectivesMarkedSecondary.toImmutableSet(),
    )

    for (serializedLocation in locations) {
      val location = serializedLocation.location

      for (item in serializedLocation.items) {
        gameState.acquireItemManually(item, location)
      }

      for (checkpointIndex in serializedLocation.progress) {
        val checkpoint = location.progressCheckpoints.firstOrNull { it.index == checkpointIndex }
        if (checkpoint != null) {
          gameState.recordProgress(checkpoint)
        }
      }

      gameState.addManualRejectionsForLocation(location, serializedLocation.manuallyRejectedItems)
      for (proof in Proof.entries) {
        when (proof) {
          in serializedLocation.possibleProofs -> gameState.markProofPossible(location, proof)
          in serializedLocation.impossibleProofs -> gameState.markProofImpossible(location, proof)
          else -> gameState.markProofUnknown(location, proof)
        }
      }
      gameState.setUserMarkForLocation(location, serializedLocation.userMark)
    }

    return gameState
  }

  @Serializable
  data class LocationStateSerializedForm(
    val location: Location,
    val items: List<ItemPrototype>,
    val progress: List<Int>,
    val manuallyRejectedItems: Set<ItemPrototype>,
    val possibleProofs: Set<Proof>,
    val impossibleProofs: Set<Proof>,
    val userMark: Int,
  )

}
