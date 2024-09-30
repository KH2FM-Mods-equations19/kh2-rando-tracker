package com.kh2rando.tracker.auto

import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.GameLocalizationVersion

/**
 * Epic Games Global version 1.0.0.9.
 */
class EpicGlobal1009 : GameAddresses(
  localizationVersion = GameLocalizationVersion.Global,
  versionCheckAddress = Address(0x660E04),
  versionCheckExpectedValue = 106,
  now = Address(0x716DF8),
  save = Address(0x9A92F0),
  sys3Pointer = Address(0x2AE5890),
  btl0Pointer = Address(0x2AE5898),
  btlEnd = Address(0x2A0F720),
  slot1 = Address(0x2A22FD8),
  menu = Address(0x743350),
  abilityToPause = Address(0xABB2B8),
  battleStatus = Address(0x2A10E44),
  backgroundMusic = Address(0xABA784),
//  filePointer = Address(0x29F2CD8),
) {

  override fun toString(): String = "Epic Games Global 1.0.0.9"

}

//  https://github.com/KH2FM-Mods-Num/GoA-ROM-Edition/pull/48/commits/d9fe809c7a3364641c97bc5ae6b9ff5e27622d7c

/**
 * Epic Games Global version 1.0.0.10.
 */
class EpicGlobal10010 : GameAddresses(
  localizationVersion = GameLocalizationVersion.Global,
  versionCheckAddress = Address(0x660E44),
  versionCheckExpectedValue = 106,
  now = Address(0x716DF8),
  save = Address(0x9A9330),
  sys3Pointer = Address(0x2AE58D0),
  btl0Pointer = Address(0x2AE58D8),
  btlEnd = Address(0x2A0F760),
  slot1 = Address(0x2A23018),
  menu = Address(0x743350),
  abilityToPause = Address(0xABB2F8),
  battleStatus = Address(0x2A10E84),
  backgroundMusic = Address(0xABA7C4),
//  filePointer = Address(),
) {

  override fun toString(): String = "Epic Games Global 1.0.0.10"

}

///**
// * Epic Games JP version 1.0.0.9.
// */
//class EpicJp1009 : GameAddresses(
//  versionCheckAddress = Address(0x660DC4),
//  versionCheckExpectedValue = 106,
//  now = Address(),
//  save = Address(),
//  sys3Pointer = Address(),
//  btl0Pointer = Address(),
//  btlEnd = Address(),
//  slot1 = Address(),
//  menu = Address(),
//  death = Address(),
//  battleStatus = Address(),
////  filePointer = Address(),
//) {
//
//  override fun toString(): String = "Epic Games JP 1.0.0.9"
//
//}
