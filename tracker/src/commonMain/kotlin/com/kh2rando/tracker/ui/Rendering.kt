package com.kh2rando.tracker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.desc_location_complete
import com.kh2rando.tracker.generated.resources.hash_ability_unequip
import com.kh2rando.tracker.generated.resources.hash_accessory
import com.kh2rando.tracker.generated.resources.hash_ai_mode_frequent
import com.kh2rando.tracker.generated.resources.hash_ai_mode_moderate
import com.kh2rando.tracker.generated.resources.hash_ai_mode_rare
import com.kh2rando.tracker.generated.resources.hash_ai_settings
import com.kh2rando.tracker.generated.resources.hash_armor
import com.kh2rando.tracker.generated.resources.hash_button_circle
import com.kh2rando.tracker.generated.resources.hash_button_cross
import com.kh2rando.tracker.generated.resources.hash_button_l1
import com.kh2rando.tracker.generated.resources.hash_button_l2
import com.kh2rando.tracker.generated.resources.hash_button_r1
import com.kh2rando.tracker.generated.resources.hash_button_r2
import com.kh2rando.tracker.generated.resources.hash_button_square
import com.kh2rando.tracker.generated.resources.hash_button_triangle
import com.kh2rando.tracker.generated.resources.hash_exclamation_mark
import com.kh2rando.tracker.generated.resources.hash_form
import com.kh2rando.tracker.generated.resources.hash_gumi_block
import com.kh2rando.tracker.generated.resources.hash_gumi_blueprint
import com.kh2rando.tracker.generated.resources.hash_gumi_brush
import com.kh2rando.tracker.generated.resources.hash_gumi_gear
import com.kh2rando.tracker.generated.resources.hash_gumi_ship
import com.kh2rando.tracker.generated.resources.hash_item_consumable
import com.kh2rando.tracker.generated.resources.hash_item_key
import com.kh2rando.tracker.generated.resources.hash_item_tent
import com.kh2rando.tracker.generated.resources.hash_magic
import com.kh2rando.tracker.generated.resources.hash_material
import com.kh2rando.tracker.generated.resources.hash_party
import com.kh2rando.tracker.generated.resources.hash_question_mark
import com.kh2rando.tracker.generated.resources.hash_rank_a
import com.kh2rando.tracker.generated.resources.hash_rank_b
import com.kh2rando.tracker.generated.resources.hash_rank_c
import com.kh2rando.tracker.generated.resources.hash_rank_s
import com.kh2rando.tracker.generated.resources.hash_weapon_keyblade
import com.kh2rando.tracker.generated.resources.hash_weapon_shield
import com.kh2rando.tracker.generated.resources.hash_weapon_staff
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.HasColorToken
import com.kh2rando.tracker.model.HasCustomizableIcon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource

/**
 * Alpha value for "ghost" entities.
 */
const val GhostAlpha: Float = 0.25f

val TrackerDarkColorScheme: ColorScheme = darkColorScheme(
  primary = Color(0xFF9BCBFB),
  onPrimary = Color(0xFF003353),
  primaryContainer = Color(0xFF0E4A73),
  onPrimaryContainer = Color(0xFFCEE5FF),
  secondary = Color(0xFFB9C8DA),
  onSecondary = Color(0xFF233240),
  secondaryContainer = Color(0xFF3A4857),
  onSecondaryContainer = Color(0xFFD5E4F7),
  tertiary = Color(0xFFD3BFE6),
  onTertiary = Color(0xFF382A49),
  tertiaryContainer = Color(0xFF4F4061),
  onTertiaryContainer = Color(0xFFEEDBFF),
  surface = Color(0xFF202020),
  surfaceContainerLowest = Color(0xFF000000),
  surfaceContainerLow = Color(0xFF101010),
  surfaceContainer = Color(0xFF303030),
  surfaceContainerHigh = Color(0xFF404040),
  surfaceContainerHighest = Color(0xFF505050),
)

/**
 * Resolves a [ColorToken] to its [Color].
 */
val ColorToken?.color: Color
  get() {
    return when (this) {
      null -> Color.Unspecified
      ColorToken.Red -> Color(0xFFFF6633)
      ColorToken.Salmon -> Color(0xFFFF9781)
      ColorToken.Orange -> Color(0xFFFF8844)
      ColorToken.Gold -> Color(0xFFFFDD00)
      ColorToken.Green -> Color(0xFFCCFF44)
      ColorToken.LightBlue -> Color(0xFF55EEFF)
      ColorToken.DarkBlue -> Color(0xFF88AAFF)
      ColorToken.WhiteBlue -> Color(0xFFCCFFFF)
      ColorToken.Magenta -> Color(0xFFFF68C3)
      ColorToken.Purple -> Color(0xFFDD88FF)
      ColorToken.White -> Color(0xFFDDDDFF)
    }
  }

/**
 * Resolves a [HasColorToken] to its [Color].
 */
val HasColorToken.color: Color
  get() = colorToken.color

/**
 * Returns a [ColorFilter.tint] of this color if [isSpecified], otherwise null.
 */
fun Color.tintFilterOrNull(): ColorFilter? {
  return if (isSpecified) ColorFilter.tint(this) else null
}

@Composable
fun SmallHeader(text: String, modifier: Modifier = Modifier.fillMaxWidth()) {
  Surface(color = MaterialTheme.colorScheme.primary, modifier = modifier) {
    Text(text, modifier = Modifier.padding(4.dp))
  }
}

@Composable
fun IconCounterCell(
  text: String,
  icon: HasCustomizableIcon,
  tooltip: String,
  modifier: Modifier = Modifier,
  fontSize: TextUnit = TextUnit.Unspecified,
) {
  SimpleTooltipArea(tooltipText = tooltip, modifier = modifier) {
    BoxWithConstraints {
      val maxHeight = maxHeight
      val adjustedTextStyle = if (fontSize.isSpecified) {
        MaterialTheme.typography.titleLarge.copy(fontFamily = khMenuFontFamily(), fontSize = fontSize)
      } else {
        MaterialTheme.typography.titleLarge.copy(fontFamily = khMenuFontFamily()).shrinkableToFitHeight(maxHeight)
      }
      Row(
        modifier = Modifier.height(maxHeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
      ) {
        CustomizableIcon(icon = icon, contentDescription = tooltip, modifier = Modifier.size(maxHeight))

        Box(
          modifier = Modifier.weight(1.0f, fill = false).height(maxHeight),
          contentAlignment = Alignment.Center,
        ) {
          Text(
            text,
            color = icon.defaultIconTint,
            style = adjustedTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    }
  }
}

@Composable
fun IconBadgeCell(
  badgeText: String,
  icon: HasCustomizableIcon,
  tooltip: String,
  modifier: Modifier = Modifier,
  iconAlpha: Float = DefaultAlpha,
) {
  SimpleTooltipArea(tooltip, modifier = modifier) {
    Box {
      CustomizableIcon(
        icon = icon,
        contentDescription = tooltip,
        alpha = iconAlpha,
      )

      BoxWithConstraints(
        modifier = Modifier.fillMaxHeight(0.5f).align(Alignment.BottomEnd),
        contentAlignment = Alignment.BottomEnd
      ) {
        CounterText(textString = badgeText, maximumHeight = maxHeight)
      }
    }
  }
}

@Composable
fun CompletedIndicator(modifier: Modifier = Modifier) {
  CustomizableIcon(
    SystemIcon.Complete,
    contentDescription = stringResource(Res.string.desc_location_complete),
    modifier = modifier,
  )
}

@Composable
@Suppress("UnusedReceiverParameter") // Want to enforce within a row
fun RowScope.SeedHashIcons(seedHashIconNames: List<String>) {
  for (iconName in seedHashIconNames) {
    val iconResource = seedHashIconResource(iconName) ?: continue
    Image(imageResource(iconResource), contentDescription = iconName)
  }
}

private fun seedHashIconResource(iconName: String): DrawableResource? {
  return when (iconName) {
    "item-consumable" -> Res.drawable.hash_item_consumable
    "item-tent" -> Res.drawable.hash_item_tent
    "item-key" -> Res.drawable.hash_item_key
    "ability-unequip" -> Res.drawable.hash_ability_unequip
    "weapon-keyblade" -> Res.drawable.hash_weapon_keyblade
    "weapon-staff" -> Res.drawable.hash_weapon_staff
    "weapon-shield" -> Res.drawable.hash_weapon_shield
    "armor" -> Res.drawable.hash_armor
    "magic" -> Res.drawable.hash_magic
    "material" -> Res.drawable.hash_material
    "exclamation-mark" -> Res.drawable.hash_exclamation_mark
    "question-mark" -> Res.drawable.hash_question_mark
    "accessory" -> Res.drawable.hash_accessory
    "party" -> Res.drawable.hash_party
    "ai-mode-frequent" -> Res.drawable.hash_ai_mode_frequent
    "ai-mode-moderate" -> Res.drawable.hash_ai_mode_moderate
    "ai-mode-rare" -> Res.drawable.hash_ai_mode_rare
    "ai-settings" -> Res.drawable.hash_ai_settings
    "rank-s" -> Res.drawable.hash_rank_s
    "rank-a" -> Res.drawable.hash_rank_a
    "rank-b" -> Res.drawable.hash_rank_b
    "rank-c" -> Res.drawable.hash_rank_c
    "gumi-brush" -> Res.drawable.hash_gumi_brush
    "gumi-blueprint" -> Res.drawable.hash_gumi_blueprint
    "gumi-ship" -> Res.drawable.hash_gumi_ship
    "gumi-block" -> Res.drawable.hash_gumi_block
    "gumi-gear" -> Res.drawable.hash_gumi_gear
    "form" -> Res.drawable.hash_form
    "button-r1" -> Res.drawable.hash_button_r1
    "button-r2" -> Res.drawable.hash_button_r2
    "button-l1" -> Res.drawable.hash_button_l1
    "button-l2" -> Res.drawable.hash_button_l2
    "button-triangle" -> Res.drawable.hash_button_triangle
    "button-cross" -> Res.drawable.hash_button_cross
    "button-square" -> Res.drawable.hash_button_square
    "button-circle" -> Res.drawable.hash_button_circle
    else -> null
  }
}
