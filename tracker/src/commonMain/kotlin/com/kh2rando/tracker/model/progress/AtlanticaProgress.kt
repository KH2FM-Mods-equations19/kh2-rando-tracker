package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_at_new_day
import com.kh2rando.tracker.generated.resources.prog_at_tutorial
import com.kh2rando.tracker.generated.resources.prog_at_ursula
import com.kh2rando.tracker.generated.resources.progression_at_concert
import com.kh2rando.tracker.generated.resources.progression_at_tutorial
import com.kh2rando.tracker.generated.resources.progression_at_ursula
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class AtlanticaProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
  override val associatedFlag: ProgressFlag?,
) : ProgressCheckpoint, HasCustomizableIcon {

  Tutorial(
    displayString = Res.string.prog_at_tutorial,
    defaultIcon = Res.drawable.progression_at_tutorial,
    customIconIdentifier = "tutorial",
    associatedFlag = Flag.LM_104_END_L,
  ),

  Ursula(
    displayString = Res.string.prog_at_ursula,
    defaultIcon = Res.drawable.progression_at_ursula,
    customIconIdentifier = "ursula",
    associatedFlag = Flag.LM_409_END_L,
  ),

  NewDay(
    displayString = Res.string.prog_at_new_day,
    defaultIcon = Res.drawable.progression_at_concert,
    customIconIdentifier = "concert",
    associatedFlag = Flag.LM_502_END_L,
  );

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.Atlantica

  override val customIconPath: List<String>
    get() = listOf("Progression", "atlantica")

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<AtlanticaProgress, Int> {
      val inOrder = listOf(Tutorial, Ursula, NewDay)
      return inOrder.zip(pointsList).toMap()
    }

  }

  enum class Flag(override val saveOffset: Int, override val mask: Int) : ProgressFlag {

    LM_START(0x1DF0, 0x1),
    LM_101_END(0x1DF0, 0x2),
    LM_102_END(0x1DF0, 0x4),
    LM_103_END_L(0x1DF0, 0x8),
    LM_START_1(0x1DF0, 0x10),
    LM_107_END(0x1DF1, 0x1),
    LM_108_END(0x1DF1, 0x2),
    LM_START_2(0x1DF1, 0x4),
    LM_201_END(0x1DF1, 0x8),
    LM_202_END(0x1DF1, 0x10),
    LM_203_END_L(0x1DF1, 0x20), // Part of Your World end
    LM_204_END(0x1DF1, 0x40),
    LM_205_END(0x1DF1, 0x80),
    LM_START_3(0x1DF2, 0x1),
    LM_301_END(0x1DF2, 0x2),
    LM_302_END_L(0x1DF2, 0x4), // Under the Sea end
    LM_303_END(0x1DF2, 0x8),
    LM_START_4(0x1DF2, 0x10),
    LM_401_END(0x1DF2, 0x20),
    LM_402_END(0x1DF2, 0x40),
    LM_403_END(0x1DF2, 0x80),
    LM_404_END(0x1DF3, 0x1),
    LM_405_END(0x1DF3, 0x4),
    LM_406_END(0x1DF3, 0x8),
    LM_408_END(0x1DF3, 0x20),
    LM_START_5(0x1DF3, 0x80),
    LM_501_END(0x1DF4, 0x1),
    LM_502_END_L(0x1DF4, 0x2), // New Day end
    LM_104_END_L(0x1DF4, 0x4), // Tutorial end
    LM_105_END(0x1DF4, 0x8),
    LM_106_END_L(0x1DF4, 0x10), // Swim This Way end
    LM_109_END(0x1DF4, 0x20),
    LM_GET_ITEM_2(0x1DF4, 0x40),
    LM_GET_ITEM_3(0x1DF4, 0x80),
    LM_GET_ITEM_4(0x1DF5, 0x1),
    LM_407_END(0x1DF5, 0x2),
    LM_409_END_L(0x1DF5, 0x4), // Ursula's Revenge end
    LM_410_END(0x1DF5, 0x8),
    LM_411_END(0x1DF5, 0x10),
    LM_GET_ITEM_5(0x1DF5, 0x20),
    LM_INIT(0x1DF5, 0x40),
    LM_SCENARIO_1_START(0x1DF5, 0x80),
    LM_SCENARIO_1_OPEN(0x1DF6, 0x1),
    LM_SCENARIO_1_END(0x1DF6, 0x2),
    LM_SCENARIO_2_START(0x1DF6, 0x4),
    LM_SCENARIO_2_OPEN(0x1DF6, 0x8),
    LM_SCENARIO_2_END(0x1DF6, 0x10),
    LM_SCENARIO_3_START(0x1DF6, 0x20),
    LM_SCENARIO_3_OPEN(0x1DF6, 0x40),
    LM_SCENARIO_3_END(0x1DF6, 0x80),
    LM_SCENARIO_4_START(0x1DF7, 0x1),
    LM_SCENARIO_4_OPEN(0x1DF7, 0x2),
    LM_SCENARIO_4_END(0x1DF7, 0x4),
    LM_SCENARIO_5_START(0x1DF7, 0x8),
    LM_SCENARIO_5_OPEN(0x1DF7, 0x10),
    LM_SCENARIO_5_END(0x1DF7, 0x20),
    LM_lm04_ms103_failed(0x1DF7, 0x40),
    LM_lm01_ms201_failed(0x1DF7, 0x80),
    LM_lm03_ms301_failed(0x1DF8, 0x1),
    LM_lm09_ms401_failed(0x1DF8, 0x2),
    LM_lm04_ms501_failed(0x1DF8, 0x4),
    LM_lm04_ms103_FREE(0x1DF8, 0x8),
    LM_lm01_ms201_FREE(0x1DF8, 0x10),
    LM_lm03_ms301_FREE(0x1DF8, 0x20),
    LM_lm09_ms401_FREE(0x1DF8, 0x40),
    LM_lm04_ms501_FREE(0x1DF8, 0x80),
    LM_lm02_ms102_failed(0x1DF9, 0x1);

    override val index: Int
      get() = ordinal

    override val flagName: String
      get() = name

  }

}
