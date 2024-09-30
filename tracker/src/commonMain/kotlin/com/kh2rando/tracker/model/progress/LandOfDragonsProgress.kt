package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_chests
import com.kh2rando.tracker.generated.resources.prog_lod_cave
import com.kh2rando.tracker.generated.resources.prog_lod_data_xigbar
import com.kh2rando.tracker.generated.resources.prog_lod_missions
import com.kh2rando.tracker.generated.resources.prog_lod_mountain
import com.kh2rando.tracker.generated.resources.prog_lod_shan_yu
import com.kh2rando.tracker.generated.resources.prog_lod_storm_rider
import com.kh2rando.tracker.generated.resources.prog_lod_summit
import com.kh2rando.tracker.generated.resources.prog_lod_throne_room
import com.kh2rando.tracker.generated.resources.progression_chest
import com.kh2rando.tracker.generated.resources.progression_lod_missions
import com.kh2rando.tracker.generated.resources.progression_lod_mountain_climb
import com.kh2rando.tracker.generated.resources.progression_lod_shan_yu
import com.kh2rando.tracker.generated.resources.progression_lod_snipers
import com.kh2rando.tracker.generated.resources.progression_lod_storm_rider
import com.kh2rando.tracker.generated.resources.progression_lod_summit
import com.kh2rando.tracker.generated.resources.progression_lod_village_cave
import com.kh2rando.tracker.generated.resources.progression_lod_xigbar
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class LandOfDragonsProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
  override val associatedFlag: ProgressFlag,
) : ProgressCheckpoint, HasCustomizableIcon {

  Chests(
    displayString = Res.string.prog_chests,
    defaultIcon = Res.drawable.progression_chest,
    customIconIdentifier = "chest",
    associatedFlag = Flag.MU_SCENARIO_1_START,
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression")
  },

  Missions(
    displayString = Res.string.prog_lod_missions,
    defaultIcon = Res.drawable.progression_lod_missions,
    customIconIdentifier = "missions",
    associatedFlag = Flag.MU_MS103_COMPLETE,
  ),

  Mountain(
    displayString = Res.string.prog_lod_mountain,
    defaultIcon = Res.drawable.progression_lod_mountain_climb,
    customIconIdentifier = "mountain_climb",
    associatedFlag = Flag.MU_107_END_L,
  ),

  Cave(
    displayString = Res.string.prog_lod_cave,
    defaultIcon = Res.drawable.progression_lod_village_cave,
    customIconIdentifier = "village_cave",
    associatedFlag = Flag.MU_110_END_L,
  ),

  Summit(
    displayString = Res.string.prog_lod_summit,
    defaultIcon = Res.drawable.progression_lod_summit,
    customIconIdentifier = "summit",
    associatedFlag = Flag.MU_113_END_L,
  ),

  ShanYu(
    displayString = Res.string.prog_lod_shan_yu,
    defaultIcon = Res.drawable.progression_lod_shan_yu,
    customIconIdentifier = "shan_yu",
    associatedFlag = Flag.MU_118_END_L,
  ),

  ThroneRoom(
    displayString = Res.string.prog_lod_throne_room,
    defaultIcon = Res.drawable.progression_lod_snipers,
    customIconIdentifier = "snipers",
    associatedFlag = Flag.MU_211_END_L,
  ),

  StormRider(
    displayString = Res.string.prog_lod_storm_rider,
    defaultIcon = Res.drawable.progression_lod_storm_rider,
    customIconIdentifier = "storm_rider",
    associatedFlag = Flag.MU_215_END_L,
  ),

  DataXigbar(
    displayString = Res.string.prog_lod_data_xigbar,
    defaultIcon = Res.drawable.progression_lod_xigbar,
    customIconIdentifier = "xigbar",
    associatedFlag = WorldThatNeverWasProgress.Flag.EH_FM_XIG_RE_CLEAR,
  );

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.LandOfDragons

  override val customIconPath: List<String>
    get() = listOf("Progression", "land_of_dragons")

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<LandOfDragonsProgress, Int> {
      val inOrder = listOf(Chests, Missions, Mountain, Cave, Summit, ShanYu, ThroneRoom, StormRider, DataXigbar)
      return inOrder.zip(pointsList).toMap()
    }

  }

  enum class Flag(override val saveOffset: Int, override val mask: Int) : ProgressFlag {

    MU_START(0x1D90, 0x01),
    MU_102_END(0x1D90, 0x02),
    MU_119_END(0x1D90, 0x04),
    MU_119_OUT(0x1D90, 0x08),
    MU_INIT(0x1D91, 0x01),
    MU_SCENARIO_1_START(0x1D91, 0x02),
    MU_SCENARIO_1_OPEN(0x1D91, 0x04),
    MU_SCENARIO_1_END(0x1D91, 0x08),
    MU_SCENARIO_2_START(0x1D91, 0x10),
    MU_SCENARIO_2_OPEN(0x1D91, 0x20),
    MU_114_END(0x1D91, 0x40),
    MU_SCENARIO_2_END(0x1D91, 0x80),
    MU_116_END_L(0x1D92, 0x01),
    MU_118_END_L(0x1D92, 0x04), // Shan-Yu fight end
    MU_START2(0x1D92, 0x08),
    MU_202_END(0x1D92, 0x20),
    MU_204_END_L(0x1D92, 0x80), // Riku fight end
    MU_MULAN_START(0x1D93, 0x01),
    MU_206_END(0x1D93, 0x02),
    MU_208_END_L(0x1D93, 0x08), // Square fight second visit end
    MU_211_END_L(0x1D93, 0x40), // Pre-throne room fight (Snipers)
    MU_213_END(0x1D94, 0x01),
    MU_MS103A_CLEAR_END(0x1D94, 0x02),
    MU_215_END_L(0x1D94, 0x04), // Storm Rider end
    MU_216_END(0x1D94, 0x08),
    MU_101_END(0x1D94, 0x10),
    MU_103_END(0x1D94, 0x20),
    MU_104_END_L(0x1D94, 0x40), // Opening fight end
    MU_MS103A_CLEAR(0x1D95, 0x10), // Mission 1 end
    MU_MS103C_CLEAR(0x1D96, 0x01), // Mission 3 end
    MU_MS103B_CLEAR_END(0x1D96, 0x02),
    MU_MS103_COMPLETE(0x1D96, 0x04), // All missions complete
    MU_105_END(0x1D96, 0x08),
    MU_106_END(0x1D96, 0x10),
    MU_107_END_L(0x1D96, 0x20), // Mountain climb end
    MU_108_END(0x1D96, 0x40),
    MU_109_END(0x1D96, 0x80),
    MU_110_END_L(0x1D97, 0x01), // Cave fight end
    MU_111_END(0x1D97, 0x02),
    MU_112_END(0x1D97, 0x04),
    MU_113_END_L(0x1D97, 0x08), // Summit fight end
    MU_114_OUT(0x1D97, 0x10),
    MU_115_END(0x1D97, 0x20),
    MU_116_OUT(0x1D97, 0x40),
    MU_117_END(0x1D97, 0x80),
    MU_201_END(0x1D98, 0x01),
    MU_203_END(0x1D98, 0x02),
    MU_205_END(0x1D98, 0x04),
    MU_207_END(0x1D98, 0x08),
    MU_207A_END(0x1D98, 0x10),
    MU_209_END(0x1D98, 0x20),
    MU_210A_END(0x1D98, 0x40),
    MU_210_END(0x1D98, 0x80),
    MU_212_END(0x1D99, 0x02),
    MU_214_END(0x1D99, 0x04),
    MU_MS103B_CLEAR(0x1D99, 0x40), // Mission 2 end
    MU_202_OUT(0x1D9A, 0x20),
    MU_113_OUT(0x1D9A, 0x80),
    MU_FM_KINOKO_VEX_PLAYED(0x1D9B, 0x01);

    override val index: Int
      get() = ordinal

    override val flagName: String
      get() = name

  }

}
