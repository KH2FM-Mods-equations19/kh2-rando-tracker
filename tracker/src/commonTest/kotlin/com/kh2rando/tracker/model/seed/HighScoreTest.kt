package com.kh2rando.tracker.model.seed

import app.cash.turbine.turbineScope
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.gamestate.HighScoreState
import com.kh2rando.tracker.model.gamestate.highScoreStates
import com.kh2rando.tracker.model.hints.HighScoreData
import com.kh2rando.tracker.model.hints.HighScoreGoal
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.DriveForm
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.Magic
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.item.SummonCharm
import com.kh2rando.tracker.model.item.TornPage
import com.kh2rando.tracker.model.progress.AgrabahProgress
import com.kh2rando.tracker.model.progress.BeastsCastleProgress
import com.kh2rando.tracker.model.progress.WorldThatNeverWasProgress
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Test
import kotlin.test.assertEquals

class HighScoreTest {

  @get:Rule
  val temporaryFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

  private val preferences = testPreferences(temporaryFolder)

  @Test
  fun `high score with no hint system`() {
    val highScoreData = HighScoreData(
      pointsByItem = samplePointDistribution(),
      pointsByGoal = sampleGoalDistribution(),
    )
    runTest {
      val seed = testSeed(highScoreData = highScoreData)
      val testGameState = testGameState(seed, preferences)
      turbineScope {
        val highScoreStates = testGameState.highScoreStates.distinctUntilChanged().testIn(backgroundScope)

        var expectedState = HighScoreState()
        assertEquals(expectedState, highScoreStates.awaitItem())

        // GoA cure shouldn't trigger a score update because it's a starting item
        testGameState.acquireItemManually(Magic.Cure, Location.GardenOfAssemblage)
        testGameState.acquireItemManually(Magic.Cure, Location.Agrabah)
        expectedState = expectedState.copy(individualItems = 7)
        assertEquals(expectedState, highScoreStates.awaitItem())

        testGameState.acquireItemManually(Magic.Blizzard, Location.LandOfDragons)
        expectedState = expectedState.copy(individualItems = 14)
        assertEquals(expectedState, highScoreStates.awaitItem())

        testGameState.acquireItemManually(Magic.Cure, Location.TwilightTown)
        // Two events, one for the magic and one for the magic set
        highScoreStates.skipItems(1)
        expectedState = expectedState.copy(individualItems = 21, inventoryBonuses = 5)
        assertEquals(expectedState, highScoreStates.awaitItem())

        testGameState.recordDeath()
        expectedState = expectedState.copy(deaths = -5)
        assertEquals(expectedState, highScoreStates.awaitItem())

        testGameState.recordProgress(BeastsCastleProgress.Thresholder)
        expectedState = expectedState.copy(bossesDefeated = 15)
        assertEquals(expectedState, highScoreStates.awaitItem())

        // Twin lords is worth double
        testGameState.recordProgress(AgrabahProgress.Lords)
        expectedState = expectedState.copy(bossesDefeated = 45)
        assertEquals(expectedState, highScoreStates.awaitItem())

        testGameState.recordProgress(AgrabahProgress.Lexaeus)
        expectedState = expectedState.copy(bossesDefeated = 70)
        assertEquals(expectedState, highScoreStates.awaitItem())

        testGameState.recordProgress(AgrabahProgress.LexaeusData)
        expectedState = expectedState.copy(bossesDefeated = 120)
        assertEquals(expectedState, highScoreStates.awaitItem())

        testGameState.recordDeath()
        expectedState = expectedState.copy(deaths = -10)
        assertEquals(expectedState, highScoreStates.awaitItem())

        testGameState.recordProgress(WorldThatNeverWasProgress.FinalXemnas)
        expectedState = expectedState.copy(bossesDefeated = 220)

        val finalState = highScoreStates.awaitItem()
        assertEquals(expectedState, finalState)

        // 21 individual items + 5 set bonus + 220 bosses defeated = 246, then - 10 deaths = 236
        assertEquals(236, finalState.total)
      }
    }
  }

  companion object {

    /**
     * Commonly used point distribution for use in tests.
     */
    private fun samplePointDistribution(): Map<ItemPrototype, Int> {
      return buildMap {
        AnsemReport.entries.forEach { put(it, 3) }
        Magic.entries.forEach { put(it, 7) }
        put(TornPage, 5)
        DriveForm.entries.forEach { put(it, 9) }
        SummonCharm.entries.forEach { put(it, 5) }
        Proof.entries.forEach { put(it, 5) }
      }
    }

    /**
     * Commonly used point distribution for goals for use in tests.
     */
    private fun sampleGoalDistribution(): Map<HighScoreGoal, Int> {
      return buildMap {
        put(HighScoreGoal.AnsemReportSet, 10)
        put(HighScoreGoal.MagicSet, 5)
        put(HighScoreGoal.WorldCompletion, 20)
        put(HighScoreGoal.FinalXemnasDefeated, 100)
        put(HighScoreGoal.DataBossDefeated, 50)
        put(HighScoreGoal.AbsentSilhouetteDefeated, 25)
        put(HighScoreGoal.NormalBossDefeated, 15)
        put(HighScoreGoal.DeathPenalty, -5)
        put(HighScoreGoal.ProofSet, 75)
        put(HighScoreGoal.DriveFormSet, 60)
      }
    }

  }

}
