package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_chests
import com.kh2rando.tracker.generated.resources.prog_hb_1000_heartless
import com.kh2rando.tracker.generated.resources.prog_hb_ansem_study
import com.kh2rando.tracker.generated.resources.prog_hb_bailey
import com.kh2rando.tracker.generated.resources.prog_hb_corridor
import com.kh2rando.tracker.generated.resources.prog_hb_dancers
import com.kh2rando.tracker.generated.resources.prog_hb_data_demyx
import com.kh2rando.tracker.generated.resources.prog_hb_final_fantasy
import com.kh2rando.tracker.generated.resources.prog_hb_hb_demyx
import com.kh2rando.tracker.generated.resources.prog_hb_sephiroth
import com.kh2rando.tracker.generated.resources.progression_chest
import com.kh2rando.tracker.generated.resources.progression_hb_1000_heartless
import com.kh2rando.tracker.generated.resources.progression_hb_ansems_study
import com.kh2rando.tracker.generated.resources.progression_hb_bailey
import com.kh2rando.tracker.generated.resources.progression_hb_corridors
import com.kh2rando.tracker.generated.resources.progression_hb_dancers
import com.kh2rando.tracker.generated.resources.progression_hb_demyx
import com.kh2rando.tracker.generated.resources.progression_hb_demyx_story
import com.kh2rando.tracker.generated.resources.progression_hb_final_fantasy
import com.kh2rando.tracker.generated.resources.progression_hb_sephiroth
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class HollowBastionProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
  override val associatedFlag: Flag,
) : ProgressCheckpoint, HasCustomizableIcon {

  Chests(
    displayString = Res.string.prog_chests,
    defaultIcon = Res.drawable.progression_chest,
    customIconIdentifier = "chest",
    associatedFlag = Flag.HB_SCENARIO_1_START,
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression")
  },

  Bailey(
    displayString = Res.string.prog_hb_bailey,
    defaultIcon = Res.drawable.progression_hb_bailey,
    customIconIdentifier = "bailey",
    associatedFlag = Flag.HB_hb08_ms102, // HB_hb08_ms102 or HB_START1_2 or HB_SCENARIO_1_END
  ),

  AnsemStudy(
    displayString = Res.string.prog_hb_ansem_study,
    defaultIcon = Res.drawable.progression_hb_ansems_study,
    customIconIdentifier = "ansems_study",
    associatedFlag = Flag.HB_421_END, // HB_421_END or HB_422_END
  ),

  Corridor(
    displayString = Res.string.prog_hb_corridor,
    defaultIcon = Res.drawable.progression_hb_corridors,
    customIconIdentifier = "corridors",
    associatedFlag = Flag.HB_hb20_ms401,
  ),

  Dancers(
    displayString = Res.string.prog_hb_dancers,
    defaultIcon = Res.drawable.progression_hb_dancers,
    customIconIdentifier = "dancers",
    associatedFlag = Flag.HB_hb18_ms402,
  ),

  HBDemyx(
    displayString = Res.string.prog_hb_hb_demyx,
    defaultIcon = Res.drawable.progression_hb_demyx_story,
    customIconIdentifier = "demyx_story",
    associatedFlag = Flag.HB_hb04_ms403,
  ),

  FinalFantasy(
    displayString = Res.string.prog_hb_final_fantasy,
    defaultIcon = Res.drawable.progression_hb_final_fantasy,
    customIconIdentifier = "final_fantasy",
    associatedFlag = Flag.HB_hb16_ms404d,
  ),

  ThousandHeartless(
    displayString = Res.string.prog_hb_1000_heartless,
    defaultIcon = Res.drawable.progression_hb_1000_heartless,
    customIconIdentifier = "1000_heartless",
    associatedFlag = Flag.HB_hb17_ms405,
  ),

  Sephiroth(
    displayString = Res.string.prog_hb_sephiroth,
    defaultIcon = Res.drawable.progression_hb_sephiroth,
    customIconIdentifier = "sephiroth",
    associatedFlag = Flag.HB_hb01_ms601,
  ),

  DataDemyx(
    displayString = Res.string.prog_hb_data_demyx,
    defaultIcon = Res.drawable.progression_hb_demyx,
    customIconIdentifier = "demyx",
    associatedFlag = Flag.HB_FM_DEM_RE_CLEAR,
  );

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.HollowBastion

  override val customIconPath: List<String>
    get() = listOf("Progression", "hollow_bastion")

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<HollowBastionProgress, Int> {
      val inOrder = listOf(
        Chests,
        Bailey,
        AnsemStudy,
        Corridor,
        Dancers,
        HBDemyx,
        FinalFantasy,
        ThousandHeartless,
        Sephiroth,
        DataDemyx
      )
      return inOrder.zip(pointsList).toMap()
    }

  }

  enum class Flag(override val saveOffset: Int, override val mask: Int) : ProgressFlag {

    HB_START(0x1D10, 0x01),
    HB_101_END(0x1D10, 0x02),
    HB_102_END(0x1D10, 0x04),
    HB_103_END(0x1D10, 0x08),
    HB_104_END(0x1D10, 0x10),
    HB_106_END(0x1D10, 0x40),
    HB_107_END(0x1D10, 0x80),
    HB_START1_2(0x1D11, 0x02),
    HB_201_END(0x1D11, 0x04),
    HB_202_END(0x1D11, 0x08),
    HB_203_END(0x1D11, 0x10),
    HB_204_END(0x1D11, 0x20),
    HB_205_END(0x1D11, 0x40),
    HB_206_END(0x1D11, 0x80),
    HB_207_END(0x1D12, 0x01),
    HB_208_END(0x1D12, 0x02),
    HB_209_END(0x1D12, 0x04),
    HB_tr_107_END(0x1D12, 0x08),
    HB_301_END(0x1D12, 0x10),
    HB_302_END(0x1D12, 0x20),
    HB_303_END(0x1D12, 0x40),
    HB_304_END(0x1D12, 0x80),
    HB_tr_117_END(0x1D13, 0x01),
    HB_401_END(0x1D13, 0x02),
    HB_402_END(0x1D13, 0x04),
    HB_404_END(0x1D13, 0x10),
    HB_406_END(0x1D13, 0x40),
    HB_407_END(0x1D13, 0x80),
    HB_409_END(0x1D14, 0x02),
    HB_410_END(0x1D14, 0x04),
    HB_411_END(0x1D14, 0x08),
    HB_413_END(0x1D14, 0x40),
    HB_414_END(0x1D14, 0x80),
    HB_415_END(0x1D15, 0x01),
    HB_416_END(0x1D15, 0x02),
    HB_418_END(0x1D15, 0x08),
    HB_START2(0x1D15, 0x10),
    HB_START_wi_dc(0x1D15, 0x20),
    HB_801_END(0x1D15, 0x40),
    HB_dc_108_END(0x1D15, 0x80),
    HB_802_END(0x1D16, 0x01),
    HB_START_pooh(0x1D16, 0x02),
    HB_901_END(0x1D16, 0x04),
    HB_902_END(0x1D16, 0x08),
    HB_po_004_END(0x1D16, 0x10),
    HB_903_END(0x1D16, 0x20),
    HB_904_END(0x1D16, 0x40),
    HB_905_END(0x1D17, 0x01),
    HB_po_008_END(0x1D17, 0x02),
    HB_907_END(0x1D17, 0x08),
    HB_hb09_ms101(0x1D17, 0x10),
    HB_hb08_ms102(0x1D17, 0x20),
    HB_hb04_ms403(0x1D18, 0x01), // Or is this Demyx?
    HB_hb09_ms901(0x1D18, 0x08), // Demyx story fight end?
    HB_210_END(0x1D18, 0x20),
    HB_HB_EVENT_403(0x1D18, 0x40),
    HB_hb16_ms404a(0x1D18, 0x80), // FF fights A
    HB_hb16_ms404b(0x1D19, 0x01), // FF fights B
    HB_hb16_ms404c(0x1D19, 0x02), // FF fights C
    HB_hb16_ms404d(0x1D19, 0x04), // FF fights D
    HB_417_END(0x1D19, 0x08),
    HB_508_END(0x1D19, 0x10),
    HB_TR_202_END(0x1D19, 0x20),
    HB_501_END(0x1D19, 0x40),
    HB_TR_tr04_ms202(0x1D1A, 0x02),
    HB_503_END(0x1D1A, 0x04),
    HB_TR_tr09_ms205(0x1D1A, 0x08),
    HB_504_END(0x1D1A, 0x10),
    HB_505_END(0x1D1A, 0x20),
    HB_506_END(0x1D1A, 0x40),
    HB_hb17_ms405(0x1D1B, 0x04), // Thousand heartless end
    HB_INIT(0x1D1B, 0x08),
    HB_SCENARIO_1_OPEN(0x1D1B, 0x10),
    HB_SCENARIO_1_START(0x1D1B, 0x20),
    HB_SCENARIO_1_END(0x1D1B, 0x40),
    HB_SCENARIO_2_OPEN(0x1D1B, 0x80),
    HB_SCENARIO_2_START(0x1D1C, 0x01),
    HB_SCENARIO_2_END(0x1D1C, 0x02),
    HB_SCENARIO_5_OPEN(0x1D1C, 0x04),
    HB_SCENARIO_5_START(0x1D1C, 0x08),
    HB_509_END(0x1D1C, 0x10),
    HB_hb09_ms501(0x1D1C, 0x20),
    HB_511_END(0x1D1C, 0x40),
    HB_513_END(0x1D1D, 0x01),
    HB_514_END(0x1D1D, 0x02),
    HB_516_END(0x1D1D, 0x08),
    HB_517_END(0x1D1D, 0x10),
    HB_518_END(0x1D1D, 0x20),
    HB_519_END(0x1D1D, 0x40),
    HB_520_END(0x1D1D, 0x80),
    HB_dc_END(0x1D1E, 0x01),
    HB_SCENARIO_3_OPEN(0x1D1E, 0x02),
    HB_SCENARIO_3_START(0x1D1E, 0x04),
    HB_SCENARIO_3_END(0x1D1E, 0x08),
    HB_SCENARIO_4_OPEN(0x1D1E, 0x10),
    HB_SCENARIO_4_START(0x1D1E, 0x20),
    HB_SCENARIO_4_END(0x1D1E, 0x40),
    HB_SCENARIO_5_END(0x1D1E, 0x80),
    HB_109_END(0x1D1F, 0x01),
    HB_110_END(0x1D1F, 0x02),
    HB_hb18_ms402(0x1D1F, 0x04), // Dancers fight end
    HB_419_END(0x1D1F, 0x08),
    HB_601_END(0x1D1F, 0x10),
    HB_hb01_ms601(0x1D1F, 0x20), // Sephiroth fight end
    HB_604_ON(0x1D1F, 0x40),
    HB_607_END(0x1D1F, 0x80),
    HB_605_END(0x1D20, 0x01),
    HB_421_END(0x1D20, 0x02),
    HB_422_END(0x1D20, 0x04),
    HB_NEW_MICKEY_START(0x1D20, 0x08),
    HB_POOH_CLEAR(0x1D20, 0x10),
    HB_hb20_ms401(0x1D20, 0x40),
    HB_202_ON(0x1D20, 0x80),
    HB_203_ON(0x1D21, 0x01),
    HB_hb_event_502(0x1D21, 0x04),
    HB_hb_event_507(0x1D21, 0x08),
    HB_RTN_ON(0x1D21, 0x10),
    HB_TRON_OUT(0x1D21, 0x20),
    HB_TRON_IN(0x1D21, 0x40),
    HB_TR05_HIDDEN_ON(0x1D21, 0x80),
    HB_TR05_HIDDEN_OFF(0x1D22, 0x01),
    HB_TR08_HIDDEN_ON(0x1D22, 0x02),
    HB_TR08_HIDDEN_OFF(0x1D22, 0x04),
    HB_hb_event_512(0x1D22, 0x20),
    HB_hb_event_515(0x1D22, 0x40),
    HB_RTN_ON_OFF(0x1D22, 0x80),
    HB_420_END(0x1D23, 0x01),
    HB_607_OUT(0x1D23, 0x02),
    HB_CLOUD_ON(0x1D23, 0x04),
    HB_FM_COM_VEX_END(0x1D23, 0x08), // AS Vexen fight end (HT)
    HB_FM_COM_LEX_END(0x1D23, 0x10), // AS Lexaeus fight end (AG)
    HB_FM_COM_LAR_END(0x1D23, 0x20), // AS Larxene fight end (SP)
    HB_FM_COM_ZEX_END(0x1D23, 0x40), // AS Zexion fight end (OC)
    HB_FM_COM_MAR_END(0x1D23, 0x80), // AS Marluxia fight end (DC)
    HB_ROXAS_KINOKO_ON(0x1D24, 0x02),
    HB_FM_711_END(0x1D24, 0x08),
    HB_FM_MAP1_ROCK_OFF(0x1D24, 0x10),
    HB_FM_TSUURO1_CLEAR(0x1D24, 0x20), // First CoR fight end
    HB_FM_TSUURO2_CLEAR(0x1D24, 0x80), // Second CoR fight end
    HB_FM_13TSUURO_READY(0x1D25, 0x01), // Barrier open? Not sure.
    HB_FM_712_END(0x1D25, 0x02), // Steam valves end
    HB_FM_13TSUURO1_CLEAR(0x1D25, 0x04), // First transport fight end
    HB_FM_13TSUURO2_CLEAR(0x1D25, 0x08), // Second transport fight end
    HB_FM_13TSUURO3_CLEAR(0x1D25, 0x10), // Third transport fight end
    HB_FM_VEX_RE_CLEAR(0x1D26, 0x01), // Data Vexen fight end (HT)
    HB_FM_LEX_RE_CLEAR(0x1D26, 0x02), // Data Lexaeus fight end (AG)
    HB_FM_ZEX_RE_CLEAR(0x1D26, 0x04), // Data Zexion fight end (OC)
    HB_FM_DEM_RE_CLEAR(0x1D26, 0x20), // Data Demyx fight end (HB)
    HB_FM_MAR_RE_CLEAR(0x1D26, 0x80), // Data Marluxia fight end (DC)
    HB_FM_LAR_RE_CLEAR(0x1D27, 0x01), // Data Larxene fight end (SP)
    HB_FM_13TSUURO_OUT(0x1D27, 0x04),
    HB_FM_713_END(0x1D27, 0x08),
    HB_FM_714_END(0x1D27, 0x10),
    HB_ROXAS_KINOKO_CLEAR(0x1D27, 0x20),
    HB_FM_13TSUURO1_CLEAR_OUT(0x1D27, 0x40),
    HB_FM_13TSUURO2_CLEAR_OUT(0x1D27, 0x80),
    HB_FM_TSUURO1_CLEAR_EXIT(0x1D28, 0x01),
    HB_FM_TSUURO2_CLEAR_EXIT(0x1D28, 0x02),
    HB_FM_13RE_COMPLETE(0x1D28, 0x04),
    HB_FM_KINOKO_DEM_PLAYED(0x1D28, 0x08),
    HB_FM_KINOKO_ROX_PLAYED(0x1D28, 0x10); // Mushroom 13 (Proof of Peace check)

    override val index: Int
      get() = ordinal

    override val flagName: String
      get() = name

  }

}
