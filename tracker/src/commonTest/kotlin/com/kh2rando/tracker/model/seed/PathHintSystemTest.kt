package com.kh2rando.tracker.model.seed

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.hints.LocationAuxiliaryHintInfo
import com.kh2rando.tracker.model.hints.PathHint
import com.kh2rando.tracker.model.hints.PathHintSystem
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.progress.CavernOfRemembranceProgress
import com.kh2rando.tracker.model.progress.HundredAcreWoodProgress
import com.kh2rando.tracker.model.progress.LandOfDragonsProgress
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PathHintSystemTest {

  @get:Rule
  val temporaryFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

  private val preferences = testPreferences(temporaryFolder)

  @Test
  fun isValidReportLocation() {
    // Note: for path hints it doesn't matter whether it's progression or not for isValidReportLocation()
    val hintSystem = PathHintSystem(
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
  fun `example report scenario`() {
    val hintSystem = PathHintSystem(
      hints = listOf(
        hint(
          1,
          hintedLocation = Location.SoraLevels,
          pathToProofs = setOf(Proof.ProofOfNonexistence),
        ),
        hint(
          2,
          hintedLocation = Location.DriveForms,
          pathToProofs = setOf(Proof.ProofOfConnection, Proof.ProofOfPeace),
        ),
        hint(
          3,
          hintedLocation = Location.TwilightTown,
          pathToProofs = emptySet(),
        ),
        hint(
          4,
          hintedLocation = Location.Atlantica,
          pathToProofs = setOf(Proof.ProofOfNonexistence, Proof.ProofOfConnection),
        ),
        hint(
          5,
          hintedLocation = Location.Creations,
          pathToProofs = setOf(Proof.ProofOfPeace),
        ),
        hint(
          6,
          hintedLocation = Location.OlympusColiseum,
          pathToProofs = emptySet(),
        ),
        hint(
          7,
          hintedLocation = Location.HollowBastion,
          pathToProofs = setOf(Proof.ProofOfNonexistence),
        ),
        hint(
          8,
          hintedLocation = Location.BeastsCastle,
          pathToProofs = setOf(Proof.ProofOfConnection, Proof.ProofOfNonexistence, Proof.ProofOfPeace),
        ),
        hint(
          9,
          hintedLocation = Location.Agrabah,
          pathToProofs = setOf(Proof.ProofOfConnection, Proof.ProofOfNonexistence, Proof.ProofOfPeace),
        ),
        hint(
          10,
          hintedLocation = Location.SpaceParanoids,
          pathToProofs = emptySet(),
        ),
        hint(
          11,
          hintedLocation = Location.DisneyCastle,
          pathToProofs = setOf(Proof.ProofOfConnection),
        ),
        hint(
          12,
          hintedLocation = Location.HundredAcreWood,
          pathToProofs = setOf(Proof.ProofOfNonexistence),
        ),
        hint(
          13,
          hintedLocation = Location.HalloweenTown,
          pathToProofs = setOf(Proof.ProofOfNonexistence, Proof.ProofOfConnection),
        ),
      ),
      allItemsByLocation = mapOf(Location.GardenOfAssemblage to AnsemReport.entries),
      progressionSettings = null,
    )

    runTest {
      val seed = testSeed(hintSystem = hintSystem)
      val gameState = testGameState(seed, preferences)

      val expectedHint1 = HintInfo.PathToProofs(
        hintOrReportNumber = 1,
        location = Location.SoraLevels,
        proofs = setOf(Proof.ProofOfNonexistence)
      )
      val expectedHint7 = HintInfo.PathToProofs(
        hintOrReportNumber = 7,
        location = Location.HollowBastion,
        proofs = setOf(Proof.ProofOfNonexistence),
      )
      val expectedHint9 = HintInfo.PathToProofs(
        hintOrReportNumber = 9,
        location = Location.Agrabah,
        proofs = Proof.entries.toSet(),
      )
      val expectedHint10 = HintInfo.PathToProofs(
        hintOrReportNumber = 10,
        location = Location.SpaceParanoids,
        proofs = emptySet()
      )

      testLocationHints(gameState) {
        // Verify the initial states before doing anything
        for (location in Location.entries) {
          awaitLocationState(location) {
            if (location == Location.GardenOfAssemblage) {
              assertCounter(LocationCounterState.None)
            } else {
              assertCounter(LocationCounterState.Completed)
            }
            assertAuxiliary(LocationAuxiliaryHintInfo.Blank)
          }
        }
        awaitRevealedHints()
        awaitLastRevealedHint(null)

        // Acquire report 1 - reveals that levels -> Nonexistence
        gameState.acquireItemManually(AnsemReport.Report1, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report1)
        }
        awaitLocationState(Location.SoraLevels) {
          assertAuxiliary(expectedHint1)
        }
        awaitRevealedHints(expectedHint1)
        awaitLastRevealedHint(expectedHint1)

        // Acquire report 10 - reveals that SP -> no path
        gameState.acquireItemManually(AnsemReport.Report10, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report1, AnsemReport.Report10)
        }
        awaitLocationState(Location.SpaceParanoids) {
          assertAuxiliary(expectedHint10)
        }
        awaitRevealedHints(expectedHint1, expectedHint10)
        awaitLastRevealedHint(expectedHint10)

        // Acquire report 9 - reveals that Agrabah -> all proofs
        gameState.acquireItemManually(AnsemReport.Report9, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report1, AnsemReport.Report10, AnsemReport.Report9)
        }
        awaitLocationState(Location.Agrabah) {
          assertAuxiliary(expectedHint9)
        }
        awaitRevealedHints(expectedHint1, expectedHint10, expectedHint9)
        awaitLastRevealedHint(expectedHint9)

        // Acquire report 7 - reveals that HB -> Nonexistence
        gameState.acquireItemManually(AnsemReport.Report7, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report1, AnsemReport.Report10, AnsemReport.Report9, AnsemReport.Report7)
        }
        awaitLocationState(Location.HollowBastion) {
          assertAuxiliary(expectedHint7)
        }
        awaitRevealedHints(expectedHint1, expectedHint10, expectedHint9, expectedHint7)
        awaitLastRevealedHint(expectedHint7)
      }
    }
  }

  @Test
  fun `example progression scenario`() {
    val hintSystem = PathHintSystem(
      hints = listOf(
        hint(
          1,
          hintedLocation = Location.SoraLevels,
          pathToProofs = setOf(Proof.ProofOfNonexistence),
        ),
        hint(
          2,
          hintedLocation = Location.DriveForms,
          pathToProofs = setOf(Proof.ProofOfConnection, Proof.ProofOfPeace),
        ),
        hint(
          3,
          hintedLocation = Location.TwilightTown,
          pathToProofs = emptySet(),
        ),
      ),
      allItemsByLocation = mapOf(Location.WorldThatNeverWas to AnsemReport.entries),
      progressionSettings = PathHintSystem.ProgressionSettings(testProgressionSettings()),
    )

    runTest {
      val seed = testSeed(hintSystem = hintSystem)
      val gameState = testGameState(seed, preferences)

      val expectedHint1 = HintInfo.PathToProofs(
        hintOrReportNumber = 1,
        location = Location.SoraLevels,
        proofs = setOf(Proof.ProofOfNonexistence)
      )
      val expectedHint2 = HintInfo.PathToProofs(
        hintOrReportNumber = 2,
        location = Location.DriveForms,
        proofs = setOf(Proof.ProofOfPeace, Proof.ProofOfConnection)
      )
      val expectedHint3 = HintInfo.PathToProofs(
        hintOrReportNumber = 3,
        location = Location.TwilightTown,
        proofs = emptySet()
      )

      testLocationHints(gameState) {
        // Verify the initial states before doing anything
        for (location in Location.entries) {
          awaitLocationState(location) {
            if (location == Location.GardenOfAssemblage || location == Location.WorldThatNeverWas) {
              assertCounter(LocationCounterState.None)
            } else {
              assertCounter(LocationCounterState.Completed)
            }
            assertAuxiliary(LocationAuxiliaryHintInfo.Blank)
          }
        }
        awaitRevealedHints()
        awaitLastRevealedHint(null)

        // Acquire report 1 - does nothing
        gameState.acquireItemManually(AnsemReport.Report1, Location.WorldThatNeverWas)
        awaitLocationState(Location.WorldThatNeverWas) {
          assertAcquired(AnsemReport.Report1)
        }

        // Make some progress - reveals that levels -> Nonexistence
        gameState.recordProgress(LandOfDragonsProgress.Chests)
        awaitLocationState(Location.LandOfDragons) {
          assertProgress(LandOfDragonsProgress.Chests)
        }
        awaitLocationState(Location.SoraLevels) {
          assertAuxiliary(expectedHint1)
        }
        awaitRevealedHints(expectedHint1)
        awaitLastRevealedHint(expectedHint1)

        // Make some progress - reveals that forms -> Connection and Peace
        gameState.recordProgress(CavernOfRemembranceProgress.LastChest)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertProgress(CavernOfRemembranceProgress.LastChest)
        }
        awaitLocationState(Location.DriveForms) {
          assertAuxiliary(expectedHint2)
        }
        awaitRevealedHints(expectedHint1, expectedHint2)
        awaitLastRevealedHint(expectedHint2)

        // Make some progress - reveals that TT -> no path
        gameState.recordProgress(HundredAcreWoodProgress.Chests)
        awaitLocationState(Location.HundredAcreWood) {
          assertProgress(HundredAcreWoodProgress.Chests)
        }
        awaitLocationState(Location.TwilightTown) {
          assertAuxiliary(expectedHint3)
        }
        awaitRevealedHints(expectedHint1, expectedHint2, expectedHint3)
        awaitLastRevealedHint(expectedHint3)
      }
    }
  }

  companion object {

    fun hint(
      hintOrReportNumber: Int,
      hintedLocation: Location,
      pathToProofs: Set<Proof>,
      journalText: String? = null,
    ): PathHint {
      return PathHint(
        hintOrReportNumber = hintOrReportNumber,
        hintedLocation = hintedLocation,
        pathToProofs = pathToProofs,
        journalText = journalText,
      )
    }

  }

}
