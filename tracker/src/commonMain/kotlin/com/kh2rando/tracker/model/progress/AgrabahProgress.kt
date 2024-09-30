package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_ag_abu
import com.kh2rando.tracker.generated.resources.prog_ag_carpet
import com.kh2rando.tracker.generated.resources.prog_ag_chasm
import com.kh2rando.tracker.generated.resources.prog_ag_genie_jafar
import com.kh2rando.tracker.generated.resources.prog_ag_lexaeus
import com.kh2rando.tracker.generated.resources.prog_ag_lexaeus_data
import com.kh2rando.tracker.generated.resources.prog_ag_lords
import com.kh2rando.tracker.generated.resources.prog_ag_treasure_room
import com.kh2rando.tracker.generated.resources.prog_chests
import com.kh2rando.tracker.generated.resources.progression_ag_abu
import com.kh2rando.tracker.generated.resources.progression_ag_chasm_of_challenges
import com.kh2rando.tracker.generated.resources.progression_ag_elemental_lords
import com.kh2rando.tracker.generated.resources.progression_ag_jafar
import com.kh2rando.tracker.generated.resources.progression_ag_lexaeus
import com.kh2rando.tracker.generated.resources.progression_ag_lexaeusdata
import com.kh2rando.tracker.generated.resources.progression_ag_magic_switches
import com.kh2rando.tracker.generated.resources.progression_ag_treasure_room
import com.kh2rando.tracker.generated.resources.progression_chest
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class AgrabahProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
  override val associatedFlag: ProgressFlag,
) : ProgressCheckpoint, HasCustomizableIcon {

  Chests(
    displayString = Res.string.prog_chests,
    defaultIcon = Res.drawable.progression_chest,
    customIconIdentifier = "chest",
    associatedFlag = Flag.AL_SCENARIO_1_START,
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression")
  },

  Abu(
    displayString = Res.string.prog_ag_abu,
    defaultIcon = Res.drawable.progression_ag_abu,
    customIconIdentifier = "abu",
    associatedFlag = Flag.AL_GIMMICK_CLEAR,
  ),

  Chasm(
    displayString = Res.string.prog_ag_chasm,
    defaultIcon = Res.drawable.progression_ag_chasm_of_challenges,
    customIconIdentifier = "chasm_of_challenges",
    associatedFlag = Flag.AL_TRAP_CLEAR,
  ),

  TreasureRoom(
    displayString = Res.string.prog_ag_treasure_room,
    defaultIcon = Res.drawable.progression_ag_treasure_room,
    customIconIdentifier = "treasure_room",
    associatedFlag = Flag.AL_111_END_L,
  ),

  Lords(
    displayString = Res.string.prog_ag_lords,
    defaultIcon = Res.drawable.progression_ag_elemental_lords,
    customIconIdentifier = "elemental_lords",
    associatedFlag = Flag.AL_115_END_L,
  ),

  Carpet(
    displayString = Res.string.prog_ag_carpet,
    defaultIcon = Res.drawable.progression_ag_magic_switches,
    customIconIdentifier = "magic_switches",
    associatedFlag = Flag.AL_CARPET_06_CLEAR,
  ),

  GenieJafar(
    displayString = Res.string.prog_ag_genie_jafar,
    defaultIcon = Res.drawable.progression_ag_jafar,
    customIconIdentifier = "jafar",
    associatedFlag = Flag.AL_209_END_L,
  ),

  Lexaeus(
    displayString = Res.string.prog_ag_lexaeus,
    defaultIcon = Res.drawable.progression_ag_lexaeus,
    customIconIdentifier = "lexaeus",
    associatedFlag = HollowBastionProgress.Flag.HB_FM_COM_LEX_END,
  ),

  LexaeusData(
    displayString = Res.string.prog_ag_lexaeus_data,
    defaultIcon = Res.drawable.progression_ag_lexaeusdata,
    customIconIdentifier = "lexaeusdata",
    associatedFlag = HollowBastionProgress.Flag.HB_FM_LEX_RE_CLEAR,
  );

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.Agrabah

  override val customIconPath: List<String>
    get() = listOf("Progression", "agrabah")

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<AgrabahProgress, Int> {
      val inOrder = listOf(Chests, Abu, Chasm, TreasureRoom, Lords, Carpet, GenieJafar, Lexaeus, LexaeusData)
      return inOrder.zip(pointsList).toMap()
    }

  }

  enum class Flag(override val saveOffset: Int, override val mask: Int) : ProgressFlag {

    AL_START(0x1D70, 0x01),
    AL_CARPET_01_JUMP(0x1D70, 0x02),
    AL_102_END_L(0x1D70, 0x04),
    AL_CARPET_01_END(0x1D70, 0x08),
    AL_CARPET_02_END_L(0x1D70, 0x10), // First carpet fight end
    AL_CARPET_03_JUMP(0x1D70, 0x20),
    AL_106_END(0x1D70, 0x40),
    AL_CARPET_03_END(0x1D70, 0x80),
    AL_109_END(0x1D71, 0x02),
    AL_CARPET_04_JUMP(0x1D71, 0x04),
    AL_CARPET_04_END(0x1D71, 0x08),
    AL_110_END(0x1D71, 0x10),
    AL_CARPET_05_END_L(0x1D71, 0x20), // Second carpet fight end
    AL_115_END_L(0x1D72, 0x04), // Elemental Lords fight end
    AL_116_END(0x1D72, 0x08),
    AL_117_END(0x1D72, 0x10),
    AL_START2(0x1D72, 0x20),
    AL_201_END(0x1D72, 0x40),
    AL_INIT(0x1D73, 0x02),
    AL_SCENARIO_1_OPEN(0x1D73, 0x04),
    AL_SCENARIO_1_START(0x1D73, 0x08),
    AL_SCENARIO_1_END(0x1D73, 0x10),
    AL_SCENARIO_2_OPEN(0x1D73, 0x20),
    AL_SCENARIO_2_START(0x1D73, 0x40),
    AL_207_END(0x1D73, 0x80),
    AL_SCENARIO_2_END(0x1D74, 0x01),
    AL_ESCAPE_END_L(0x1D74, 0x02), // Carpet escape sequence end
    AL_111_END_L(0x1D74, 0x04), // Treasure room fight end
    AL_101_END(0x1D74, 0x08),
    AL_103_END(0x1D74, 0x10),
    AL_104_END(0x1D74, 0x20),
    AL_105_END(0x1D74, 0x40),
    AL_107_END(0x1D74, 0x80),
    AL_108_END(0x1D75, 0x01),
    AL_109_OUT(0x1D75, 0x02),
    AL_GIMMICK_PRE(0x1D75, 0x04),
    AL_GIMMICK_START(0x1D75, 0x08),
    AL_GIMMICK_CLEAR(0x1D75, 0x10), // Abu minigame end
    AL_TRAP_CLEAR(0x1D75, 0x20), // Chasm of Challenges end
    AL_TRAP_DOOR(0x1D75, 0x40),
    AL_112_END(0x1D75, 0x80),
    AL_113_END(0x1D76, 0x01),
    AL_114_END(0x1D76, 0x02),
    AL_202_END(0x1D76, 0x04),
    AL_203_END(0x1D76, 0x08),
    AL_204_END(0x1D76, 0x10),
    AL_205_END(0x1D76, 0x40),
    AL_206_END(0x1D77, 0x01),
    AL_212_END(0x1D77, 0x02),
    AL_208_END(0x1D77, 0x04),
    AL_209_END_L(0x1D77, 0x08), // Jafar fight end
    AL_210_END(0x1D77, 0x10),
    AL_152_END(0x1D78, 0x80),
    AL_CARPET_05_CLEAR(0x1D79, 0x04), // Cleared all the carpet switches
    AL_CARPET_06_CLEAR(0x1D79, 0x08), // Made it to the door after the switches
    AL_al14_ms202_free(0x1D79, 0x10),
    AL_al13_trap_free(0x1D79, 0x20),
    AL_212_OUT(0x1D79, 0x40),
    AL_CARPET_05_OUT(0x1D79, 0x80),
    AL_FM_COM_OBJ_OFF(0x1D7A, 0x01),
    AL_FM_KINOKO_LEX_PLAYED(0x1D7A, 0x02);

    override val index: Int
      get() = ordinal

    override val flagName: String
      get() = name

  }

}
