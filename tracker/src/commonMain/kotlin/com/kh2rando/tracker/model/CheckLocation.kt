package com.kh2rando.tracker.model

/**
 * Category for check locations. Matches the set from the seed generator.
 */
enum class CheckLocationCategory(
  /**
   * The name used for this category in a hint file from the seed generator.
   */
  val hintFileJsonName: String
) {

  Chest(hintFileJsonName = "Chest"),
  Popup(hintFileJsonName = "Popup"),
  Creation(hintFileJsonName = "Creation"),
  ItemBonus(hintFileJsonName = "Item Bonus"),
  StatBonus(hintFileJsonName = "Stat Bonus"),
  ItemAndStatBonus(hintFileJsonName = "Item and Stat Bonus"),
  DoubleStatBonus(hintFileJsonName = "Double Stat Bonus"),
  Level(hintFileJsonName = "Level"),
  SummonLevel(hintFileJsonName = "Summon Level"),
  ValorLevel(hintFileJsonName = "Valor Level"),
  WisdomLevel(hintFileJsonName = "Wisdom Level"),
  LimitLevel(hintFileJsonName = "Limit Level"),
  MasterLevel(hintFileJsonName = "Master Level"),
  FinalLevel(hintFileJsonName = "Final Level"),
  WeaponSlot(hintFileJsonName = "Weapon Slot");

}

/**
 * A location where an item can be obtained.
 */
data class CheckLocation(val category: CheckLocationCategory, val id: Int)
