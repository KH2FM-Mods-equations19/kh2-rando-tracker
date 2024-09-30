package com.kh2rando.tracker.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.agrabah
import com.kh2rando.tracker.generated.resources.atlantica
import com.kh2rando.tracker.generated.resources.beasts_castle
import com.kh2rando.tracker.generated.resources.creations
import com.kh2rando.tracker.generated.resources.disney_castle
import com.kh2rando.tracker.generated.resources.drive_forms
import com.kh2rando.tracker.generated.resources.garden_of_assemblage
import com.kh2rando.tracker.generated.resources.halloween_town
import com.kh2rando.tracker.generated.resources.hollow_bastion
import com.kh2rando.tracker.generated.resources.hundred_acre_wood
import com.kh2rando.tracker.generated.resources.hundred_acre_wood_short
import com.kh2rando.tracker.generated.resources.land_of_dragons
import com.kh2rando.tracker.generated.resources.location_agrabah
import com.kh2rando.tracker.generated.resources.location_atlantica
import com.kh2rando.tracker.generated.resources.location_beasts_castle
import com.kh2rando.tracker.generated.resources.location_creations
import com.kh2rando.tracker.generated.resources.location_disney_castle
import com.kh2rando.tracker.generated.resources.location_drive_forms
import com.kh2rando.tracker.generated.resources.location_garden_of_assemblage
import com.kh2rando.tracker.generated.resources.location_halloween_town
import com.kh2rando.tracker.generated.resources.location_hollow_bastion
import com.kh2rando.tracker.generated.resources.location_hundred_acre_wood
import com.kh2rando.tracker.generated.resources.location_land_of_dragons
import com.kh2rando.tracker.generated.resources.location_levels
import com.kh2rando.tracker.generated.resources.location_olympus_coliseum
import com.kh2rando.tracker.generated.resources.location_port_royal
import com.kh2rando.tracker.generated.resources.location_pride_lands
import com.kh2rando.tracker.generated.resources.location_simulated_twilight_town
import com.kh2rando.tracker.generated.resources.location_space_paranoids
import com.kh2rando.tracker.generated.resources.location_twilight_town
import com.kh2rando.tracker.generated.resources.location_twtnw
import com.kh2rando.tracker.generated.resources.olympus_coliseum
import com.kh2rando.tracker.generated.resources.port_royal
import com.kh2rando.tracker.generated.resources.pride_lands
import com.kh2rando.tracker.generated.resources.simulated_twilight_town
import com.kh2rando.tracker.generated.resources.simulated_twilight_town_short
import com.kh2rando.tracker.generated.resources.sora_levels
import com.kh2rando.tracker.generated.resources.sora_levels_short
import com.kh2rando.tracker.generated.resources.space_paranoids
import com.kh2rando.tracker.generated.resources.twilight_town
import com.kh2rando.tracker.generated.resources.world_that_never_was
import com.kh2rando.tracker.generated.resources.world_that_never_was_short
import com.kh2rando.tracker.ui.color
import com.kh2rando.tracker.ui.localizedName
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.persistentMapOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * The primary locations in the game that can be tracked.
 */
enum class Location(
  /**
   * The number of visits defined for this location, or 0 if visits don't apply to this location.
   */
  val visitCount: Int,
) : HasCustomizableIcon, HasColorToken {

  SoraLevels(visitCount = 0) {
    override val displayName: StringResource
      get() = Res.string.sora_levels
    override val shortName: StringResource
      get() = Res.string.sora_levels_short
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_levels
    override val customIconIdentifier: String
      get() = "level"
    override val colorToken: ColorToken
      get() = ColorToken.Salmon
  },

  SimulatedTwilightTown(visitCount = 1) {
    override val displayName: StringResource
      get() = Res.string.simulated_twilight_town
    override val shortName: StringResource
      get() = Res.string.simulated_twilight_town_short
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_simulated_twilight_town
    override val customIconIdentifier: String
      get() = "simulated_twilight_town"
    override val colorToken: ColorToken
      get() = ColorToken.WhiteBlue
  },

  HollowBastion(visitCount = 2) {
    override val displayName: StringResource
      get() = Res.string.hollow_bastion
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_hollow_bastion
    override val customIconIdentifier: String
      get() = "hollow_bastion"
    override val colorToken: ColorToken
      get() = ColorToken.Salmon
  },

  OlympusColiseum(visitCount = 2) {
    override val displayName: StringResource
      get() = Res.string.olympus_coliseum
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_olympus_coliseum
    override val customIconIdentifier: String
      get() = "olympus_coliseum"
    override val colorToken: ColorToken
      get() = ColorToken.Green
  },

  LandOfDragons(visitCount = 2) {
    override val displayName: StringResource
      get() = Res.string.land_of_dragons
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_land_of_dragons
    override val customIconIdentifier: String
      get() = "land_of_dragons"
    override val colorToken: ColorToken
      get() = ColorToken.Red
  },

  PrideLands(visitCount = 2) {
    override val displayName: StringResource
      get() = Res.string.pride_lands
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_pride_lands
    override val customIconIdentifier: String
      get() = "pride_land"
    override val colorToken: ColorToken
      get() = ColorToken.Red
  },

  HalloweenTown(visitCount = 2) {
    override val displayName: StringResource
      get() = Res.string.halloween_town
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_halloween_town
    override val customIconIdentifier: String
      get() = "halloween_town"
    override val colorToken: ColorToken
      get() = ColorToken.Purple
  },

  SpaceParanoids(visitCount = 2) {
    override val displayName: StringResource
      get() = Res.string.space_paranoids
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_space_paranoids
    override val customIconIdentifier: String
      get() = "space_paranoids"
    override val colorToken: ColorToken
      get() = ColorToken.Purple
  },

  GardenOfAssemblage(visitCount = 0) {
    override val displayName: StringResource
      get() = Res.string.garden_of_assemblage
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_garden_of_assemblage
    override val customIconIdentifier: String
      get() = "replica_data"
    override val colorToken: ColorToken
      get() = ColorToken.WhiteBlue
  },

  DriveForms(visitCount = 0) {
    override val displayName: StringResource
      get() = Res.string.drive_forms
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_drive_forms
    override val customIconIdentifier: String
      get() = "drive_form"
    override val colorToken: ColorToken
      get() = ColorToken.Gold
  },

  TwilightTown(visitCount = 3) {
    override val displayName: StringResource
      get() = Res.string.twilight_town
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_twilight_town
    override val customIconIdentifier: String
      get() = "twilight_town"
    override val colorToken: ColorToken
      get() = ColorToken.Orange
  },

  BeastsCastle(visitCount = 2) {
    override val displayName: StringResource
      get() = Res.string.beasts_castle
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_beasts_castle
    override val customIconIdentifier: String
      get() = "beast's_castle"
    override val colorToken: ColorToken
      get() = ColorToken.Magenta
  },

  Agrabah(visitCount = 2) {
    override val displayName: StringResource
      get() = Res.string.agrabah
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_agrabah
    override val customIconIdentifier: String
      get() = "agrabah"
    override val colorToken: ColorToken
      get() = ColorToken.Gold
  },

  HundredAcreWood(visitCount = 0) {
    override val displayName: StringResource
      get() = Res.string.hundred_acre_wood
    override val shortName: StringResource
      get() = Res.string.hundred_acre_wood_short
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_hundred_acre_wood
    override val customIconIdentifier: String
      get() = "100_acre_wood"
    override val colorToken: ColorToken
      get() = ColorToken.Gold
  },

  DisneyCastle(visitCount = 2) {
    override val displayName: StringResource
      get() = Res.string.disney_castle
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_disney_castle
    override val customIconIdentifier: String
      get() = "disney_castle"
    override val colorToken: ColorToken
      get() = ColorToken.LightBlue
  },

  PortRoyal(visitCount = 2) {
    override val displayName: StringResource
      get() = Res.string.port_royal
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_port_royal
    override val customIconIdentifier: String
      get() = "port_royal"
    override val colorToken: ColorToken
      get() = ColorToken.DarkBlue
  },

  WorldThatNeverWas(visitCount = 2) {
    override val displayName: StringResource
      get() = Res.string.world_that_never_was
    override val shortName: StringResource
      get() = Res.string.world_that_never_was_short
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_twtnw
    override val customIconIdentifier: String
      get() = "the_world_that_never_was"
    override val colorToken: ColorToken
      get() = ColorToken.White
  },

  Atlantica(visitCount = 0) {
    override val displayName: StringResource
      get() = Res.string.atlantica
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_atlantica
    override val customIconIdentifier: String
      get() = "atlantica"
    override val colorToken: ColorToken
      get() = ColorToken.LightBlue
  },

  Creations(visitCount = 0) {
    override val displayName: StringResource
      get() = Res.string.creations
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_creations
    override val customIconIdentifier: String
      get() = "PuzzSynth"
    override val colorToken: ColorToken
      get() = ColorToken.Salmon
  };

  /**
   * Name to display for this location.
   */
  abstract val displayName: StringResource

  /**
   * Short name to display for this location - used in places where space may be limited.
   */
  open val shortName: StringResource
    get() = displayName

  abstract override val colorToken: ColorToken

  override val defaultIconTint: Color
    get() = color

  override val customIconPath: List<String>
    get() = listOf("Worlds")

  companion object {

    @Composable
    fun byDisplayName(): Map<String, Location> {
      return entries.map { it.localizedName to it }
        .sortedBy { it.first }
        .associate { it.first to it.second }
    }

  }

}

inline fun <V> Iterable<Location>.locationsMap(valueSelector: (Location) -> V): ImmutableMap<Location, V> {
  return persistentMapOf<Location, V>().mutate { builder -> associateWithTo(builder, valueSelector) }
}
