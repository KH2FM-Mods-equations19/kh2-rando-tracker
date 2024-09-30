package com.kh2rando.tracker.io

import com.kh2rando.tracker.auto.ProgressReader
import com.kh2rando.tracker.model.DriveFormsState
import com.kh2rando.tracker.model.progress.DriveFormProgress
import com.kh2rando.tracker.model.progress.ProgressCheckpoint
import com.kh2rando.tracker.model.progress.SoraLevelProgress
import com.kh2rando.tracker.model.seed.assertEmpty
import kotlin.test.Test
import kotlin.test.assertEquals

class LevelsAndDriveFormsProgressTest {

  @Test
  fun `sora levels`() {
    val expectedUpToTen = emptySet<ProgressCheckpoint>()
    val expectedTenUpToTwenty = setOf<ProgressCheckpoint>(SoraLevelProgress.Level10)
    val expectedTwentyUpToThirty = expectedTenUpToTwenty + SoraLevelProgress.Level20
    val expectedThirtyUpToForty = expectedTwentyUpToThirty + SoraLevelProgress.Level30
    val expectedFortyUpToFifty = expectedThirtyUpToForty + SoraLevelProgress.Level40
    val expectedFiftyAndUp = expectedFortyUpToFifty + SoraLevelProgress.Level50

    for (level in 1..99) {
      val expected = when (level) {
        in 1 until 10 -> expectedUpToTen
        in 10 until 20 -> expectedTenUpToTwenty
        in 20 until 30 -> expectedTwentyUpToThirty
        in 30 until 40 -> expectedThirtyUpToForty
        in 40 until 50 -> expectedFortyUpToFifty
        else -> expectedFiftyAndUp
      }
      assertEquals(expected = expected, actual = levelsResult(level), message = "For level $level")
    }
  }

  @Test
  fun `all forms level 1`() {
    val driveFormsState = driveFormsState()
    assertEmpty(formsResult(driveFormsState))
  }

  @Test
  fun `all forms level 7`() {
    val driveFormsState = driveFormsState(
      valorLevel = 7,
      wisdomLevel = 7,
      limitLevel = 7,
      masterLevel = 7,
      finalLevel = 7,
    )
    val allProgress: Set<ProgressCheckpoint> = DriveFormProgress.entries.toSet()
    assertEquals(expected = allProgress, actual = formsResult(driveFormsState))
  }

  @Test
  fun `valor level 4`() {
    val driveFormsState = driveFormsState(
      valorLevel = 4,
    )
    assertEquals(
      expected = setOf<ProgressCheckpoint>(
        DriveFormProgress.Valor2,
        DriveFormProgress.Valor3,
        DriveFormProgress.Valor4,
      ),
      actual = formsResult(driveFormsState)
    )
  }

  @Test
  fun `mixed form levels`() {
    val driveFormsState = driveFormsState(
      valorLevel = 4,
      wisdomLevel = 1,
      limitLevel = 7,
      masterLevel = 2,
      finalLevel = 7,
    )
    assertEquals(
      expected = setOf<ProgressCheckpoint>(
        DriveFormProgress.Valor2,
        DriveFormProgress.Valor3,
        DriveFormProgress.Valor4,
        DriveFormProgress.Limit2,
        DriveFormProgress.Limit3,
        DriveFormProgress.Limit4,
        DriveFormProgress.Limit5,
        DriveFormProgress.Limit6,
        DriveFormProgress.Limit7,
        DriveFormProgress.Master2,
        DriveFormProgress.Final2,
        DriveFormProgress.Final3,
        DriveFormProgress.Final4,
        DriveFormProgress.Final5,
        DriveFormProgress.Final6,
        DriveFormProgress.Final7,
      ),
      actual = formsResult(driveFormsState)
    )
  }

  private fun levelsResult(level: Int): Set<ProgressCheckpoint> {
    return buildSet { ProgressReader.populateLevelProgress(level, this) }
  }

  private fun formsResult(driveFormsState: DriveFormsState): Set<ProgressCheckpoint> {
    return buildSet { ProgressReader.populateDriveFormProgress(driveFormsState, this) }
  }

  private fun driveFormsState(
    valorLevel: Int = 1,
    wisdomLevel: Int = 1,
    limitLevel: Int = 1,
    masterLevel: Int = 1,
    finalLevel: Int = 1,
  ): DriveFormsState {
    return DriveFormsState(
      currentDriveGauge = 3,
      maximumDriveGauge = 3,
      valorLevel = valorLevel,
      wisdomLevel = wisdomLevel,
      limitLevel = limitLevel,
      masterLevel = masterLevel,
      finalLevel = finalLevel,
      valorAcquired = false,
      wisdomAcquired = false,
      limitAcquired = false,
      masterAcquired = false,
      finalAcquired = false,
      finalForced = false,
    )
  }

}
