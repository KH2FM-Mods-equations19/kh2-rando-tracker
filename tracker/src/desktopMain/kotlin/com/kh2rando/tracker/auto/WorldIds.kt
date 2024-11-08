package com.kh2rando.tracker.auto

import com.kh2rando.tracker.model.hex

@JvmInline
value class WorldId(val id: Int) {

  fun toHex(): String {
    return id.hex()
  }

}

@JvmInline
value class RoomId(val id: Int) {

  fun toHex(): String {
    return id.hex()
  }

}

/**
 * Used for map, battle, and/or event IDs.
 */
@JvmInline
value class EventId(val id: Int) {

  fun toHex(): String {
    return id.hex()
  }

}

object WorldIds {

  val WorldOfDarkness = WorldId(0x01)
  val TwilightTown = WorldId(0x02)
//  val DestinyIslands = WorldId(0x03)
  val HollowBastion = WorldId(0x04)
  val BeastsCastle = WorldId(0x05)
  val OlympusColiseum = WorldId(0x06)
  val Agrabah = WorldId(0x07)
  val LandOfDragons = WorldId(0x08)
  val HundredAcreWood = WorldId(0x09)
  val PrideLands = WorldId(0x0A)
  val Atlantica = WorldId(0x0B)
  val DisneyCastle = WorldId(0x0C)
  val TimelessRiver = WorldId(0x0D)
  val HalloweenTown = WorldId(0x0E)
  val PortRoyal = WorldId(0x10)
  val SpaceParanoids = WorldId(0x11)
  val WorldThatNeverWas = WorldId(0x12)

}

object HollowBastionIds {

  /**
   * 04.1A. Garden of Assemblage
   *
   *     01. The 13 Strange Portals
   *     03. The Garden of Assemblage
   */
  val GardenOfAssemblage = RoomId(0x1A)

  /**
   * 04.20. The Old Mansion (Vexen)
   *
   *     73. Vexen Battle (Absent Silhouette)
   *     78. The Chilly Academic, Vexen
   *     79. The Absent Silhouette of Vexen Disappears
   *     82. Data Vexen Appears
   *     83. Data Vexen Disappears
   *     92. Vexen Battle (Data)
   */
  val TheOldMansion = RoomId(0x20)

  /**
   * 04.21. Station of Remembrance
   *
   *     7A. The Silent Hero, Lexaeus
   *     7B. The Absent Silhouette of Lexaeus Disappears
   *     80. The Savage Nymph, Larxene
   *     81. The Absent Silhouette of Larxene Disappears
   *     84. Data Lexaeus Appears
   *     85. Data Lexaeus Disappears
   *     8A. Data Larxene Appears
   *     8B. Data Larxene Disappears
   *     8E. Lexaeus Battle (Absent Silhouette)
   *     8F. Larxene Battle (Absent Silhouette)
   *     93. Lexaeus Battle (Data)
   *     94. Larxene Battle (Data)
   */
  object StationOfRemembrance {

    val RoomId = RoomId(0x21)

    val ASLexaeusAppears = EventId(0x7A)
    val ASLexaeusDisappears = EventId(0x7B)
    val ASLarxeneAppears = EventId(0x80)
    val ASLarxeneDisappears = EventId(0x81)
    val DataLexaeusAppears = EventId(0x84)
    val DataLexaeusDisappears = EventId(0x85)
    val DataLarxeneAppears = EventId(0x8A)
    val DataLarxeneDisappears = EventId(0x8B)
    val ASLexaeusBattle = EventId(0x8E)
    val ASLarxeneBattle = EventId(0x8F)
    val DataLexaeusBattle = EventId(0x93)
    val DataLarxeneBattle = EventId(0x94)

    val lexaeusEventIds: Set<EventId> = setOf(
      ASLexaeusAppears,
      ASLexaeusBattle,
      ASLexaeusDisappears,
      DataLexaeusAppears,
      DataLexaeusBattle,
      DataLexaeusDisappears,
    )

    val larxeneEventIds: Set<EventId> = setOf(
      ASLarxeneAppears,
      ASLarxeneBattle,
      ASLarxeneDisappears,
      DataLarxeneAppears,
      DataLarxeneBattle,
      DataLarxeneDisappears,
    )

  }

  /**
   * 04.22. Destiny Islands
   *
   *     7C. The Cloaked Schemer, Zexion
   *     7D. The Absent Silhouette of Zexion Disappears
   *     86. Data Zexion Appears
   *     87. Data Zexion Disappears
   *     97. Zexion Battle (Absent Silhouette)
   *     98. Zexion Battle (Data)
   */
  val DestinyIslands = RoomId(0x22)

  /**
   * 04.26. Station of Oblivion
   *
   *     7E. The Graceful Assassin, Marluxia
   *     7F. The Absent Silhouette of Marluxia Disappears
   *     88. Data Marluxia Appears
   *     89. Data Marluxia Disappears
   *     91. Marluxia Battle (Absent Silhouette)
   *     96. Marluxia Battle (Data)
   */
  val StationOfOblivion = RoomId(0x26)

}

object HundredAcreWoodIds {

  /**
   * 09.02. Pooh Bear's House
   *
   *     01. Pooh's House
   *     02. Reunion, and Then a Strange Incident
   *     03. Pooh's Situation
   *     04. Something's Out of Place
   */
  val PoohBearsHowse = RoomId(0x02)

}

object TwilightTownIds {

  /**
   * 02.01. Roxas's Room
   *
   *     34. Dreams Connected
   *     35. The Dream Is the Key
   *     36. A Dream Full of Promises
   *     37. A Dream of Good-byes
   *     38. Waking from a Dream
   *     39. Just Another Morning
   *     3A. Awakened by an Illusion
   *     3B. A Troubled Awakening
   *     3C. A Hazy Morning
   *     3D. Shadow of Another
   *     E5. Dreams Connected (Theater Mode - English)
   *     E6. Dreams Connected (Theater Mode - Japanese)
   *     E7. Waking from a Dream (Theater Mode)
   *     F2. The Dream Is the Key (Theater Mode - English)
   *     F3. The Dream Is the Key (Theater Mode - Japanese)
   *     F4. Just Another Morning (Theater Mode)
   */
  object RoxasRoom {

    val RoomId = RoomId(0x01)

    val DreamsConnected = EventId(0x34)

  }

  /**
   * 02.20. Station of Serenity
   *
   *     01. Select Your Level Up Setup
   *     9A. Triple Dusks Battle
   *     9B. The Keyblade
   *     9C. The Station of Awakening
   */
  object StationOfSerenity {

    val RoomId = RoomId(0x20)

    val SelectYourLevelUpSetup = EventId(0x01)

  }

}

object WorldThatNeverWasIds {

  /**
   * 12.15. Station of Awakening (Roxas)
   *
   *     01. Why Sora Was Chosen
   *     41. Roxas Battle (Normal)
   *     63. Roxas Battle (Data)
   *     71. Data Roxas Appears
   *     72. Data Roxas Disappears
   *     79. Time to Sleep
   *     B4. Why Sora Was Chosen (Theater Mode - English)
   *     B5. Time to Sleep (Theater Mode - English)
   *     DD. Why Sora Was Chosen (Theater Mode - Japanese)
   *     DE. Time to Sleep (Theater Mode - Japanese)
   */
  object StationOfAwakeningRoxas {

    val RoomId = RoomId(0x15)

    val DataRoxasDisappears = EventId(0x72)

  }

  /**
   * 12.0A. Hall of Empty Melodies (Lower Level)
   *
   *     01. Together Again
   *     02. Xigbar
   *     39. Xigbar Battle (Normal)
   *     64. Xigbar Battle (Data)
   *     6B. Data Xigbar Appears
   *     6C. Data Xigbar Disappears
   *     BC. Together Again (Theater Mode - English)
   *     BE. Xigbar (Theater Mode - English)
   *     BF. On Our Way (Theater Mode - English)
   *     E5. Together Again (Theater Mode - Japanese)
   *     E7. Xigbar (Theater Mode - Japanese)
   *     E8. On Our Way (Theater Mode - Japanese)
   *
   */
  object HallOfEmptyMelodiesLower {

    val RoomId = RoomId(0x0A)

    val DataXigbarDisappears = EventId(0x6C)

  }

  /**
   * 12.0E. Havoc's Divide
   *
   *     01. Luxord
   *     3A. Luxord Battle (Normal)
   *     65. Luxord Battle (Data)
   *     6F. Data Luxord Appears
   *     70. Data Luxord Disappears
   *     C3. Luxord (Theater Mode - English)
   *     C4. As the Battle Ends (Theater Mode - English)
   *     EC. Luxord (Theater Mode - Japanese)
   *     ED. As the Battle Ends (Theater Mode - Japanese)
   */
  object HavocsDivide {

    val RoomId = RoomId(0x0E)

    val DataLuxordDisappears = EventId(0x70)

  }

  /**
   * 12.0F. Addled Impasse
   *
   *     01. Saix
   *     38. Saix Battle (Normal)
   *     60. A Friend Within
   *     66. Saix Battle (Data)
   *     6D. Data Saix Appears
   *     6E. Data Saix Disappears
   *     7A. I Wish I Could Meet Him, Too
   *     C6. Saix (Theater Mode - English)
   *     C7. A Friend Within (Theater Mode - English)
   *     C9. I Wish I Could Meet Him, Too (Theater Mode - English)
   *     EF. Saix (Theater Mode - Japanese)
   *     F0. A Friend Within (Theater Mode - Japanese)
   *     F2. I Wish I Could Meet Him, Too (Theater Mode - Japanese)
   */
  object AddledImpasse {

    val RoomId = RoomId(0x0F)

    val DataSaixDisappears = EventId(0x6E)

  }

}
