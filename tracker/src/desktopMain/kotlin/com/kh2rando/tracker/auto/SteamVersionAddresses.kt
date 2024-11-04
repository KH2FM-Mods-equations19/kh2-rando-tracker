package com.kh2rando.tracker.auto

import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.GameLocalizationVersion

/**
 * Steam Global version 1.0.0.9.
 */
class SteamGlobal1009 : GameAddresses(
  localizationVersion = GameLocalizationVersion.Global,
  versionCheckAddress = Address(0x660E74),
  versionCheckExpectedValue = 106,
  now = Address(0x717008),
  save = Address(0x9A9830),
  sys3Pointer = Address(0x2AE5DD0),
  btl0Pointer = Address(0x2AE5DD8),
  btlEnd = Address(0x2A0FC60),
  slot1 = Address(0x2A23518),
  menu = Address(0x7435D0),
  abilityToPause = Address(0xABB7F8),
  battleStatus = Address(0x2A11384),
  backgroundMusic = Address(0xABACC4),
//  filePointer = Address(0x29F33D8),
) {

  override fun toString(): String = "Steam Global 1.0.0.9"

}

//  https://github.com/KH2FM-Mods-Num/GoA-ROM-Edition/pull/48/commits/d9fe809c7a3364641c97bc5ae6b9ff5e27622d7c

/**
 * Steam Global version 1.0.0.10.
 */
class SteamGlobal10010 : GameAddresses(
  localizationVersion = GameLocalizationVersion.Global,
  versionCheckAddress = Address(0x660EF4),
  versionCheckExpectedValue = 106,
  now = Address(0x717008),
  save = Address(0x9A98B0),
  sys3Pointer = Address(0x2AE5E50),
  btl0Pointer = Address(0x2AE5E58),
  btlEnd = Address(0x2A0FCE0),
  slot1 = Address(0x2A23598),
  menu = Address(0x7435D0),
  abilityToPause = Address(0xABB878),
  battleStatus = Address(0x2A11404),
  backgroundMusic = Address(0xABAD44),
//  filePointer = Address(0x29F3458),
) {

  override fun toString(): String = "Steam Global 1.0.0.10"

}

/**
 * Steam JP version 1.0.0.9.
 */
class SteamJP1009 : GameAddresses(
  localizationVersion = GameLocalizationVersion.JP,
  versionCheckAddress = Address(0x65FDF4),
  versionCheckExpectedValue = 106,
  now = Address(0x716008),
  save = Address(0x9A8830),
  sys3Pointer = Address(0x2AE4DD0),
  btl0Pointer = Address(0x2AE4DD8),
  btlEnd = Address(0x2A0EC60),
  slot1 = Address(0x2A22518),
  menu = Address(0x7425D0),
  abilityToPause = Address(0xABA7F8),
  battleStatus = Address(0x2A10384),
  backgroundMusic = Address(0xAB9CC4),
//  filePointer = Address(),
) {

  override fun toString(): String = "Steam JP 1.0.0.9"

}

///**
// * Steam JP version 1.0.0.10.
// */
//class SteamJP10010 : GameAddresses(
//  localizationVersion = GameLocalizationVersion.JP,
//  versionCheckAddress = Address(0x660E74),
//  versionCheckExpectedValue = 106,
//  now = Address(0x717008),
//  save = Address(0x9A98B0),
//  sys3Pointer = Address(0x2AE5E50),
//  btl0Pointer = Address(0x2AE5E58),
//  btlEnd = Address(0x2A0FCE0),
//  slot1 = Address(0x2A23598),
//  menu = Address(),
//  death = Address(0xABB878),
//  battleStatus = Address(0x2A11404),
//  backgroundMusic = Address(0xABAD44),
////  filePointer = Address(),
//) {
//
//  override fun toString(): String = "Steam JP 1.0.0.10"
//
//}
