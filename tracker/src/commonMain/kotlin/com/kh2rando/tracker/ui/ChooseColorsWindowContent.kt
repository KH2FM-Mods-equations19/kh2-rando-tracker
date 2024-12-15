package com.kh2rando.tracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.grid_all_complete_color
import com.kh2rando.tracker.generated.resources.grid_cell_complete_color
import com.kh2rando.tracker.generated.resources.grid_cell_mark_color
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.preferences.ColorPreference
import com.kh2rando.tracker.model.preferences.TrackerPreferences
import com.kh2rando.tracker.model.preferences.collectAsState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChooseColorsWindowContent(
  preferences: TrackerPreferences,
  modifier: Modifier = Modifier,
) {
  Surface(modifier) {
    Column(Modifier.fillMaxSize().padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
      ColorRow(
        label = stringResource(Res.string.grid_cell_mark_color),
        preference = preferences.gridCellMarkColor,
        modifier = Modifier.fillMaxWidth(),
      )

      ColorRow(
        label = stringResource(Res.string.grid_cell_complete_color),
        preference = preferences.gridCellCompleteColor,
        modifier = Modifier.fillMaxWidth(),
      )

      ColorRow(
        label = stringResource(Res.string.grid_all_complete_color),
        preference = preferences.gridCompletionColor,
        modifier = Modifier.fillMaxWidth(),
      )
    }
  }
}

@Composable
private fun ColorRow(
  label: String,
  preference: ColorPreference,
  modifier: Modifier = Modifier,
) {
  val scope = rememberCoroutineScope()

  val currentColor: Color by preference.collectAsState()
  Row(modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
    Box(
      Modifier.width(240.dp).background(currentColor.copy(alpha = 0.5f)).padding(2.dp),
      contentAlignment = Alignment.Center
    ) {
      OutlinedText(
        label,
        outlineColor = MaterialTheme.colorScheme.inverseOnSurface,
        outlineStroke = Stroke(width = 4.0f)
      )
    }

    for (colorToken in ColorToken.entries) {
      val color = colorToken.color
      Surface(
        onClick = {
          scope.launch { preference.save(color) }
        },
        color = color.copy(alpha = 0.5f),
        modifier = Modifier.size(48.dp),
      ) {
        if (color == currentColor) {
          Icon(Icons.Default.CheckCircle, contentDescription = null)
        }
      }
    }
  }
}
