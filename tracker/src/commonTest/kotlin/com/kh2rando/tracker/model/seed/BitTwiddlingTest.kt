package com.kh2rando.tracker.model.seed

import com.kh2rando.tracker.auto.isSetByMask
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BitTwiddlingTest {

  @Test
  fun isSetByMaskAllZeroes() {
    val value = 0b00000000
    assertFalse(value.isSetByMask(0x01))
    assertFalse(value.isSetByMask(0x02))
    assertFalse(value.isSetByMask(0x04))
    assertFalse(value.isSetByMask(0x08))
    assertFalse(value.isSetByMask(0x10))
    assertFalse(value.isSetByMask(0x20))
    assertFalse(value.isSetByMask(0x40))
    assertFalse(value.isSetByMask(0x80))
  }

  @Test
  fun isSetByMaskAllOnes() {
    val value = 0b11111111
    assertTrue(value.isSetByMask(0x01))
    assertTrue(value.isSetByMask(0x02))
    assertTrue(value.isSetByMask(0x04))
    assertTrue(value.isSetByMask(0x08))
    assertTrue(value.isSetByMask(0x10))
    assertTrue(value.isSetByMask(0x20))
    assertTrue(value.isSetByMask(0x40))
    assertTrue(value.isSetByMask(0x80))
  }

  @Test
  fun isSetByMaskKnownExample() {
    val value = 0b00001111
    assertTrue(value.isSetByMask(0x01))
    assertTrue(value.isSetByMask(0x02))
    assertTrue(value.isSetByMask(0x04))
    assertTrue(value.isSetByMask(0x08))
    assertFalse(value.isSetByMask(0x10))
    assertFalse(value.isSetByMask(0x20))
    assertFalse(value.isSetByMask(0x40))
    assertFalse(value.isSetByMask(0x80))
  }

}
