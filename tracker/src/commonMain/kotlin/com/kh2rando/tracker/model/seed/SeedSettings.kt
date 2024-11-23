package com.kh2rando.tracker.model.seed

import androidx.compose.runtime.Immutable
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.settings_absent_silhouette
import com.kh2rando.tracker.generated.resources.settings_absent_silhouette_split
import com.kh2rando.tracker.generated.resources.settings_absent_silhouettes
import com.kh2rando.tracker.generated.resources.settings_absent_silhouettes_with_split
import com.kh2rando.tracker.generated.resources.settings_cavern_of_remembrance
import com.kh2rando.tracker.generated.resources.settings_cups
import com.kh2rando.tracker.generated.resources.settings_data_organization
import com.kh2rando.tracker.generated.resources.settings_hades_paradox_cup
import com.kh2rando.tracker.generated.resources.settings_level_01
import com.kh2rando.tracker.generated.resources.settings_level_1
import com.kh2rando.tracker.generated.resources.settings_level_50
import com.kh2rando.tracker.generated.resources.settings_level_99
import com.kh2rando.tracker.generated.resources.settings_lingering_will
import com.kh2rando.tracker.generated.resources.settings_paradox_cup
import com.kh2rando.tracker.generated.resources.settings_sephiroth
import com.kh2rando.tracker.generated.resources.settings_transport
import com.kh2rando.tracker.generated.resources.settings_transport_to_remembrance
import com.kh2rando.tracker.generated.resources.settings_underdrome_cups
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.hints.HintSystem
import com.kh2rando.tracker.model.item.DreamWeapon
import com.kh2rando.tracker.model.item.DriveForm
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.objective.Objective
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * Settings for a randomizer seed.
 */
@Immutable
@Serializable
data class SeedSettings(
  /**
   * The version of the seed generator used to generate this seed.
   */
  val generatorVersion: String,
  /**
   * The locations that can contain randomized items.
   */
  val enabledLocations: Set<Location>,
  /**
   * The set of item types that the tracker should track.
   */
  val trackableItems: Set<ItemPrototype>,
  /**
   * The hint system in place for the seed.
   */
  val hintSystem: HintSystem,
  /**
   * The seed's Final Door Requirement.
   */
  val finalDoorRequirement: FinalDoorRequirement,
  /**
   * Listings of all items that can be found on Sora and Drive Form levels.
   */
  val levelChecks: LevelChecks,
  /**
   * The configured [LevelSetting].
   */
  val levelSetting: LevelSetting,
  /**
   * The types of creations that are enabled.
   */
  val creationsOptions: Set<CreationsOption>,
  /**
   * Which [ToggleSetting]s are enabled.
   */
  val toggleSettings: Set<ToggleSetting>,
)

/**
 * Requirements needed to enter the Final Door.
 */
@Serializable
sealed interface FinalDoorRequirement {

  /**
   * Requires acquiring all three proofs.
   */
  @Serializable
  @SerialName("ThreeProofs")
  data object ThreeProofs : FinalDoorRequirement

  /**
   * Requires completing a number of objectives.
   */
  @Serializable
  @SerialName("Objectives")
  data class Objectives(val objectiveList: List<Objective>, val objectivesNeeded: Int) : FinalDoorRequirement

  /**
   * Requires acquiring a set number of Lucky Emblem objects.
   */
  @Serializable
  @SerialName("Emblems")
  data class LuckyEmblems(val emblemsNeeded: Int, val emblemsAvailable: Int) : FinalDoorRequirement

}

private val levelChecks50: List<Int>
  get() = listOf(0, 2, 4, 7, 9, 10, 12, 14, 15, 17, 20, 23, 25, 28, 30, 32, 34, 36, 39, 41, 44, 46, 48, 50)
private val levelChecks99: List<Int>
  get() = listOf(0, 7, 9, 12, 15, 17, 20, 23, 25, 28, 31, 33, 36, 39, 41, 44, 47, 49, 53, 59, 65, 73, 85, 99)

/**
 * Maximum level for randomized checks.
 */
@Immutable
enum class LevelSetting(
  val displayString: StringResource,
  val defaultIcon: DrawableResource,
  private val levelsWithChecks: List<Int>,
) {

  Level1(
    displayString = Res.string.settings_level_1,
    defaultIcon = Res.drawable.settings_level_01,
    levelsWithChecks = emptyList(),
  ),
  Level50(
    displayString = Res.string.settings_level_50,
    defaultIcon = Res.drawable.settings_level_50,
    levelsWithChecks = levelChecks50
  ),
  Level99(
    displayString = Res.string.settings_level_99,
    defaultIcon = Res.drawable.settings_level_99,
    levelsWithChecks = levelChecks99
  );

  fun nextLevelWithChecks(currentLevel: Int): Int? {
    return levelsWithChecks.firstOrNull { it > currentLevel }
  }

}

/**
 * Listings of items that can be found on Sora and Drive Form levels. Each list of lists is indexed by the items for
 * each level.
 *
 * Used primarily to ensure auto-tracked items go to the right place, rather than just to the current location.
 */
@Immutable
@Serializable
data class LevelChecks(
  val soraLevels: Map<DreamWeapon, List<List<ItemPrototype>>>,
  val formLevels: Map<DriveForm, List<List<ItemPrototype>>>,
) {

  companion object {

    fun List<List<ItemPrototype>>.checksBetween(previousLevel: Int, newLevel: Int): List<ItemPrototype> {
      return subList(previousLevel.coerceAtLeast(0), newLevel).flatten()
    }

    fun soraLevelsBuilder(): Map<DreamWeapon, List<MutableList<ItemPrototype>>> {
      return DreamWeapon.entries.associateWith { List(99) { mutableListOf() } }
    }

    fun formLevelsBuilder(): Map<DriveForm, List<MutableList<ItemPrototype>>> {
      return DriveForm.entries.associateWith { List(7) { mutableListOf() } }
    }

  }

}

@Immutable
enum class CreationsOption {

  Puzzle, Synthesis,

}

/**
 * Various seed settings that are simple toggles.
 */
enum class ToggleSetting(
  val displayString: StringResource? = null,
  val defaultIcon: DrawableResource? = null,
) {

  /**
   * Whether or not Absent Silhouette checks are enabled.
   */
  AbsentSilhouettes(
    displayString = Res.string.settings_absent_silhouettes,
    defaultIcon = Res.drawable.settings_absent_silhouette,
  ),

  /**
   * Whether or not Absent Silhouette checks are enabled (and split with data fight checks).
   */
  AbsentSilhouettesSplit(
    displayString = Res.string.settings_absent_silhouettes_with_split,
    defaultIcon = Res.drawable.settings_absent_silhouette_split,
  ),

  /**
   * Whether or not Data Organization checks are enabled.
   */
  DataOrganization(
    displayString = Res.string.settings_data_organization,
    defaultIcon = Res.drawable.settings_data_organization,
  ),

  /**
   * Whether or not Sephiroth checks are enabled.
   */
  Sephiroth(
    displayString = Res.string.settings_sephiroth,
    defaultIcon = Res.drawable.settings_sephiroth,
  ),

  /**
   * Whether or not Lingering Will checks are enabled.
   */
  LingeringWill(
    displayString = Res.string.settings_lingering_will,
    defaultIcon = Res.drawable.settings_lingering_will,
  ),

  /**
   * Whether or not Cavern of Remembrance checks are enabled.
   */
  CavernOfRemembrance(
    displayString = Res.string.settings_cavern_of_remembrance,
    defaultIcon = Res.drawable.settings_cavern_of_remembrance,
  ),

  /**
   * Whether or not Transport to Remembrance checks are enabled.
   */
  TransportToRemembrance(
    displayString = Res.string.settings_transport_to_remembrance,
    defaultIcon = Res.drawable.settings_transport,
  ),

  /**
   * Whether or not Underdrome Cups are enabled.
   */
  UnderdromeCups(
    displayString = Res.string.settings_cups,
    defaultIcon = Res.drawable.settings_underdrome_cups,
  ),

  /**
   * Whether or not the Hades Paradox Cup is enabled.
   */
  HadesParadoxCup(
    displayString = Res.string.settings_hades_paradox_cup,
    defaultIcon = Res.drawable.settings_paradox_cup,
  ),

  /**
   * Whether or not dream weapons affect level checks.
   */
  DreamWeaponMatters,

  /**
   * Whether or not the "better" Roxas movement and abilities is enabled.
   */
  RoxasMovementEtc,

  /**
   * Whether or not the tracker should track and display a cumulative number of points based on checks acquired and
   * other factors.
   */
  // TODO: This isn't implemented yet. May not be worth the effort - would have to start keeping track of a lot more.
  HighScoreMode,

}
