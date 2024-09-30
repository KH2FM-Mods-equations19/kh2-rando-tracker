package com.kh2rando.tracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.dialog_reset_cancel
import com.kh2rando.tracker.generated.resources.dialog_reset_confirm
import com.kh2rando.tracker.generated.resources.dialog_reset_tracker_question
import com.kh2rando.tracker.generated.resources.state_loading_seed
import com.kh2rando.tracker.model.AutoTrackingDisplayInfo
import com.kh2rando.tracker.model.gamestate.TrackerStateViewModel
import com.kh2rando.tracker.model.preferences.TrackerPreferences
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainWindowContent(
  gameStateState: TrackerStateViewModel.GameStateState,
  preferences: TrackerPreferences,
  autoTrackingDisplayInfo: () -> AutoTrackingDisplayInfo? = { null },
  noSeedContent: @Composable () -> Unit = {},
) {
  Surface(modifier = Modifier.fillMaxSize()) {
    when (gameStateState) {
      is TrackerStateViewModel.GameStateState.NotLoaded -> {
        noSeedContent()
      }

      TrackerStateViewModel.GameStateState.Loading -> {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            CircularProgressIndicator()
            Text(
              stringResource(Res.string.state_loading_seed),
              style = MaterialTheme.typography.headlineLarge,
              fontFamily = khMenuFontFamily(),
            )
          }
        }
      }

      is TrackerStateViewModel.GameStateState.Loaded -> {
        val gameState = gameStateState.gameState

        var showingReportSummary: Boolean by remember { mutableStateOf(false) }
        var showingProgressionSummary: Boolean by remember { mutableStateOf(false) }

        Column(Modifier.fillMaxSize()) {
          LocationsLayout(gameState = gameState, preferences = preferences, modifier = Modifier.weight(1.4f))

          Column(Modifier.fillMaxWidth().weight(1.0f)) {
            HintStatusBar(
              gameState = gameState,
              onShowReportDetails = { showingReportSummary = true },
              onShowProgressionDetails = { showingProgressionSummary = true }
            )
            SoraStatsBar(gameState)
            FormsAndGrowthBar(gameState)
            AvailableItemsLayout(gameState = gameState, modifier = Modifier.weight(1.0f))
            StatusBar(
              seedSettings = gameState.seed.settings,
              autoTrackingDisplayInfo = autoTrackingDisplayInfo
            )
          }
        }

        if (showingReportSummary) {
          HintSummaryDialog(
            hintInfoProvider = { gameState.revealedReportHintSets },
            onDismissRequest = { showingReportSummary = false }
          )
        }
        if (showingProgressionSummary) {
          HintSummaryDialog(
            hintInfoProvider = { gameState.revealedProgressionHintSets },
            onDismissRequest = { showingProgressionSummary = false }
          )
        }
      }
    }
  }
}

@Composable
fun ResetConfirmationDialog(
  onDismiss: (reset: Boolean) -> Unit,
  modifier: Modifier = Modifier,
) {
  AlertDialog(
    onDismissRequest = { onDismiss(false) },
    title = { Text(stringResource(Res.string.dialog_reset_tracker_question)) },
    confirmButton = {
      Button(onClick = { onDismiss(true) }) {
        Text(stringResource(Res.string.dialog_reset_confirm))
      }
    },
    dismissButton = {
      TextButton(onClick = { onDismiss(false) }) {
        Text(stringResource(Res.string.dialog_reset_cancel))
      }
    },
    modifier = modifier,
  )
}
