@file:OptIn(ExperimentalFoundationApi::class)

package com.kh2rando.tracker.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TrackerTooltipArea(
  modifier: Modifier = Modifier,
  delay: Duration = 500.milliseconds,
  tooltipPlacement: Alignment = Alignment.BottomCenter,
  tooltip: @Composable () -> Unit,
  content: @Composable () -> Unit,
) {
  TooltipArea(
    tooltip = tooltip,
    modifier = modifier,
    delayMillis = delay.inWholeMilliseconds.toInt(),
    tooltipPlacement = TooltipPlacement.ComponentRect(
      anchor = tooltipPlacement,
      alignment = tooltipPlacement
    ),
    content = content
  )
}

@Composable
fun SimpleTooltipArea(
  tooltipText: String,
  modifier: Modifier = Modifier,
  delay: Duration = 500.milliseconds,
  tooltipPlacement: Alignment = Alignment.BottomCenter,
  content: @Composable () -> Unit,
) {
  if (tooltipText.isEmpty()) {
    Box(modifier = modifier) {
      content()
    }
  } else {
    TrackerTooltipArea(
      modifier = modifier,
      delay = delay,
      tooltip = {
        Surface(
          modifier = Modifier.shadow(4.dp),
          shape = MaterialTheme.shapes.small,
          color = MaterialTheme.colorScheme.inverseSurface
        ) {
          Text(
            tooltipText,
            modifier = Modifier.padding(4.dp),
            style = MaterialTheme.typography.titleMedium,
          )
        }
      },
      content = content,
      tooltipPlacement = tooltipPlacement,
    )
  }
}
