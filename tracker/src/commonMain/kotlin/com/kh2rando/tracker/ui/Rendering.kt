package com.kh2rando.tracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.HasColorToken
import com.kh2rando.tracker.model.HasCustomizableIcon

/**
 * Alpha value for "ghost" entities.
 */
const val GhostAlpha: Float = 0.25f

val TrackerDarkColorScheme: ColorScheme = darkColorScheme(
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

val ColorScheme.uiDarkGreen: Color
  get() = Color(0xFF2A732E)

@Composable
fun SmallHeader(text: String, modifier: Modifier = Modifier) {
  Surface(color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth()) {
    Text(text, modifier = modifier.padding(4.dp))
  }
}

@Composable
fun IconCounterCell(
  text: String,
  icon: HasCustomizableIcon,
  tooltip: String,
  modifier: Modifier = Modifier,
) {
  SimpleTooltipArea(tooltipText = tooltip, modifier = modifier) {
    BoxWithConstraints {
      val adjustedTextStyle = MaterialTheme.typography.titleLarge.copy(
        fontFamily = khMenuFontFamily(),
      ).shrinkableToFitHeight(maxHeight)
      Row(
        modifier = Modifier.fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
      ) {
        CustomizableIcon(icon = icon, contentDescription = tooltip)

        Text(
          text,
          color = icon.defaultIconTint,
          style = adjustedTextStyle,
          modifier = Modifier.weight(1.0f, fill = false),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
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
