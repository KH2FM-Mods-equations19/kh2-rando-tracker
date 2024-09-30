package com.kh2rando.tracker.io

import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class AutoSaveFileTimestampTest {

  private val zone = ZoneId.of("America/Chicago")

  @Test
  fun `scenario that was broken at one time`() {
    val testTime = ZonedDateTime.of(
      LocalDate.of(2024, Month.OCTOBER, 25),
      LocalTime.of(0, 35, 1),
      zone,
    )
    val result = TrackerFileHandler.autoSaveFileTimestamp(testTime)
    assertEquals(expected = "20241025-003501", actual = result)
  }

  @Test
  fun `just before midnight`() {
    val testTime = ZonedDateTime.of(
      LocalDate.of(2024, Month.OCTOBER, 24),
      LocalTime.of(23, 59, 59),
      zone,
    )
    val result = TrackerFileHandler.autoSaveFileTimestamp(testTime)
    assertEquals(expected = "20241024-235959", actual = result)
  }

  @Test
  fun `exactly midnight`() {
    val testTime = ZonedDateTime.of(
      LocalDate.of(2024, Month.OCTOBER, 25),
      LocalTime.of(0, 0, 0),
      zone,
    )
    val result = TrackerFileHandler.autoSaveFileTimestamp(testTime)
    assertEquals(expected = "20241025-000000", actual = result)
  }

}
