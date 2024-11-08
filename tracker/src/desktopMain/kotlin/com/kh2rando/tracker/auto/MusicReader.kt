package com.kh2rando.tracker.auto

import com.kh2rando.tracker.model.BattleStatus
import com.kh2rando.tracker.model.GameLocalizationVersion
import com.kh2rando.tracker.model.MusicState
import com.kh2rando.tracker.model.SongEntry

/**
 * Reads information about the current in-game music.
 */
class MusicReader(
  private val gameProcess: GameProcess,
  private val musicReplacements: Map<Int, SongEntry>,
) {

  private val addresses = gameProcess.addresses

  private val localizationVersion: GameLocalizationVersion = addresses.localizationVersion

  private val battleStatusAddress = addresses.battleStatus
  private val backgroundMusicAddress = addresses.backgroundMusic
  private val battleMusicAddress = backgroundMusicAddress + 0x10

  /**
   * Reads the current game music state.
   */
  fun readMusicState(locationState: LocationReader.LocationState): MusicState {
    val battleStatusValue = gameProcess.readByteAsInt(battleStatusAddress) and 0xFF
    val battleStatus = when (battleStatusValue) {
      0x01 -> BattleStatus.NormalBattle
      0x02 -> BattleStatus.ForcedBattle
      else -> BattleStatus.None
    }

    val songAddress = when (battleStatus) {
      BattleStatus.None -> backgroundMusicAddress
      BattleStatus.NormalBattle -> battleMusicAddress
      BattleStatus.ForcedBattle -> backgroundMusicAddress
    }

    // At least one place in the game (Minnie Hallway section) treats things like a forced battle even though it's one
    // you can leave from. Seems to work correctly to treat it like a forced battle and the song ID is actually in the
    // background music slot.
    var rawSongId = gameProcess.readShortAsInt(songAddress)
    if (rawSongId == 0x00) {
      rawSongId = gameProcess.readShortAsInt(backgroundMusicAddress)
    }
    val resolvedSongId = resolveSongId(
      rawSongId = rawSongId,
      localizationVersion = localizationVersion,
      locationState = locationState,
    )

    val replacement = musicReplacements[resolvedSongId]
    return if (replacement == null) {
      val vanillaSongName = vanillaSongName(rawSongId = rawSongId)
      if (vanillaSongName.isEmpty()) {
        MusicState.Unspecified
      } else {
        MusicState(battleStatus = battleStatus, song = SongEntry(vanillaSongName, group = "Kingdom Hearts II"))
      }
    } else {
      MusicState(battleStatus = battleStatus, song = replacement)
    }
  }

  companion object {

    /**
     * Resolves [rawSongId] into the actual relevant song ID, factoring in the [localizationVersion] and [locationState]
     * if needed.
     */
    fun resolveSongId(
      rawSongId: Int,
      localizationVersion: GameLocalizationVersion,
      locationState: LocationReader.LocationState,
    ): Int {
      return when (rawSongId) {
        // (If we don't know the localization for sure, we're assuming Global - sorry JP players...)

        // Under the Sea (English vs. JP)
        0x6A -> {
          when (localizationVersion) {
            GameLocalizationVersion.Global, GameLocalizationVersion.Unknown -> 0x1FA
            GameLocalizationVersion.JP -> rawSongId
          }
        }

        // Ursula's Revenge (English vs. JP)
        0x6B -> {
          when (localizationVersion) {
            GameLocalizationVersion.Global, GameLocalizationVersion.Unknown -> 0x1FB
            GameLocalizationVersion.JP -> rawSongId
          }
        }

        // Part of Your World (English vs. JP)
        0x6C -> {
          when (localizationVersion) {
            GameLocalizationVersion.Global, GameLocalizationVersion.Unknown -> 0x1FC
            GameLocalizationVersion.JP -> rawSongId
          }
        }

        // A New Day is Dawning (English vs. JP)
        0x6D -> {
          when (localizationVersion) {
            GameLocalizationVersion.Global, GameLocalizationVersion.Unknown -> 0x1FD
            GameLocalizationVersion.JP -> rawSongId
          }
        }

        // Swim This Way (English vs. JP)
        0x71 -> {
          when (localizationVersion) {
            GameLocalizationVersion.Global, GameLocalizationVersion.Unknown -> 0x201
            GameLocalizationVersion.JP -> rawSongId
          }
        }

        // Rowdy Rumble (Timeless River vs. elsewhere)
        0x75 -> {
          when (locationState.worldId) {
            WorldIds.TimelessRiver -> 0x205
            else -> rawSongId
          }
        }

        // Desire for All That is Lost (Timeless River vs. elsewhere)
        0x79 -> {
          when (locationState.worldId) {
            WorldIds.TimelessRiver -> 0x209
            else -> rawSongId
          }
        }

        else -> rawSongId
      }
    }

    /**
     * Returns the name of the vanilla song with the given [rawSongId], or an empty string if none matched.
     */
    fun vanillaSongName(rawSongId: Int): String {
      return when (rawSongId) {
        0x32 -> "Dive into the Heart -Destati-"
        0x33 -> "Fragments of Sorrow"
        0x34 -> "The Afternoon Streets"
        0x35 -> "Working Together"
        0x36 -> "Sacred Moon"
        0x37 -> "Deep Drive"
        0x38 -> "Nights of the Cursed (unused)"
        0x39 -> "He's a Pirate (unused)"
        0x3B -> "A Fight to the Death"
        0x3C -> "Darkness of the Unknown I"
        0x3D -> "Darkness of the Unknown II"
        0x3E -> "Darkness of the Unknown III"
        0x3F -> "The 13th Reflection"
        0x40 -> "What a Surprise?!"
        0x41 -> "Happy Holidays!"
        0x42 -> "The Other Promise"
        0x43 -> "Rage Awakened"
        0x44 -> "Cavern of Remembrance"
        0x45 -> "Deep Anxiety"
        0x51 -> "Beneath the Ground"
        0x52 -> "The Escapade"
        0x54 -> "Arabian Daydream"
        0x55 -> "Byte Striking"
        0x57 -> "Disappeared"
        0x58 -> "Sora"
        0x59 -> "Friends in My Heart"
        0x5A -> "Riku"
        0x5B -> "Kairi"
        0x5C -> "A Walk in Andante"
        0x5D -> "Villains of a Sort"
        0x5E -> "Organization XIII"
        0x5F -> "Apprehension"
        0x60 -> "Courage"
        0x61 -> "Laughter and Merriment"
        0x62 -> "Hesitation"
        0x63 -> "Missing You"
        0x64 -> "The Underworld"
        0x65 -> "Waltz of the Damned"
        0x66 -> "What Lies Beneath"
        0x67 -> "Olympus Coliseum"
        0x68 -> "Dance of the Daring"
        0x6A -> "Under the Sea"
        0x6B -> "Ursula's Revenge"
        0x6C -> "Part of Your World"
        0x6D -> "A New Day is Dawning"
        0x6E -> "The Encounter"
        0x6F -> "Sinister Shadows"
        0x70 -> "Fields of Honor"
        0x71 -> "Swim This Way"
        0x72 -> "Tension Rising"
        0x73 -> "The Corrupted"
        0x74 -> "The Home of Dragons"
        0x75 -> "Rowdy Rumble"
        0x76 -> "Lazy Afternoons"
        0x77 -> "Sinister Sundowns"
        0x78 -> "Beneath the Ground"
        0x79 -> "Desire for All That is Lost"
        0x7A -> "Let's Sing and Dance! I"
        0x7B -> "Let's Sing and Dance! II"
        0x7C -> "Let's Sing and Dance! I"
        0x7D -> "Let's Sing and Dance! III"
        0x7F -> "A Day in Agrabah"
        0x80 -> "Arabian Dream"
        0x81 -> "Isn't It Lovely"
        0x82 -> "Neverland Sky (KH I)"
        0x83 -> "Dance to the Death"
        0x84 -> "Beauty and the Beast"
        0x85 -> "Magical Mystery"
        0x86 -> "Working Together"
        0x87 -> "Space Paranoids"
        0x88 -> "Byte Bashing"
        0x89 -> "A Twinkle in the Sky"
        0x8A -> "Shipmeister's Shanty"
        0x8B -> "Gearing Up"
        0x8C -> "Under the Sea (KH I)"
        0x8D -> "Winnie the Pooh"
        0x8E -> "Crossing the Finish Line"
        0x8F -> "Mickey Mouse Club March"
        0x90 -> "This is Halloween"
        0x91 -> "Vim and Vigor"
        0x92 -> "Roxas"
        0x93 -> "An Adventure in Atlantica (KH I)"
        0x94 -> "Blast Off!"
        0x95 -> "Spooks of Halloween Town"
        0x96 -> "Squirming Evil (KH I)"
        0x97 -> "The 13th Struggle"
        0x98 -> "Reviving Hollow Bastion"
        0x99 -> "Scherzo Di Notte"
        0x9A -> "Nights of the Cursed"
        0x9B -> "He's a Pirate (No Intro)"
        0x9C -> "Guardando nel buio (KH I)"
        0x9E -> "Bounce-O-Rama"
        0x9F -> "Bounce-O-Rama (Speed Up Ver.)"
        0xA3 -> "Villains of a Sort (KH I)"
        0xA4 -> "Road to a Hero"
        0xB9 -> "The 13th Dilemma"
        0xBA -> "Adventures in the Savannah"
        0xBB -> "Savannah Pride"
        0xBC -> "One Winged Angel"
        0xBD -> "Monochrome Dreams"
        0xBE -> "Old Friends, Old Rivals"
        else -> ""
      }
    }

  }

}
