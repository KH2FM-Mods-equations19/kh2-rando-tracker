package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_chests
import com.kh2rando.tracker.generated.resources.prog_twtnw_data_xemnas
import com.kh2rando.tracker.generated.resources.prog_twtnw_luxord
import com.kh2rando.tracker.generated.resources.prog_twtnw_roxas
import com.kh2rando.tracker.generated.resources.prog_twtnw_saix
import com.kh2rando.tracker.generated.resources.prog_twtnw_xemnas_1
import com.kh2rando.tracker.generated.resources.prog_twtnw_xigbar
import com.kh2rando.tracker.generated.resources.progression_chest
import com.kh2rando.tracker.generated.resources.progression_twtnw_luxord
import com.kh2rando.tracker.generated.resources.progression_twtnw_roxas
import com.kh2rando.tracker.generated.resources.progression_twtnw_saix
import com.kh2rando.tracker.generated.resources.progression_twtnw_xemnas
import com.kh2rando.tracker.generated.resources.progression_twtnw_xemnas_data
import com.kh2rando.tracker.generated.resources.progression_twtnw_xigbar
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class WorldThatNeverWasProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
  override val associatedFlag: Flag,
) : ProgressCheckpoint, HasCustomizableIcon {

  Chests(
    displayString = Res.string.prog_chests,
    defaultIcon = Res.drawable.progression_chest,
    customIconIdentifier = "chest",
    associatedFlag = Flag.EH_SCENARIO_1_START,
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression")
  },

  Roxas(
    displayString = Res.string.prog_twtnw_roxas,
    defaultIcon = Res.drawable.progression_twtnw_roxas,
    customIconIdentifier = "roxas",
    associatedFlag = Flag.EH_eh_event_103,
  ),

  Xigbar(
    displayString = Res.string.prog_twtnw_xigbar,
    defaultIcon = Res.drawable.progression_twtnw_xigbar,
    customIconIdentifier = "xigbar",
    associatedFlag = Flag.EH_eh10_ms102,
  ),

  Luxord(
    displayString = Res.string.prog_twtnw_luxord,
    defaultIcon = Res.drawable.progression_twtnw_luxord,
    customIconIdentifier = "luxord",
    associatedFlag = Flag.EH_eh14_ms103,
  ),

  Saix(
    displayString = Res.string.prog_twtnw_saix,
    defaultIcon = Res.drawable.progression_twtnw_saix,
    customIconIdentifier = "saix",
    associatedFlag = Flag.EH_eh15_ms104,
  ),

  Xemnas1(
    displayString = Res.string.prog_twtnw_xemnas_1,
    defaultIcon = Res.drawable.progression_twtnw_xemnas,
    customIconIdentifier = "xemnas",
    associatedFlag = Flag.EH_eh19_ms105,
  ),

  DataXemnas(
    displayString = Res.string.prog_twtnw_data_xemnas,
    defaultIcon = Res.drawable.progression_twtnw_xemnas_data,
    customIconIdentifier = "xemnas_data",
    associatedFlag = Flag.EH_FM_XEM_RE_CLEAR,
  );

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.WorldThatNeverWas

  override val customIconPath: List<String>
    get() = listOf("Progression", "the_world_that_never_was")

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<WorldThatNeverWasProgress, Int> {
      val inOrder = listOf(Chests, Roxas, Xigbar, Luxord, Saix, Xemnas1, DataXemnas)
      return inOrder.zip(pointsList).toMap()
    }

  }

  enum class Flag(override val saveOffset: Int, override val mask: Int) : ProgressFlag {

    EH_START(0x1ED0, 0x01),
    EH_INIT(0x1ED0, 0x10),
    EH_eh_event_101(0x1ED0, 0x20),
    EH_eh_event_102(0x1ED0, 0x40),
    EH_eh_event_103(0x1ED0, 0x80),
    EH_eh_event_105(0x1ED1, 0x02),
    EH_eh_event_106(0x1ED1, 0x04),
    EH_eh_event_107(0x1ED1, 0x08),
    EH_eh_event_108(0x1ED1, 0x10),
    EH_eh_event_109(0x1ED1, 0x20),
    EH_eh_event_110(0x1ED1, 0x40),
    EH_eh_event_111(0x1ED1, 0x80),
    EH_eh_event_112(0x1ED2, 0x01),
    EH_eh_event_113(0x1ED2, 0x02),
    EH_eh10_ms102(0x1ED2, 0x04),
    EH_eh_event_115(0x1ED2, 0x08),
    EH_eh_event_116(0x1ED2, 0x10),
    EH_eh_event_117(0x1ED2, 0x20),
    EH_eh_event_118(0x1ED2, 0x40),
    EH_eh14_ms103(0x1ED2, 0x80),
    EH_eh_event_120(0x1ED3, 0x01),
    EH_eh_event_121(0x1ED3, 0x02),
    EH_eh15_ms104(0x1ED3, 0x04),
    EH_eh_event_123(0x1ED3, 0x08),
    EH_eh_event_124(0x1ED3, 0x10),
    EH_eh_event_125(0x1ED3, 0x20),
    EH_eh_event_126(0x1ED3, 0x40),
    EH_eh_event_127(0x1ED3, 0x80),
    EH_eh_event_128(0x1ED4, 0x01),
    EH_eh_event_129(0x1ED4, 0x02),
    EH_eh_event_130(0x1ED4, 0x04),
    EH_eh_event_131(0x1ED4, 0x08),
    EH_eh19_ms105(0x1ED4, 0x10),
    EH_eh_event_133(0x1ED4, 0x20),
    EH_eh_event_134(0x1ED4, 0x40),
    EH_eh_event_135(0x1ED4, 0x80),
    EH_eh_event_136(0x1ED5, 0x02),
    EH_eh_event_201(0x1ED5, 0x08),
    EH_eh_event_202(0x1ED5, 0x10),
    EH_eh_event_203(0x1ED5, 0x20),
    EH_eh_event_204(0x1ED5, 0x40),
    EH_SCENARIO_1_OPEN(0x1ED5, 0x80),
    EH_SCENARIO_1_START(0x1ED6, 0x01),
    EH_start_ev301(0x1ED6, 0x02),
    EH_eh01_ev301(0x1ED6, 0x04),
    EH_JIMMNY_FULL_OPEN(0x1ED6, 0x80),
    EH_eh27_ms106(0x1ED7, 0x04),
    EH_eh28_ms107(0x1ED7, 0x08),
    EH_eh26_ms108(0x1ED7, 0x10),
    EH_eh25_ms109(0x1ED7, 0x20),
    EH_eh24_ms110(0x1ED7, 0x40),
    EH_eh22_ms111(0x1ED7, 0x80),
    EH_eh23_ms112(0x1ED8, 0x01),
    EH_eh20_ms113(0x1ED8, 0x02),
    EH_FINAL_CHANCE_START(0x1ED8, 0x04),
    EH_NO_RIKU_START(0x1ED8, 0x08),
    EH_NO_RIKU_END(0x1ED8, 0x10),
    EH_RIKU_LAST_START(0x1ED8, 0x20),
    EH_EH_OUT(0x1ED8, 0x40),
    EH_EH_IN(0x1ED8, 0x80),
    EH_GAME_COMPLETE(0x1ED9, 0x01),
    EH_EH_LASTBOSS_RESET(0x1ED9, 0x02),
    EH_NO_RIKU_START_RE(0x1ED9, 0x04),
    EH_NO_RIKU_END_RE(0x1ED9, 0x08),
    EH_RIKU_LAST_RE(0x1ED9, 0x10),
    EH_RIKU_LAST_END_RE(0x1ED9, 0x20),
    EH_FM_ROX_RE_CLEAR(0x1ED9, 0x40),
    EH_FM_XIG_RE_CLEAR(0x1ED9, 0x80),
    EH_FM_SAI_RE_CLEAR(0x1EDA, 0x01),
    EH_FM_LUX_RE_CLEAR(0x1EDA, 0x02),
    EH_FM_XEM_RE_CLEAR(0x1EDA, 0x04),
    EH_FM_KINOKO_XEM_PLAYED(0x1EDA, 0x08);

    override val index: Int
      get() = ordinal

    override val flagName: String
      get() = name

  }

}
