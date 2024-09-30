package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_chests
import com.kh2rando.tracker.generated.resources.prog_haw_kanga
import com.kh2rando.tracker.generated.resources.prog_haw_piglet
import com.kh2rando.tracker.generated.resources.prog_haw_rabbit
import com.kh2rando.tracker.generated.resources.prog_haw_spooky_cave
import com.kh2rando.tracker.generated.resources.prog_haw_starry_hill
import com.kh2rando.tracker.generated.resources.progression_chest
import com.kh2rando.tracker.generated.resources.progression_haw_balloon_bounce
import com.kh2rando.tracker.generated.resources.progression_haw_blustery_rescue
import com.kh2rando.tracker.generated.resources.progression_haw_expotition
import com.kh2rando.tracker.generated.resources.progression_haw_hunny_pot
import com.kh2rando.tracker.generated.resources.progression_haw_hunny_slider
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class HundredAcreWoodProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
  override val associatedFlag: Flag? = null,
) : ProgressCheckpoint, HasCustomizableIcon {

  Chests(
    displayString = Res.string.prog_chests,
    defaultIcon = Res.drawable.progression_chest,
    customIconIdentifier = "chest",
    // There's not really a progress flag for this, so we have special handling for it elsewhere
    associatedFlag = null,
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression")
  },

  Piglet(
    displayString = Res.string.prog_haw_piglet,
    defaultIcon = Res.drawable.progression_haw_blustery_rescue,
    customIconIdentifier = "blustery_rescue",
    associatedFlag = Flag.PO_MS101_END_L,
  ),

  Rabbit(
    displayString = Res.string.prog_haw_rabbit,
    defaultIcon = Res.drawable.progression_haw_hunny_slider,
    customIconIdentifier = "hunny_slider",
    associatedFlag = Flag.PO_MS201_END_L,
  ),

  Kanga(
    displayString = Res.string.prog_haw_kanga,
    defaultIcon = Res.drawable.progression_haw_balloon_bounce,
    customIconIdentifier = "balloon_bounce",
    associatedFlag = Flag.PO_MS301_END_L,
  ),

  SpookyCave(
    displayString = Res.string.prog_haw_spooky_cave,
    defaultIcon = Res.drawable.progression_haw_expotition,
    customIconIdentifier = "expotition",
    associatedFlag = Flag.PO_MS401_END_L,
  ),

  StarryHill(
    displayString = Res.string.prog_haw_starry_hill,
    defaultIcon = Res.drawable.progression_haw_hunny_pot,
    customIconIdentifier = "hunny_pot",
    associatedFlag = Flag.PO_MS501_END_L,
  );

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.HundredAcreWood

  override val customIconPath: List<String>
    get() = listOf("Progression", "100_acre_wood")

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<HundredAcreWoodProgress, Int> {
      val inOrder = listOf(Chests, Piglet, Rabbit, Kanga, SpookyCave, StarryHill)
      return inOrder.zip(pointsList).toMap()
    }

  }

  enum class Flag(override val saveOffset: Int, override val mask: Int) : ProgressFlag {

    PO_START(0x1DB0, 0x01),
    PO_001_END(0x1DB0, 0x02),
    PO_003_END(0x1DB0, 0x04),
    PO_004_END(0x1DB0, 0x08),
    PO_HB_BATTLE_END(0x1DB0, 0x10),
    PO_005_END(0x1DB0, 0x20),
    PO_007_END(0x1DB0, 0x40),
    PO_008_END(0x1DB0, 0x80),
    PO_PAGE_1(0x1DB1, 0x01),
    PO_101_END(0x1DB1, 0x02),
    PO_102_END(0x1DB1, 0x04),
    PO_103_END(0x1DB1, 0x08),
    PO_MS101_END_L(0x1DB1, 0x40), // Blustery Rescue end
    PO_105_END(0x1DB1, 0x80),
    PO_PAGE_2(0x1DB2, 0x01),
    PO_201_END(0x1DB2, 0x02),
    PO_202_END(0x1DB2, 0x04),
    PO_203_END(0x1DB2, 0x08),
    PO_MS201_END_L(0x1DB2, 0x40), // Hunny Slider end
    PO_205_END(0x1DB2, 0x80),
    PO_PAGE_3(0x1DB3, 0x01),
    PO_301_END(0x1DB3, 0x02),
    PO_302_END(0x1DB3, 0x04),
    PO_303_END(0x1DB3, 0x08),
    PO_MS301_END_L(0x1DB3, 0x40), // Balloon Bounce end
    PO_305_END(0x1DB3, 0x80),
    PO_PAGE_4(0x1DB4, 0x01),
    PO_401_END(0x1DB4, 0x02),
    PO_402_END(0x1DB4, 0x04),
    PO_MS401_END_L(0x1DB4, 0x20), // Spooky Cave end
    PO_404_END(0x1DB4, 0x40),
    PO_PAGE_5(0x1DB4, 0x80),
    PO_501_END(0x1DB5, 0x01),
    PO_502_END(0x1DB5, 0x02),
    PO_MS501_END_L(0x1DB5, 0x10), // Yeet the Bear end
    PO_504_END(0x1DB5, 0x20),
    PO_106_END(0x1DB6, 0x10),
    PO_206_END(0x1DB6, 0x20),
    PO_307_END(0x1DB6, 0x40),
    PO_405_END(0x1DB6, 0x80),
    PO_INIT(0x1DB7, 0x01),
    PO_SCENARIO_0_START(0x1DB7, 0x02),
    PO_SCENARIO_0_OPEN(0x1DB7, 0x04),
    PO_SCENARIO_0_END(0x1DB7, 0x08),
    PO_SCENARIO_1_START(0x1DB7, 0x10),
    PO_SCENARIO_1_OPEN(0x1DB7, 0x20),
    PO_SCENARIO_1_END(0x1DB7, 0x40),
    PO_SCENARIO_2_START(0x1DB7, 0x80),
    PO_SCENARIO_2_OPEN(0x1DB8, 0x01),
    PO_SCENARIO_3_END(0x1DB8, 0x02),
    PO_SCENARIO_3_START(0x1DB8, 0x04),
    PO_SCENARIO_3_OPEN(0x1DB8, 0x08),
    PO_SCENARIO_4_START(0x1DB8, 0x10),
    PO_SCENARIO_4_OPEN(0x1DB8, 0x20),
    PO_SCENARIO_4_END(0x1DB8, 0x40),
    PO_SCENARIO_5_START(0x1DB8, 0x80),
    PO_SCENARIO_5_OPEN(0x1DB9, 0x01),
    PO_SCENARIO_5_END(0x1DB9, 0x02),
    PO_SCENARIO_2_END(0x1DB9, 0x04),
    PO_402_END_L(0x1DBA, 0x01),
    PO_402_END_ON(0x1DBA, 0x02),
    PO_FREE_ON(0x1DBA, 0x04),
    PO_FREE_OFF(0x1DBA, 0x08);

    override val index: Int
      get() = ordinal

    override val flagName: String
      get() = name

  }

}
