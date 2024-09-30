package com.kh2rando.tracker.model.seed

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.hints.DisabledHint
import com.kh2rando.tracker.model.hints.DisabledHintSystem
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.hints.LocationAuxiliaryHintInfo
import com.kh2rando.tracker.model.item.AnsemReport
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Test
import kotlin.test.assertEquals

class DisabledHintSystemTest {

  @get:Rule
  val temporaryFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

  private val preferences = testPreferences(temporaryFolder)

  @Test
  fun `basic test`() {
    val hintSystem = DisabledHintSystem(
      hints = listOf(
        DisabledHint(hintOrReportNumber = 1, journalText = "Journal 1"),
        DisabledHint(hintOrReportNumber = 2, journalText = "Journal 2"),
        DisabledHint(hintOrReportNumber = 3, journalText = "Journal 3"),
        DisabledHint(hintOrReportNumber = 4, journalText = "Journal 4"),
        DisabledHint(hintOrReportNumber = 5, journalText = "Journal 5"),
        DisabledHint(hintOrReportNumber = 6, journalText = "Journal 6"),
        DisabledHint(hintOrReportNumber = 7, journalText = "Journal 7"),
        DisabledHint(hintOrReportNumber = 8, journalText = "Journal 8"),
        DisabledHint(hintOrReportNumber = 9, journalText = "Journal 9"),
        DisabledHint(hintOrReportNumber = 10, journalText = "Journal 10"),
        DisabledHint(hintOrReportNumber = 11, journalText = "Journal 11"),
        DisabledHint(hintOrReportNumber = 12, journalText = "Journal 12"),
        DisabledHint(hintOrReportNumber = 13, journalText = "Journal 13"),
      )
    )

    runTest {
      val seed = testSeed(hintSystem = hintSystem)
      val gameState = testGameState(seed, preferences)
      testLocationHints(gameState) {
        for (location in Location.entries) {
          awaitLocationState(location) {
            assertEquals(expected = LocationCounterState.None, counterState)
            assertEquals(expected = LocationAuxiliaryHintInfo.NotApplicableToHintSystem, actual = auxiliaryHintInfo)
          }
        }
        awaitRevealedHints()
        awaitLastRevealedHint(null)

        for (report in AnsemReport.entries) {
          gameState.acquireItemManually(report, Location.GardenOfAssemblage)
        }

        // Skip all the intermediates for each report - we'll just assert against the final one
        skip(Location.GardenOfAssemblage, 12)
        awaitLocationState(Location.GardenOfAssemblage) {
          assertAcquired(*AnsemReport.entries.toTypedArray())
        }

        for (report in AnsemReport.entries) {
          val reportNumber = report.reportNumber
          assertEquals(
            expected = HintInfo.JournalTextOnly(
              hintOrReportNumber = reportNumber,
              journalText = "Journal $reportNumber"
            ),
            actual = hintSystem.hintInfoForAcquiredReport(report),
          )
        }
      }
    }
  }

}
