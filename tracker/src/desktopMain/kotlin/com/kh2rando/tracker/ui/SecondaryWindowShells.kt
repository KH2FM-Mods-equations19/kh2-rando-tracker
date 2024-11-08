package com.kh2rando.tracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.desc_objectives
import com.kh2rando.tracker.generated.resources.extended_settings_show_on_startup
import com.kh2rando.tracker.generated.resources.extended_settings_show_song_information
import com.kh2rando.tracker.generated.resources.extended_settings_song_folder_as_group
import com.kh2rando.tracker.generated.resources.extended_window_title
import com.kh2rando.tracker.generated.resources.menu_about_tracker
import com.kh2rando.tracker.generated.resources.menu_settings
import com.kh2rando.tracker.generated.resources.tracker_logo
import com.kh2rando.tracker.model.gamestate.BaseGameStateUpdateApi
import com.kh2rando.tracker.model.gamestate.FullGameState
import com.kh2rando.tracker.model.preferences.TrackerPreferences
import com.kh2rando.tracker.model.preferences.collectAsState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AboutWindow(
  version: String,
  onCloseRequest: () -> Unit,
) {
  val windowState = rememberWindowState(position = WindowPosition(Alignment.Center))
  Window(
    state = windowState,
    onCloseRequest = onCloseRequest,
    icon = painterResource(Res.drawable.tracker_logo),
    title = stringResource(Res.string.menu_about_tracker),
  ) {
    AboutWindowContent(version = version)
  }
}

@Composable
fun ExtendedWindow(
  gameState: FullGameState?,
  preferences: TrackerPreferences,
  onCloseRequest: () -> Unit,
) {
  val sizePreference = preferences.extendedWindowSize
  val positionPreference = preferences.extendedWindowPosition
  val (size, position) = runBlocking {
    sizePreference.values.first() to positionPreference.values.first()
  }
  val windowState = rememberWindowState(
    size = if (size.isSpecified) size else DpSize(width = 800.dp, height = 600.dp),
    position = position,
  )

  Window(
    state = windowState,
    onCloseRequest = onCloseRequest,
    icon = painterResource(Res.drawable.tracker_logo),
    title = stringResource(Res.string.extended_window_title)
  ) {
    MenuBar {
      Menu(stringResource(Res.string.menu_settings)) {
        val scope = rememberCoroutineScope()
        val showOnLaunch by preferences.showExtendedWindowOnLaunch.collectAsState()
        val showSongInfo by preferences.showSongInfoExtendedWindow.collectAsState()
        val songFolderAsGroup by preferences.songFolderAsGroup.collectAsState()

        CheckboxItem(
          text = stringResource(Res.string.extended_settings_show_on_startup),
          checked = showOnLaunch,
          onCheckedChange = { newValue ->
            scope.launch { preferences.showExtendedWindowOnLaunch.save(newValue) }
          }
        )

        Separator()

        CheckboxItem(
          text = stringResource(Res.string.extended_settings_show_song_information),
          checked = showSongInfo,
          onCheckedChange = { newValue ->
            scope.launch { preferences.showSongInfoExtendedWindow.save(newValue) }
          }
        )

        CheckboxItem(
          text = stringResource(Res.string.extended_settings_song_folder_as_group),
          checked = songFolderAsGroup,
          onCheckedChange = { newValue ->
            scope.launch { preferences.songFolderAsGroup.save(newValue) }
          }
        )
      }
    }

    ExtendedWindowContent(gameState, preferences)

    SaveWindowSizeAndPosition(windowState, sizePreference, positionPreference)
  }
}

@Composable
fun ObjectiveWindow(
  gameState: BaseGameStateUpdateApi?,
  preferences: TrackerPreferences,
  onCloseRequest: () -> Unit,
) {
  val sizePreference = preferences.objectiveWindowSize
  val positionPreference = preferences.objectiveWindowPosition
  val (size, position) = runBlocking {
    sizePreference.values.first() to positionPreference.values.first()
  }
  val windowState = rememberWindowState(
    size = if (size.isSpecified) size else DpSize(width = 800.dp, height = 600.dp),
    position = position,
  )

  Window(
    state = windowState,
    onCloseRequest = onCloseRequest,
    icon = painterResource(Res.drawable.tracker_logo),
    title = stringResource(Res.string.desc_objectives)
  ) {
    ObjectiveWindowContent(gameState)

    SaveWindowSizeAndPosition(windowState, sizePreference, positionPreference)
  }
}
