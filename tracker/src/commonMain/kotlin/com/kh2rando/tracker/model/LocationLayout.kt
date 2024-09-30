package com.kh2rando.tracker.model

/**
 * Layout of the locations on the tracker.
 */
enum class LocationLayout {

  /**
   * The layout that was originally used in the tracker going back to its early days.
   */
  Classic {
    override val leftLocations: List<Location>
      get() {
        return listOf(
          Location.SoraLevels,
          Location.SimulatedTwilightTown,
          Location.HollowBastion,
          Location.OlympusColiseum,
          Location.LandOfDragons,
          Location.PrideLands,
          Location.HalloweenTown,
          Location.SpaceParanoids,
          Location.GardenOfAssemblage,
        )
      }

    override val rightLocations: List<Location>
      get() {
        return listOf(
          Location.DriveForms,
          Location.TwilightTown,
          Location.BeastsCastle,
          Location.Agrabah,
          Location.HundredAcreWood,
          Location.DisneyCastle,
          Location.PortRoyal,
          Location.WorldThatNeverWas,
          Location.Atlantica,
          Location.Creations,
        )
      }
  },

  /**
   * Arranges the locations roughly based on the order they're visited in the vanilla game.
   */
  Vanilla {

    override val leftLocations: List<Location>
      get() {
        return listOf(
          Location.SoraLevels,
          Location.SimulatedTwilightTown,
          Location.TwilightTown,
          Location.HollowBastion,
          Location.LandOfDragons,
          Location.BeastsCastle,
          Location.OlympusColiseum,
          Location.DisneyCastle,
          Location.GardenOfAssemblage,
        )
      }

    override val rightLocations: List<Location>
      get() {
        return listOf(
          Location.DriveForms,
          Location.PortRoyal,
          Location.Agrabah,
          Location.HalloweenTown,
          Location.PrideLands,
          Location.SpaceParanoids,
          Location.WorldThatNeverWas,
          Location.HundredAcreWood,
          Location.Atlantica,
          Location.Creations,
        )
      }

  },

  /**
   * Arranges the locations roughly based on where they're accessed in the Garden of Assemblage.
   */
  GardenOfAssemblage {

    override val leftLocations: List<Location>
      get() {
        return listOf(
          Location.SoraLevels,
          Location.WorldThatNeverWas,
          Location.LandOfDragons,
          Location.HalloweenTown,
          Location.OlympusColiseum,
          Location.TwilightTown,
          Location.PortRoyal,
          Location.SpaceParanoids,
          Location.GardenOfAssemblage,
        )
      }

    override val rightLocations: List<Location>
      get() {
        return listOf(
          Location.DriveForms,
          Location.Atlantica,
          Location.BeastsCastle,
          Location.Agrabah,
          Location.PrideLands,
          Location.HollowBastion,
          Location.DisneyCastle,
          Location.SimulatedTwilightTown,
          Location.HundredAcreWood,
          Location.Creations,
        )
      }

  };

  /**
   * The locations for the left half of the tracker, in order.
   */
  abstract val leftLocations: List<Location>

  /**
   * The locations for the right half of the tracker, in order.
   */
  abstract val rightLocations: List<Location>

}
