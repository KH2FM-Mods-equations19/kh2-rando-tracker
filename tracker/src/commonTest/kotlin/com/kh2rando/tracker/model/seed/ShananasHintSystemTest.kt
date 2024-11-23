package com.kh2rando.tracker.model.seed

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.hints.ShananasHint
import com.kh2rando.tracker.model.hints.ShananasHintSystem
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.Magic
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.item.SummonCharm
import com.kh2rando.tracker.model.progress.AtlanticaProgress
import com.kh2rando.tracker.model.progress.TwilightTownProgress
import com.kh2rando.tracker.model.progress.WorldThatNeverWasProgress
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ShananasHintSystemTest {

  @get:Rule
  val temporaryFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

  private val preferences = testPreferences(temporaryFolder)

  @Test
  fun isValidReportLocation() {
    // Note: for Shananas hints it doesn't matter whether it's progression or not for isValidReportLocation()
    val hintSystem = ShananasHintSystem(
      hints = emptyList(),
      allItemsByLocation = mapOf(
        Location.OlympusColiseum to listOf(AnsemReport.Report6),
        Location.DisneyCastle to listOf(AnsemReport.Report11),
      ),
      progressionSettings = null,
    )
    assertTrue(hintSystem.isValidReportLocation(Location.OlympusColiseum, AnsemReport.Report6))
    assertFalse(hintSystem.isValidReportLocation(Location.SoraLevels, AnsemReport.Report6))
    assertFalse(hintSystem.isValidReportLocation(Location.WorldThatNeverWas, AnsemReport.Report11))
    assertTrue(hintSystem.isValidReportLocation(Location.DisneyCastle, AnsemReport.Report11))
  }

  @Test
  fun `example non-progression scenario`() {
    val hintSystem = ShananasHintSystem(
      hints = emptyList(),
      allItemsByLocation = mapOf(
        Location.GardenOfAssemblage to AnsemReport.entries,
        Location.SoraLevels to listOf(Proof.ProofOfConnection, SummonCharm.BaseballCharm, Magic.Fire),
        Location.DriveForms to listOf(Magic.Reflect, Magic.Reflect, Magic.Blizzard, Magic.Blizzard),
        Location.Agrabah to listOf(SummonCharm.LampCharm),
        Location.TwilightTown to listOf(Magic.Thunder),
      ),
      progressionSettings = null,
    )

    runTest {
      val seed = testSeed(hintSystem = hintSystem)
      val gameState = testGameState(seed, preferences)

      testLocationHints(gameState) {
        // Verify the initial states before doing anything
        for (location in Location.entries) {
          awaitLocationState(location) {
            when (location) {
              Location.GardenOfAssemblage -> {
                assertCounter(LocationCounterState.None)
              }

              Location.SoraLevels, Location.DriveForms, Location.Agrabah, Location.TwilightTown -> {
                assertCounter(LocationCounterState.None)
              }

              else -> {
                assertCounter(LocationCounterState.Completed)
              }
            }
          }
        }
        awaitRevealedHints()
        awaitLastRevealedHint(null)

        // Acquire Baseball Charm - does not complete levels
        gameState.acquireItemManually(SummonCharm.BaseballCharm, Location.SoraLevels)
        awaitLocationState(Location.SoraLevels) {
          assertAcquired(SummonCharm.BaseballCharm)
          assertCounter(LocationCounterState.None)
        }

        // Acquire Lamp Charm - completes AG
        // (two events, one for item and one for counter)
        gameState.acquireItemManually(SummonCharm.LampCharm, Location.Agrabah)
        skip(Location.Agrabah, 1)
        awaitLocationState(Location.Agrabah) {
          assertAcquired(SummonCharm.LampCharm)
          assertCounter(LocationCounterState.Completed)
        }
      }
    }
  }

  @Test
  @Ignore("These may need a re-work after some changes made in 1.9.2")
  fun `example progression scenario with previously revealed hints`() {
    val hintSystem = ShananasHintSystem(
      hints = emptyList(),
      allItemsByLocation = mapOf(
        Location.GardenOfAssemblage to AnsemReport.entries,
        Location.SoraLevels to listOf(Proof.ProofOfConnection, SummonCharm.BaseballCharm, Magic.Fire),
        Location.DriveForms to listOf(Magic.Reflect, Magic.Reflect, Magic.Blizzard, Magic.Blizzard),
        Location.Agrabah to listOf(SummonCharm.LampCharm),
        Location.TwilightTown to listOf(Magic.Thunder),
      ),
      progressionSettings = ShananasHintSystem.ProgressionSettings(
        testProgressionSettings(),
        locationRevealOrder = listOf(
          Location.SpaceParanoids,
          Location.PrideLands,
          Location.Agrabah,
          Location.SoraLevels,
          Location.TwilightTown,
          Location.DriveForms
        ),
      ),
    )

    runTest {
      val expectedHint1 = HintInfo.GeneralRevealed(hintOrReportNumber = 1, location = Location.SpaceParanoids)
      val expectedHint2 = HintInfo.GeneralRevealed(hintOrReportNumber = 2, location = Location.PrideLands)
      val expectedHint3 = HintInfo.GeneralRevealed(hintOrReportNumber = 3, location = Location.Agrabah)
      val expectedHint4 = HintInfo.GeneralRevealed(hintOrReportNumber = 4, location = Location.SoraLevels)
      val expectedHint5 = HintInfo.GeneralRevealed(hintOrReportNumber = 5, location = Location.TwilightTown)

      val seed = testSeed(hintSystem = hintSystem)
      val gameState = testGameState(
        seed,
        preferences,
        previouslyRevealedHints = persistentListOf(expectedHint1, expectedHint2),
      )

      // (Record a couple points to match current progress up with the fact that we have two hints already revealed)
      gameState.recordProgress(WorldThatNeverWasProgress.Chests)
      gameState.recordProgress(WorldThatNeverWasProgress.Roxas)

      testLocationHints(gameState) {
        // Verify the initial states before doing anything
        for (location in Location.entries) {
          awaitLocationState(location) {
            when (location) {
              Location.GardenOfAssemblage -> {
                assertCounter(LocationCounterState.None)
              }

              // These were revealed by the already revealed hints
              Location.SpaceParanoids, Location.PrideLands -> {
                assertCounter(LocationCounterState.Completed)
              }

              else -> {
                assertCounter(LocationCounterState.Unrevealed)
              }
            }
          }
        }
        awaitRevealedHints(expectedHint1, expectedHint2)
        awaitLastRevealedHint(expectedHint2)

        // Acquire Baseball Charm - does not complete levels
        gameState.acquireItemManually(SummonCharm.BaseballCharm, Location.SoraLevels)
        awaitLocationState(Location.SoraLevels) {
          assertAcquired(SummonCharm.BaseballCharm)
          assertCounter(LocationCounterState.Unrevealed)
        }

        // Acquire Lamp Charm - completes AG
        gameState.acquireItemManually(SummonCharm.LampCharm, Location.Agrabah)
        awaitLocationState(Location.Agrabah) {
          assertAcquired(SummonCharm.LampCharm)
          assertCounter(LocationCounterState.Unrevealed)
        }

        // Make some progress - reveals AG as complete
        gameState.recordProgress(AtlanticaProgress.Tutorial)
        awaitLocationState(Location.Atlantica) {
          assertProgress(AtlanticaProgress.Tutorial)
        }
        awaitLocationState(Location.Agrabah) {
          assertAcquired(SummonCharm.LampCharm)
          assertCounter(LocationCounterState.Completed)
        }
        awaitRevealedHints(expectedHint1, expectedHint2, expectedHint3)
        awaitLastRevealedHint(expectedHint3)

        // Make some progress - reveals levels as incomplete
        gameState.recordProgress(AtlanticaProgress.Ursula)
        awaitLocationState(Location.Atlantica) {
          assertProgress(AtlanticaProgress.Tutorial, AtlanticaProgress.Ursula)
        }
        awaitLocationState(Location.SoraLevels) {
          assertAcquired(SummonCharm.BaseballCharm)
          assertCounter(LocationCounterState.None)
        }
        awaitRevealedHints(expectedHint1, expectedHint2, expectedHint3, expectedHint4)
        awaitLastRevealedHint(expectedHint4)

        // Make some progress - reveals TT as incomplete
        gameState.recordProgress(TwilightTownProgress.Chests)
        skip(Location.TwilightTown, 1)
        awaitLocationState(Location.TwilightTown) {
          assertProgress(TwilightTownProgress.Chests)
          assertCounter(LocationCounterState.None)
        }
        awaitRevealedHints(expectedHint1, expectedHint2, expectedHint3, expectedHint4, expectedHint5)
        awaitLastRevealedHint(expectedHint5)

        // Acquire Thunder - completes TT
        gameState.acquireItemManually(Magic.Thunder, Location.TwilightTown)
        skip(Location.TwilightTown, 1)
        awaitLocationState(Location.TwilightTown) {
          assertProgress(TwilightTownProgress.Chests)
          assertAcquired(Magic.Thunder)
          assertCounter(LocationCounterState.Completed)
        }
      }
    }
  }

  companion object {

    fun hint(
      hintOrReportNumber: Int,
      journalText: String? = null,
    ): ShananasHint {
      return ShananasHint(
        hintOrReportNumber = hintOrReportNumber,
        journalText = journalText,
      )
    }

  }

}
