package com.kh2rando.tracker.model.seed

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.hints.LocationAuxiliaryHintInfo
import com.kh2rando.tracker.model.hints.PointsHint
import com.kh2rando.tracker.model.hints.PointsHintSystem
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.DriveForm
import com.kh2rando.tracker.model.item.ImportantAbility
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.Magic
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.item.SummonCharm
import com.kh2rando.tracker.model.item.TornPage
import com.kh2rando.tracker.model.progress.DriveFormProgress
import com.kh2rando.tracker.model.progress.HollowBastionProgress
import com.kh2rando.tracker.model.progress.HundredAcreWoodProgress
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PointsHintSystemTest {

  @get:Rule
  val temporaryFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

  private val preferences = testPreferences(temporaryFolder)

  @Test
  fun isValidReportLocation() {
    // Note: for points hints it doesn't matter whether it's progression or not for isValidReportLocation()
    val hintSystem = PointsHintSystem(
      hints = emptyList(),
      allItemsByLocation = mapOf(
        Location.OlympusColiseum to listOf(AnsemReport.Report6),
        Location.DisneyCastle to listOf(AnsemReport.Report11),
      ),
      pointValuesByPrototype = samplePointDistribution(),
      progressionSettings = null,
    )
    assertTrue(hintSystem.isValidReportLocation(Location.OlympusColiseum, AnsemReport.Report6))
    assertFalse(hintSystem.isValidReportLocation(Location.SoraLevels, AnsemReport.Report6))
    assertFalse(hintSystem.isValidReportLocation(Location.WorldThatNeverWas, AnsemReport.Report11))
    assertTrue(hintSystem.isValidReportLocation(Location.DisneyCastle, AnsemReport.Report11))
  }

  @Test
  fun `example report scenario with auto math enabled`() {
    val hintSystem = PointsHintSystem(
      hints = listOf(
        hint(
          1,
          hintedLocation = Location.SoraLevels,
          revealedItem = Magic.Fire,
        ),
        hint(
          2,
          hintedLocation = Location.DriveForms,
          revealedItem = Magic.Blizzard,
        ),
        hint(
          3,
          hintedLocation = Location.TwilightTown,
          revealedItem = Magic.Thunder,
        ),
        hint(
          4,
          hintedLocation = Location.Atlantica,
          revealedItem = Magic.Cure,
        ),
        hint(
          5,
          hintedLocation = Location.Creations,
          revealedItem = Magic.Reflect,
        ),
        hint(
          6,
          hintedLocation = Location.OlympusColiseum,
          revealedItem = Magic.Magnet,
        ),
        hint(
          7,
          hintedLocation = Location.SoraLevels,
          revealedItem = SummonCharm.BaseballCharm,
        ),
        hint(
          8,
          hintedLocation = Location.BeastsCastle,
          revealedItem = SummonCharm.UkuleleCharm,
        ),
        hint(
          9,
          hintedLocation = Location.Agrabah,
          revealedItem = SummonCharm.LampCharm,
        ),
        hint(
          10,
          hintedLocation = Location.SpaceParanoids,
          revealedItem = SummonCharm.FeatherCharm,
        ),
        hint(
          11,
          hintedLocation = Location.DisneyCastle,
          revealedItem = ImportantAbility.SecondChance,
        ),
        hint(
          12,
          hintedLocation = Location.HundredAcreWood,
          revealedItem = Magic.Fire,
        ),
        hint(
          13,
          hintedLocation = Location.HalloweenTown,
          revealedItem = DriveForm.WisdomForm,
        ),
      ),
      allItemsByLocation = mapOf(
        Location.GardenOfAssemblage to AnsemReport.entries,
        Location.SoraLevels to listOf(Proof.ProofOfConnection, SummonCharm.BaseballCharm, Magic.Fire),
        Location.Agrabah to listOf(SummonCharm.LampCharm),
        Location.SpaceParanoids to listOf(
          DriveForm.FinalFormDummy,
          DriveForm.LimitForm,
          SummonCharm.FeatherCharm,
          Magic.Cure,
        ),
      ),
      pointValuesByPrototype = samplePointDistribution(),
      progressionSettings = null,
    )

    runTest {
      preferences.pointsAutoMath.save(value = true)

      val seed = testSeed(hintSystem = hintSystem)
      val gameState = testGameState(seed, preferences)

      val expectedHint1 = HintInfo.ItemLocation(
        hintOrReportNumber = 1,
        location = Location.SoraLevels,
        item = Magic.Fire,
      )
      val expectedHint7 = HintInfo.ItemLocation(
        hintOrReportNumber = 7,
        location = Location.SoraLevels,
        item = SummonCharm.BaseballCharm,
      )
      val expectedHint9 = HintInfo.ItemLocation(
        hintOrReportNumber = 9,
        location = Location.Agrabah,
        item = SummonCharm.LampCharm,
      )
      val expectedHint10 = HintInfo.ItemLocation(
        hintOrReportNumber = 10,
        location = Location.SpaceParanoids,
        item = SummonCharm.FeatherCharm,
      )

      testLocationHints(gameState) {
        // Verify the initial states before doing anything
        for (location in Location.entries) {
          awaitLocationState(location) {
            when (location) {
              Location.GardenOfAssemblage -> assertCounter(LocationCounterState.None)
              Location.SoraLevels -> assertCounter(LocationCounterState.Revealed(17))
              Location.Agrabah -> assertCounter(LocationCounterState.Revealed(5))
              Location.SpaceParanoids -> assertCounter(LocationCounterState.Revealed(30))
              else -> assertCounter(LocationCounterState.Completed)
            }
            assertEmpty(revealedItems)
            assertAuxiliary(LocationAuxiliaryHintInfo.Blank)
          }
        }
        awaitRevealedHints()
        awaitLastRevealedHint(null)

        // Acquire report 1 - reveals that levels has Fire and adjusts levels' counter
        gameState.acquireItemManually(AnsemReport.Report1, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report1)
        }
        // We get three location updates, one for the new item in revealed, one with the updated counter, one with the
        // updated auxiliary.
        skip(Location.SoraLevels, 2)
        awaitLocationState(Location.SoraLevels) {
          assertRevealed(Magic.Fire)
          assertCounter(LocationCounterState.Revealed(10, adjustedByRevealedItems = true))
          assertAuxiliary(LocationAuxiliaryHintInfo.CountAdjustedByRevealedItems)
        }
        awaitRevealedHints(expectedHint1)
        awaitLastRevealedHint(expectedHint1)

        // Acquire Limit Form - adjusts SP's counter
        // Unfortunately right now we get two updates, one for the new item in acquired, and one for the counter update
        gameState.acquireItemManually(DriveForm.LimitForm, Location.SpaceParanoids)
        skip(Location.SpaceParanoids, 1)
        awaitLocationState(Location.SpaceParanoids) {
          assertAcquired(DriveForm.LimitForm)
          assertEmpty(revealedItems)
          assertCounter(LocationCounterState.Revealed(21))
          assertAuxiliary(LocationAuxiliaryHintInfo.Blank)
        }

        // Acquire report 10 - reveals that SP has Feather Charm and adjusts SP's counter
        // Unfortunately right now we get the three updates
        gameState.acquireItemManually(AnsemReport.Report10, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report1, AnsemReport.Report10)
        }
        skip(Location.SpaceParanoids, 2)
        awaitLocationState(Location.SpaceParanoids) {
          assertAcquired(DriveForm.LimitForm)
          assertRevealed(SummonCharm.FeatherCharm)
          assertCounter(LocationCounterState.Revealed(16, adjustedByRevealedItems = true))
          assertAuxiliary(LocationAuxiliaryHintInfo.CountAdjustedByRevealedItems)
        }
        awaitRevealedHints(expectedHint1, expectedHint10)
        awaitLastRevealedHint(expectedHint10)

        // Acquire report 9 - reveals that Agrabah has Lamp Charm
        gameState.acquireItemManually(AnsemReport.Report9, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report1, AnsemReport.Report10, AnsemReport.Report9)
        }
        skip(Location.Agrabah, 2)
        awaitLocationState(Location.Agrabah) {
          assertRevealed(SummonCharm.LampCharm)
          assertCounter(LocationCounterState.Revealed(0, adjustedByRevealedItems = true))
          assertAuxiliary(LocationAuxiliaryHintInfo.CountAdjustedByRevealedItems)
        }
        awaitRevealedHints(expectedHint1, expectedHint10, expectedHint9)
        awaitLastRevealedHint(expectedHint9)

        // Acquire Lamp Charm - adjusts AG's counter to done
        gameState.acquireItemManually(SummonCharm.LampCharm, Location.Agrabah)
        skip(Location.Agrabah, 2)
        awaitLocationState(Location.Agrabah) {
          assertAcquired(SummonCharm.LampCharm)
          assertRevealed(SummonCharm.LampCharm)
          assertCounter(LocationCounterState.Completed)
          assertAuxiliary(LocationAuxiliaryHintInfo.Blank)
        }

        // Acquire report 7 - reveals that levels has Baseball Charm
        gameState.acquireItemManually(AnsemReport.Report7, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertEquals(
            expected = listOf(AnsemReport.Report1, AnsemReport.Report10, AnsemReport.Report9, AnsemReport.Report7),
            acquiredPrototypes
          )
        }
        skip(Location.SoraLevels, 1)
        awaitLocationState(Location.SoraLevels) {
          assertRevealed(Magic.Fire, SummonCharm.BaseballCharm)
          assertCounter(LocationCounterState.Revealed(5, adjustedByRevealedItems = true))
          assertAuxiliary(LocationAuxiliaryHintInfo.CountAdjustedByRevealedItems)
        }
        awaitRevealedHints(expectedHint1, expectedHint10, expectedHint9, expectedHint7)
        awaitLastRevealedHint(expectedHint7)

        // Acquire Baseball Charm - adjusts levels' counter
        gameState.acquireItemManually(SummonCharm.BaseballCharm, Location.SoraLevels)
        // We don't have to skip one here because the counterState doesn't actually update.
        // We still have 5 points left, adjusted for the fire.
        awaitLocationState(Location.SoraLevels) {
          assertAcquired(SummonCharm.BaseballCharm)
          assertRevealed(Magic.Fire, SummonCharm.BaseballCharm)
          assertCounter(LocationCounterState.Revealed(5, adjustedByRevealedItems = true))
          assertAuxiliary(LocationAuxiliaryHintInfo.CountAdjustedByRevealedItems)
        }

        // Acquire Proof of Connection - adjusts levels' counter
        gameState.acquireItemManually(Proof.ProofOfConnection, Location.SoraLevels)
        skip(Location.SoraLevels, 1)
        awaitLocationState(Location.SoraLevels) {
          assertAcquired(SummonCharm.BaseballCharm, Proof.ProofOfConnection)
          assertRevealed(Magic.Fire, SummonCharm.BaseballCharm)
          assertCounter(LocationCounterState.Revealed(0, adjustedByRevealedItems = true))
          assertAuxiliary(LocationAuxiliaryHintInfo.CountAdjustedByRevealedItems)
        }

        // Acquire Feather Charm - adjusts SP's counter
        gameState.acquireItemManually(SummonCharm.FeatherCharm, Location.SpaceParanoids)
        skip(Location.SpaceParanoids, 2)
        awaitLocationState(Location.SpaceParanoids) {
          assertAcquired(DriveForm.LimitForm, SummonCharm.FeatherCharm)
          assertRevealed(SummonCharm.FeatherCharm)
          assertCounter(LocationCounterState.Revealed(16))
          assertAuxiliary(LocationAuxiliaryHintInfo.Blank)
        }

        // Acquire Fire - adjusts levels' counter
        gameState.acquireItemManually(Magic.Fire, Location.SoraLevels)
        skip(Location.SoraLevels, 2)
        awaitLocationState(Location.SoraLevels) {
          assertAcquired(SummonCharm.BaseballCharm, Proof.ProofOfConnection, Magic.Fire)
          assertRevealed(Magic.Fire, SummonCharm.BaseballCharm)
          assertCounter(LocationCounterState.Completed)
          assertAuxiliary(LocationAuxiliaryHintInfo.Blank)
        }
      }
    }
  }

  @Test
  fun `example progression scenario`() {
    val hintSystem = PointsHintSystem(
      hints = listOf(
        hint(
          1,
          hintedLocation = Location.SoraLevels,
          revealedItem = Magic.Fire,
        ),
        hint(
          2,
          hintedLocation = Location.DriveForms,
          revealedItem = Magic.Blizzard,
        ),
        hint(
          3,
          hintedLocation = Location.TwilightTown,
          revealedItem = Magic.Thunder,
        ),
      ),
      allItemsByLocation = mapOf(
        Location.GardenOfAssemblage to AnsemReport.entries,
        Location.SoraLevels to listOf(Proof.ProofOfConnection, SummonCharm.BaseballCharm, Magic.Fire),
        Location.DriveForms to listOf(Magic.Reflect, Magic.Reflect, Magic.Blizzard, Magic.Blizzard),
        Location.Agrabah to listOf(SummonCharm.LampCharm),
        Location.TwilightTown to listOf(Magic.Thunder),
      ),
      pointValuesByPrototype = samplePointDistribution(),
      progressionSettings = PointsHintSystem.ProgressionSettings(
        testProgressionSettings(),
        locationRevealOrder = listOf(Location.Agrabah, Location.SoraLevels, Location.TwilightTown, Location.DriveForms),
      ),
    )

    runTest {
      val seed = testSeed(hintSystem = hintSystem)
      val gameState = testGameState(seed, preferences)

      val expectedHint1 = HintInfo.ItemLocation(
        hintOrReportNumber = 2,
        location = Location.DriveForms,
        item = Magic.Blizzard,
      )
      val expectedHint2 = HintInfo.PointsCount(
        hintOrReportNumber = 1,
        location = Location.Agrabah,
        points = 5
      )
      val expectedHint3 = HintInfo.PointsCount(
        hintOrReportNumber = 2,
        location = Location.SoraLevels,
        points = 17
      )
      val expectedHint4 = HintInfo.ItemLocation(
        hintOrReportNumber = 1,
        location = Location.SoraLevels,
        item = Magic.Fire,
      )
      val expectedHint5 = HintInfo.PointsCount(
        hintOrReportNumber = 3,
        location = Location.TwilightTown,
        points = 7
      )

      testLocationHints(gameState) {
        // Verify the initial states before doing anything
        for (location in Location.entries) {
          awaitLocationState(location) {
            if (location == Location.GardenOfAssemblage) {
              assertCounter(LocationCounterState.None)
            } else {
              assertCounter(LocationCounterState.Unrevealed)
            }
            assertEmpty(revealedItems)
            assertAuxiliary(LocationAuxiliaryHintInfo.Blank)
          }
        }
        awaitRevealedHints()
        awaitLastRevealedHint(null)

        // Acquire report 2 - reveals Blizzard on drive forms
        gameState.acquireItemManually(AnsemReport.Report2, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report2)
        }
        awaitLocationState(Location.DriveForms) {
          assertRevealed(Magic.Blizzard)
        }
        awaitRevealedHints(expectedHint1)
        awaitLastRevealedHint(expectedHint1)

        // Make some progress - reveals points for Agrabah
        gameState.recordProgress(HundredAcreWoodProgress.StarryHill)
        awaitLocationState(Location.HundredAcreWood) {
          assertProgress(HundredAcreWoodProgress.StarryHill)
        }
        awaitLocationState(Location.Agrabah) {
          assertCounter(LocationCounterState.Revealed(5))
          assertAuxiliary(LocationAuxiliaryHintInfo.Blank)
        }
        awaitRevealedHints(expectedHint1, expectedHint2)
        awaitLastRevealedHint(expectedHint2)

        // Make some progress - reveals points for levels
        gameState.recordProgress(DriveFormProgress.Final2)
        awaitLocationState(Location.DriveForms) {
          assertProgress(DriveFormProgress.Final2)
        }
        awaitLocationState(Location.SoraLevels) {
          assertCounter(LocationCounterState.Revealed(17))
          assertAuxiliary(LocationAuxiliaryHintInfo.Blank)
        }
        awaitRevealedHints(expectedHint1, expectedHint2, expectedHint3)
        awaitLastRevealedHint(expectedHint3)

        // Acquire report 1 - reveals Fire on levels and adjusts points
        gameState.acquireItemManually(AnsemReport.Report1, Location.GardenOfAssemblage)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(AnsemReport.Report2, AnsemReport.Report1)
        }
        skip(Location.SoraLevels, 2)
        awaitLocationState(Location.SoraLevels) {
          assertRevealed(Magic.Fire)
          assertCounter(LocationCounterState.Revealed(10, adjustedByRevealedItems = true))
          assertAuxiliary(LocationAuxiliaryHintInfo.CountAdjustedByRevealedItems)
        }
        awaitRevealedHints(expectedHint1, expectedHint2, expectedHint3, expectedHint4)
        awaitLastRevealedHint(expectedHint4)

        // Acquire Thunder - completes TT but not revealed yet
        gameState.acquireItemManually(Magic.Thunder, Location.TwilightTown)
        awaitLocationState(Location.TwilightTown) {
          assertAcquired(Magic.Thunder)
          assertAuxiliary(LocationAuxiliaryHintInfo.Blank)
        }

        // Make some progress - reveals TT as complete now
        gameState.recordProgress(HollowBastionProgress.Sephiroth)
        awaitLocationState(Location.HollowBastion) {
          assertProgress(HollowBastionProgress.Sephiroth)
        }
        awaitLocationState(Location.TwilightTown) {
          assertCounter(LocationCounterState.Completed)
          assertAuxiliary(LocationAuxiliaryHintInfo.Blank)
        }
        awaitRevealedHints(expectedHint1, expectedHint2, expectedHint3, expectedHint4, expectedHint5)
        awaitLastRevealedHint(expectedHint5)
      }
    }
  }

  companion object {

    fun hint(
      hintOrReportNumber: Int,
      hintedLocation: Location,
      revealedItem: ItemPrototype,
      journalText: String? = null,
    ): PointsHint {
      return PointsHint(
        hintOrReportNumber = hintOrReportNumber,
        hintedLocation = hintedLocation,
        revealedItem = revealedItem,
        journalText = journalText,
      )
    }

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

  }

}
