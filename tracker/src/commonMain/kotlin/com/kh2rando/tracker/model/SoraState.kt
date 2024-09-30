package com.kh2rando.tracker.model

import androidx.compose.runtime.Immutable
import com.kh2rando.tracker.model.item.DreamWeapon

/**
 * Sora's basic status.
 */
@Immutable
data class SoraState(
  val dreamWeapon: DreamWeapon,
  val currentLevel: Int,
  val strengthStat: Int,
  val magicStat: Int,
  val defenseStat: Int,
  val munny: Int,
) {

  companion object {

    /**
     * [SoraState] representing an unknown state.
     */
    val Unspecified: SoraState = SoraState(
      dreamWeapon = DreamWeapon.Sword,
      currentLevel = -1,
      strengthStat = -1,
      magicStat = -1,
      defenseStat = -1,
      munny = -1,
    )

  }

}

/**
 * Status of each of the drive forms.
 */
@Immutable
data class DriveFormsState(
  val currentDriveGauge: Int,
  val maximumDriveGauge: Int,
  val valorLevel: Int,
  val valorAcquired: Boolean,
  val wisdomLevel: Int,
  val wisdomAcquired: Boolean,
  val limitLevel: Int,
  val limitAcquired: Boolean,
  val masterLevel: Int,
  val masterAcquired: Boolean,
  val finalLevel: Int,
  /**
   * This is true if Final Form was forced, in addition to true if the dummy item was found.
   */
  val finalAcquired: Boolean,
  /**
   * This is only true if Final Form was forced.
   */
  val finalForced: Boolean,
) {

  /**
   * Returns the maximum obtainable drive form level, based on how many forms are acquired.
   */
  fun maximumDriveFormLevel(): Int {
    var maximumLevel = 2
    if (valorAcquired) maximumLevel++
    if (wisdomAcquired) maximumLevel++
    if (limitAcquired) maximumLevel++
    if (masterAcquired) maximumLevel++
    if (finalAcquired) maximumLevel++
    return maximumLevel
  }

  companion object {

    /**
     * [DriveFormsState] representing an unknown state.
     */
    val Unspecified: DriveFormsState = DriveFormsState(
      currentDriveGauge = -1,
      maximumDriveGauge = -1,
      valorAcquired = false,
      valorLevel = -1,
      wisdomAcquired = false,
      wisdomLevel = -1,
      limitAcquired = false,
      limitLevel = -1,
      masterAcquired = false,
      masterLevel = -1,
      finalAcquired = false,
      finalLevel = -1,
      finalForced = false,
    )

  }

}

/**
 * Status of each of the growth abilities.
 */
@Immutable
data class GrowthState(
  val highJumpLevel: Int,
  val quickRunLevel: Int,
  val dodgeRollLevel: Int,
  val aerialDodgeLevel: Int,
  val glideLevel: Int,
) {

  companion object {

    /**
     * [GrowthState] representing an unknown state.
     */
    val Unspecified: GrowthState = GrowthState(
      highJumpLevel = -1,
      quickRunLevel = -1,
      dodgeRollLevel = -1,
      aerialDodgeLevel = -1,
      glideLevel = -1
    )

  }

}
