package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_chests
import com.kh2rando.tracker.generated.resources.prog_pl_data_saix
import com.kh2rando.tracker.generated.resources.prog_pl_ground_shaker
import com.kh2rando.tracker.generated.resources.prog_pl_hyenas_1
import com.kh2rando.tracker.generated.resources.prog_pl_hyenas_2
import com.kh2rando.tracker.generated.resources.prog_pl_scar
import com.kh2rando.tracker.generated.resources.prog_pl_simba
import com.kh2rando.tracker.generated.resources.progression_chest
import com.kh2rando.tracker.generated.resources.progression_pl_groundshaker
import com.kh2rando.tracker.generated.resources.progression_pl_hyenas
import com.kh2rando.tracker.generated.resources.progression_pl_oasis
import com.kh2rando.tracker.generated.resources.progression_pl_saix
import com.kh2rando.tracker.generated.resources.progression_pl_scar
import com.kh2rando.tracker.generated.resources.progression_pl_timon_and_pumba
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class PrideLandsProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
  override val associatedFlag: ProgressFlag,
) : ProgressCheckpoint, HasCustomizableIcon {

  Chests(
    displayString = Res.string.prog_chests,
    defaultIcon = Res.drawable.progression_chest,
    customIconIdentifier = "chest",
    associatedFlag = Flag.LK_SCENARIO_1_START,
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression")
  },

  Simba(
    displayString = Res.string.prog_pl_simba,
    defaultIcon = Res.drawable.progression_pl_oasis,
    customIconIdentifier = "oasis",
    associatedFlag = Flag.LK_117_END,
  ),

  Hyenas1(
    displayString = Res.string.prog_pl_hyenas_1,
    defaultIcon = Res.drawable.progression_pl_timon_and_pumba,
    customIconIdentifier = "timon_and_pumba",
    associatedFlag = Flag.LK_lk02_ms102,
  ),

  Scar(
    displayString = Res.string.prog_pl_scar,
    defaultIcon = Res.drawable.progression_pl_scar,
    customIconIdentifier = "scar",
    associatedFlag = Flag.LK_lk14_ms103,
  ),

  Hyenas2(
    displayString = Res.string.prog_pl_hyenas_2,
    defaultIcon = Res.drawable.progression_pl_hyenas,
    customIconIdentifier = "hyenas",
    associatedFlag = Flag.LK_lk05_ms201,
  ),

  GroundShaker(
    displayString = Res.string.prog_pl_ground_shaker,
    defaultIcon = Res.drawable.progression_pl_groundshaker,
    customIconIdentifier = "groundshaker",
    associatedFlag = Flag.LK_lk15_ms202,
  ),

  DataSaix(
    displayString = Res.string.prog_pl_data_saix,
    defaultIcon = Res.drawable.progression_pl_saix,
    customIconIdentifier = "saix",
    associatedFlag = WorldThatNeverWasProgress.Flag.EH_FM_SAI_RE_CLEAR,
  );

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.PrideLands

  override val customIconPath: List<String>
    get() = listOf("Progression", "pride_lands")

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<PrideLandsProgress, Int> {
      val inOrder = listOf(Chests, Simba, Hyenas1, Scar, Hyenas2, GroundShaker, DataSaix)
      return inOrder.zip(pointsList).toMap()
    }

  }

  enum class Flag(override val saveOffset: Int, override val mask: Int) : ProgressFlag {

    LK_START(0x1DD0, 0x01),
    LK_101_END(0x1DD0, 0x02),
    LK_102_END(0x1DD0, 0x04),
    LK_103_END(0x1DD0, 0x08),
    LK_104_END(0x1DD0, 0x10),
    LK_lk05_ms101(0x1DD0, 0x20),
    LK_106_END(0x1DD0, 0x40),
    LK_107_END(0x1DD0, 0x80),
    LK_108_END(0x1DD1, 0x01),
    LK_109_END(0x1DD1, 0x02),
    LK_110_END(0x1DD1, 0x04),
    LK_111_END(0x1DD1, 0x08),
    LK_112_END(0x1DD1, 0x10),
    LK_113_END(0x1DD1, 0x20),
    LK_114_END(0x1DD1, 0x40),
    LK_115_END(0x1DD1, 0x80),
    LK_116_END(0x1DD2, 0x01),
    LK_117_END(0x1DD2, 0x02),
    LK_118_END(0x1DD2, 0x04),
    LK_119_END(0x1DD2, 0x08),
    LK_lk02_ms102(0x1DD2, 0x10),
    LK_121_END(0x1DD2, 0x20),
    LK_lk14_ms103(0x1DD2, 0x40),
    LK_START2(0x1DD3, 0x01),
    LK_201_END(0x1DD3, 0x02),
    LK_202_END(0x1DD3, 0x04),
    LK_203_END(0x1DD3, 0x08),
    LK_204_END(0x1DD3, 0x10),
    LK_205_END(0x1DD3, 0x20),
    LK_206_END(0x1DD3, 0x40),
    LK_208_END(0x1DD4, 0x01),
    LK_209_END(0x1DD4, 0x02),
    LK_210_END(0x1DD4, 0x04),
    LK_211_END(0x1DD4, 0x08),
    LK_212_END(0x1DD4, 0x20),
    LK_123_END(0x1DD4, 0x80),
    LK_lk05_ms201(0x1DD5, 0x01),
    LK_INIT(0x1DD5, 0x04),
    LK_SCENARIO_1_OPEN(0x1DD5, 0x08),
    LK_SCENARIO_1_START(0x1DD5, 0x10),
    LK_SCENARIO_1_END(0x1DD5, 0x20),
    LK_SCENARIO_2_OPEN(0x1DD5, 0x40),
    LK_SCENARIO_2_START(0x1DD5, 0x80),
    LK_SCENARIO_2_END(0x1DD6, 0x01),
    LK_214_END(0x1DD6, 0x02),
    LK_lk06_ms100(0x1DD6, 0x04),
    LK_lk15_ms202(0x1DD6, 0x08);

    override val index: Int
      get() = ordinal

    override val flagName: String
      get() = name

  }

}
