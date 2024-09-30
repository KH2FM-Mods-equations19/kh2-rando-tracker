package com.kh2rando.tracker.model.seed

import app.cash.turbine.test
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.gamestate.BaseGameState
import com.kh2rando.tracker.model.gamestate.totalProgressionPointsEarned
import com.kh2rando.tracker.model.hints.HintSystem
import com.kh2rando.tracker.model.hints.ShananasHintSystem
import com.kh2rando.tracker.model.item.AnsemReport
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ProgressionPointsEarnedTest {

  @Test
  fun `no report bonus is applied if bonus is 0`() {
    val gameState = BaseGameState(
      seed = testSeed(hintSystem = testHintSystem(reportBonus = 0))
    )
    runTest {
      gameState.totalProgressionPointsEarned.test {
        assertEquals(0, awaitItem())

        for (report in AnsemReport.entries) {
          gameState.acquireItemManually(report, Location.GardenOfAssemblage)
        }

        // No new emissions since the value hasn't changed
      }
    }
  }

  @Test
  fun `report bonus of 1 is applied per report`() {
    val gameState = BaseGameState(
      seed = testSeed(hintSystem = testHintSystem(reportBonus = 1))
    )

    runTest {
      gameState.totalProgressionPointsEarned.test {
        assertEquals(0, awaitItem())

        gameState.acquireItemManually(AnsemReport.Report9, Location.GardenOfAssemblage)
        assertEquals(expected = 1, actual = awaitItem())

        gameState.acquireItemManually(AnsemReport.Report3, Location.GardenOfAssemblage)
        gameState.acquireItemManually(AnsemReport.Report10, Location.GardenOfAssemblage)
        gameState.acquireItemManually(AnsemReport.Report1, Location.GardenOfAssemblage)
        skipItems(2)
        assertEquals(expected = 4, actual = awaitItem())
      }
    }
  }

  @Test
  fun `report bonus of more than 1 is applied per report`() {
    val gameState = BaseGameState(
      seed = testSeed(hintSystem = testHintSystem(reportBonus = 5))
    )

    runTest {
      gameState.totalProgressionPointsEarned.test {
        assertEquals(0, awaitItem())

        gameState.acquireItemManually(AnsemReport.Report9, Location.GardenOfAssemblage)
        assertEquals(expected = 5, actual = awaitItem())

        gameState.acquireItemManually(AnsemReport.Report3, Location.GardenOfAssemblage)
        gameState.acquireItemManually(AnsemReport.Report10, Location.GardenOfAssemblage)
        gameState.acquireItemManually(AnsemReport.Report1, Location.GardenOfAssemblage)
        skipItems(2)
        assertEquals(expected = 20, actual = awaitItem())
      }
    }
  }

  private fun testHintSystem(reportBonus: Int): HintSystem {
    val basicProgressionSettings = testProgressionSettings(reportBonus = reportBonus)
    return ShananasHintSystem(
      hints = emptyList(),
      allItemsByLocation = mapOf(Location.GardenOfAssemblage to AnsemReport.entries),
      progressionSettings = ShananasHintSystem.ProgressionSettings(
        basicProgressionSettings,
        locationRevealOrder = Location.entries,
      ),
    )
  }

}
