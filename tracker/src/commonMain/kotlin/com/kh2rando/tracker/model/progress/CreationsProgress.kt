package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_puz_awakening
import com.kh2rando.tracker.generated.resources.prog_puz_daylight
import com.kh2rando.tracker.generated.resources.prog_puz_duality
import com.kh2rando.tracker.generated.resources.prog_puz_frontier
import com.kh2rando.tracker.generated.resources.prog_puz_heart
import com.kh2rando.tracker.generated.resources.prog_puz_sunset
import com.kh2rando.tracker.generated.resources.progression_puz_awakening
import com.kh2rando.tracker.generated.resources.progression_puz_daylight
import com.kh2rando.tracker.generated.resources.progression_puz_duality
import com.kh2rando.tracker.generated.resources.progression_puz_frontier
import com.kh2rando.tracker.generated.resources.progression_puz_heart
import com.kh2rando.tracker.generated.resources.progression_puz_sunset
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class CreationsProgress(
  override val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
) : ProgressCheckpoint, HasCustomizableIcon {

  AwakeningPuzzle(
    displayString = Res.string.prog_puz_awakening,
    defaultIcon = Res.drawable.progression_puz_awakening,
    customIconIdentifier = "Puzzle_Awakening",
  ),

  HeartPuzzle(
    displayString = Res.string.prog_puz_heart,
    defaultIcon = Res.drawable.progression_puz_heart,
    customIconIdentifier = "Puzzle_Heart",
  ),

  DualityPuzzle(
    displayString = Res.string.prog_puz_duality,
    defaultIcon = Res.drawable.progression_puz_duality,
    customIconIdentifier = "Puzzle_Duality",
  ),

  FrontierPuzzle(
    displayString = Res.string.prog_puz_frontier,
    defaultIcon = Res.drawable.progression_puz_frontier,
    customIconIdentifier = "Puzzle_Frontier",
  ),

  DaylightPuzzle(
    displayString = Res.string.prog_puz_daylight,
    defaultIcon = Res.drawable.progression_puz_daylight,
    customIconIdentifier = "Puzzle_Daylight",
  ),

  SunsetPuzzle(
    displayString = Res.string.prog_puz_sunset,
    defaultIcon = Res.drawable.progression_puz_sunset,
    customIconIdentifier = "Puzzle_Sunset",
  );

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.Creations

  override val customIconPath: List<String>
    get() = listOf("Grid", "Objectives")

  override val showInMainLocationProgress: Boolean
    get() = false

  // There aren't any known progress flags for puzzles
  // TODO: If we want to auto-track puzzles, need to figure something out
  override val associatedFlag: ProgressFlag?
    get() = null

}
