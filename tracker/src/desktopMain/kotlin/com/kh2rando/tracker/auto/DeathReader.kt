package com.kh2rando.tracker.auto

/**
 * Reads information about whether Sora is dead.
 */
class DeathReader(private val gameProcess: GameProcess) {

  private val abilityToPauseAddress = gameProcess.addresses.abilityToPause

  /**
   * Reads the current death state.
   */
  fun isSoraDead(): Boolean {
    val value = gameProcess.readShortAsInt(abilityToPauseAddress) and 0xFF
    // Reminder: 04 = dying, 05 = continue screen
    return value == 0x04 || value == 0x05
  }

}
