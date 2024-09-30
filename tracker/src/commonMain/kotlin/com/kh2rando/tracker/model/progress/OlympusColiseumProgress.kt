package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_chests
import com.kh2rando.tracker.generated.resources.prog_oc_auron_statue
import com.kh2rando.tracker.generated.resources.prog_oc_cerberus
import com.kh2rando.tracker.generated.resources.prog_oc_cups_cerberus
import com.kh2rando.tracker.generated.resources.prog_oc_cups_goddess
import com.kh2rando.tracker.generated.resources.prog_oc_cups_pain_panic
import com.kh2rando.tracker.generated.resources.prog_oc_cups_titan
import com.kh2rando.tracker.generated.resources.prog_oc_demyx
import com.kh2rando.tracker.generated.resources.prog_oc_hades
import com.kh2rando.tracker.generated.resources.prog_oc_hydra
import com.kh2rando.tracker.generated.resources.prog_oc_pete
import com.kh2rando.tracker.generated.resources.prog_oc_urns
import com.kh2rando.tracker.generated.resources.prog_oc_zexion
import com.kh2rando.tracker.generated.resources.prog_oc_zexion_data
import com.kh2rando.tracker.generated.resources.progression_chest
import com.kh2rando.tracker.generated.resources.progression_oc_cerberus
import com.kh2rando.tracker.generated.resources.progression_oc_cups_cerberus
import com.kh2rando.tracker.generated.resources.progression_oc_cups_goddess
import com.kh2rando.tracker.generated.resources.progression_oc_cups_pain_panic
import com.kh2rando.tracker.generated.resources.progression_oc_cups_titan
import com.kh2rando.tracker.generated.resources.progression_oc_demyx
import com.kh2rando.tracker.generated.resources.progression_oc_dusks
import com.kh2rando.tracker.generated.resources.progression_oc_hades
import com.kh2rando.tracker.generated.resources.progression_oc_hydra
import com.kh2rando.tracker.generated.resources.progression_oc_the_lock
import com.kh2rando.tracker.generated.resources.progression_oc_urns
import com.kh2rando.tracker.generated.resources.progression_oc_zexion
import com.kh2rando.tracker.generated.resources.progression_oc_zexion_data
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class OlympusColiseumProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
  override val associatedFlag: ProgressFlag,
) : ProgressCheckpoint, HasCustomizableIcon {

  Chests(
    displayString = Res.string.prog_chests,
    defaultIcon = Res.drawable.progression_chest,
    customIconIdentifier = "chest",
    associatedFlag = Flag.HE_SCENARIO_1_START,
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression")
  },

  Cerberus(
    displayString = Res.string.prog_oc_cerberus,
    defaultIcon = Res.drawable.progression_oc_cerberus,
    customIconIdentifier = "cerberus",
    associatedFlag = Flag.HE_he07_ms103,
  ),

  Urns(
    displayString = Res.string.prog_oc_urns,
    defaultIcon = Res.drawable.progression_oc_urns,
    customIconIdentifier = "urns",
    associatedFlag = Flag.HE_he00_ms104b,
  ),

  OCDemyx(
    displayString = Res.string.prog_oc_demyx,
    defaultIcon = Res.drawable.progression_oc_demyx,
    customIconIdentifier = "demyx",
    associatedFlag = Flag.HE_he17_ms105,
  ),

  OCPete(
    displayString = Res.string.prog_oc_pete,
    defaultIcon = Res.drawable.progression_oc_the_lock,
    customIconIdentifier = "the_lock",
    associatedFlag = Flag.HE_he08_ms107,
  ),

  Hydra(
    displayString = Res.string.prog_oc_hydra,
    defaultIcon = Res.drawable.progression_oc_hydra,
    customIconIdentifier = "hydra",
    associatedFlag = Flag.HE_he18_ms108,
  ),

  AuronStatue(
    displayString = Res.string.prog_oc_auron_statue,
    defaultIcon = Res.drawable.progression_oc_dusks,
    customIconIdentifier = "dusks",
    associatedFlag = Flag.HE_he06_ms203,
  ),

  Hades(
    displayString = Res.string.prog_oc_hades,
    defaultIcon = Res.drawable.progression_oc_hades,
    customIconIdentifier = "hades",
    associatedFlag = Flag.HE_he19_ms205,
  ),

  Zexion(
    displayString = Res.string.prog_oc_zexion,
    defaultIcon = Res.drawable.progression_oc_zexion,
    customIconIdentifier = "zexion",
    associatedFlag = HollowBastionProgress.Flag.HB_FM_COM_ZEX_END,
  ),

  ZexionData(
    displayString = Res.string.prog_oc_zexion_data,
    defaultIcon = Res.drawable.progression_oc_zexion_data,
    customIconIdentifier = "zexion_data",
    associatedFlag = HollowBastionProgress.Flag.HB_FM_ZEX_RE_CLEAR,
  ),

  PainPanicCup(
    displayString = Res.string.prog_oc_cups_pain_panic,
    defaultIcon = Res.drawable.progression_oc_cups_pain_panic,
    customIconIdentifier = "PainandPanicCup",
    associatedFlag = Flag.HE_he_colosseum_1_CLEAR,
  ) {
    override val showInMainLocationProgress: Boolean
      get() = false
    override val customIconPath: List<String>
      get() = listOf("Grid", "Objectives")
  },

  CerberusCup(
    displayString = Res.string.prog_oc_cups_cerberus,
    defaultIcon = Res.drawable.progression_oc_cups_cerberus,
    customIconIdentifier = "CerberusCup",
    associatedFlag = Flag.HE_he_colosseum_2_CLEAR,
  ) {
    override val showInMainLocationProgress: Boolean
      get() = false
    override val customIconPath: List<String>
      get() = listOf("Grid", "Objectives")
  },

  TitanCup(
    displayString = Res.string.prog_oc_cups_titan,
    defaultIcon = Res.drawable.progression_oc_cups_titan,
    customIconIdentifier = "TitanCup",
    associatedFlag = Flag.HE_he_colosseum_3_CLEAR,
  ) {
    override val showInMainLocationProgress: Boolean
      get() = false
    override val customIconPath: List<String>
      get() = listOf("Grid", "Objectives")
  },

  GoddessOfFateCup(
    displayString = Res.string.prog_oc_cups_goddess,
    defaultIcon = Res.drawable.progression_oc_cups_goddess,
    customIconIdentifier = "GoddessCup",
    associatedFlag = Flag.HE_he_colosseum_4_CLEAR,
  ) {
    override val showInMainLocationProgress: Boolean
      get() = false
    override val customIconPath: List<String>
      get() = listOf("Grid", "Objectives")
  };

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.OlympusColiseum

  override val customIconPath: List<String>
    get() = listOf("Progression", "olympus_coliseum")

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<OlympusColiseumProgress, Int> {
      val inOrder = listOf(Chests, Cerberus, Urns, OCDemyx, OCPete, Hydra, AuronStatue, Hades, Zexion, ZexionData)
      return inOrder.zip(pointsList).toMap()
    }

  }

  enum class Flag(override val saveOffset: Int, override val mask: Int) : ProgressFlag {

    HE_START(0x1D50, 0x01),
    HE_101_END(0x1D50, 0x02),
    HE_104_END(0x1D50, 0x10),
    HE_HE_EvEMT_106(0x1D50, 0x40),
    HE_START2A(0x1D50, 0x80),
    HE_108_END(0x1D51, 0x01),
    HE_START2B(0x1D51, 0x02),
    HE_HE_EvENT_114(0x1D51, 0x40),
    HE_115_END(0x1D51, 0x80),
    HE_117_END(0x1D52, 0x02),
    HE_HE_EvENT_119(0x1D52, 0x08),
    HE_120_END(0x1D52, 0x10),
    HE_122_END(0x1D52, 0x40),
    HE_he00_ms104a(0x1D52, 0x80),
    HE_123_END(0x1D53, 0x01),
    HE_ms104a_FAILED(0x1D53, 0x02),
    HE_he00_ms104b(0x1D53, 0x04), // Urns end
    HE_ms104b_FAILED(0x1D53, 0x08),
    HE_HE_EvENT_127(0x1D53, 0x10),
    HE_INIT(0x1D53, 0x20),
    HE_129_END(0x1D53, 0x40),
    HE_SCENARIO_1_OPEN(0x1D53, 0x80),
    HE_SCENARIO_1_START(0x1D54, 0x01),
    HE_132_END(0x1D54, 0x02),
    HE_SCENARIO_1_END(0x1D54, 0x04),
    HE_SCENARIO_2_OPEN(0x1D54, 0x08),
    HE_135_END(0x1D54, 0x10),
    HE_SCENARIO_2_START(0x1D54, 0x20),
    HE_SCENARIO_2_END(0x1D54, 0x40),
    HE_138_END(0x1D54, 0x80),
    HE_W_COL_ON(0x1D55, 0x02),
    HE_he18_ms108(0x1D55, 0x80), // Hydra end
    HE_205_END(0x1D56, 0x02),
    HE_HE_EVENT_102(0x1D56, 0x08),
    HE_HE_EVENT_118(0x1D56, 0x10),
    HE_HE_EVENT_216(0x1D56, 0x20),
    HE_NO_AURON01_START(0x1D56, 0x40),
    HE_NO_AURON01_END(0x1D56, 0x80),
    HE_HE_EvENT_210(0x1D57, 0x01),
    HE_211_END(0x1D57, 0x02),
    HE_NO_AURON02_START(0x1D57, 0x04),
    HE_213_END(0x1D57, 0x08),
    HE_NO_AURON02_END(0x1D57, 0x10),
    HE_214_END(0x1D57, 0x20),
    HE_he_colosseum_1_CLEAR(0x1D57, 0x40),
    HE_he_colosseum_2_CLEAR(0x1D58, 0x01),
    HE_he_colosseum_3_CLEAR(0x1D58, 0x02),
    HE_103_END(0x1D58, 0x08),
    HE_he_colosseum_4_CLEAR(0x1D58, 0x10),
    HE_105_END(0x1D58, 0x20),
    HE_107_END(0x1D58, 0x40),
    HE_he_colosseum_5_CLEAR(0x1D58, 0x80),
    HE_109_END(0x1D59, 0x01),
    HE_he06_ms101(0x1D59, 0x02),
    HE_112_END(0x1D59, 0x04),
    HE_he05_ms102(0x1D59, 0x08), // Hades escape end
    HE_113_END(0x1D59, 0x10),
    HE_he_colosseum_6_CLEAR(0x1D59, 0x20),
    HE_he07_ms103(0x1D59, 0x40), // Cerberus end
    HE_he_colosseum_7_CLEAR(0x1D59, 0x80),
    HE_he_colosseum_8_CLEAR(0x1D5A, 0x02),
    HE_121_END(0x1D5A, 0x04),
    HE_he19_ms204(0x1D5A, 0x08),
    HE_140_END(0x1D5A, 0x10),
    HE_he19_ms205(0x1D5A, 0x20), // Hades fight end
    HE_125_END(0x1D5A, 0x80),
    HE_126_END(0x1D5B, 0x01),
    HE_128_END(0x1D5B, 0x02),
    HE_he17_ms105(0x1D5B, 0x08), // Demyx end
    HE_131_END(0x1D5B, 0x10),
    HE_he08_ms106(0x1D5B, 0x40),
    HE_134_END(0x1D5B, 0x80),
    HE_he08_ms107(0x1D5C, 0x02), // Pete end
    HE_137_END(0x1D5C, 0x04),
    HE_201_END(0x1D5C, 0x20),
    HE_201202_END(0x1D5C, 0x40),
    HE_202_END(0x1D5C, 0x80),
    HE_207_END(0x1D5D, 0x20),
    HE_208_END(0x1D5D, 0x40),
    HE_209_END(0x1D5D, 0x80),
    HE_HADES_ON(0x1D5E, 0x04),
    HE_125_OUT(0x1D5E, 0x08),
    HE_125_In(0x1D5E, 0x10),
    HE_FM_COM_OBJ_OFF(0x1D5E, 0x20),
    HE_FM_KINOKO_ZEX_PLAYED(0x1D5E, 0x40),
    HE_217_END(0x1D5E, 0x80),
    HE_he09_ms201(0x1D5F, 0x01), // First forced cup battle in OC2
    HE_he09_ms202(0x1D5F, 0x02), // Second forced cup battle
    HE_he06_ms203(0x1D5F, 0x04), // Dusks in Hades chamber / Auron Status
    HE_203_END(0x1D5F, 0x20),
    HE_204_LOSE(0x1D5F, 0x40),
    HE_206_LOSE(0x1D5F, 0x80);

    override val index: Int
      get() = ordinal

    override val flagName: String
      get() = name

  }

}
