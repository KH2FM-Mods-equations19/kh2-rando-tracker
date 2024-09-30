package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_chests
import com.kh2rando.tracker.generated.resources.prog_goa_fight_1
import com.kh2rando.tracker.generated.resources.prog_goa_fight_2
import com.kh2rando.tracker.generated.resources.prog_goa_last_chest
import com.kh2rando.tracker.generated.resources.prog_goa_transport
import com.kh2rando.tracker.generated.resources.prog_goa_valves
import com.kh2rando.tracker.generated.resources.progression_chest
import com.kh2rando.tracker.generated.resources.progression_hb_cor_first_fight
import com.kh2rando.tracker.generated.resources.progression_hb_cor_last_chest
import com.kh2rando.tracker.generated.resources.progression_hb_cor_second_fight
import com.kh2rando.tracker.generated.resources.progression_hb_steam_valve
import com.kh2rando.tracker.generated.resources.progression_hb_transport
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class CavernOfRemembranceProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
  override val associatedFlag: HollowBastionProgress.Flag?,
) : ProgressCheckpoint, HasCustomizableIcon {

  Chests(
    displayString = Res.string.prog_chests,
    defaultIcon = Res.drawable.progression_chest,
    customIconIdentifier = "chest",
    associatedFlag = HollowBastionProgress.Flag.HB_FM_711_END,
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression")
  },

  Fight1(
    displayString = Res.string.prog_goa_fight_1,
    defaultIcon = Res.drawable.progression_hb_cor_first_fight,
    customIconIdentifier = "first_fight",
    associatedFlag = HollowBastionProgress.Flag.HB_FM_TSUURO1_CLEAR,
  ),

  Valves(
    displayString = Res.string.prog_goa_valves,
    defaultIcon = Res.drawable.progression_hb_steam_valve,
    customIconIdentifier = "steam_valve",
    associatedFlag = HollowBastionProgress.Flag.HB_FM_712_END,
  ),

  Fight2(
    displayString = Res.string.prog_goa_fight_2,
    defaultIcon = Res.drawable.progression_hb_cor_second_fight,
    customIconIdentifier = "second_fight",
    associatedFlag = HollowBastionProgress.Flag.HB_FM_TSUURO2_CLEAR,
  ),

  LastChest(
    displayString = Res.string.prog_goa_last_chest,
    defaultIcon = Res.drawable.progression_hb_cor_last_chest,
    customIconIdentifier = "last_chest_cor",
    associatedFlag = null, // This is done differently due to tracking a chest
  ),

  Transport(
    displayString = Res.string.prog_goa_transport,
    defaultIcon = Res.drawable.progression_hb_transport,
    customIconIdentifier = "transport",
    associatedFlag = HollowBastionProgress.Flag.HB_FM_13TSUURO3_CLEAR,
  );

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.GardenOfAssemblage

  override val customIconPath: List<String>
    get() = listOf("Progression", "hollow_bastion")

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<CavernOfRemembranceProgress, Int> {
      val inOrder = listOf(Chests, Fight1, Valves, Fight2, Transport)
      return inOrder.zip(pointsList).toMap()
    }

  }

}
