package com.kh2rando.tracker.auto

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.auto.HollowBastionIds as HB
import com.kh2rando.tracker.auto.TwilightTownIds as TT
import com.kh2rando.tracker.auto.WorldThatNeverWasIds as TWTNW

/**
 * Reads information about the current in-game location.
 */
class LocationReader(private val gameProcess: GameProcess) {

  private val addresses = gameProcess.addresses
  private val now = addresses.now
  private val worldIdAddress = now + 0x00
  private val roomIdAddress = now + 0x01
  private val placeIdAddress = now + 0x00
  private val doorIdAddress = now + 0x02
  private val mapIdAddress = now + 0x04
  private val battleIdAddress = now + 0x06
  private val eventIdAddress = now + 0x08
  private val eventCompleteAddress = addresses.btlEnd + 0x820
  private val inSimulatedTwilightTownAddress = addresses.save + 0x1CFF
  private val menuAddress = addresses.menu
  private val journalAddress = menuAddress - 0xF0

  /**
   * Reads the current location state.
   */
  fun readLocationState(): LocationState {
    val worldId = WorldId(gameProcess.readByteAsInt(worldIdAddress))
    val roomId = RoomId(gameProcess.readByteAsInt(roomIdAddress))
    val placeId = gameProcess.readShortAsInt(placeIdAddress)
    val doorId = gameProcess.readShortAsInt(doorIdAddress)
    val mapId = EventId(gameProcess.readShortAsInt(mapIdAddress))
    val battleId = EventId(gameProcess.readShortAsInt(battleIdAddress))
    val eventId = EventId(gameProcess.readShortAsInt(eventIdAddress))
    val eventComplete = gameProcess.readByteAsInt(eventCompleteAddress)

    val newLocation: Location? = when (val initalResolvedLocation = worldCodes[worldId]) {
      Location.HollowBastion -> resolveHollowBastionRoom(roomId = roomId, mapId = mapId)
      Location.TwilightTown -> resolveTwilightTownRoom(roomId = roomId, mapId = mapId)
      Location.WorldThatNeverWas -> resolveWorldThatNeverWasRoom(roomId = roomId, mapId = mapId)
      else -> initalResolvedLocation
    }

    return LocationState(
      worldId = worldId,
      roomId = roomId,
      placeId = placeId,
      doorId = doorId,
      mapId = mapId,
      battleId = battleId,
      eventId = eventId,
      eventComplete = eventComplete,
      location = newLocation,
    )
  }

  /**
   * Returns true if one of the menus for [Location.Creations] is open (shop or synth, Journal for puzzle, etc.).
   */
  fun inCreationsMenu(): Boolean {
    // menuByte:
    // 0xFF = none
    // 0x01 = save menu
    // 0x03 = load menu
    // 0x05 = moogle
    // 0x07 = item popup
    // 0x08 = pause menu (cutscene/fight)
    // 0x0A = pause Menu (normal)
    val menuByte = gameProcess.readShortAsInt(menuAddress) and 0xFF

    // journalByte:
    // 0xFF = none
    // other = in journal? Seems to go back and forth between 0x00 and 0x01 when navigating around journal.
    val journalByte = gameProcess.readShortAsInt(journalAddress) and 0xFF

    val shopOrSynth = journalByte == 0xFF && menuByte == 0x05
    val puzzle = journalByte != 0xFF && menuByte == 0x0A

    return shopOrSynth || puzzle
  }

  private fun resolveHollowBastionRoom(roomId: RoomId, mapId: EventId): Location {
    return when (roomId) {
      HB.GardenOfAssemblage -> {
        Location.GardenOfAssemblage
      }

      HB.TheOldMansion -> { // Vexen
        Location.HalloweenTown
      }

      HB.StationOfRemembrance.RoomId -> {
        when (mapId) {
          in HB.StationOfRemembrance.lexaeusEventIds -> Location.Agrabah
          in HB.StationOfRemembrance.larxeneEventIds -> Location.SpaceParanoids
          else -> Location.HollowBastion
        }
      }

      HB.DestinyIslands -> { // Zexion
        Location.OlympusColiseum
      }

      HB.StationOfOblivion -> { // Marluxia
        Location.DisneyCastle
      }

      else -> {
        Location.HollowBastion
      }
    }
  }

  private fun resolveTwilightTownRoom(roomId: RoomId, mapId: EventId): Location {
    return if (gameProcess.readByteAsInt(inSimulatedTwilightTownAddress) == 13) { // Handle STT
      Location.SimulatedTwilightTown
    } else if (
      (roomId == TT.StationOfSerenity.RoomId && mapId == TT.StationOfSerenity.SelectYourLevelUpSetup) ||
      (roomId == TT.RoxasRoom.RoomId && mapId == TT.RoxasRoom.DreamsConnected)
    ) { // Crit bonuses
      Location.GardenOfAssemblage
    } else {
      Location.TwilightTown
    }
  }

  private fun resolveWorldThatNeverWasRoom(roomId: RoomId, mapId: EventId): Location {
    return when (roomId) {
      TWTNW.StationOfAwakeningRoxas.RoomId -> {
        if (mapId == TWTNW.StationOfAwakeningRoxas.DataRoxasDisappears) {
          Location.SimulatedTwilightTown
        } else {
          Location.WorldThatNeverWas
        }
      }

      TWTNW.HallOfEmptyMelodiesLower.RoomId -> {
        if (mapId == TWTNW.HallOfEmptyMelodiesLower.DataXigbarDisappears) {
          Location.LandOfDragons
        } else {
          Location.WorldThatNeverWas
        }
      }

      TWTNW.AddledImpasse.RoomId -> {
        if (mapId == TWTNW.AddledImpasse.DataSaixDisappears) {
          Location.PrideLands
        } else {
          Location.WorldThatNeverWas
        }
      }

      TWTNW.HavocsDivide.RoomId -> {
        if (mapId == TWTNW.HavocsDivide.DataLuxordDisappears) {
          Location.PortRoyal
        } else {
          Location.WorldThatNeverWas
        }
      }

      else -> {
        Location.WorldThatNeverWas
      }
    }
  }

  companion object {

    private val worldCodes: Map<WorldId, Location> = mapOf(
      WorldIds.WorldOfDarkness to Location.GardenOfAssemblage, // Title Demo
      WorldIds.TwilightTown to Location.TwilightTown,
//      WorldIds.DestinyIslands to "DestinyIsland",
      WorldIds.HollowBastion to Location.HollowBastion,
      WorldIds.BeastsCastle to Location.BeastsCastle,
      WorldIds.OlympusColiseum to Location.OlympusColiseum,
      WorldIds.Agrabah to Location.Agrabah,
      WorldIds.LandOfDragons to Location.LandOfDragons,
      WorldIds.HundredAcreWood to Location.HundredAcreWood,
      WorldIds.PrideLands to Location.PrideLands,
      WorldIds.Atlantica to Location.Atlantica,
      WorldIds.DisneyCastle to Location.DisneyCastle,
      WorldIds.TimelessRiver to Location.DisneyCastle, // Timeless River
      WorldIds.HalloweenTown to Location.HalloweenTown,
      WorldIds.PortRoyal to Location.PortRoyal,
      WorldIds.SpaceParanoids to Location.SpaceParanoids,
      WorldIds.WorldThatNeverWas to Location.WorldThatNeverWas,
      WorldId(0xFF) to Location.GardenOfAssemblage,
    )

  }

  data class LocationState(
    val worldId: WorldId,
    val roomId: RoomId,
    /**
     * Combination of the room ID and the world ID. i.e. if the world ID is `0x04` and location ID is `0x21` the place
     * ID will be `0x2104`.
     */
    val placeId: Int,
    val doorId: Int,
    val mapId: EventId,
    val battleId: EventId,
    val eventId: EventId,
    val eventComplete: Int,
    val location: Location?,
  )

}
