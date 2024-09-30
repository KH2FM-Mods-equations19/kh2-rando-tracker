package com.kh2rando.tracker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.ansem_report_blank
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.ItemPrototype
import org.jetbrains.compose.resources.imageResource

/**
 * Color to use for tinting items that are disabled.
 */
val ColorScheme.disabledItemTint: Color
  get() = surfaceContainerLowest

/**
 * Indicates the type of rendering to use for an item.
 */
sealed interface ItemRenderState {

  /**
   * Default rendering.
   */
  data object Default : ItemRenderState

  /**
   * Render the item as revealed by a hint.
   */
  data object Revealed : ItemRenderState

  /**
   * Render the item as disabled.
   */
  data object Disabled : ItemRenderState

}

@Composable
fun ItemPrototype.ItemIcon(
  modifier: Modifier = Modifier,
  renderState: ItemRenderState = ItemRenderState.Default,
  strikeCount: Int = 0,
) {
  val colorScheme = MaterialTheme.colorScheme

  val tintOverride = when (renderState) {
    ItemRenderState.Default, ItemRenderState.Revealed -> Color.Unspecified
    ItemRenderState.Disabled -> colorScheme.disabledItemTint
  }

  val alpha = when (renderState) {
    ItemRenderState.Default, ItemRenderState.Disabled -> DefaultAlpha
    ItemRenderState.Revealed -> GhostAlpha
  }

  BoxWithConstraints(modifier = modifier) {
    if (findCustomIconFile() == null && this@ItemIcon is AnsemReport) {
      Image(
        imageResource(Res.drawable.ansem_report_blank),
        contentDescription = localizedName,
        alpha = alpha,
        colorFilter = tintOverride.tintFilterOrNull() ?: defaultIconTint.tintFilterOrNull(),
        modifier = Modifier.align(Alignment.Center)
      )

      val acquired = renderState is ItemRenderState.Disabled
      OutlinedText(
        textString = reportNumber.toString(),
        outlineColor = when (renderState) {
          ItemRenderState.Default -> colorScheme.onSurface.copy(alpha = alpha)
          ItemRenderState.Revealed, ItemRenderState.Disabled -> Color.Transparent
        },
        outlineStroke = Stroke(width = 2.0f, join = StrokeJoin.Round),
        color = if (acquired) colorScheme.surface else colorScheme.surfaceContainerLow.copy(alpha = alpha),
        style = MaterialTheme.typography.titleLarge.shrinkableToFitHeight(maxHeight),
        fontFamily = khMenuFontFamily(),
        modifier = Modifier.align(Alignment.Center)
      )
    } else {
      CustomizableIcon(
        icon = this@ItemIcon,
        contentDescription = localizedName,
        modifier = modifier,
        alpha = alpha,
        tintColorOverride = tintOverride,
      )
    }

    if (strikeCount > 0) {
      StrikeCountIndicator(strikeCount = strikeCount, modifier = Modifier.align(Alignment.TopCenter))
    }
  }
}

@Composable
private fun StrikeCountIndicator(strikeCount: Int, modifier: Modifier = Modifier) {
  val strikeAlpha = 0.95f
  OutlinedText(
    textString = List(strikeCount) { 'X' }.joinToString(""),
    outlineColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = strikeAlpha),
    outlineStroke = Stroke(width = 2.0f, join = StrokeJoin.Round),
    maxLines = 1,
    style = MaterialTheme.typography.labelSmall.shrinkableToFitHeight(12.dp),
    color = ColorToken.Red.color.copy(alpha = strikeAlpha),
    modifier = modifier
  )
}
