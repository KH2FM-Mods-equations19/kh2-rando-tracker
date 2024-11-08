package com.kh2rando.tracker.auto

import com.kh2rando.tracker.model.SoraState
import com.kh2rando.tracker.model.item.DreamWeapon

/**
 * Reads and interprets a [SoraState].
 */
class SoraStateReader(private val gameProcess: GameProcess) {

  private val addresses = gameProcess.addresses
  private val levelAddress = addresses.save + 0x24FE
  private val statsAddress = addresses.slot1 + 0x188
//  private val formAddress = addresses.save + 0x3524
//  private val bonusAddress = addresses.save + 0x3700
  private val munnyAddress = addresses.save + 0x2440
//  private val nextSlot = addresses.nextSlot

  /**
   * Reads and interprets a [SoraState].
   */
  fun readSoraState(): SoraState {
    val (dreamWeaponData, level) = gameProcess.readBytes(levelAddress, 2)
    val dreamWeapon = when (dreamWeaponData.toInt()) {
      1 -> DreamWeapon.Shield
      2 -> DreamWeapon.Staff
      else -> DreamWeapon.Sword
    }

    val statsData = gameProcess.readBytes(statsAddress, 5)
    val strength = statsData[0].toInt()
    val magic = statsData[2].toInt()
    val defense = statsData[4].toInt()

    val munny = gameProcess.readShortAsInt(munnyAddress)

    return SoraState(
      dreamWeapon = dreamWeapon,
      currentLevel = level.toInt(),
      strengthStat = strength,
      magicStat = magic,
      defenseStat = defense,
      munny = munny,
    )
  }

}
