package com.kh2rando.tracker.auto

import androidx.compose.ui.util.fastForEach
import com.kh2rando.tracker.model.item.BitmaskedInventory
import com.kh2rando.tracker.model.item.ImportantAbility
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.RealForm
import com.kh2rando.tracker.model.item.RepeatableInventory
import com.kh2rando.tracker.model.item.TornPage
import com.kh2rando.tracker.model.progress.HundredAcreWoodProgress

/**
 * Reads and determines the current inventory.
 */
class InventoryReader(
  private val gameProcess: GameProcess,
  trackableItems: Set<ItemPrototype>,
) {

  private val addresses = gameProcess.addresses
  private val abilityDataAddress = addresses.save + 0x2544
  private val hundredAcreFirstScenariosAddress = addresses.save + 0x1DB7
  private val hundredAcreSecondScenariosAddress = addresses.save + 0x1DB8

  private val fullInventorySize = ItemPrototype.fullList.size
  private val trackableItemsList = trackableItems.toList()
  private val trackTornPages = TornPage in trackableItems

  /**
   * Reads and determines the current inventory.
   */
  fun readInventory(): MutableList<ItemPrototype> {
    // Size the list up front to the full size to avoid ArrayList resizing
    val inventory = ArrayList<ItemPrototype>(fullInventorySize)

    trackableItemsList.fastForEach { item ->
      if (item is RepeatableInventory) {
        val acquiredItemCount = gameProcess.readByteAsInt(item.inventoryCountAddress(addresses))
        repeat(acquiredItemCount) {
          inventory.add(item)
        }
      } else if (item is BitmaskedInventory) {
        if (itemAcquired(item)) {
          inventory.add(item)
        }
      }
    }

    // Second Chance and Once More seem to be determined by their presence in this data structure.
    // The values at (i + 1) are 0x01 if obtained, 0x81 if equipped.
    val abilityData = gameProcess.readBytes(abilityDataAddress, 158)
    for (i in abilityData.indices step 2) {
      val thisAbilityData = abilityData[i]
      if (thisAbilityData == 0x9F.toByte()) {
        inventory.add(ImportantAbility.SecondChance)
      }
      if (thisAbilityData == 0xA0.toByte()) {
        inventory.add(ImportantAbility.OnceMore)
      }
    }

    // Add additional "acquired" Torn Pages if needed to compensate for turned in pages in Hundred Acre Wood
    if (trackTornPages) {
      repeat(tornPagesTurnedIn()) {
        inventory.add(TornPage)
      }
    }

    // Include real Final Form temporarily to help determine if we forced, even though it's never trackable.
    // This will get removed in a later step in the auto-tracking.
    if (itemAcquired(RealForm.Final)) {
      inventory.add(RealForm.Final)
    }

    return inventory
  }

  private fun itemAcquired(item: BitmaskedInventory): Boolean {
    val byte = gameProcess.readByteAsInt(item.inventoryBitmaskAddress(addresses))
    return byte.isSetByMask(item.inventoryBitmask)
  }

  /**
   * Computes the number of torn pages that have already been turned in, since they are removed
   * from inventory when starting the next "visit" of Hundred Acre Wood.
   */
  private fun tornPagesTurnedIn(): Int {
    val firstByte = gameProcess.readByteAsInt(hundredAcreFirstScenariosAddress)
    val secondByte = gameProcess.readByteAsInt(hundredAcreSecondScenariosAddress)

    var scenariosStarted = 0
    if (firstByte.isSetByMask(HundredAcreWoodProgress.Flag.PO_SCENARIO_1_START.mask)) scenariosStarted++
    if (firstByte.isSetByMask(HundredAcreWoodProgress.Flag.PO_SCENARIO_2_START.mask)) scenariosStarted++
    if (secondByte.isSetByMask(HundredAcreWoodProgress.Flag.PO_SCENARIO_3_START.mask)) scenariosStarted++
    if (secondByte.isSetByMask(HundredAcreWoodProgress.Flag.PO_SCENARIO_4_START.mask)) scenariosStarted++
    if (secondByte.isSetByMask(HundredAcreWoodProgress.Flag.PO_SCENARIO_5_START.mask)) scenariosStarted++
    return scenariosStarted
  }

}
