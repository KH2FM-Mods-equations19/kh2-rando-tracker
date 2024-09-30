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
  private val formAddress = addresses.save + 0x3524
  private val bonusAddress = addresses.save + 0x3700
  private val munnyAddress = addresses.save + 0x2440
  private val nextSlot = addresses.nextSlot

  /**
   * Reads and interprets a [SoraState].
   */
  fun readSoraState(): SoraState {
    val correctSlot = 0

    val (dreamWeaponData, level) = gameProcess.readBytes(levelAddress, 2)
    val dreamWeapon = when (dreamWeaponData.toInt()) {
      1 -> DreamWeapon.Shield
      2 -> DreamWeapon.Staff
      else -> DreamWeapon.Sword
    }

    val statsData = gameProcess.readBytes(statsAddress - (nextSlot * correctSlot), 5)
    val strength = statsData[0].toInt()
    val magic = statsData[2].toInt()
    val defense = statsData[4].toInt()

    val munny = gameProcess.readShortAsInt(munnyAddress)

//    try
//    {
    //test displaying sora's correct stats for PR 1st forsed fight
    // TODO
//      if (world.worldNum == 16 && world.roomNumber == 1 && (world.eventID1 == 0x33 || world.eventID1 == 0x34))
//        correctSlot = 2; //move forward this number of slots

    /*
  byte[] levelData = memory.ReadMemory(levelAddress + ADDRESS_OFFSET, 2);

            if (levelData[0] == 0 && Weapon != "Sword")
                Weapon = "Sword";
            else if (levelData[0] == 1 && Weapon != "Shield")
                Weapon = "Shield";
            else if (levelData[0] == 2 && Weapon != "Staff")
                Weapon = "Staff";

            previousLevels[0] = previousLevels[1];
            previousLevels[1] = previousLevels[2];
            previousLevels[2] = Level;

            if (Level != levelData[1])
                Level = levelData[1];

            byte[] statsData = memory.ReadMemory(statsAddress - (nextSlotNum * correctSlot) + ADDRESS_OFFSET, 5);
            if (Strength != statsData[0])
                Strength = statsData[0];
            if (Magic != statsData[2])
                Magic = statsData[2];
            if (Defense != statsData[4])
                Defense = statsData[4];

            byte[] modelData = memory.ReadMemory(formAddress + ADDRESS_OFFSET, 1);
            form = modelData[0];

            byte[] BonusData = memory.ReadMemory(bonusAddress + ADDRESS_OFFSET, 1);
            BonusLevel = BonusData[0];

            //change levelreward number
            if (level >= currentCheckArray[currentCheckArray.Length - 1])
            {
                LevelCheck = currentCheckArray[currentCheckArray.Length - 1];
                return;
            }

            if (Level >= currentCheckArray[nextLevelCheck])
            {
                nextLevelCheck++;
                LevelCheck = currentCheckArray[nextLevelCheck];
            }
 */

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
