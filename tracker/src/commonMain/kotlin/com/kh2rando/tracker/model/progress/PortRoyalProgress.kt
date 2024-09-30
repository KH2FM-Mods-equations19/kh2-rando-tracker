package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_chests
import com.kh2rando.tracker.generated.resources.prog_pr_1_minute
import com.kh2rando.tracker.generated.resources.prog_pr_barbossa
import com.kh2rando.tracker.generated.resources.prog_pr_barrels
import com.kh2rando.tracker.generated.resources.prog_pr_data_luxord
import com.kh2rando.tracker.generated.resources.prog_pr_gambler
import com.kh2rando.tracker.generated.resources.prog_pr_grim_reaper
import com.kh2rando.tracker.generated.resources.prog_pr_grim_reaper_1
import com.kh2rando.tracker.generated.resources.prog_pr_medallions
import com.kh2rando.tracker.generated.resources.prog_pr_town
import com.kh2rando.tracker.generated.resources.progression_chest
import com.kh2rando.tracker.generated.resources.progression_pr_barbossa
import com.kh2rando.tracker.generated.resources.progression_pr_cursed_reaper
import com.kh2rando.tracker.generated.resources.progression_pr_explosives
import com.kh2rando.tracker.generated.resources.progression_pr_gamblers
import com.kh2rando.tracker.generated.resources.progression_pr_grim_reaper
import com.kh2rando.tracker.generated.resources.progression_pr_isla_de_muerta
import com.kh2rando.tracker.generated.resources.progression_pr_luxord
import com.kh2rando.tracker.generated.resources.progression_pr_medallions
import com.kh2rando.tracker.generated.resources.progression_pr_town
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class PortRoyalProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
  override val associatedFlag: ProgressFlag,
) : ProgressCheckpoint, HasCustomizableIcon {

  Chests(
    displayString = Res.string.prog_chests,
    defaultIcon = Res.drawable.progression_chest,
    customIconIdentifier = "chest",
    Flag.CA_SCENARIO_1_START,
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression")
  },

  Town(
    displayString = Res.string.prog_pr_town,
    defaultIcon = Res.drawable.progression_pr_town,
    customIconIdentifier = "town",
    associatedFlag = Flag.CA_ca02_ms103,
  ),

  OneMinute(
    displayString = Res.string.prog_pr_1_minute,
    defaultIcon = Res.drawable.progression_pr_isla_de_muerta,
    customIconIdentifier = "isla_de_muerta",
    associatedFlag = Flag.CA_ca09_ms104,
  ),

  Medallions(
    displayString = Res.string.prog_pr_medallions,
    defaultIcon = Res.drawable.progression_pr_medallions,
    customIconIdentifier = "medallions",
    associatedFlag = Flag.CA_ca07_ms105,
  ),

  Barrels(
    displayString = Res.string.prog_pr_barrels,
    defaultIcon = Res.drawable.progression_pr_explosives,
    customIconIdentifier = "explosives",
    associatedFlag = Flag.CA_ca03_ms106,
  ),

  Barbossa(
    displayString = Res.string.prog_pr_barbossa,
    defaultIcon = Res.drawable.progression_pr_barbossa,
    customIconIdentifier = "barbossa",
    associatedFlag = Flag.CA_ca10_ms107,
  ),

  GrimReaper1(
    displayString = Res.string.prog_pr_grim_reaper_1,
    defaultIcon = Res.drawable.progression_pr_grim_reaper,
    customIconIdentifier = "grim_reaper",
    associatedFlag = Flag.CA_ca18_ms202,
  ),

  Gambler(
    displayString = Res.string.prog_pr_gambler,
    defaultIcon = Res.drawable.progression_pr_gamblers,
    customIconIdentifier = "gamblers",
    associatedFlag = Flag.CA_ca14_ms203,
  ),

  GrimReaper(
    displayString = Res.string.prog_pr_grim_reaper,
    defaultIcon = Res.drawable.progression_pr_cursed_reaper,
    customIconIdentifier = "cursed_reaper",
    associatedFlag = Flag.CA_ca01_ms204,
  ),

  DataLuxord(
    displayString = Res.string.prog_pr_data_luxord,
    defaultIcon = Res.drawable.progression_pr_luxord,
    customIconIdentifier = "luxord",
    associatedFlag = WorldThatNeverWasProgress.Flag.EH_FM_LUX_RE_CLEAR,
  );

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.PortRoyal

  override val customIconPath: List<String>
    get() = listOf("Progression", "port_royal")

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<PortRoyalProgress, Int> {
      val inOrder =
        listOf(Chests, Town, OneMinute, Medallions, Barrels, Barbossa, GrimReaper1, Gambler, GrimReaper, DataLuxord)
      return inOrder.zip(pointsList).toMap()
    }

  }

  enum class Flag(override val saveOffset: Int, override val mask: Int) : ProgressFlag {

    CA_START(0x1E90, 0x01),
    CA_START2(0x1E90, 0x04),
    CA_101_END(0x1E90, 0x10),
    CA_102_END(0x1E90, 0x20),
    CA_ca01_ms101(0x1E90, 0x40),
    CA_ca01_ms102(0x1E90, 0x80),
    CA_105_END(0x1E91, 0x01),
    CA_ca02_ms103(0x1E91, 0x02),
    CA_107_END(0x1E91, 0x04),
    CA_109_END(0x1E91, 0x08),
    CA_112_END(0x1E92, 0x01),
    CA_ca_event_113(0x1E92, 0x02),
    CA_114_END(0x1E92, 0x04),
    CA_ca09_ms104(0x1E92, 0x08),
    CA_116_END(0x1E92, 0x10),
    CA_118_END(0x1E92, 0x40),
    CA_119_END(0x1E92, 0x80),
    CA_ca07_ms105(0x1E93, 0x01),
    CA_121_END(0x1E93, 0x02),
    CA_ca03_ms106(0x1E93, 0x04),
    CA_126_END(0x1E93, 0x08),
    CA_123_END(0x1E93, 0x10),
    CA_ca10_ms107(0x1E93, 0x20),
    CA_125_END(0x1E93, 0x40),
    CA_201_END(0x1E93, 0x80),
    CA_202_END(0x1E94, 0x01),
    CA_203_END(0x1E94, 0x02),
    CA_ca01_ms201(0x1E94, 0x04), // Second visit first forced fight end
    CA_205_END(0x1E94, 0x10),
    CA_206_END(0x1E94, 0x20),
    CA_207_END(0x1E94, 0x40),
    CA_209_END(0x1E95, 0x01),
    CA_210_END(0x1E95, 0x02),
    CA_ca14_ms203(0x1E95, 0x04), // Gambler forced fight end
    CA_212_END(0x1E95, 0x08),
    CA_213_END(0x1E95, 0x20),
    CA_214_END(0x1E95, 0x40),
    CA_ca01_ms204(0x1E95, 0x80), // Grim Reaper 2 fight end
    CA_216_END(0x1E96, 0x01),
    CA_ca09_ms_ht(0x1E96, 0x04),
    CA_ca10_ms_ht(0x1E96, 0x10),
    CA_ca12_ms_ht(0x1E96, 0x40),
    CA_ca13_ms_ht(0x1E97, 0x01),
    CA_ca14_ms_ht(0x1E97, 0x04),
    CA_ca15_ms_ht(0x1E97, 0x10),
    CA_mission_success(0x1E97, 0x20), // Collected all the medallions before Grim Reaper 2
    CA_ca01_on(0x1E98, 0x01),
    CA_ca08_on(0x1E98, 0x04),
    CA_ca14_on(0x1E98, 0x08),
    CA_EVENT_111(0x1E98, 0x10),
    CA_EVENT_117(0x1E98, 0x20),
    CA_EVENT_205(0x1E98, 0x80),
    CA_ca16_on(0x1E99, 0x02),
    CA_INIT(0x1E99, 0x04),
    CA_SCENARIO_1_OPEN(0x1E99, 0x08),
    CA_SCENARIO_1_START(0x1E99, 0x10),
    CA_SCENARIO_1_END(0x1E99, 0x20),
    CA_SCENARIO_2_OPEN(0x1E99, 0x40),
    CA_SCENARIO_2_START(0x1E99, 0x80),
    CA_SCENARIO_2_END(0x1E9A, 0x01),
    CA_104_OUT(0x1E9A, 0x02),
    CA_JACK01_START(0x1E9B, 0x04),
    CA_JACK01_END(0x1E9B, 0x08),
    CA_JACK02_START(0x1E9B, 0x10),
    CA_JACK02_END(0x1E9B, 0x20),
    CA_NO_JACK01_START(0x1E9B, 0x40),
    CA_NO_JACK01_END(0x1E9B, 0x80),
    CA_NO_JACK02_START(0x1E9C, 0x01),
    CA_NO_JACK02_END(0x1E9C, 0x02),
    CA_ca18_ms202(0x1E9C, 0x04), // Grim Reaper 1 fight end
    CA_FM_COM_OBJ_OFF(0x1E9C, 0x08),
    CA_FM_KINOKO_LUX_PLAYED(0x1E9C, 0x10);

    override val index: Int
      get() = ordinal

    override val flagName: String
      get() = name

  }

}
