package com.kh2rando.tracker.model

/**
 * Representation of a memory address.
 */
@JvmInline
value class Address(val address: Long) {

  constructor(address: Int) : this(address.toLong())

  /**
   * Simple addition for an [Address].
   */
  operator fun plus(offset: Int): Address = Address(address + offset)

  /**
   * Simple subraction for an [Address].
   */
  operator fun minus(offset: Int): Address = Address(address - offset)

  override fun toString(): String = address.hex()

}

/**
 * Primary localization version of the game.
 */
enum class GameLocalizationVersion {

  /**
   * Global version.
   */
  Global,

  /**
   * JP version.
   */
  JP,

  /**
   * Unknown version (some game versions make it difficult to distinguish at this time).
   */
  Unknown,

}

/**
 * Abstraction of game addresses to allow for supporting the various game versions.
 */
abstract class GameAddresses(
  val localizationVersion: GameLocalizationVersion,
  val versionCheckAddress: Address,
  val versionCheckExpectedValue: Int,

  /**
   * Current location.
   */
  val now: Address,

  /**
   * Save file.
   */
  val save: Address,

  /**
   * `03system.bin` pointer address.
   */
  val sys3Pointer: Address,

  /**
   * `00battle.bin` pointer address.
   */
  val btl0Pointer: Address,

  /**
   * End-of-Battle camera & signal.
   */
  val btlEnd: Address,

  /**
   * Unit Slot 1.
   */
  val slot1: Address,

  /**
   * Currently open menu.
   *
   * - `0xFF` = none
   * - `0x01` = save
   * - `0x03` = load
   * - `0x05` = moogle
   * - `0x07` = item popup
   * - `0x08` = pause (cutscene/fight)
   * - `0x0A` = pause (normal)
   */
  val menu: Address,

  /**
   * Note: This is also the ability to pause from the variables listings?
   * - `0x00` = pause enabled
   * - `0x01` = paused
   * - `0x02` = pause disabled
   * - `0x03` = menu
   * - `0x04` = dying
   * - `0x05` = continue screen
   */
  val abilityToPause: Address,

  /**
   * Battle status.
   *
   * - `0x00` = not in battle (blue menu)
   * - `0x01` = in "normal" battle (yellow menu)
   * - `0x02` = in forced fight/boss battle (red menu)
   */
  val battleStatus: Address,

  /**
   * Background music (seems to only be the field music / forced battle music).
   */
  val backgroundMusic: Address,

  // Used by DA tracker to look into certain game files. Try to avoid this if possible.
//  val filePointer: Address,

  /**
   * The current world ID (1 byte).
   */
  val worldId: Address = now + 0x00,

  /**
   * The current room ID (1 byte).
   */
  val roomId: Address = now + 0x01,

  /**
   * The current place (combination of [worldId] and [roomId]) (2 bytes).
   */
  val placeId: Address = now + 0x00,

  val doorId: Address = now + 0x02,
  val mapId: Address = now + 0x04,
  val battleId: Address = now + 0x06,
  val eventId: Address = now + 0x08,
)

@OptIn(ExperimentalStdlibApi::class)
fun Int.hex(): String {
  return toHexString(HexFormat.UpperCase)
}

@OptIn(ExperimentalStdlibApi::class)
fun Long.hex(): String {
  return toHexString(HexFormat.UpperCase)
}
