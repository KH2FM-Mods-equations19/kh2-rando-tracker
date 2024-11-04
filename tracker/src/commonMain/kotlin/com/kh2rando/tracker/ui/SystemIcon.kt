package com.kh2rando.tracker.ui

import androidx.compose.ui.graphics.Color
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.ansem_report
import com.kh2rando.tracker.generated.resources.location_levels
import com.kh2rando.tracker.generated.resources.progression_points
import com.kh2rando.tracker.generated.resources.stats_chest
import com.kh2rando.tracker.generated.resources.stats_deaths
import com.kh2rando.tracker.generated.resources.stats_defense
import com.kh2rando.tracker.generated.resources.stats_emblems
import com.kh2rando.tracker.generated.resources.stats_magic
import com.kh2rando.tracker.generated.resources.stats_strength
import com.kh2rando.tracker.generated.resources.status_pc_connected
import com.kh2rando.tracker.generated.resources.status_pc_detected
import com.kh2rando.tracker.generated.resources.status_searching
import com.kh2rando.tracker.generated.resources.system_block
import com.kh2rando.tracker.generated.resources.system_mickey
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.HasColorToken
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource

enum class SystemIcon : HasCustomizableIcon, HasColorToken {

  LevelStat {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.location_levels
    override val customIconPath: List<String>
      get() = listOf("System", "stats")
    override val customIconIdentifier: String
      get() = "level"
    override val colorToken: ColorToken
      get() = Location.SoraLevels.colorToken
  },

  StrengthStat {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.stats_strength
    override val customIconPath: List<String>
      get() = listOf("System", "stats")
    override val customIconIdentifier: String
      get() = "strength"
    override val colorToken: ColorToken
      get() = ColorToken.Orange
  },

  MagicStat {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.stats_magic
    override val customIconPath: List<String>
      get() = listOf("System", "stats")
    override val customIconIdentifier: String
      get() = "magic"
    override val colorToken: ColorToken
      get() = ColorToken.Purple
  },

  DefenseStat {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.stats_defense
    override val customIconPath: List<String>
      get() = listOf("System", "stats")
    override val customIconIdentifier: String
      get() = "defence"
    override val colorToken: ColorToken
      get() = ColorToken.Gold
  },

  DeathCount {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.stats_deaths
    override val customIconPath: List<String>
      get() = listOf("System", "stats")
    override val customIconIdentifier: String
      get() = "deaths"
    override val colorToken: ColorToken
      get() = ColorToken.Red
  },

  ItemCount {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.stats_chest
    override val customIconPath: List<String>
      get() = listOf("System", "stats")
    override val customIconIdentifier: String
      get() = "chest"
    override val colorToken: ColorToken
      get() = ColorToken.Gold
  },

  Prohibition {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.system_block
    override val customIconPath: List<String>
      get() = listOf("System")
    override val customIconIdentifier: String
      get() = "prohibition"
    override val colorToken: ColorToken
      get() = ColorToken.Red
  },

  ProgressionPoints {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.progression_points
    override val customIconPath: List<String>
      get() = listOf("System", "stats")
    override val customIconIdentifier: String
      get() = "ProgPoints"
    override val colorToken: ColorToken
      get() = ColorToken.Gold
  },

  AutoTrackerDisconnected {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.system_block
    override val customIconPath: List<String>
      get() = listOf("System")
    override val customIconIdentifier: String
      get() = "cross"
    override val colorToken: ColorToken
      get() = ColorToken.Red
  },

  AutoTrackerConnected {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.status_pc_connected
    override val customIconPath: List<String>
      get() = listOf("System", "config")
    override val customIconIdentifier: String
      get() = "pc_connected"
    override val colorToken: ColorToken
      get() = ColorToken.Green
  },

  AutoTrackerScanning {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.status_searching
    override val customIconPath: List<String>
      get() = listOf("System", "config")
    override val customIconIdentifier: String
      get() = "searching"
    override val colorToken: ColorToken
      get() = ColorToken.WhiteBlue
  },

  AutoTrackerError {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.status_pc_detected
    override val customIconPath: List<String>
      get() = listOf("System", "config")
    override val customIconIdentifier: String
      get() = "pc_detected"
    override val colorToken: ColorToken
      get() = ColorToken.Red
  },

  LuckyEmblem {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.stats_emblems
    override val customIconPath: List<String>
      get() = listOf("System", "stats")
    override val customIconIdentifier: String
      get() = "emblem"
    override val colorToken: ColorToken
      get() = ColorToken.Orange
  },

  Complete {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.system_mickey
    override val customIconPath: List<String>
      get() = listOf("System")
    override val customIconIdentifier: String
      get() = "complete"
    override val colorToken: ColorToken
      get() = ColorToken.Orange
  },

  AnsemReport {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.ansem_report
    override val customIconPath: List<String>
      get() = listOf("checks")
    override val customIconIdentifier: String
      get() = "ansem_report"
    override val colorToken: ColorToken
      get() = ColorToken.White
  };

  override val defaultIconTint: Color
    get() = color

}
