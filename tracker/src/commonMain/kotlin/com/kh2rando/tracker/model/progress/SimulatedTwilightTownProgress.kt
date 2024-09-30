package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_chests
import com.kh2rando.tracker.generated.resources.prog_stt_axel_1
import com.kh2rando.tracker.generated.resources.prog_stt_axel_2
import com.kh2rando.tracker.generated.resources.prog_stt_computer_room
import com.kh2rando.tracker.generated.resources.prog_stt_data_roxas
import com.kh2rando.tracker.generated.resources.prog_stt_minigame
import com.kh2rando.tracker.generated.resources.prog_stt_struggle
import com.kh2rando.tracker.generated.resources.prog_stt_twilight_thorn
import com.kh2rando.tracker.generated.resources.progression_chest
import com.kh2rando.tracker.generated.resources.progression_stt_axel_story
import com.kh2rando.tracker.generated.resources.progression_stt_computer_room
import com.kh2rando.tracker.generated.resources.progression_stt_munny_pouch
import com.kh2rando.tracker.generated.resources.progression_stt_pod_room
import com.kh2rando.tracker.generated.resources.progression_stt_roxas
import com.kh2rando.tracker.generated.resources.progression_stt_struggle
import com.kh2rando.tracker.generated.resources.progression_stt_twilight_thorn
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class SimulatedTwilightTownProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
  override val associatedFlag: ProgressFlag,
) : ProgressCheckpoint, HasCustomizableIcon {

  Chests(
    displayString = Res.string.prog_chests,
    defaultIcon = Res.drawable.progression_chest,
    customIconIdentifier = "chest",
    associatedFlag = TwilightTownProgress.Flag.TT_103_END_L,
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression")
  },

  Minigame(
    displayString = Res.string.prog_stt_minigame,
    defaultIcon = Res.drawable.progression_stt_munny_pouch,
    customIconIdentifier = "munny_pouch",
    associatedFlag = TwilightTownProgress.Flag.TT_207_END_L,
  ),

  TwilightThorn(
    displayString = Res.string.prog_stt_twilight_thorn,
    defaultIcon = Res.drawable.progression_stt_twilight_thorn,
    customIconIdentifier = "twilight_thorn",
    associatedFlag = TwilightTownProgress.Flag.TT_315_END_L,
  ),

  Axel1(
    displayString = Res.string.prog_stt_axel_1,
    defaultIcon = Res.drawable.progression_stt_axel_story,
    customIconIdentifier = "axel_story",
    associatedFlag = TwilightTownProgress.Flag.TT_410_END_L,
  ),

  Struggle(
    displayString = Res.string.prog_stt_struggle,
    defaultIcon = Res.drawable.progression_stt_struggle,
    customIconIdentifier = "struggle",
    associatedFlag = TwilightTownProgress.Flag.TT_SCENARIO_4_END,
  ),

  ComputerRoom(
    displayString = Res.string.prog_stt_computer_room,
    defaultIcon = Res.drawable.progression_stt_computer_room,
    customIconIdentifier = "computer_room",
    associatedFlag = TwilightTownProgress.Flag.TT_611_END_L,
  ),

  Axel(
    displayString = Res.string.prog_stt_axel_2,
    defaultIcon = Res.drawable.progression_stt_pod_room,
    customIconIdentifier = "pod_room",
    associatedFlag = TwilightTownProgress.Flag.TT_614_END_L,
  ),

  DataRoxas(
    displayString = Res.string.prog_stt_data_roxas,
    defaultIcon = Res.drawable.progression_stt_roxas,
    customIconIdentifier = "roxas",
    associatedFlag = WorldThatNeverWasProgress.Flag.EH_FM_ROX_RE_CLEAR,
  );

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.SimulatedTwilightTown

  override val customIconPath: List<String>
    get() = listOf("Progression", "simulated_twilight_town")

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<SimulatedTwilightTownProgress, Int> {
      val inOrder = listOf(Chests, Minigame, TwilightThorn, Axel1, Struggle, ComputerRoom, Axel, DataRoxas)
      return inOrder.zip(pointsList).toMap()
    }

  }

}
