package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_bc_beast
import com.kh2rando.tracker.generated.resources.prog_bc_dark_thorn
import com.kh2rando.tracker.generated.resources.prog_bc_data_xaldin
import com.kh2rando.tracker.generated.resources.prog_bc_dragoons
import com.kh2rando.tracker.generated.resources.prog_bc_thresholder
import com.kh2rando.tracker.generated.resources.prog_bc_xaldin
import com.kh2rando.tracker.generated.resources.prog_chests
import com.kh2rando.tracker.generated.resources.progression_bc_beast
import com.kh2rando.tracker.generated.resources.progression_bc_dark_thorn
import com.kh2rando.tracker.generated.resources.progression_bc_dragoons
import com.kh2rando.tracker.generated.resources.progression_bc_thresholder
import com.kh2rando.tracker.generated.resources.progression_bc_xaldin
import com.kh2rando.tracker.generated.resources.progression_bc_xaldin_story
import com.kh2rando.tracker.generated.resources.progression_chest
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class BeastsCastleProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
  override val associatedFlag: Flag,
) : ProgressCheckpoint, HasCustomizableIcon {

  Chests(
    displayString = Res.string.prog_chests,
    defaultIcon = Res.drawable.progression_chest,
    customIconIdentifier = "chest",
    associatedFlag = Flag.BB_SCENARIO_1_START,
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression")
  },

  Thresholder(
    displayString = Res.string.prog_bc_thresholder,
    defaultIcon = Res.drawable.progression_bc_thresholder,
    customIconIdentifier = "thresholder",
    associatedFlag = Flag.BB_bb11_ms102,
  ),

  Beast(
    displayString = Res.string.prog_bc_beast,
    defaultIcon = Res.drawable.progression_bc_beast,
    customIconIdentifier = "beast",
    associatedFlag = Flag.BB_bb03_ms103,
  ),

  DarkThorn(
    displayString = Res.string.prog_bc_dark_thorn,
    defaultIcon = Res.drawable.progression_bc_dark_thorn,
    customIconIdentifier = "dark_thorn",
    associatedFlag = Flag.BB_bb05_ms104b,
  ),

  Dragoons(
    displayString = Res.string.prog_bc_dragoons,
    defaultIcon = Res.drawable.progression_bc_dragoons,
    customIconIdentifier = "dragoons",
    associatedFlag = Flag.BB_bb00_ms202,
  ),

  Xaldin(
    displayString = Res.string.prog_bc_xaldin,
    defaultIcon = Res.drawable.progression_bc_xaldin_story,
    customIconIdentifier = "xaldin_story",
    associatedFlag = Flag.BB_bb15_ms203,
  ),

  DataXaldin(
    displayString = Res.string.prog_bc_data_xaldin,
    defaultIcon = Res.drawable.progression_bc_xaldin,
    customIconIdentifier = "xaldin",
    associatedFlag = Flag.BB_FM_XAL_RE_CLEAR,
  );

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.BeastsCastle

  override val customIconPath: List<String>
    get() = listOf("Progression", "beast's_castle")

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<BeastsCastleProgress, Int> {
      val inOrder = listOf(Chests, Thresholder, Beast, DarkThorn, Dragoons, Xaldin, DataXaldin)
      return inOrder.zip(pointsList).toMap()
    }

  }

  enum class Flag(override val saveOffset: Int, override val mask: Int) : ProgressFlag {

    BB_START(0x1D30, 0x01),
    BB_101_END(0x1D30, 0x02),
    BB_102_END(0x1D30, 0x04),
    BB_bb05_ms104a(0x1D30, 0x20), // Shadowstalker fight end
    BB_bb05_ms104b(0x1D30, 0x40), // Dark Thorn fight end
    BB_124_END(0x1D30, 0x80),
    BB_108_END(0x1D31, 0x01),
    BB_104_END(0x1D31, 0x02),
    BB_bb15_ms203(0x1D31, 0x04), // Xaldin fight end
    BB_INIT(0x1D31, 0x08),
    BB_SCENARIO_1_OPEN(0x1D31, 0x10),
    BB_SCENARIO_1_START(0x1D31, 0x20),
    BB_114_END(0x1D31, 0x40),
    BB_SCENARIO_1_END(0x1D31, 0x80),
    BB_SCENARIO_2_OPEN(0x1D32, 0x01),
    BB_SCENARIO_2_START(0x1D32, 0x02),
    BB_SCENARIO_2_END(0x1D32, 0x04),
    BB_119_END(0x1D32, 0x20),
    BB_START2(0x1D33, 0x01),
    BB_201_END(0x1D33, 0x02),
    BB_204_END(0x1D33, 0x10),
    BB_205_END(0x1D33, 0x20),
    BB_207_END(0x1D33, 0x80),
    BB_208_OUT(0x1D34, 0x02),
    BB_210_END(0x1D34, 0x04),
    BB_FM_COM_OBJ_OFF(0x1D34, 0x10),
    BB_213_END(0x1D34, 0x20),
    BB_214_END(0x1D34, 0x40),
    BB_FM_XAL_RE_CLEAR(0x1D34, 0x80),
    BB_216_END(0x1D35, 0x01),
    BB_bb01_ms101(0x1D35, 0x02),
    BB_bb11_ms102(0x1D35, 0x04), // Thresholder fight end
    BB_bb03_ms103(0x1D35, 0x08), // Beast fight end
    BB_FM_KINOKO_XAL_PLAYED(0x1D35, 0x20),
    BB_105_END(0x1D36, 0x04),
    BB_106_END(0x1D36, 0x08),
    BB_107_END(0x1D36, 0x10),
    BB_110_END(0x1D36, 0x40),
    BB_111_END(0x1D36, 0x80),
    BB_112_END(0x1D37, 0x01),
    BB_113_END(0x1D37, 0x02),
    BB_116_END(0x1D37, 0x08),
    BB_117_END(0x1D37, 0x10),
    BB_118_END(0x1D37, 0x20),
    BB_202_END(0x1D38, 0x08),
    BB_208_END(0x1D38, 0x80),
    BB_209_END(0x1D39, 0x01),
    BB_212_END(0x1D39, 0x04),
    BB_111_OUT(0x1D39, 0x40),
    BB_bb04_ms201(0x1D39, 0x80), // Ballroom fight end
    BB_bb00_ms202(0x1D3A, 0x01), // Dragoons fight Pre-Xaldin end
    BB_107_OUT(0x1D3A, 0x04),
    BB_113_OUT(0x1D3A, 0x08),
    BB_117_OUT(0x1D3A, 0x10),
    BB_bb00_ms202_OUT(0x1D3A, 0x20);

    override val index: Int
      get() = ordinal

    override val flagName: String
      get() = name

  }

}
