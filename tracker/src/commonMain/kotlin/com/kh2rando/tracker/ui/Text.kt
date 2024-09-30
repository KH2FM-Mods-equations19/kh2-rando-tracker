package com.kh2rando.tracker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import com.kh2rando.tracker.generated.resources.KHMenu
import com.kh2rando.tracker.generated.resources.KHMenuBold
import com.kh2rando.tracker.generated.resources.KHMenuBoldItalic
import com.kh2rando.tracker.generated.resources.KHMenuItalic
import com.kh2rando.tracker.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun khMenuFontFamily(): FontFamily {
  return FontFamily(
    Font(Res.font.KHMenu, weight = FontWeight.Normal),
    Font(Res.font.KHMenuBold, weight = FontWeight.Bold),
    Font(Res.font.KHMenuItalic, style = FontStyle.Italic),
    Font(Res.font.KHMenuBoldItalic, weight = FontWeight.Bold, style = FontStyle.Italic)
  )
}

/**
 * Returns a [TextStyle] that will shrink the font size and line height to fit [height] if needed.
 */
@Composable
private fun TextStyle.shrinkToFitHeightIfNeeded(height: Dp): TextStyle {
  return with(LocalDensity.current) {
    if (fontSize.toDp() <= height) {
      this@shrinkToFitHeightIfNeeded
    } else {
      copy(fontSize = height.toSp())
    }
  }
}

/**
 * Returns a [TextStyle] that will shrink the font size to fit [height] if needed.
 */
@Composable
fun TextStyle.shrinkableToFitHeight(height: Dp): TextStyle {
  return copy(lineHeightStyle = LineHeightStyle(LineHeightStyle.Alignment.Center, LineHeightStyle.Trim.Both))
    .shrinkToFitHeightIfNeeded(height)
}

@Composable
fun OutlinedText(
  textString: String,
  outlineColor: Color,
  outlineStroke: Stroke = Stroke(width = 8.0f, join = StrokeJoin.Round),
  modifier: Modifier = Modifier,
  color: Color = Color.Unspecified,
  fontSize: TextUnit = TextUnit.Unspecified,
  fontStyle: FontStyle? = null,
  fontWeight: FontWeight? = null,
  fontFamily: FontFamily? = null,
  letterSpacing: TextUnit = TextUnit.Unspecified,
  textDecoration: TextDecoration? = null,
  textAlign: TextAlign? = null,
  lineHeight: TextUnit = TextUnit.Unspecified,
  overflow: TextOverflow = TextOverflow.Clip,
  softWrap: Boolean = true,
  maxLines: Int = Int.MAX_VALUE,
  minLines: Int = 1,
  style: TextStyle = LocalTextStyle.current,
) {
  Box(modifier = modifier) {
    Text(
      text = textString,
      color = outlineColor,
      fontSize = fontSize,
      fontStyle = fontStyle,
      fontWeight = fontWeight,
      fontFamily = fontFamily,
      letterSpacing = letterSpacing,
      textDecoration = textDecoration,
      textAlign = textAlign,
      lineHeight = lineHeight,
      overflow = overflow,
      softWrap = softWrap,
      maxLines = maxLines,
      minLines = minLines,
      style = style.copy(drawStyle = outlineStroke)
    )
    Text(
      text = textString,
      color = color,
      fontSize = fontSize,
      fontStyle = fontStyle,
      fontWeight = fontWeight,
      fontFamily = fontFamily,
      letterSpacing = letterSpacing,
      textDecoration = textDecoration,
      textAlign = textAlign,
      lineHeight = lineHeight,
      overflow = overflow,
      softWrap = softWrap,
      maxLines = maxLines,
      minLines = minLines,
      style = style
    )
  }
}

@Composable
fun CounterText(
  textString: String,
  maximumHeight: Dp,
  modifier: Modifier = Modifier,
  color: Color = Color.Unspecified,
) {
  OutlinedText(
    textString = textString,
    outlineColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    maxLines = 1,
    style = MaterialTheme.typography.titleLarge.shrinkableToFitHeight(maximumHeight.coerceAtMost(32.dp)),
    color = color,
    fontFamily = khMenuFontFamily(),
    fontWeight = FontWeight.Bold,
    modifier = modifier
  )
}
