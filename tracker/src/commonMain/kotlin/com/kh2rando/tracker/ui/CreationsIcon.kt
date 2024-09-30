package com.kh2rando.tracker.ui

import androidx.compose.ui.graphics.Color
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.location_creations
import com.kh2rando.tracker.generated.resources.location_puzzle
import com.kh2rando.tracker.generated.resources.location_synthesis
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.HasColorToken
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.seed.CreationsOption
import com.kh2rando.tracker.model.seed.SeedSettings
import org.jetbrains.compose.resources.DrawableResource

/**
 * Location icon for [com.kh2rando.tracker.model.Location.Creations]. Dynamically changes based on [SeedSettings].
 */
enum class CreationsIcon: HasCustomizableIcon, HasColorToken {

  PuzzleOnly {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_puzzle
    override val customIconIdentifier: String
      get() = "Puzzle"
    override val colorToken: ColorToken
      get() = ColorToken.Gold
  },

  MoogleOnly {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_synthesis
    override val customIconIdentifier: String
      get() = "Synth"
    override val colorToken: ColorToken
      get() = ColorToken.Red
  },

  PuzzleAndMoogle {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_creations
    override val customIconIdentifier: String
      get() = "PuzzSynth"
    override val colorToken: ColorToken
      get() = ColorToken.Salmon
  };

  override val defaultIconTint: Color
    get() = color

  override val customIconPath: List<String>
    get() = listOf("Worlds")

  companion object {

    fun fromSeedSettings(seedSettings: SeedSettings): CreationsIcon {
      val creationsOptions = seedSettings.creationsOptions
      val hasPuzzle = CreationsOption.Puzzle in creationsOptions
      val hasSynthesis = CreationsOption.Synthesis in creationsOptions
      return if (hasPuzzle && hasSynthesis) {
        PuzzleAndMoogle
      } else if (hasPuzzle) {
        PuzzleOnly
      } else if (hasSynthesis) {
        MoogleOnly
      } else {
        error("Expected at least one of puzzle or synthesis if Creations is active")
      }
    }

  }

}
