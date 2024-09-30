package com.kh2rando.tracker.model.seed

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.hints.JSmarteeHint
import com.kh2rando.tracker.model.hints.JSmarteeHintSystem
import com.kh2rando.tracker.model.hints.LocationAuxiliaryHintInfo
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.progress.CavernOfRemembranceProgress
import com.kh2rando.tracker.model.progress.HundredAcreWoodProgress
import com.kh2rando.tracker.model.progress.LandOfDragonsProgress
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JSmarteeHintSystemTest {

  @get:Rule
  val temporaryFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

  private val preferences = testPreferences(temporaryFolder)

  @Test
  fun `fails on not enough hints provided in report mode`() {
    assertFails {
      JSmarteeHintSystem(
        hints = listOf(
          hint(1),
          hint(2),
          hint(3),
          hint(4),
          hint(5),
          hint(6),
          hint(7),
          hint(8),
          hint(9),
          // Missing hint 10
          hint(11),
          hint(12),
          hint(13),
        ),
        progressionSettings = null,
      )
    }
  }

  @Test
  fun `allows no hints provided in progression mode`() {
    JSmarteeHintSystem(
      hints = emptyList(),
      progressionSettings = JSmarteeHintSystem.ProgressionSettings(testProgressionSettings()),
    )
  }

  @Test
  fun `fails on missing report location in report mode`() {
    val hints = listOf(
      hint(1),
      hint(2),
      hint(3),
      hint(4),
      hint(5),
      hint(6, reportLocation = null),
      hint(7),
      hint(8),
      hint(9),
      hint(10),
      hint(11),
      hint(12),
      hint(13),
    )
    assertFails {
      JSmarteeHintSystem(hints, progressionSettings = null)
    }
  }

  @Test
  fun `isValidReportLocation in report mode`() {
    val hintSystem = JSmarteeHintSystem(
      hints = listOf(
        hint(1, reportLocation = Location.WorldThatNeverWas),
        hint(2, reportLocation = Location.LandOfDragons),
        hint(3, reportLocation = Location.BeastsCastle),
        hint(4, reportLocation = Location.HalloweenTown),
        hint(5, reportLocation = Location.Agrabah),
        hint(6, reportLocation = Location.OlympusColiseum),
        hint(7, reportLocation = Location.PrideLands),
        hint(8, reportLocation = Location.TwilightTown),
        hint(9, reportLocation = Location.HollowBastion),
        hint(10, reportLocation = Location.PortRoyal),
        hint(11, reportLocation = Location.DisneyCastle),
        hint(12, reportLocation = Location.SpaceParanoids),
        hint(13, reportLocation = Location.SimulatedTwilightTown),
      ),
      progressionSettings = null,
    )
    assertTrue(hintSystem.isValidReportLocation(Location.OlympusColiseum, AnsemReport.Report6))
    assertFalse(hintSystem.isValidReportLocation(Location.SoraLevels, AnsemReport.Report6))
    assertFalse(hintSystem.isValidReportLocation(Location.WorldThatNeverWas, AnsemReport.Report11))
    assertTrue(hintSystem.isValidReportLocation(Location.DisneyCastle, AnsemReport.Report11))
  }

  @Test
  fun `isValidReportLocation in progression mode`() {
    val hintSystem = JSmarteeHintSystem(
      hints = listOf(
        hint(1, reportLocation = Location.WorldThatNeverWas),
        hint(2, reportLocation = Location.LandOfDragons),
        hint(3, reportLocation = Location.BeastsCastle),
        hint(4, reportLocation = Location.HalloweenTown),
        hint(5, reportLocation = Location.Agrabah),
        hint(6, reportLocation = Location.OlympusColiseum),
        hint(7, reportLocation = Location.PrideLands),
        hint(8, reportLocation = Location.TwilightTown),
        hint(9, reportLocation = Location.HollowBastion),
        hint(10, reportLocation = Location.PortRoyal),
        hint(11, reportLocation = Location.DisneyCastle),
        hint(12, reportLocation = Location.SpaceParanoids),
        hint(13, reportLocation = Location.SimulatedTwilightTown),
      ),
      progressionSettings = JSmarteeHintSystem.ProgressionSettings(testProgressionSettings()),
    )
    // Progression doesn't provide any information about report locations (yet!) so we have to assume all locations are
    // valid
    for (report in AnsemReport.entries) {
      for (location in Location.entries) {
        assertTrue(hintSystem.isValidReportLocation(location, report))
      }
    }
  }

  @Test
  fun `example report scenario`() {
    val hintSystem = JSmarteeHintSystem(
      hints = listOf(
        hint(
          1,
          reportLocation = Location.WorldThatNeverWas,
          hintedLocation = Location.SoraLevels,
          importantCheckCount = 3,
        ),
        hint(
          2,
          reportLocation = Location.LandOfDragons,
          hintedLocation = Location.DriveForms,
          importantCheckCount = 0,
        ),
        hint(
          3,
          reportLocation = Location.BeastsCastle,
          hintedLocation = Location.TwilightTown,
          importantCheckCount = 7,
        ),
        hint(
          4,
          reportLocation = Location.HalloweenTown,
          hintedLocation = Location.Atlantica,
          importantCheckCount = 1,
        ),
        hint(
          5,
          reportLocation = Location.Agrabah,
          hintedLocation = Location.Creations,
          importantCheckCount = 2,
        ),
        hint(
          6,
          reportLocation = Location.OlympusColiseum,
          hintedLocation = Location.OlympusColiseum,
          importantCheckCount = 3,
        ),
        hint(
          7,
          reportLocation = Location.PrideLands,
          hintedLocation = Location.HollowBastion,
          importantCheckCount = 5,
        ),
        hint(
          8,
          reportLocation = Location.TwilightTown,
          hintedLocation = Location.BeastsCastle,
          importantCheckCount = 2,
        ),
        hint(
          9,
          reportLocation = Location.HollowBastion,
          hintedLocation = Location.Agrabah,
          importantCheckCount = 0,
        ),
        hint(
          10,
          reportLocation = Location.HollowBastion,
          hintedLocation = Location.SpaceParanoids,
          importantCheckCount = 2,
        ),
        hint(
          11,
          reportLocation = Location.DisneyCastle,
          hintedLocation = Location.DisneyCastle,
          importantCheckCount = 2,
        ),
        hint(
          12,
          reportLocation = Location.SimulatedTwilightTown,
          hintedLocation = Location.HundredAcreWood,
          importantCheckCount = 6,
        ),
        hint(
          13,
          reportLocation = Location.SimulatedTwilightTown,
          hintedLocation = Location.HalloweenTown,
          importantCheckCount = 3,
        ),
      ),
      progressionSettings = null,
    )

    runTest {
      val seed = testSeed(hintSystem = hintSystem)
      val gameState = testGameState(seed, preferences)

      val expectedHint1 = HintInfo.ImportantCheckCount(hintOrReportNumber = 1, Location.SoraLevels, count = 3)
      val expectedHint7 = HintInfo.ImportantCheckCount(hintOrReportNumber = 7, Location.HollowBastion, count = 5)
      val expectedHint9 = HintInfo.ImportantCheckCount(hintOrReportNumber = 9, Location.Agrabah, count = 0)
      val expectedHint10 = HintInfo.ImportantCheckCount(hintOrReportNumber = 10, Location.SpaceParanoids, count = 2)

      testLocationHints(gameState) {
        // Verify the initial states before doing anything
        for (location in Location.entries) {
          awaitLocationState(location) {
            if (location == Location.GardenOfAssemblage) {
              assertCounter(LocationCounterState.None)
            } else {
              assertCounter(LocationCounterState.Unrevealed)
            }
            assertAuxiliary(LocationAuxiliaryHintInfo.Blank)
          }
        }
        awaitRevealedHints()
        awaitLastRevealedHint(null)

        // Acquire report 1 - reveals that levels has 3
        gameState.acquireItemManually(AnsemReport.Report1, Location.WorldThatNeverWas)
        awaitLocationState(Location.WorldThatNeverWas) {
          assertAcquired(AnsemReport.Report1)
        }
        awaitLocationState(Location.SoraLevels) {
          assertCounter(LocationCounterState.Revealed(3))
        }
        awaitRevealedHints(expectedHint1)
        awaitLastRevealedHint(expectedHint1)

        // Acquire report 10 - reveals that SP has 2
        gameState.acquireItemManually(AnsemReport.Report10, Location.HollowBastion)
        awaitLocationState(Location.HollowBastion) {
          assertAcquired(AnsemReport.Report10)
        }
        awaitLocationState(Location.SpaceParanoids) {
          assertCounter(LocationCounterState.Revealed(2))
        }
        awaitRevealedHints(expectedHint1, expectedHint10)
        awaitLastRevealedHint(expectedHint10)

        // Acquire report 9 - reveals that Agrabah has 0
        gameState.acquireItemManually(AnsemReport.Report9, Location.HollowBastion)
        awaitLocationState(Location.HollowBastion) {
          assertAcquired(AnsemReport.Report10, AnsemReport.Report9)
        }
        awaitLocationState(Location.Agrabah) {
          assertCounter(LocationCounterState.Completed)
        }
        awaitRevealedHints(expectedHint1, expectedHint10, expectedHint9)
        awaitLastRevealedHint(expectedHint9)

        // Acquire report 7 - reveals that HB has 5 total but 3 more (and makes some hints hinted hints)
        gameState.acquireItemManually(AnsemReport.Report7, Location.PrideLands)
        awaitLocationState(Location.PrideLands) {
          assertAcquired(AnsemReport.Report7)
        }
        awaitLocationState(Location.HollowBastion) {
          assertCounter(LocationCounterState.Revealed(3))
        }
        awaitLocationState(Location.Agrabah) {
          assertAuxiliary(LocationAuxiliaryHintInfo.HintedHint)
        }
        awaitLocationState(Location.SpaceParanoids) {
          assertAuxiliary(LocationAuxiliaryHintInfo.HintedHint)
        }
        awaitRevealedHints(expectedHint1, expectedHint10, expectedHint9, expectedHint7)
        awaitLastRevealedHint(expectedHint7)
      }
    }
  }

  @Test
  fun `example progression scenario`() {
    val hintSystem = JSmarteeHintSystem(
      hints = listOf(
        hint(
          1,
          reportLocation = null,
          hintedLocation = Location.SoraLevels,
          importantCheckCount = 3,
        ),
        hint(
          2,
          reportLocation = null,
          hintedLocation = Location.DriveForms,
          importantCheckCount = 0,
        ),
        hint(
          3,
          reportLocation = null,
          hintedLocation = Location.TwilightTown,
          importantCheckCount = 7,
        ),
      ),
      progressionSettings = JSmarteeHintSystem.ProgressionSettings(testProgressionSettings()),
    )

    runTest {
      val seed = testSeed(hintSystem = hintSystem)
      val gameState = testGameState(seed, preferences)

      val expectedHint1 = HintInfo.ImportantCheckCount(hintOrReportNumber = 1, Location.SoraLevels, count = 3)
      val expectedHint2 = HintInfo.ImportantCheckCount(hintOrReportNumber = 2, Location.DriveForms, count = 0)
      val expectedHint3 = HintInfo.ImportantCheckCount(hintOrReportNumber = 3, Location.TwilightTown, count = 7)

      testLocationHints(gameState) {
        // Verify the initial states before doing anything
        for (location in Location.entries) {
          awaitLocationState(location) {
            if (location == Location.GardenOfAssemblage) {
              assertCounter(LocationCounterState.None)
            } else {
              assertCounter(LocationCounterState.Unrevealed)
            }
            assertAuxiliary(LocationAuxiliaryHintInfo.NotApplicableToHintSystem)
          }
        }
        awaitRevealedHints()
        awaitLastRevealedHint(null)

        // Acquire report 1 - does nothing
        gameState.acquireItemManually(AnsemReport.Report1, Location.WorldThatNeverWas)
        awaitLocationState(Location.WorldThatNeverWas) {
          assertAcquired(AnsemReport.Report1)
        }

        // Make some progress - reveals that levels has 3
        gameState.recordProgress(LandOfDragonsProgress.Chests)
        awaitLocationState(Location.LandOfDragons) {
          assertProgress(LandOfDragonsProgress.Chests)
        }
        awaitLocationState(Location.SoraLevels) {
          assertCounter(LocationCounterState.Revealed(3))
        }
        awaitRevealedHints(expectedHint1)
        awaitLastRevealedHint(expectedHint1)

        // Make some progress - reveals that forms has 0
        gameState.recordProgress(CavernOfRemembranceProgress.LastChest)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertProgress(CavernOfRemembranceProgress.LastChest)
        }
        awaitLocationState(Location.DriveForms) {
          assertCounter(LocationCounterState.Completed)
        }
        awaitRevealedHints(expectedHint1, expectedHint2)
        awaitLastRevealedHint(expectedHint2)

        // Make some progress - reveals that TT has 7
        gameState.recordProgress(HundredAcreWoodProgress.Chests)
        awaitLocationState(Location.HundredAcreWood) {
          assertProgress(HundredAcreWoodProgress.Chests)
        }
        awaitLocationState(Location.TwilightTown) {
          assertCounter(LocationCounterState.Revealed(7))
        }
        awaitRevealedHints(expectedHint1, expectedHint2, expectedHint3)
        awaitLastRevealedHint(expectedHint3)
      }
    }
  }

  companion object {

    fun hint(
      hintOrReportNumber: Int,
      reportLocation: Location? = Location.WorldThatNeverWas,
      hintedLocation: Location = Location.TwilightTown,
      importantCheckCount: Int = 0,
      journalText: String? = null,
    ): JSmarteeHint {
      return JSmarteeHint(
        hintOrReportNumber = hintOrReportNumber,
        reportLocation = reportLocation,
        hintedLocation = hintedLocation,
        importantCheckCount = importantCheckCount,
        journalText = journalText,
      )
    }

  }

}
