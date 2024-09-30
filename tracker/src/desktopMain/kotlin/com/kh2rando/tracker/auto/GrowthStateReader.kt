package com.kh2rando.tracker.auto

import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.GrowthState
import com.kh2rando.tracker.model.item.GrowthAbilityPrototype

/**
 * Reads the state of each growth ability.
 */
class GrowthStateReader(private val gameProcess: GameProcess) {

  private val addresses = gameProcess.addresses
  private val highJumpAddress = GrowthAbilityPrototype.HighJump.inventoryAddress(addresses)
  private val quickRunAddress = GrowthAbilityPrototype.QuickRun.inventoryAddress(addresses)
  private val dodgeRollAddress = GrowthAbilityPrototype.DodgeRoll.inventoryAddress(addresses)
  private val aerialDodgeAddress = GrowthAbilityPrototype.AerialDodge.inventoryAddress(addresses)
  private val glideAddress = GrowthAbilityPrototype.Glide.inventoryAddress(addresses)

  /**
   * Reads the state of each growth ability.
   */
  fun readGrowthState(): GrowthState {
    return GrowthState(
      highJumpLevel = readGrowthLevel(GrowthAbilityPrototype.HighJump, highJumpAddress),
      quickRunLevel = readGrowthLevel(GrowthAbilityPrototype.QuickRun, quickRunAddress),
      dodgeRollLevel = readGrowthLevel(GrowthAbilityPrototype.DodgeRoll, dodgeRollAddress),
      aerialDodgeLevel = readGrowthLevel(GrowthAbilityPrototype.AerialDodge, aerialDodgeAddress),
      glideLevel = readGrowthLevel(GrowthAbilityPrototype.Glide, glideAddress),
    )
  }

  // Note: Logic here pieced together from AccurateGrowthText Lua script
  private fun readGrowthLevel(growth: GrowthAbilityPrototype, address: Address): Int {
    val memoryValue = gameProcess.readShortAsInt(address) and 0x0FFF
    val level = memoryValue - growth.levelOffset
    return if (level < 0) 0 else level
  }

}
