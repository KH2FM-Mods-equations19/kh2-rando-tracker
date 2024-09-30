package com.kh2rando.tracker.model.seed

import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.Magic
import com.kh2rando.tracker.model.item.PromiseCharm
import com.kh2rando.tracker.model.seed.LevelChecks.Companion.checksBetween
import kotlin.test.Test
import kotlin.test.assertEquals

class LevelChecksTest {

  private val levels = List(99) { mutableListOf<ItemPrototype>() }

  @Test
  fun nothingOnLevelTwo() {
    add(3, PromiseCharm)
    assertEmpty(levels.checksBetween(previousLevel = 1, newLevel = 2))
  }

  @Test
  fun somethingOnLevelTwo() {
    add(2, PromiseCharm)
    assertEquals(expected = listOf(PromiseCharm), actual = levels.checksBetween(previousLevel = 1, newLevel = 2))
  }

  @Test
  fun singleLevelUps() {
    add(5, Magic.Fire)
    add(6, Magic.Blizzard)
    add(7, Magic.Thunder)
    add(8, Magic.Cure)

    assertEquals(expected = listOf(Magic.Fire), levels.checksBetween(previousLevel = 4, newLevel = 5))
    assertEquals(expected = listOf(Magic.Blizzard), levels.checksBetween(previousLevel = 5, newLevel = 6))
    assertEquals(expected = listOf(Magic.Thunder), levels.checksBetween(previousLevel = 6, newLevel = 7))
    assertEquals(expected = listOf(Magic.Cure), levels.checksBetween(previousLevel = 7, newLevel = 8))
  }

  @Test
  fun multiLevelUps() {
    add(5, Magic.Fire)
    add(6, Magic.Blizzard)
    add(7, Magic.Thunder)
    add(8, Magic.Cure)

    assertEmpty(levels.checksBetween(previousLevel = 1, newLevel = 4))
    assertEquals(
      expected = listOf(Magic.Fire),
      levels.checksBetween(previousLevel = 1, newLevel = 5)
    )
    assertEquals(
      expected = listOf(Magic.Fire, Magic.Blizzard),
      levels.checksBetween(previousLevel = 1, newLevel = 6)
    )
    assertEquals(
      expected = listOf(Magic.Fire, Magic.Blizzard, Magic.Thunder),
      levels.checksBetween(previousLevel = 1, newLevel = 7)
    )
    assertEquals(
      expected = listOf(Magic.Fire, Magic.Blizzard, Magic.Thunder, Magic.Cure),
      levels.checksBetween(previousLevel = 1, newLevel = 8)
    )
    assertEquals(
      expected = listOf(Magic.Fire, Magic.Blizzard, Magic.Thunder, Magic.Cure),
      levels.checksBetween(previousLevel = 1, newLevel = 9)
    )

    assertEquals(
      expected = listOf(Magic.Blizzard, Magic.Thunder),
      levels.checksBetween(previousLevel = 5, newLevel = 7)
    )
  }

  private fun add(level: Int, item: ItemPrototype) {
    levels[level - 1].add(item)
  }

}
