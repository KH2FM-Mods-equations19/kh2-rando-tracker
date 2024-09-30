package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_chests
import com.kh2rando.tracker.generated.resources.prog_tt_betwixt_and_between
import com.kh2rando.tracker.generated.resources.prog_tt_data_axel
import com.kh2rando.tracker.generated.resources.prog_tt_mansion
import com.kh2rando.tracker.generated.resources.prog_tt_mysterious_tower
import com.kh2rando.tracker.generated.resources.prog_tt_sandlot
import com.kh2rando.tracker.generated.resources.prog_tt_station
import com.kh2rando.tracker.generated.resources.progression_chest
import com.kh2rando.tracker.generated.resources.progression_tt_axel
import com.kh2rando.tracker.generated.resources.progression_tt_berserkers
import com.kh2rando.tracker.generated.resources.progression_tt_betwixt_and_between
import com.kh2rando.tracker.generated.resources.progression_tt_creepers
import com.kh2rando.tracker.generated.resources.progression_tt_mickey
import com.kh2rando.tracker.generated.resources.progression_tt_mysterious_tower
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class TwilightTownProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
  override val associatedFlag: Flag,
) : ProgressCheckpoint, HasCustomizableIcon {

  Chests(
    displayString = Res.string.prog_chests,
    defaultIcon = Res.drawable.progression_chest,
    customIconIdentifier = "chest",
    associatedFlag = Flag.TT_SCENARIO_7_START,
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression")
  },

  Station(
    displayString = Res.string.prog_tt_station,
    defaultIcon = Res.drawable.progression_tt_creepers,
    customIconIdentifier = "creepers",
    associatedFlag = Flag.TT_707_END_L,
  ),

  MysteriousTower(
    displayString = Res.string.prog_tt_mysterious_tower,
    defaultIcon = Res.drawable.progression_tt_mysterious_tower,
    customIconIdentifier = "mysterious_tower",
    associatedFlag = Flag.TT_808_END,
  ),

  Sandlot(
    displayString = Res.string.prog_tt_sandlot,
    defaultIcon = Res.drawable.progression_tt_berserkers,
    customIconIdentifier = "berserkers",
    associatedFlag = Flag.TT_904_END_L,
  ),

  Mansion(
    displayString = Res.string.prog_tt_mansion,
    defaultIcon = Res.drawable.progression_tt_mickey,
    customIconIdentifier = "mickey",
    associatedFlag = Flag.TT_003_END_L,
  ),

  BetwixtAndBetween(
    displayString = Res.string.prog_tt_betwixt_and_between,
    defaultIcon = Res.drawable.progression_tt_betwixt_and_between,
    customIconIdentifier = "betwixt_and_between",
    associatedFlag = Flag.TT_012_END_L,
  ),

  DataAxel(
    displayString = Res.string.prog_tt_data_axel,
    defaultIcon = Res.drawable.progression_tt_axel,
    customIconIdentifier = "axel",
    associatedFlag = Flag.TT_FM_AXE_RE_CLEAR,
  );

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.TwilightTown

  override val customIconPath: List<String>
    get() = listOf("Progression", "twilight_town")

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<TwilightTownProgress, Int> {
      val inOrder = listOf(Chests, Station, MysteriousTower, Sandlot, Mansion, BetwixtAndBetween, DataAxel)
      return inOrder.zip(pointsList).toMap()
    }

  }

  enum class Flag(override val saveOffset: Int, override val mask: Int) : ProgressFlag {

    TT_START_1(0x1CD0, 0x01),
    TT_INIT(0x1CD2, 0x10),
    TT_SCENARIO_1_OPEN(0x1CD2, 0x20),
    TT_MOVIE_101_END_L(0x1CD2, 0x40),
    TT_SCENARIO_1_START(0x1CD2, 0x80),
    TT_101_END_L(0x1CD3, 0x01),
    TT_102_END_L(0x1CD3, 0x02),
    TT_103_END_L(0x1CD3, 0x04), // All the STT opening cutscenes done
    TT_104_END_L(0x1CD3, 0x08),
    TT_105_END_L(0x1CD3, 0x10),
    TT_106_END_L(0x1CD3, 0x20),
    TT_107_END_L(0x1CD3, 0x40),
    TT_108_END_L(0x1CD3, 0x80),
    TT_109_END_L(0x1CD4, 0x01),
    TT_110_END_L(0x1CD4, 0x02),
    TT_111_END_L(0x1CD4, 0x04),
    TT_112_END_L(0x1CD4, 0x08), // Seifer tutorial battles end
    TT_113_END_L(0x1CD4, 0x10),
    TT_114_END(0x1CD4, 0x20),
    TT_115_END_L(0x1CD4, 0x40),
    TT_116_END_L(0x1CD4, 0x80), // Dusk tutorial battles end
    TT_117_END_L(0x1CD5, 0x01),
    TT_118_END_L(0x1CD5, 0x02),
    TT_119_END_L(0x1CD5, 0x04),
    TT_SCENARIO_1_END(0x1CD5, 0x08),
    TT_MOVIE_201_END_L(0x1CD5, 0x10),
    TT_SCENARIO_2_OPEN(0x1CD5, 0x20),
    TT_201_END_L(0x1CD5, 0x40),
    TT_SCENARIO_2_START(0x1CD5, 0x80),
    TT_202_END_L(0x1CD6, 0x01),
    TT_203_END_L(0x1CD6, 0x02),
    TT_204_END_L(0x1CD6, 0x04),
    TT_205_END(0x1CD6, 0x08),
    TT_206_END(0x1CD6, 0x10),
    TT_207_END_L(0x1CD6, 0x20), // Done with money collecting
    TT_208_END(0x1CD6, 0x40),
    TT_209_END(0x1CD6, 0x80),
    TT_210_END_L(0x1CD7, 0x01),
    TT_211_END_L(0x1CD7, 0x02),
    TT_SCENARIO_2_END(0x1CD7, 0x04),
    TT_MOVIE_301_END_L(0x1CD7, 0x08),
    TT_SCENARIO_3_OPEN(0x1CD7, 0x10),
    TT_301_END_L(0x1CD7, 0x20),
    TT_SCENARIO_3_START(0x1CD7, 0x40),
    TT_302_END_L(0x1CD7, 0x80),
    TT_303_END_L(0x1CD8, 0x01),
    TT_304_END_L(0x1CD8, 0x02),
    TT_305_END(0x1CD8, 0x04),
    TT_306_END(0x1CD8, 0x08),
    TT_307_END(0x1CD8, 0x10),
    TT_308_END_L(0x1CD8, 0x20),
    TT_309_END_L(0x1CD8, 0x40),
    TT_310_END_L(0x1CD8, 0x80),
    TT_311_END(0x1CD9, 0x01),
    TT_312_END_L(0x1CD9, 0x02),
    TT_313_END_L(0x1CD9, 0x04),
    TT_314_END(0x1CD9, 0x08),
    TT_315_END_L(0x1CD9, 0x10), // Twilight Thorn fight end
    TT_316_END_L(0x1CD9, 0x20),
    TT_317_END_L(0x1CD9, 0x40),
    TT_318_END_L(0x1CD9, 0x80),
    TT_319_END_L(0x1CDA, 0x01),
    TT_SCENARIO_3_END(0x1CDA, 0x02),
    TT_MOVIE_401_END_L(0x1CDA, 0x04),
    TT_SCENARIO_4_OPEN(0x1CDA, 0x08),
    TT_401_END_L(0x1CDA, 0x10),
    TT_SCENARIO_4_START(0x1CDA, 0x20),
    TT_402_END(0x1CDA, 0x40),
    TT_403_END(0x1CDA, 0x80),
    TT_404_END(0x1CDB, 0x01),
    TT_405_END(0x1CDB, 0x02),
    TT_406_LOSE_L(0x1CDB, 0x04),
    TT_406_END_L(0x1CDB, 0x08), // Hayner struggle fight end (win?)
    TT_407_END(0x1CDB, 0x10),
    TT_408_LOSE_L(0x1CDB, 0x20),
    TT_408_END_L(0x1CDB, 0x40), // Vivi struggle fight end (win?)
    TT_409_END_L(0x1CDB, 0x80), // Dusks in struggle arena fight end
    TT_410_END_L(0x1CDC, 0x01), // Axel in struggle arena fight end
    TT_411_END(0x1CDC, 0x02),
    TT_412_END_L(0x1CDC, 0x04), // Setzer struggle fight end (win?)
    TT_413_END_L(0x1CDC, 0x08),
    TT_414_END_L(0x1CDC, 0x10),
    TT_415_END_L(0x1CDC, 0x20),
    TT_SCENARIO_4_END(0x1CDC, 0x40), // Done with all struggle stuff
    TT_501_END_L(0x1CDC, 0x80),
    TT_SCENARIO_5_OPEN(0x1CDD, 0x01),
    TT_502_END_L(0x1CDD, 0x02),
    TT_SCENARIO_5_START(0x1CDD, 0x04),
    TT_503_END(0x1CDD, 0x08),
    TT_504_END_L(0x1CDD, 0x10),
    TT_505_END_L(0x1CDD, 0x20),
    TT_MISTERY_A_END(0x1CDD, 0x40),
    TT_MISTERY_B_END(0x1CDD, 0x80),
    TT_MISTERY_C_END(0x1CDE, 0x01),
    TT_MISTERY_D_END(0x1CDE, 0x02),
    TT_506_END_L(0x1CDE, 0x04),
    TT_507_END(0x1CDE, 0x08),
    TT_508_END_L(0x1CDE, 0x10),
    TT_509_END_L(0x1CDE, 0x20),
    TT_510_END(0x1CDE, 0x40),
    TT_511_END_L(0x1CDE, 0x80),
    TT_512_END_L(0x1CDF, 0x01),
    TT_513_END_L(0x1CDF, 0x02),
    TT_514_END(0x1CDF, 0x04),
    TT_515_END_L(0x1CDF, 0x08),
    TT_SCENARIO_5_END(0x1CDF, 0x10),
    TT_MOVIE_601_END_L(0x1CDF, 0x20),
    TT_SCENARIO_6_OPEN(0x1CDF, 0x40),
    TT_601_END_L(0x1CDF, 0x80),
    TT_602_END_L(0x1CE0, 0x01),
    TT_603_END(0x1CE0, 0x02),
    TT_604_END_L(0x1CE0, 0x04),
    TT_605_END(0x1CE0, 0x08),
    TT_606_END(0x1CE0, 0x10),
    TT_607_END(0x1CE0, 0x20),
    TT_608_END_L(0x1CE0, 0x40), // White room item popups
    TT_609_END(0x1CE0, 0x80),
    TT_610_END(0x1CE1, 0x01),
    TT_611_END_L(0x1CE1, 0x02), // Computer room
    TT_612_END(0x1CE1, 0x04),
    TT_613_END_L(0x1CE1, 0x08), // Pre-Axel nobodies
    TT_614_END_L(0x1CE1, 0x10), // Axel fight end
    TT_615_END_L(0x1CE1, 0x20),
    TT_615_OUT(0x1CE1, 0x40),
    TT_616_END(0x1CE1, 0x80),
    TT_617_END(0x1CE2, 0x01),
    TT_618_END(0x1CE2, 0x02),
    TT_SCENARIO_6_END(0x1CE2, 0x04),
    TT_701_END_L(0x1CE2, 0x08),
    TT_SCENARIO_7_OPEN(0x1CE2, 0x10),
    TT_702_END_L(0x1CE2, 0x20),
    TT_SCENARIO_7_START(0x1CE2, 0x40),
    TT_704_END(0x1CE3, 0x01),
    TT_705_END(0x1CE3, 0x02),
    TT_706_END(0x1CE3, 0x04),
    TT_707_END_L(0x1CE3, 0x08),
    TT_708_END_L(0x1CE3, 0x10),
    TT_709_END(0x1CE3, 0x20),
    TT_710_END(0x1CE3, 0x40),
    TT_711_END_L(0x1CE3, 0x80),
    TT_SCENARIO_7_END(0x1CE4, 0x01),
    TT_801_END_L(0x1CE4, 0x02),
    TT_SCENARIO_8_OPEN(0x1CE4, 0x04),
    TT_802_END(0x1CE4, 0x08),
    TT_803_END_L(0x1CE4, 0x10),
    TT_MS802_CLEAR_L(0x1CE4, 0x20),
    TT_804_END_L(0x1CE4, 0x40),
    TT_805_END(0x1CE4, 0x80),
    TT_806_END(0x1CE5, 0x01),
    TT_807_END(0x1CE5, 0x02),
    TT_808_END(0x1CE5, 0x04),
    TT_809_END(0x1CE5, 0x08),
    TT_810_END_L(0x1CE5, 0x10),
    TT_SCENARIO_8_END(0x1CE5, 0x20),
    TT_901_END_L(0x1CE5, 0x40),
    TT_SCENARIO_9_OPEN(0x1CE5, 0x80),
    TT_902_END_L(0x1CE6, 0x01),
    TT_SCENARIO_9_START(0x1CE6, 0x02),
    TT_903_END(0x1CE6, 0x08),
    TT_904_END_L(0x1CE6, 0x10),
    TT_905_END(0x1CE6, 0x40),
    TT_906_END_L(0x1CE6, 0x80),
    TT_907_END_L(0x1CE7, 0x01),
    TT_SCENARIO_9_END(0x1CE7, 0x02),
    TT_001_END_L(0x1CE7, 0x04),
    TT_SCENARIO_10_OPEN(0x1CE7, 0x08),
    TT_002_END(0x1CE7, 0x10),
    TT_003_END_L(0x1CE7, 0x20),
    TT_004_END(0x1CE7, 0x80),
    TT_005_END(0x1CE8, 0x01),
    TT_006_END(0x1CE8, 0x02),
    TT_007_END(0x1CE8, 0x04),
    TT_008_END_L(0x1CE8, 0x08),
    TT_009_END(0x1CE8, 0x40),
    TT_010_END(0x1CE8, 0x80),
    TT_011_END_L(0x1CE9, 0x01),
    TT_012_END_L(0x1CE9, 0x02),
    TT_013_END(0x1CE9, 0x04),
    TT_SCENARIO_10_END(0x1CE9, 0x08),
    TT_tt20_ms603_end_l(0x1CE9, 0x10),
    TT_908_END_L(0x1CE9, 0x20),
    TT_ROXAS2_START(0x1CE9, 0x40),
    TT_ROXAS2_END(0x1CE9, 0x80),
    TT_ROXAS_END(0x1CEA, 0x01),
    TT_SORA_OLD_END(0x1CEA, 0x02),
    TT_tt06_work_letter_END(0x1CEA, 0x04),
    TT_tt06_work_baggage_END(0x1CEA, 0x08),
    TT_FM_KINOKO_LAR_PLAYED(0x1CEA, 0x10),
    TT_tt07_work_poster_END(0x1CEA, 0x20),
    TT_tt07_work_cook_END(0x1CEA, 0x40),
    TT_FM_KINOKO_AXE_PLAYED(0x1CEA, 0x80),
    TT_tt07_work_clean_END(0x1CEB, 0x01),
    TT_tt07_work_worm_END(0x1CEB, 0x02),
    TT_tt06_work_perform_END(0x1CEB, 0x04),
    TT_ROXAS_START(0x1CEB, 0x08),
    TT_FM_AXE_RE_CLEAR(0x1CEB, 0x10),
    TT_206_END_work01(0x1CEB, 0x20),
    TT_FM_091_END(0x1CEB, 0x40),
    TT_206_END_work03(0x1CEB, 0x80),
    TT_206_END_work02_01(0x1CEC, 0x01),
    TT_206_END_work02_02(0x1CEC, 0x02),
    TT_206_END_work02_03(0x1CEC, 0x04),
    TT_MIX_SHOP_OPEN(0x1CEC, 0x08),
    TT_MISTERY_A_LOSE(0x1CEC, 0x10),
    TT_MISTERY_B_LOSE(0x1CEC, 0x20),
    TT_MISTERY_C_LOSE(0x1CEC, 0x40),
    TT_MISTERY_D_LOSE(0x1CEC, 0x80),
    TT_STRUGGLE_ON2(0x1CED, 0x02),
    TT_STRUGGLE_ON3(0x1CED, 0x04),
    TT_702_OUT(0x1CED, 0x08),
    TT_START_01(0x1CED, 0x10),
    TT_START_02(0x1CED, 0x20),
    TT_SCENARIO_10_START(0x1CED, 0x40),
    TT_PLAY_STRUGGLE_01(0x1CED, 0x80),
    TT_PLAY_STRUGGLE_02(0x1CEE, 0x01),
    TT_PLAY_STRUGGLE_03(0x1CEE, 0x02),
    TT_TT21_REAL(0x1CEE, 0x04),
    TT_TT21_FAKE(0x1CEE, 0x08),
    TT_FM_COM_OBJ_OFF(0x1CEE, 0x10),
    TT_FM_KINOKO_SAI_PLAYED(0x1CEE, 0x20),
    TT_STRUGGLE_ON(0x1CEE, 0x40),
    TT_BATTLE2_OPEN(0x1CEE, 0x80),
    TT_113_IN(0x1CEF, 0x01),
    TT_TT29_OUT(0x1CEF, 0x02),
    TT_TT30_OUT(0x1CEF, 0x04),
    TT_REAL_TT_EVENT(0x1CEF, 0x08),
    TT_REAL_TT_FREE(0x1CEF, 0x10),
    TT_FAKE_TT_EVENT(0x1CEF, 0x20),
    TT_FAKE_TT_FREE(0x1CEF, 0x40),
    TT_MISTERY_C_OUT(0x1CEF, 0x80);

    override val index: Int
      get() = ordinal

    override val flagName: String
      get() = name

  }

}
