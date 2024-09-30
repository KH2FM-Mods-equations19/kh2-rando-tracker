package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_chests
import com.kh2rando.tracker.generated.resources.prog_ht_candy_cane_lane
import com.kh2rando.tracker.generated.resources.prog_ht_children
import com.kh2rando.tracker.generated.resources.prog_ht_experiment
import com.kh2rando.tracker.generated.resources.prog_ht_oogie_boogie
import com.kh2rando.tracker.generated.resources.prog_ht_presents
import com.kh2rando.tracker.generated.resources.prog_ht_prison_keeper
import com.kh2rando.tracker.generated.resources.prog_ht_stolen_presents
import com.kh2rando.tracker.generated.resources.prog_ht_vexen
import com.kh2rando.tracker.generated.resources.prog_ht_vexen_data
import com.kh2rando.tracker.generated.resources.progression_chest
import com.kh2rando.tracker.generated.resources.progression_ht_candy_cane_lane
import com.kh2rando.tracker.generated.resources.progression_ht_children
import com.kh2rando.tracker.generated.resources.progression_ht_experiment
import com.kh2rando.tracker.generated.resources.progression_ht_oogie_boogie
import com.kh2rando.tracker.generated.resources.progression_ht_presents
import com.kh2rando.tracker.generated.resources.progression_ht_prison_keeper
import com.kh2rando.tracker.generated.resources.progression_ht_stolen_presents
import com.kh2rando.tracker.generated.resources.progression_ht_vexen
import com.kh2rando.tracker.generated.resources.progression_ht_vexen_data
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class HalloweenTownProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
  override val associatedFlag: ProgressFlag,
) : ProgressCheckpoint, HasCustomizableIcon {

  Chests(
    displayString = Res.string.prog_chests,
    defaultIcon = Res.drawable.progression_chest,
    customIconIdentifier = "chest",
    associatedFlag = Flag.NM_SCENARIO_1_START,
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression")
  },

  CandyCaneLane(
    displayString = Res.string.prog_ht_candy_cane_lane,
    defaultIcon = Res.drawable.progression_ht_candy_cane_lane,
    customIconIdentifier = "candy_cane_lane",
    associatedFlag = Flag.NM_nm06_ms102,
  ),

  PrisonKeeper(
    displayString = Res.string.prog_ht_prison_keeper,
    defaultIcon = Res.drawable.progression_ht_prison_keeper,
    customIconIdentifier = "prison_keeper",
    associatedFlag = Flag.NM_nm03_ms103,
  ),

  OogieBoogie(
    displayString = Res.string.prog_ht_oogie_boogie,
    defaultIcon = Res.drawable.progression_ht_oogie_boogie,
    customIconIdentifier = "oogie_boogie",
    associatedFlag = Flag.NM_nm09_ms104,
  ),

  Children(
    displayString = Res.string.prog_ht_children,
    defaultIcon = Res.drawable.progression_ht_children,
    customIconIdentifier = "children",
    associatedFlag = Flag.NM_nm10_ms201,
  ),

  StolenPresents(
    displayString = Res.string.prog_ht_stolen_presents,
    defaultIcon = Res.drawable.progression_ht_stolen_presents,
    customIconIdentifier = "retrieve_presents",
    associatedFlag = Flag.NM_nm00_ms202,
  ),

  PresentsMinigame(
    displayString = Res.string.prog_ht_presents,
    defaultIcon = Res.drawable.progression_ht_presents,
    customIconIdentifier = "presents",
    associatedFlag = Flag.NM_nm10_ms203,
  ),

  Experiment(
    displayString = Res.string.prog_ht_experiment,
    defaultIcon = Res.drawable.progression_ht_experiment,
    customIconIdentifier = "experiment",
    associatedFlag = Flag.NM_nm07_ms204,
  ),

  Vexen(
    displayString = Res.string.prog_ht_vexen,
    defaultIcon = Res.drawable.progression_ht_vexen,
    customIconIdentifier = "vexen",
    associatedFlag = HollowBastionProgress.Flag.HB_FM_COM_VEX_END,
  ),

  VexenData(
    displayString = Res.string.prog_ht_vexen_data,
    defaultIcon = Res.drawable.progression_ht_vexen_data,
    customIconIdentifier = "vexen_data",
    associatedFlag = HollowBastionProgress.Flag.HB_FM_VEX_RE_CLEAR,
  );

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<HalloweenTownProgress, Int> {
      val inOrder = listOf(
          Chests,
          CandyCaneLane,
          PrisonKeeper,
          OogieBoogie,
          Children,
          PresentsMinigame,
          Experiment,
          Vexen,
          VexenData
        )
      return inOrder.zip(pointsList).toMap()
    }

  }

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.HalloweenTown

  override val customIconPath: List<String>
    get() = listOf("Progression", "halloween_town")

  enum class Flag(override val saveOffset: Int, override val mask: Int) : ProgressFlag {

    NM_START(0x1E50, 0x01),
    NM_START2(0x1E50, 0x02),
    NM_102_END(0x1E50, 0x04),
    NM_103_END(0x1E50, 0x08),
    NM_104_END(0x1E50, 0x10),
    NM_105_END(0x1E50, 0x20),
    NM_nm00_ms101(0x1E50, 0x40),
    NM_107_END(0x1E50, 0x80),
    NM_108_END(0x1E51, 0x01),
    NM_109_END(0x1E51, 0x02),
    NM_110_END(0x1E51, 0x04),
    NM_111_END(0x1E51, 0x08),
    NM_201_END(0x1E51, 0x10),
    NM_113_END(0x1E51, 0x20),
    NM_114_END(0x1E51, 0x40),
    NM_115_END(0x1E51, 0x80),
    NM_NM_EVENT_116(0x1E52, 0x01),
    NM_117_END(0x1E52, 0x02),
    NM_118_END(0x1E52, 0x04),
    NM_nm03_ms103(0x1E52, 0x08), // Prison Keeper fight end
    NM_NM_EVENT_120(0x1E52, 0x10),
    NM_121_END(0x1E52, 0x20),
    NM_NM_EVENT_122(0x1E52, 0x40),
    NM_123_END(0x1E52, 0x80),
    NM_124_END(0x1E53, 0x01),
    NM_nm09_ms104(0x1E53, 0x02), // Oogie Boogie fight end
    NM_126_END(0x1E53, 0x04),
    NM_nm06_ms102(0x1E53, 0x08), // Candy Cane Lane fight end
    NM_101_END(0x1E53, 0x10),
    NM_108_OUT(0x1E53, 0x20),
    NM_117_OUT(0x1E54, 0x01),
    NM_202_END(0x1E54, 0x02),
    NM_121_OUT(0x1E54, 0x04),
    NM_203_END(0x1E54, 0x08),
    NM_204_END(0x1E54, 0x10),
    NM_205_END(0x1E54, 0x20),
    NM_nm10_ms201(0x1E54, 0x40), // LSB fight end
    NM_207_END(0x1E54, 0x80),
    NM_208_END(0x1E55, 0x01),
    NM_nm00_ms202(0x1E55, 0x02), // Town Square present fight end
    NM_210_END(0x1E55, 0x04),
    NM_211_END(0x1E55, 0x08),
    NM_nm10_ms203(0x1E55, 0x10), // Present wrapping minigame end
    NM_213_END(0x1E55, 0x20),
    NM_214_END(0x1E55, 0x40),
    NM_215_END(0x1E55, 0x80),
    NM_nm07_ms204(0x1E56, 0x01), // Experiment fight end
    NM_217_END(0x1E56, 0x02),
    NM_NM_EVENT_214(0x1E56, 0x04),
    NM_INIT(0x1E56, 0x08),
    NM_SCENARIO_1_END(0x1E56, 0x10),
    NM_SCENARIO_2_OPEN(0x1E56, 0x20),
    NM_SCENARIO_2_START(0x1E56, 0x40),
    NM_SCENARIO_2_END(0x1E56, 0x80),
    NM_SCENARIO_1_OPEN(0x1E57, 0x01),
    NM_SCENARIO_1_START(0x1E57, 0x02),
    NM_SANTA_START(0x1E57, 0x04),
    NM_112_OUT(0x1E57, 0x10),
    NM_NM_EVENT_122_OUT(0x1E57, 0x20),
    NM_NM_EVENT_122_IN(0x1E57, 0x40),
    NM_FM_KINOKO_XIG_PLAYED(0x1E57, 0x80);

    override val index: Int
      get() = ordinal

    override val flagName: String
      get() = name

  }

}
