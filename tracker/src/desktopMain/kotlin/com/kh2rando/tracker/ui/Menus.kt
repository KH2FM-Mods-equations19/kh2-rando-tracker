package com.kh2rando.tracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.MenuBarScope
import com.kh2rando.tracker.auto.AutoTrackingState
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.custom_icons_open_folder
import com.kh2rando.tracker.generated.resources.custom_icons_title
import com.kh2rando.tracker.generated.resources.debug_autotracking_console
import com.kh2rando.tracker.generated.resources.debug_menu
import com.kh2rando.tracker.generated.resources.debug_progress_flags_viewer
import com.kh2rando.tracker.generated.resources.debug_progression_menu
import com.kh2rando.tracker.generated.resources.menu_about_tracker
import com.kh2rando.tracker.generated.resources.menu_auto_math
import com.kh2rando.tracker.generated.resources.menu_auto_save_progress
import com.kh2rando.tracker.generated.resources.menu_auto_tracking_auto_start
import com.kh2rando.tracker.generated.resources.menu_extended_window
import com.kh2rando.tracker.generated.resources.menu_layout_classic
import com.kh2rando.tracker.generated.resources.menu_layout_goa
import com.kh2rando.tracker.generated.resources.menu_layout_vanilla
import com.kh2rando.tracker.generated.resources.menu_location_layout
import com.kh2rando.tracker.generated.resources.menu_other
import com.kh2rando.tracker.generated.resources.menu_reset_tracker
import com.kh2rando.tracker.generated.resources.menu_reset_window_size
import com.kh2rando.tracker.generated.resources.menu_save_progress
import com.kh2rando.tracker.generated.resources.menu_settings
import com.kh2rando.tracker.generated.resources.menu_start_auto_tracking
import com.kh2rando.tracker.generated.resources.menu_tracker
import com.kh2rando.tracker.generated.resources.menu_use_custom_images
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationLayout
import com.kh2rando.tracker.model.gamestate.FullGameState
import com.kh2rando.tracker.model.preferences.TrackerPreferences
import com.kh2rando.tracker.model.preferences.collectAsState
import com.kh2rando.tracker.model.progress.progressCheckpoints
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import java.awt.Desktop

@Composable
fun MenuBarScope.TrackerMenu(
  gameState: FullGameState?,
  autoTrackingState: AutoTrackingState,
  onShowAboutTracker: () -> Unit,
  onStartAutoTracking: (FullGameState) -> Unit,
  onShowExtendedWindow: () -> Unit,
  onSaveProgress: (FullGameState) -> Unit,
  onResetTracker: () -> Unit,
  onResetWindowSize: () -> Unit,
) {
  Menu(stringResource(Res.string.menu_tracker)) {
    Item(stringResource(Res.string.menu_about_tracker)) {
      onShowAboutTracker()
    }

    if (gameState != null) {
      Item(
        stringResource(Res.string.menu_start_auto_tracking),
        enabled = autoTrackingState is AutoTrackingState.None || autoTrackingState is AutoTrackingState.Failed
      ) {
        onStartAutoTracking(gameState)
      }

      Separator()

      Item(stringResource(Res.string.menu_save_progress)) {
        onSaveProgress(gameState)
      }

      Item(stringResource(Res.string.menu_reset_tracker)) { onResetTracker() }
    }

    Separator()

    Item(stringResource(Res.string.menu_extended_window)) {
      onShowExtendedWindow()
    }

    Item(stringResource(Res.string.menu_reset_window_size)) { onResetWindowSize() }
  }
}

@Composable
fun MenuBarScope.SettingsMenu(preferences: TrackerPreferences) {
  val scope = rememberCoroutineScope()

  Menu(stringResource(Res.string.menu_settings)) {
    Menu(stringResource(Res.string.menu_location_layout)) {
      val trackerLayout by preferences.locationLayout.collectAsState()
      RadioButtonItem(
        text = stringResource(Res.string.menu_layout_classic),
        selected = trackerLayout == LocationLayout.Classic,
        onClick = {
          scope.launch { preferences.locationLayout.save(LocationLayout.Classic) }
        }
      )

      RadioButtonItem(
        text = stringResource(Res.string.menu_layout_vanilla),
        selected = trackerLayout == LocationLayout.Vanilla,
        onClick = {
          scope.launch { preferences.locationLayout.save(LocationLayout.Vanilla) }
        }
      )

      RadioButtonItem(
        text = stringResource(Res.string.menu_layout_goa),
        selected = trackerLayout == LocationLayout.GardenOfAssemblage,
        onClick = {
          scope.launch { preferences.locationLayout.save(LocationLayout.GardenOfAssemblage) }
        }
      )
    }

    val autoTrackingAutoStart by preferences.autoTrackingAutoStart.collectAsState()
    CheckboxItem(
      stringResource(Res.string.menu_auto_tracking_auto_start),
      checked = autoTrackingAutoStart,
      onCheckedChange = { newValue ->
        scope.launch { preferences.autoTrackingAutoStart.save(newValue) }
      }
    )

    val autoSaveProgress by preferences.autoSaveTrackerProgress.collectAsState()
    CheckboxItem(
      stringResource(Res.string.menu_auto_save_progress),
      checked = autoSaveProgress,
      onCheckedChange = { newValue ->
        scope.launch { preferences.autoSaveTrackerProgress.save(newValue) }
      }
    )

    val useCustomImages by preferences.useCustomImages.collectAsState()
    CheckboxItem(
      text = stringResource(Res.string.menu_use_custom_images),
      checked = useCustomImages,
      onCheckedChange = { newValue ->
        scope.launch {
          preferences.useCustomImages.save(newValue)
        }
      }
    )

    val pointsAutoMath by preferences.pointsAutoMath.collectAsState()
    CheckboxItem(
      text = stringResource(Res.string.menu_auto_math),
      checked = pointsAutoMath,
      onCheckedChange = { newValue ->
        scope.launch {
          preferences.pointsAutoMath.save(newValue)
        }
      }
    )
  }
}

@Composable
fun MenuBarScope.OtherMenu(onShowCustomIconsWindow: () -> Unit) {
  Menu(stringResource(Res.string.menu_other)) {
    Item(stringResource(Res.string.custom_icons_title)) {
      onShowCustomIconsWindow()
    }
    if (Desktop.isDesktopSupported()) {
      Item(stringResource(Res.string.custom_icons_open_folder)) {
        val desktop = Desktop.getDesktop()
        if (desktop.isSupported(Desktop.Action.OPEN)) {
          desktop.open(CustomizableIconRegistry.customImagesPath.toFile())
        }
      }
    }
  }
}

@Composable
fun MenuBarScope.DebugMenu(
  onShowAutoTrackingConsole: () -> Unit,
  onShowProgressFlagsViewer: (Location) -> Unit,
) {
  Menu(stringResource(Res.string.debug_menu)) {
    Item(stringResource(Res.string.debug_autotracking_console)) {
      onShowAutoTrackingConsole()
    }
    Menu(stringResource(Res.string.debug_progress_flags_viewer)) {
      Location.byDisplayName().forEach { (displayName, location) ->
        Item(displayName) { onShowProgressFlagsViewer(location) }
      }
    }
  }
}

@Composable
fun MenuBarScope.ProgressionDebugMenu(gameState: FullGameState?) {
  if (gameState == null) {
    return
  }
  Menu(stringResource(Res.string.debug_progression_menu)) {
    Location.byDisplayName().forEach { (displayName, location) ->
      Menu(displayName) {
        val completed by gameState.stateForLocation(location).completedProgressCheckpoints.collectAsState()
        for (checkpoint in location.progressCheckpoints) {
          CheckboxItem(
            stringResource(checkpoint.displayString),
            checked = checkpoint in completed,
            onCheckedChange = { checked ->
              if (checked) {
                gameState.recordProgress(checkpoint)
              } else {
                gameState.removeProgress(checkpoint)
              }
            }
          )
        }
      }
    }
  }
}
