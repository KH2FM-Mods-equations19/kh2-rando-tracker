package com.kh2rando.tracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.window.WindowState
import com.kh2rando.tracker.model.preferences.DpSizePreference
import com.kh2rando.tracker.model.preferences.WindowPositionPreference
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.seconds

/**
 * Composes a [LaunchedEffect] that will store off the window's size and position to the specified preferences.
 */
@Composable
@OptIn(FlowPreview::class)
fun SaveWindowSizeAndPosition(
  windowState: WindowState,
  sizePreference: DpSizePreference,
  positionPreference: WindowPositionPreference,
) {
  // Debounce each of these so that we're not spamming the save
  LaunchedEffect(windowState) {
    snapshotFlow { windowState.size }
      .debounce(1.seconds)
      .onEach(sizePreference::save)
      .launchIn(this)
    snapshotFlow { windowState.position }
      .debounce(1.seconds)
      .onEach(positionPreference::save)
      .launchIn(this)
  }
}
