package com.kh2rando.tracker.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * The type of battle currently taking place.
 */
enum class BattleStatus(override val colorToken: ColorToken) : HasColorToken {

  /**
   * Not in battle (blue command menu).
   */
  None(ColorToken.WhiteBlue),

  /**
   * In normal battle (yellow command menu).
   */
  NormalBattle(ColorToken.Gold),

  /**
   * In a forced battle or boss fight (red command menu).
   */
  ForcedBattle(ColorToken.Salmon)

}

/**
 * The name of a song and an optional group name.
 */
@Immutable
@Serializable
data class SongEntry(val songName: String, val group: String = "")

/**
 * State of the currently playing game music.
 */
@Immutable
data class MusicState(val battleStatus: BattleStatus, val song: SongEntry) {

  companion object {

    /**
     * [MusicState] representing an unknown state.
     */
    val Unspecified: MusicState = MusicState(battleStatus = BattleStatus.None, song = SongEntry(songName = ""))

  }

}
