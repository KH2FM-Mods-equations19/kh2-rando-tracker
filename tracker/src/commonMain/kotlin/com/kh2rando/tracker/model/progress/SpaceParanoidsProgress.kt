package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_chests
import com.kh2rando.tracker.generated.resources.prog_sp_hostile_program
import com.kh2rando.tracker.generated.resources.prog_sp_larxene
import com.kh2rando.tracker.generated.resources.prog_sp_larxene_data
import com.kh2rando.tracker.generated.resources.prog_sp_mcp
import com.kh2rando.tracker.generated.resources.prog_sp_screens
import com.kh2rando.tracker.generated.resources.prog_sp_solar_sailer
import com.kh2rando.tracker.generated.resources.progression_chest
import com.kh2rando.tracker.generated.resources.progression_sp_dataspace
import com.kh2rando.tracker.generated.resources.progression_sp_hostile_program
import com.kh2rando.tracker.generated.resources.progression_sp_larxene
import com.kh2rando.tracker.generated.resources.progression_sp_larxene_data
import com.kh2rando.tracker.generated.resources.progression_sp_master_control_program
import com.kh2rando.tracker.generated.resources.progression_sp_solar_sailer
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class SpaceParanoidsProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
  override val associatedFlag: ProgressFlag,
) : ProgressCheckpoint, HasCustomizableIcon {

  Chests(
    displayString = Res.string.prog_chests,
    defaultIcon = Res.drawable.progression_chest,
    customIconIdentifier = "chest",
    associatedFlag = Flag.TR_SCENARIO_1_START,
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression")
  },

  Screens(
    displayString = Res.string.prog_sp_screens,
    defaultIcon = Res.drawable.progression_sp_dataspace,
    customIconIdentifier = "dataspace",
    associatedFlag = Flag.TR_tr03_ms103,
  ),

  HostileProgram(
    displayString = Res.string.prog_sp_hostile_program,
    defaultIcon = Res.drawable.progression_sp_hostile_program,
    customIconIdentifier = "hostile_program",
    associatedFlag = Flag.TR_tr04_ms104,
  ),

  SolarSailer(
    displayString = Res.string.prog_sp_solar_sailer,
    defaultIcon = Res.drawable.progression_sp_solar_sailer,
    customIconIdentifier = "solar_sailer",
    associatedFlag = Flag.TR_tr07_ms203,
  ),

  MCP(
    displayString = Res.string.prog_sp_mcp,
    defaultIcon = Res.drawable.progression_sp_master_control_program,
    customIconIdentifier = "master_control_program",
    associatedFlag = Flag.TR_tr09_ms205,
  ),

  Larxene(
    displayString = Res.string.prog_sp_larxene,
    defaultIcon = Res.drawable.progression_sp_larxene,
    customIconIdentifier = "larxene",
    associatedFlag = HollowBastionProgress.Flag.HB_FM_COM_LAR_END,
  ),

  LarxeneData(
    displayString = Res.string.prog_sp_larxene_data,
    defaultIcon = Res.drawable.progression_sp_larxene_data,
    customIconIdentifier = "larxene_data",
    associatedFlag = HollowBastionProgress.Flag.HB_FM_LAR_RE_CLEAR,
  );

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.SpaceParanoids

  override val customIconPath: List<String>
    get() = listOf("Progression", "space_paranoids")

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<SpaceParanoidsProgress, Int> {
      val inOrder = listOf(Chests, Screens, HostileProgram, SolarSailer, MCP, Larxene, LarxeneData)
      return inOrder.zip(pointsList).toMap()
    }

  }

  enum class Flag(override val saveOffset: Int, override val mask: Int) : ProgressFlag {

    TR_START(0x1EB0, 0x01),
    TR_101_END(0x1EB0, 0x02),
    TR_102_END(0x1EB0, 0x04),
    TR_103_END(0x1EB0, 0x08),
    TR_104_END(0x1EB0, 0x10),
    TR_105_END(0x1EB0, 0x20),
    TR_tr01_ms101(0x1EB0, 0x40),
    TR_107_END(0x1EB0, 0x80),
    TR_108_END(0x1EB1, 0x02),
    TR_109_END(0x1EB1, 0x04),
    TR_110_END(0x1EB1, 0x10),
    TR_111_END(0x1EB1, 0x20),
    TR_tr03_ms103(0x1EB1, 0x40),
    TR_113_END(0x1EB1, 0x80),
    TR_114_END(0x1EB2, 0x01),
    TR_tr04_ms104(0x1EB2, 0x02),
    TR_116_END(0x1EB2, 0x04),
    TR_117_END(0x1EB2, 0x08),
    TR_START2(0x1EB2, 0x10),
    TR_201_END(0x1EB2, 0x20),
    TR_202_END(0x1EB2, 0x40),
    TR_203_END(0x1EB2, 0x80),
    TR_tr02_ms210(0x1EB3, 0x01), // Second visit Light Cycle forced fight end
    TR_204_END(0x1EB3, 0x02),
    TR_205_END(0x1EB3, 0x04),
    TR_tr04_ms202(0x1EB3, 0x08), // Second visit Hallway forced fight end
    TR_206_END(0x1EB3, 0x10),
    TR_207_END(0x1EB3, 0x20),
    TR_208_END(0x1EB3, 0x40),
    TR_tr07_ms203(0x1EB3, 0x80), // Solar sailer fight end
    TR_209_END(0x1EB4, 0x01),
    TR_210_END(0x1EB4, 0x02),
    TR_tr09_ms204(0x1EB4, 0x04), // Sark fight end
    TR_tr09_ms205(0x1EB4, 0x08), // MCP fight end
    TR_hb_304_END(0x1EB4, 0x10),
    TR_117_tr05_jump(0x1EB4, 0x40),
    TR_hb_501_END(0x1EB5, 0x01),
    TR_hb_502_END(0x1EB5, 0x02),
    TR_503_END(0x1EB5, 0x04),
    TR_tr02_ms102a(0x1EB5, 0x08),
    TR_tr02_ms102b(0x1EB5, 0x10),
    TR_INIT(0x1EB5, 0x20),
    TR_SCENARIO_1_OPEN(0x1EB5, 0x40),
    TR_SCENARIO_1_START(0x1EB5, 0x80),
    TR_SCENARIO_1_END(0x1EB6, 0x01),
    TR_SCENARIO_2_OPEN(0x1EB6, 0x02),
    TR_SCENARIO_2_START(0x1EB6, 0x04),
    TR_SCENARIO_2_END(0x1EB6, 0x08),
    TR_tr01_ms101_EXIT(0x1EB6, 0x10),
    TR_RTN_ON(0x1EB6, 0x40),
    TR_113_OUT(0x1EB6, 0x80),
    TR_113_IN(0x1EB7, 0x01),
    TR_HB05_HIDDEN_OFF(0x1EB7, 0x04),
    TR_FRAG_OFF(0x1EB7, 0x08);

    override val index: Int
      get() = ordinal

    override val flagName: String
      get() = name

  }

}
