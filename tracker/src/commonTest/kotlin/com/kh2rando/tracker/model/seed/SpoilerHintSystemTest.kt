package com.kh2rando.tracker.model.seed

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.hints.LocationAuxiliaryHintInfo
import com.kh2rando.tracker.model.hints.SpoilerHint
import com.kh2rando.tracker.model.hints.SpoilerHintSystem
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.Magic
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.item.SummonCharm
import com.kh2rando.tracker.model.progress.LandOfDragonsProgress
import com.kh2rando.tracker.model.progress.PortRoyalProgress
import com.kh2rando.tracker.model.progress.SimulatedTwilightTownProgress
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpoilerHintSystemTest {

  @get:Rule
  val temporaryFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

  private val preferences = testPreferences(temporaryFolder)

  @Test
  fun isValidReportLocation() {
    // Note: for spoiler hints the settings don't really matter for isValidReportLocation()
    val hintSystem = SpoilerHintSystem(
      hints = emptyList(),
      allItemsByLocation = mapOf(
        Location.OlympusColiseum to listOf(AnsemReport.Report6),
        Location.DisneyCastle to listOf(AnsemReport.Report11),
      ),
      revealedItemTypes = ItemPrototype.fullList.toSet(),
      revealMode = SpoilerHintSystem.RevealMode.Always,
      revealWorldCompletion = false,
      progressionSettings = null,
    )
    assertTrue(hintSystem.isValidReportLocation(Location.OlympusColiseum, AnsemReport.Report6))
    assertFalse(hintSystem.isValidReportLocation(Location.SoraLevels, AnsemReport.Report6))
    assertFalse(hintSystem.isValidReportLocation(Location.WorldThatNeverWas, AnsemReport.Report11))
    assertTrue(hintSystem.isValidReportLocation(Location.DisneyCastle, AnsemReport.Report11))
  }

  @Test
  fun `example reveal always scenario without reveal world completion`() {
    val hintSystem = SpoilerHintSystem(
      hints = listOf(
        hint(1, hintedLocation = Location.SoraLevels),
        hint(2, hintedLocation = Location.DriveForms),
        hint(3, hintedLocation = Location.TwilightTown),
        hint(4, hintedLocation = Location.Atlantica),
        hint(5, hintedLocation = Location.Creations),
        hint(6, hintedLocation = Location.OlympusColiseum),
        hint(7, hintedLocation = Location.HollowBastion),
        hint(8, hintedLocation = Location.BeastsCastle),
        hint(9, hintedLocation = Location.Agrabah),
        hint(10, hintedLocation = Location.SpaceParanoids),
        hint(11, hintedLocation = Location.DisneyCastle),
        hint(12, hintedLocation = Location.HundredAcreWood),
        hint(13, hintedLocation = Location.HalloweenTown),
      ),
      allItemsByLocation = mapOf(
        Location.GardenOfAssemblage to AnsemReport.entries,
        Location.SoraLevels to listOf(Proof.ProofOfConnection, SummonCharm.BaseballCharm, Magic.Fire),
        Location.DriveForms to listOf(Magic.Reflect, Magic.Reflect, Magic.Blizzard, Magic.Blizzard),
        Location.Agrabah to listOf(SummonCharm.LampCharm),
        Location.TwilightTown to listOf(Magic.Thunder),
      ),
      revealMode = SpoilerHintSystem.RevealMode.Always,
      revealedItemTypes = ItemPrototype.fullList.toSet() - Proof.entries - AnsemReport.entries,
      revealWorldCompletion = false,
      progressionSettings = null,
    )

    runTest {
      val seed = testSeed(hintSystem = hintSystem)
      val gameState = testGameState(seed, preferences)

      testLocationHints(gameState) {
        for (location in Location.entries) {
          awaitLocationState(location) {
            assertCounter(LocationCounterState.None)
            assertAuxiliary(LocationAuxiliaryHintInfo.NotApplicableToHintSystem)

            when (location) {
              Location.SoraLevels -> {
                assertRevealed(SummonCharm.BaseballCharm, Magic.Fire)
              }

              Location.DriveForms -> {
                assertRevealed(Magic.Reflect, Magic.Reflect, Magic.Blizzard, Magic.Blizzard)
              }

              Location.Agrabah -> {
                assertRevealed(SummonCharm.LampCharm)
              }

              Location.TwilightTown -> {
                assertRevealed(Magic.Thunder)
              }

              else -> {
                assertEmpty(revealedItems)
              }
            }
          }
        }
        awaitRevealedHints()
        awaitLastRevealedHint(null)
      }
    }
  }

  @Test
  fun `example reveal always scenario with reveal world completion`() {
    val hintSystem = SpoilerHintSystem(
      hints = listOf(
        hint(1, hintedLocation = Location.SoraLevels),
        hint(2, hintedLocation = Location.DriveForms),
        hint(3, hintedLocation = Location.TwilightTown),
        hint(4, hintedLocation = Location.Atlantica),
        hint(5, hintedLocation = Location.Creations),
        hint(6, hintedLocation = Location.OlympusColiseum),
        hint(7, hintedLocation = Location.HollowBastion),
        hint(8, hintedLocation = Location.BeastsCastle),
        hint(9, hintedLocation = Location.Agrabah),
        hint(10, hintedLocation = Location.SpaceParanoids),
        hint(11, hintedLocation = Location.DisneyCastle),
        hint(12, hintedLocation = Location.HundredAcreWood),
        hint(13, hintedLocation = Location.HalloweenTown),
      ),
      allItemsByLocation = mapOf(
        Location.GardenOfAssemblage to AnsemReport.entries,
        Location.SoraLevels to listOf(Proof.ProofOfConnection, SummonCharm.BaseballCharm, Magic.Fire),
        Location.DriveForms to listOf(Magic.Reflect, Magic.Reflect, Magic.Blizzard, Magic.Blizzard),
        Location.Agrabah to listOf(SummonCharm.LampCharm),
        Location.TwilightTown to listOf(Magic.Thunder),
      ),
      revealMode = SpoilerHintSystem.RevealMode.Always,
      revealedItemTypes = ItemPrototype.fullList.toSet() - Proof.entries - AnsemReport.entries,
      revealWorldCompletion = true,
      progressionSettings = null,
    )

    runTest {
      val seed = testSeed(hintSystem = hintSystem)
      val gameState = testGameState(seed, preferences)

      testLocationHints(gameState) {
        for (location in Location.entries) {
          awaitLocationState(location) {
            assertAuxiliary(LocationAuxiliaryHintInfo.NotApplicableToHintSystem)

            when (location) {
              Location.GardenOfAssemblage -> {
                assertEmpty(revealedItems)
                assertCounter(LocationCounterState.None)
              }

              Location.SoraLevels -> {
                assertRevealed(SummonCharm.BaseballCharm, Magic.Fire)
                assertCounter(LocationCounterState.None)
              }

              Location.DriveForms -> {
                assertRevealed(Magic.Reflect, Magic.Reflect, Magic.Blizzard, Magic.Blizzard)
                assertCounter(LocationCounterState.None)
              }

              Location.Agrabah -> {
                assertRevealed(SummonCharm.LampCharm)
                assertCounter(LocationCounterState.None)
              }

              Location.TwilightTown -> {
                assertRevealed(Magic.Thunder)
                assertCounter(LocationCounterState.None)
              }

              else -> {
                assertEmpty(revealedItems)
                assertCounter(LocationCounterState.Completed)
              }
            }
          }
        }

        // Acquire Lamp Charm - completes AG
        // Triggers two events though, one for item obtained, one for completion
        gameState.acquireItemManually(SummonCharm.LampCharm, Location.Agrabah)
        skip(Location.Agrabah, 1)
        awaitLocationState(Location.Agrabah) {
          assertRevealed(SummonCharm.LampCharm)
          assertCounter(LocationCounterState.Completed)
        }

        awaitRevealedHints()
        awaitLastRevealedHint(null)
      }
    }
  }

  @Test
  @Ignore("These may need a re-work after some changes made in 1.9.2")
  fun `example reveal by report scenario without reveal world completion`() {
    val hintSystem = SpoilerHintSystem(
      hints = listOf(
        hint(1, hintedLocation = Location.SoraLevels),
        hint(2, hintedLocation = Location.DriveForms),
        hint(3, hintedLocation = Location.TwilightTown),
        hint(4, hintedLocation = Location.Atlantica),
        hint(5, hintedLocation = Location.Creations),
        hint(6, hintedLocation = Location.OlympusColiseum),
        hint(7, hintedLocation = Location.HollowBastion),
        hint(8, hintedLocation = Location.BeastsCastle),
        hint(9, hintedLocation = Location.Agrabah),
        hint(10, hintedLocation = Location.SpaceParanoids),
        hint(11, hintedLocation = Location.DisneyCastle),
        hint(12, hintedLocation = Location.HundredAcreWood),
        hint(13, hintedLocation = Location.HalloweenTown),
      ),
      allItemsByLocation = mapOf(
        Location.GardenOfAssemblage to AnsemReport.entries,
        Location.SoraLevels to listOf(Proof.ProofOfConnection, SummonCharm.BaseballCharm, Magic.Fire),
        Location.DriveForms to listOf(Magic.Reflect, Magic.Reflect, Magic.Blizzard, Magic.Blizzard),
        Location.Agrabah to listOf(SummonCharm.LampCharm),
        Location.TwilightTown to listOf(Magic.Thunder),
      ),
      revealMode = SpoilerHintSystem.RevealMode.Gradual,
      revealedItemTypes = ItemPrototype.fullList.toSet() - Proof.entries - AnsemReport.entries,
      revealWorldCompletion = false,
      progressionSettings = null,
    )

    runTest {
      val seed = testSeed(hintSystem = hintSystem)
      val gameState = testGameState(seed, preferences)

      testLocationHints(gameState) {
        for (location in Location.entries) {
          awaitLocationState(location) {
            assertEmpty(revealedItems)
            assertAuxiliary(LocationAuxiliaryHintInfo.NotApplicableToHintSystem)

            when (location) {
              Location.GardenOfAssemblage -> {
                assertCounter(LocationCounterState.None)
              }

              else -> {
                assertCounter(LocationCounterState.Unrevealed)
              }
            }
          }
        }

        awaitRevealedHints()
        awaitLastRevealedHint(null)

        val expectedHint1 = HintInfo.GeneralRevealed(hintOrReportNumber = 1, location = Location.SoraLevels)
        val expectedHint7 = HintInfo.GeneralRevealed(hintOrReportNumber = 7, location = Location.HollowBastion)
        val expectedHint9 = HintInfo.GeneralRevealed(hintOrReportNumber = 9, location = Location.Agrabah)
        val expectedHint10 = HintInfo.GeneralRevealed(hintOrReportNumber = 10, location = Location.SpaceParanoids)

        // Acquire report 1 - reveals levels
        // Gives two events - one for revealed items, one for counter update
        gameState.acquireItemManually(AnsemReport.Report1, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report1)
        }
        skip(Location.SoraLevels, 1)
        awaitLocationState(Location.SoraLevels) {
          // Note that ProofOfConnection is here but not revealed per settings
          assertRevealed(SummonCharm.BaseballCharm, Magic.Fire)
          assertCounter(LocationCounterState.None)
        }
        awaitRevealedHints(expectedHint1)
        awaitLastRevealedHint(expectedHint1)

        // Acquire report 10 - reveals SP
        gameState.acquireItemManually(AnsemReport.Report10, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report1, AnsemReport.Report10)
        }
        // (Don't need to skip an event since the revealed list didn't change)
        awaitLocationState(Location.SpaceParanoids) {
          assertEmpty(revealedItems)
          assertCounter(LocationCounterState.None)
        }
        awaitRevealedHints(expectedHint1, expectedHint10)
        awaitLastRevealedHint(expectedHint10)

        // Acquire report 9 - reveals AG
        gameState.acquireItemManually(AnsemReport.Report9, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report1, AnsemReport.Report10, AnsemReport.Report9)
        }
        skip(Location.Agrabah, 1)
        awaitLocationState(Location.Agrabah) {
          assertRevealed(SummonCharm.LampCharm)
          assertCounter(LocationCounterState.None)
        }
        awaitRevealedHints(expectedHint1, expectedHint10, expectedHint9)
        awaitLastRevealedHint(expectedHint9)

        // Acquire Lamp Charm - completes AG but reveal world completion is false so still not shown as complete
        gameState.acquireItemManually(SummonCharm.LampCharm, Location.Agrabah)
        awaitLocationState(Location.Agrabah) {
          assertAcquired(SummonCharm.LampCharm)
          assertRevealed(SummonCharm.LampCharm)
          assertCounter(LocationCounterState.None)
        }

        // Acquire report 7 - reveals HB
        gameState.acquireItemManually(AnsemReport.Report7, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report1, AnsemReport.Report10, AnsemReport.Report9, AnsemReport.Report7)
        }
        awaitLocationState(Location.HollowBastion) {
          assertEmpty(revealedItems)
          assertCounter(LocationCounterState.None)
        }
        awaitRevealedHints(expectedHint1, expectedHint10, expectedHint9, expectedHint7)
        awaitLastRevealedHint(expectedHint7)
      }
    }
  }

  @Test
  @Ignore("These may need a re-work after some changes made in 1.9.2")
  fun `example reveal by report scenario with reveal world completion`() {
    val hintSystem = SpoilerHintSystem(
      hints = listOf(
        hint(1, hintedLocation = Location.SoraLevels),
        hint(2, hintedLocation = Location.DriveForms),
        hint(3, hintedLocation = Location.TwilightTown),
        hint(4, hintedLocation = Location.Atlantica),
        hint(5, hintedLocation = Location.Creations),
        hint(6, hintedLocation = Location.OlympusColiseum),
        hint(7, hintedLocation = Location.HollowBastion),
        hint(8, hintedLocation = Location.BeastsCastle),
        hint(9, hintedLocation = Location.Agrabah),
        hint(10, hintedLocation = Location.SpaceParanoids),
        hint(11, hintedLocation = Location.DisneyCastle),
        hint(12, hintedLocation = Location.HundredAcreWood),
        hint(13, hintedLocation = Location.HalloweenTown),
      ),
      allItemsByLocation = mapOf(
        Location.GardenOfAssemblage to AnsemReport.entries,
        Location.SoraLevels to listOf(Proof.ProofOfConnection, SummonCharm.BaseballCharm, Magic.Fire),
        Location.DriveForms to listOf(Magic.Reflect, Magic.Reflect, Magic.Blizzard, Magic.Blizzard),
        Location.Agrabah to listOf(SummonCharm.LampCharm),
        Location.TwilightTown to listOf(Magic.Thunder),
      ),
      revealMode = SpoilerHintSystem.RevealMode.Gradual,
      revealedItemTypes = ItemPrototype.fullList.toSet() - Proof.entries - AnsemReport.entries,
      revealWorldCompletion = true,
      progressionSettings = null,
    )

    runTest {
      val seed = testSeed(hintSystem = hintSystem)
      val gameState = testGameState(seed, preferences)

      testLocationHints(gameState) {
        for (location in Location.entries) {
          awaitLocationState(location) {
            assertEmpty(revealedItems)
            assertAuxiliary(LocationAuxiliaryHintInfo.NotApplicableToHintSystem)

            when (location) {
              Location.GardenOfAssemblage -> {
                assertCounter(LocationCounterState.None)
              }

              else -> {
                assertCounter(LocationCounterState.Unrevealed)
              }
            }
          }
        }

        awaitRevealedHints()
        awaitLastRevealedHint(null)

        val expectedHint1 = HintInfo.GeneralRevealed(hintOrReportNumber = 1, location = Location.SoraLevels)
        val expectedHint7 = HintInfo.GeneralRevealed(hintOrReportNumber = 7, location = Location.HollowBastion)
        val expectedHint9 = HintInfo.GeneralRevealed(hintOrReportNumber = 9, location = Location.Agrabah)
        val expectedHint10 = HintInfo.GeneralRevealed(hintOrReportNumber = 10, location = Location.SpaceParanoids)

        // Acquire report 1 - reveals levels
        // Gives two events - one for revealed items, one for counter update
        gameState.acquireItemManually(AnsemReport.Report1, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report1)
        }
        skip(Location.SoraLevels, 1)
        awaitLocationState(Location.SoraLevels) {
          // Note that ProofOfConnection is here but not revealed per settings
          assertRevealed(SummonCharm.BaseballCharm, Magic.Fire)
          assertCounter(LocationCounterState.None)
        }
        awaitRevealedHints(expectedHint1)
        awaitLastRevealedHint(expectedHint1)

        // Acquire report 10 - reveals SP
        gameState.acquireItemManually(AnsemReport.Report10, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report1, AnsemReport.Report10)
        }
        // (Don't need to skip an event since the revealed list didn't change)
        awaitLocationState(Location.SpaceParanoids) {
          assertEmpty(revealedItems)
          assertCounter(LocationCounterState.Completed)
        }
        awaitRevealedHints(expectedHint1, expectedHint10)
        awaitLastRevealedHint(expectedHint10)

        // Acquire report 9 - reveals AG
        gameState.acquireItemManually(AnsemReport.Report9, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report1, AnsemReport.Report10, AnsemReport.Report9)
        }
        skip(Location.Agrabah, 1)
        awaitLocationState(Location.Agrabah) {
          assertRevealed(SummonCharm.LampCharm)
          assertCounter(LocationCounterState.None)
        }
        awaitRevealedHints(expectedHint1, expectedHint10, expectedHint9)
        awaitLastRevealedHint(expectedHint9)

        // Acquire Lamp Charm - completes AG
        gameState.acquireItemManually(SummonCharm.LampCharm, Location.Agrabah)
        skip(Location.Agrabah, 1)
        awaitLocationState(Location.Agrabah) {
          assertAcquired(SummonCharm.LampCharm)
          assertRevealed(SummonCharm.LampCharm)
          assertCounter(LocationCounterState.Completed)
        }

        // Acquire report 7 - reveals HB
        gameState.acquireItemManually(AnsemReport.Report7, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report1, AnsemReport.Report10, AnsemReport.Report9, AnsemReport.Report7)
        }
        awaitLocationState(Location.HollowBastion) {
          assertEmpty(revealedItems)
          assertCounter(LocationCounterState.Completed)
        }
        awaitRevealedHints(expectedHint1, expectedHint10, expectedHint9, expectedHint7)
        awaitLastRevealedHint(expectedHint7)
      }
    }
  }

  @Test
  @Ignore("These may need a re-work after some changes made in 1.9.2")
  fun `example reveal by progression scenario without reveal world completion`() {
    val hintSystem = SpoilerHintSystem(
      hints = listOf(
        hint(1, hintedLocation = Location.SoraLevels),
        hint(2, hintedLocation = Location.DriveForms),
        hint(3, hintedLocation = Location.TwilightTown),
        hint(4, hintedLocation = Location.Atlantica),
      ),
      allItemsByLocation = mapOf(
        Location.GardenOfAssemblage to AnsemReport.entries,
        Location.SoraLevels to listOf(Proof.ProofOfConnection, SummonCharm.BaseballCharm, Magic.Fire),
        Location.DriveForms to listOf(Magic.Reflect, Magic.Reflect, Magic.Blizzard, Magic.Blizzard),
        Location.Agrabah to listOf(SummonCharm.LampCharm),
        Location.TwilightTown to listOf(Magic.Thunder),
      ),
      revealMode = SpoilerHintSystem.RevealMode.Gradual,
      revealedItemTypes = ItemPrototype.fullList.toSet(),
      revealWorldCompletion = false,
      progressionSettings = SpoilerHintSystem.ProgressionSettings(testProgressionSettings()),
    )

    runTest {
      val seed = testSeed(hintSystem = hintSystem)
      val gameState = testGameState(seed, preferences)

      testLocationHints(gameState) {
        for (location in Location.entries) {
          awaitLocationState(location) {
            assertEmpty(revealedItems)
            assertAuxiliary(LocationAuxiliaryHintInfo.NotApplicableToHintSystem)

            when (location) {
              Location.GardenOfAssemblage -> {
                assertCounter(LocationCounterState.None)
              }

              else -> {
                assertCounter(LocationCounterState.Unrevealed)
              }
            }
          }
        }

        awaitRevealedHints()
        awaitLastRevealedHint(null)

        val expectedHint1 = HintInfo.GeneralRevealed(hintOrReportNumber = 1, location = Location.SoraLevels)
        val expectedHint2 = HintInfo.GeneralRevealed(hintOrReportNumber = 2, location = Location.DriveForms)
        val expectedHint3 = HintInfo.GeneralRevealed(hintOrReportNumber = 3, location = Location.TwilightTown)

        // Acquire report 1 - does nothing
        gameState.acquireItemManually(AnsemReport.Report1, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report1)
        }

        // Make some progress - reveals levels
        gameState.recordProgress(PortRoyalProgress.Gambler)
        awaitLocationState(Location.PortRoyal) {
          assertProgress(PortRoyalProgress.Gambler)
        }
        // Two events, one for revealed items and one for counter
        skip(Location.SoraLevels, 1)
        awaitLocationState(Location.SoraLevels) {
          assertRevealed(Proof.ProofOfConnection, SummonCharm.BaseballCharm, Magic.Fire)
          assertCounter(LocationCounterState.None)
        }
        awaitRevealedHints(expectedHint1)
        awaitLastRevealedHint(expectedHint1)

        // Make some progress - reveals forms
        gameState.recordProgress(LandOfDragonsProgress.DataXigbar)
        awaitLocationState(Location.LandOfDragons) {
          assertProgress(LandOfDragonsProgress.DataXigbar)
        }
        skip(Location.DriveForms, 1)
        awaitLocationState(Location.DriveForms) {
          assertRevealed(Magic.Reflect, Magic.Reflect, Magic.Blizzard, Magic.Blizzard)
          assertCounter(LocationCounterState.None)
        }
        awaitRevealedHints(expectedHint1, expectedHint2)
        awaitLastRevealedHint(expectedHint2)

        // Make some progress - reveals TT
        gameState.recordProgress(SimulatedTwilightTownProgress.Axel)
        awaitLocationState(Location.SimulatedTwilightTown) {
          assertProgress(SimulatedTwilightTownProgress.Axel)
        }
        skip(Location.TwilightTown, 1)
        awaitLocationState(Location.TwilightTown) {
          assertRevealed(Magic.Thunder)
          assertCounter(LocationCounterState.None)
        }
        awaitRevealedHints(expectedHint1, expectedHint2, expectedHint3)
        awaitLastRevealedHint(expectedHint3)

        // Acquire Thunder - completes TT but reveal world completion is false so still not shown as complete
        gameState.acquireItemManually(Magic.Thunder, Location.TwilightTown)
        awaitLocationState(Location.TwilightTown) {
          assertRevealed(Magic.Thunder)
          assertAcquired(Magic.Thunder)
          assertCounter(LocationCounterState.None)
        }
      }
    }
  }

  companion object {

    fun hint(
      hintOrReportNumber: Int,
      hintedLocation: Location,
      journalText: String? = null,
    ): SpoilerHint {
      return SpoilerHint(
        hintOrReportNumber = hintOrReportNumber,
        hintedLocation = hintedLocation,
        journalText = journalText,
      )
    }

  }

}
