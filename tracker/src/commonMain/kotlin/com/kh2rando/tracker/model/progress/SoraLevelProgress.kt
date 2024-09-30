package com.kh2rando.tracker.model.progress

import androidx.compose.ui.graphics.Color
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_levels_10
import com.kh2rando.tracker.generated.resources.prog_levels_20
import com.kh2rando.tracker.generated.resources.prog_levels_30
import com.kh2rando.tracker.generated.resources.prog_levels_40
import com.kh2rando.tracker.generated.resources.prog_levels_50
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class SoraLevelProgress(
  val level: Int,
  override val displayString: StringResource,
) : ProgressCheckpoint {

  Level10(level = 10, displayString = Res.string.prog_levels_10),
  Level20(level = 20, displayString = Res.string.prog_levels_20),
  Level30(level = 30, displayString = Res.string.prog_levels_30),
  Level40(level = 40, displayString = Res.string.prog_levels_40),
  Level50(level = 50, displayString = Res.string.prog_levels_50);

  override val defaultIcon: DrawableResource
    get() = Location.SoraLevels.defaultIcon

  override val defaultIconTint: Color
    get() = Location.SoraLevels.defaultIconTint

  override val customIconPath: List<String>
    get() = listOf("Grid", "Checks")

  override val customIconIdentifier: String
    get() = "Level_$level"

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.SoraLevels

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<SoraLevelProgress, Int> {
      val inOrder = listOf(Level10, Level20, Level30, Level40, Level50)
      return inOrder.zip(pointsList).toMap()
    }

  }

}
