package com.kh2rando.tracker.auto

/**
 * Reads the number of acquired Lucky Emblems.
 */
class EmblemsReader(private val gameProcess: GameProcess) {

  private val emblemAddress = gameProcess.addresses.save + 0x363D

  /**
   * Reads the number of acquired Lucky Emblems.
   */
  fun readEmblemCount(): Int {
    return gameProcess.readByteAsInt(emblemAddress)
  }

}
