package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_chests
import com.kh2rando.tracker.generated.resources.prog_dc_boat_pete
import com.kh2rando.tracker.generated.resources.prog_dc_dc_pete
import com.kh2rando.tracker.generated.resources.prog_dc_lingering_will
import com.kh2rando.tracker.generated.resources.prog_dc_marluxia
import com.kh2rando.tracker.generated.resources.prog_dc_marluxia_data
import com.kh2rando.tracker.generated.resources.prog_dc_minnie
import com.kh2rando.tracker.generated.resources.prog_dc_old_pete
import com.kh2rando.tracker.generated.resources.prog_dc_windows
import com.kh2rando.tracker.generated.resources.progression_chest
import com.kh2rando.tracker.generated.resources.progression_dc_lingering_will
import com.kh2rando.tracker.generated.resources.progression_dc_marluxia
import com.kh2rando.tracker.generated.resources.progression_dc_marluxia_data
import com.kh2rando.tracker.generated.resources.progression_dc_minnie
import com.kh2rando.tracker.generated.resources.progression_dc_old_pete
import com.kh2rando.tracker.generated.resources.progression_dc_pete
import com.kh2rando.tracker.generated.resources.progression_dc_steamboat_pete
import com.kh2rando.tracker.generated.resources.progression_dc_windows_of_time
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class DisneyCastleProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
  override val associatedFlag: ProgressFlag,
) : ProgressCheckpoint, HasCustomizableIcon {

  Chests(
    displayString = Res.string.prog_chests,
    defaultIcon = Res.drawable.progression_chest,
    customIconIdentifier = "chest",
    associatedFlag = Flag.DC_SCENARIO_1_START,
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression")
  },

  Minnie(
    displayString = Res.string.prog_dc_minnie,
    defaultIcon = Res.drawable.progression_dc_minnie,
    customIconIdentifier = "minnie",
    associatedFlag = Flag.DC_107_END_L,
  ),

  OldPete(
    displayString = Res.string.prog_dc_old_pete,
    defaultIcon = Res.drawable.progression_dc_old_pete,
    customIconIdentifier = "old_pete",
    associatedFlag = Flag.WI_103_END_L,
  ),

  Windows(
    displayString = Res.string.prog_dc_windows,
    defaultIcon = Res.drawable.progression_dc_windows_of_time,
    customIconIdentifier = "windows_of_time",
    associatedFlag = Flag.WI_116_END,
  ),

  BoatPete(
    displayString = Res.string.prog_dc_boat_pete,
    defaultIcon = Res.drawable.progression_dc_steamboat_pete,
    customIconIdentifier = "steamboat_pete",
    associatedFlag = Flag.WI_118_END_L,
  ),

  DCPete(
    displayString = Res.string.prog_dc_dc_pete,
    defaultIcon = Res.drawable.progression_dc_pete,
    customIconIdentifier = "pete",
    associatedFlag = Flag.WI_120_END_L,
  ),

  Marluxia(
    displayString = Res.string.prog_dc_marluxia,
    defaultIcon = Res.drawable.progression_dc_marluxia,
    customIconIdentifier = "marluxia",
    associatedFlag = HollowBastionProgress.Flag.HB_FM_COM_MAR_END,
  ),

  MarluxiaData(
    displayString = Res.string.prog_dc_marluxia_data,
    defaultIcon = Res.drawable.progression_dc_marluxia_data,
    customIconIdentifier = "marluxia_data",
    associatedFlag = HollowBastionProgress.Flag.HB_FM_MAR_RE_CLEAR,
  ),

  LingeringWill(
    displayString = Res.string.prog_dc_lingering_will,
    defaultIcon = Res.drawable.progression_dc_lingering_will,
    customIconIdentifier = "lingering_will",
    associatedFlag = Flag.DC_FM_NAZO_BTL_CLEAR,
  );

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.DisneyCastle

  override val customIconPath: List<String>
    get() = listOf("Progression", "disney_castle")

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<DisneyCastleProgress, Int> {
      val inOrder = listOf(Chests, Minnie, OldPete, Windows, BoatPete, DCPete, Marluxia, MarluxiaData, LingeringWill)
      return inOrder.zip(pointsList).toMap()
    }

  }

  enum class Flag(override val saveOffset: Int, override val mask: Int) : ProgressFlag {

    DC_DC_OPEN_L(0x1E10, 0x01),
    DC_START(0x1E10, 0x02),
    DC_102_END(0x1E10, 0x04),
    DC_103_END(0x1E10, 0x08),
    DC_104_END(0x1E10, 0x10),
    DC_MINNIE_CANCEL(0x1E10, 0x20),
    DC_105_END(0x1E10, 0x40),
    DC_106_END(0x1E11, 0x01),
    DC_107_END_L(0x1E11, 0x02),
    DC_108_END(0x1E11, 0x04),
    DC_SCENARIO_1_OPEN(0x1E11, 0x08),
    DC_109_END(0x1E11, 0x10),
    DC_110_END(0x1E11, 0x20),
    DC_WI_CLEAR(0x1E11, 0x40),
    DC_111_END(0x1E11, 0x80),
    DC_SCENARIO_1_START(0x1E12, 0x01),
    DC_HB_MERLIN_MEET(0x1E12, 0x02),
    DC_SCENARIO_1_END(0x1E12, 0x04),
    DC_INIT(0x1E12, 0x08),
    DC_MINNIE_GET(0x1E13, 0x20),
    DC_105_OUT(0x1E13, 0x40),
    DC_FM_NAZO_ON(0x1E13, 0x80),
    DC_FM_NAZO_BTL_READY(0x1E14, 0x02),
    DC_FM_NAZO_BTL_CLEAR(0x1E14, 0x04), // Lingering Will fight end
    WI_START(0x1E30, 0x01),
    WI_101_END(0x1E30, 0x02),
    WI_102_END(0x1E30, 0x04),
    WI_103_END_L(0x1E30, 0x08),
    WI_104_END(0x1E30, 0x10),
    WI_106_END_L(0x1E30, 0x40), // Window 4 fight end
    WI_108_END_L(0x1E31, 0x01), // Window 3 fight end
    WI_110_END_L(0x1E31, 0x04), // Window 2 fight end
    WI_112_END_L(0x1E31, 0x10), // Window 1 fight end
    WI_VISION_1_END_L(0x1E31, 0x20), // Window 1 fight end
    WI_VISION_2_END_L(0x1E31, 0x40), // Window 2 fight end
    WI_VISION_3_END_L(0x1E31, 0x80), // Window 3 fight end
    WI_VISION_4_END_L(0x1E32, 0x01), // Window 4 fight end
    WI_113_END(0x1E32, 0x02),
    WI_114_END(0x1E32, 0x04),
    WI_115_END(0x1E32, 0x08),
    WI_116_END(0x1E32, 0x10), // All windows done
    WI_117_END(0x1E32, 0x20),
    WI_118_END_L(0x1E32, 0x40), // Boat Pete fight end
    WI_118_OUT(0x1E32, 0x80),
    WI_119_END(0x1E33, 0x01),
    WI_120_END_L(0x1E33, 0x02), // Pete/Pete fight end
    WI_121_END(0x1E33, 0x04),
    WI_122_END(0x1E33, 0x08),
    WI_105_END_A(0x1E33, 0x10),
    WI_105_END_B(0x1E33, 0x20),
    WI_107_END_A(0x1E33, 0x40),
    WI_107_END_B(0x1E33, 0x80),
    WI_109_END_A(0x1E34, 0x01),
    WI_109_END_B(0x1E34, 0x02),
    WI_111_END_A(0x1E34, 0x04),
    WI_111_END_B(0x1E34, 0x08),
    WI_INIT(0x1E34, 0x10),
    WI_SCENARIO_1_OPEN(0x1E34, 0x20),
    WI_SCENARIO_1_START(0x1E34, 0x40),
    WI_SCENARIO_1_END(0x1E34, 0x80),
    WI_START2(0x1E35, 0x01),
    WI_SCENARIO_2_OPEN(0x1E35, 0x02),
    WI_FM_KINOKO_MAR_PLAYED(0x1E35, 0x04);

    override val index: Int
      get() = ordinal

    override val flagName: String
      get() = name

  }

}
