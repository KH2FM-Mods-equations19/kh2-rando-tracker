package com.kh2rando.tracker

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kh2rando.tracker.auto.AutoTracker
import com.kh2rando.tracker.auto.AutoTrackingState
import com.kh2rando.tracker.auto.GameProcessFinderException
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.app_title
import com.kh2rando.tracker.generated.resources.debug_autotracking_console
import com.kh2rando.tracker.generated.resources.debug_progress_flags_viewer
import com.kh2rando.tracker.generated.resources.tracker_logo
import com.kh2rando.tracker.io.TrackerFileHandler
import com.kh2rando.tracker.model.AutoTrackingDisplayInfo
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.gamestate.FullGameState
import com.kh2rando.tracker.model.gamestate.GameStateFactory
import com.kh2rando.tracker.model.gamestate.TrackerStateViewModel
import com.kh2rando.tracker.model.preferences.TrackerPreferences
import com.kh2rando.tracker.model.preferences.collectAsState
import com.kh2rando.tracker.model.seed.FinalDoorRequirement
import com.kh2rando.tracker.ui.AboutWindow
import com.kh2rando.tracker.ui.AutoTrackingConsoleContent
import com.kh2rando.tracker.ui.ChooseColorsWindow
import com.kh2rando.tracker.ui.CustomIconsWindow
import com.kh2rando.tracker.ui.CustomizableIconRegistry
import com.kh2rando.tracker.ui.DebugMenu
import com.kh2rando.tracker.ui.DropFileType
import com.kh2rando.tracker.ui.ExtendedWindow
import com.kh2rando.tracker.ui.MainWindowContent
import com.kh2rando.tracker.ui.ObjectiveWindow
import com.kh2rando.tracker.ui.OtherMenu
import com.kh2rando.tracker.ui.ProgressFlagsViewerContent
import com.kh2rando.tracker.ui.ProgressionDebugMenu
import com.kh2rando.tracker.ui.ResetConfirmationDialog
import com.kh2rando.tracker.ui.SaveWindowSizeAndPosition
import com.kh2rando.tracker.ui.SeedDropTarget
import com.kh2rando.tracker.ui.SettingsMenu
import com.kh2rando.tracker.ui.TrackerDarkColorScheme
import com.kh2rando.tracker.ui.TrackerMenu
import com.kh2rando.tracker.ui.rememberViewModelStoreOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okio.buffer
import okio.use
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.io.File
import java.io.PrintWriter
import kotlin.time.Duration

fun main(args: Array<String>) {
  val logFile = TrackerFileSystem.logFile

  Thread.setDefaultUncaughtExceptionHandler { _, e ->
    TrackerFileSystem.fileSystem.appendingSink(logFile).use { sink ->
      sink.buffer().use { buffer ->
        buffer.outputStream().use { stream ->
          PrintWriter(stream).use { writer ->
            e.printStackTrace(writer)
          }
        }
      }
    }
  }

  application {
    var trackerVersion = args.firstOrNull() ?: "UNKNOWN"
    if ("debug" in args) {
      debugMode = true
      trackerVersion += "-DEBUG"
    }

    // Note: Be careful adjusting this scope for auto-tracking.
    //       Picking a different scope caused the tracker to crash if the connection to the game was lost.
    val applicationCoroutineScope = rememberCoroutineScope()

    val preferenceStore = TrackerPreferences.createDataStore(
      producePath = { TrackerFileSystem.preferencesFile }
    )
    val preferences = TrackerPreferences(preferenceStore)

    val trackerFileHandler = TrackerFileHandler(ioDispatcher = Dispatchers.IO)
    val gameStateFactory = GameStateFactory(
      preferences = preferences,
      scope = applicationCoroutineScope,
      backgroundDispatcher = Dispatchers.Default,
    )

    val defaultSize = DpSize(width = 580.dp, height = 916.dp)
    val initialPreferences = runBlocking {
      val mainSize = preferences.mainWindowSize.values.first()
      InitialPreferences(
        mainWindowSize = if (mainSize.isSpecified) mainSize else defaultSize,
        mainWindowPosition = preferences.mainWindowPosition.values.first(),
        showExtendedWindow = preferences.showExtendedWindowOnLaunch.values.first(),
      )
    }

    val windowState = rememberWindowState(
      size = initialPreferences.mainWindowSize,
      position = initialPreferences.mainWindowPosition
    )

    val viewModelStoreOwner = rememberViewModelStoreOwner()
    val trackerStateViewModel = viewModel(viewModelStoreOwner) { TrackerStateViewModel(debugMode) }
    var autoTrackingState: AutoTrackingState by remember { mutableStateOf(AutoTrackingState.None) }

    var showingExtendedWindow: Boolean by remember { mutableStateOf(initialPreferences.showExtendedWindow) }
    var showingChooseColorsWindow: Boolean by remember { mutableStateOf(false) }
    var showingCustomIconsWindow: Boolean by remember { mutableStateOf(false) }
    var showingAboutTracker: Boolean by remember { mutableStateOf(false) }
    var showingResetConfirmation: Boolean by remember { mutableStateOf(false) }
    var showingAutoTrackingConsole: Boolean by remember { mutableStateOf(false) }
    var showingProgressFlagsViewer: Location? by remember { mutableStateOf(null) }

    CompositionLocalProvider(
      TrackerPreferences.LocalPreferences provides preferences,
      CustomizableIconRegistry.LocalCustomizableIconRegistry provides CustomizableIconRegistry(),
    ) {
      MaterialTheme(TrackerDarkColorScheme) {
        val trackerLogoIcon = painterResource(Res.drawable.tracker_logo)

        Window(
          state = windowState,
          title = stringResource(Res.string.app_title),
          icon = trackerLogoIcon,
          onCloseRequest = ::exitApplication,
        ) {
          val gameStateState by trackerStateViewModel.gameStateState.collectAsState()
          val gameState = gameStateState.gameState

          MenuBar {
            TrackerMenu(
              gameState = gameState,
              autoTrackingState = autoTrackingState,
              onStartAutoTracking = { gameState ->
                startAutoTracking(
                  gameState = gameState,
                  coroutineScope = applicationCoroutineScope,
                  onTrackingStateChange = { autoTrackingState = it },
                  onScanFinished = { duration ->
                    trackerStateViewModel.publishAutoTrackerScanTime(duration)
                  }
                )
              },
              onShowAboutTracker = { showingAboutTracker = true },
              onShowExtendedWindow = { showingExtendedWindow = true },
              onSaveProgress = { gameState ->
                trackerFileHandler.saveProgressWithPrompt(gameState)
              },
              onResetTracker = { showingResetConfirmation = true },
              onResetWindowSize = { windowState.size = defaultSize }
            )
            SettingsMenu(
              preferences = preferences,
              onShowChooseColorsWindow = { showingChooseColorsWindow = true },
            )
            OtherMenu(
              onShowCustomIconsWindow = { showingCustomIconsWindow = true },
            )
            if (debugMode) {
              DebugMenu(
                onShowAutoTrackingConsole = { showingAutoTrackingConsole = true },
                onShowProgressFlagsViewer = { location -> showingProgressFlagsViewer = location }
              )
              ProgressionDebugMenu(gameState)
            }
          }

          MainWindow(
            gameStateState = gameStateState,
            autoTrackingState = autoTrackingState,
            preferences = preferences,
            onFileDropped = { file, dropFileType ->
              trackerStateViewModel.startLoadingGameState {
                val dropResult = SeedDropTarget.handleDroppedFile(file, dropFileType, trackerFileHandler)
                if (dropResult == null) {
                  throw IllegalStateException("Invalid dropped file")
                } else {
                  gameStateFactory.create(
                    baseGameState = dropResult.baseGameState,
                    previouslyRevealedHints = dropResult.previouslyRevealedHints,
                  )
                }
              }
            },
          )

          if (showingAboutTracker) {
            AboutWindow(version = trackerVersion, onCloseRequest = { showingAboutTracker = false })
          }

          if (showingResetConfirmation) {
            ResetConfirmationDialog(onDismiss = { reset ->
              if (reset) {
                when (val state = autoTrackingState) {
                  AutoTrackingState.None, is AutoTrackingState.Failed -> {

                  }

                  is AutoTrackingState.Scanning -> {
                    state.job.cancel()
                  }

                  is AutoTrackingState.Tracking -> {
                    state.handle.job.cancel()
                  }
                }
                autoTrackingState = AutoTrackingState.None
                trackerStateViewModel.resetGameState()
              }

              showingResetConfirmation = false
            })
          }

          if (showingExtendedWindow) {
            ExtendedWindow(
              gameState = gameState,
              preferences = preferences,
              onCloseRequest = { showingExtendedWindow = false }
            )
          }

          if (showingChooseColorsWindow) {
            ChooseColorsWindow(
              preferences = preferences,
              onCloseRequest = { showingChooseColorsWindow = false },
            )
          }

          if (showingCustomIconsWindow) {
            CustomIconsWindow(
              onCloseRequest = { showingCustomIconsWindow = false }
            )
          }

          if (gameState?.seed?.settings?.finalDoorRequirement is FinalDoorRequirement.Objectives) {
            ObjectiveWindow(
              gameState = gameState,
              preferences = preferences,
              onCloseRequest = { }
            )
          }

          if (showingProgressFlagsViewer != null) {
            Window(
              title = stringResource(Res.string.debug_progress_flags_viewer),
              icon = trackerLogoIcon,
              onCloseRequest = { showingProgressFlagsViewer = null }
            ) {
              ProgressFlagsViewerContent(showingProgressFlagsViewer)
            }
          }

          if (showingAutoTrackingConsole) {
            Window(
              title = stringResource(Res.string.debug_autotracking_console),
              icon = trackerLogoIcon,
              onCloseRequest = { showingAutoTrackingConsole = false }
            ) {
              AutoTrackingConsoleContent(trackerStateViewModel.latestAutoTrackerScanTime)
            }
          }

          val autoTrackingAutoStart by preferences.autoTrackingAutoStart.collectAsState()
          LaunchedEffect(gameState, autoTrackingAutoStart) {
            if (gameState != null && autoTrackingAutoStart) {
              startAutoTracking(
                gameState = gameState,
                coroutineScope = applicationCoroutineScope,
                onTrackingStateChange = { autoTrackingState = it },
                onScanFinished = { duration ->
                  trackerStateViewModel.publishAutoTrackerScanTime(duration)
                }
              )
            }
          }

          val autoSaveProgress by preferences.autoSaveTrackerProgress.collectAsState()
          LaunchedEffect(gameState, autoSaveProgress) {
            if (gameState != null && autoSaveProgress) {
              trackerFileHandler.launchAutoSaver(scope = this, gameState = gameState)
            }
          }

          SaveWindowSizeAndPosition(
            windowState = windowState,
            sizePreference = preferences.mainWindowSize,
            positionPreference = preferences.mainWindowPosition,
          )
        }
      }
    }
  }
}

@Composable
private fun MainWindow(
  gameStateState: TrackerStateViewModel.GameStateState,
  autoTrackingState: AutoTrackingState,
  preferences: TrackerPreferences,
  onFileDropped: (File, DropFileType) -> Unit,
) {
  MainWindowContent(
    gameStateState = gameStateState,
    preferences = preferences,
    autoTrackingDisplayInfo = {
      when (autoTrackingState) {
        AutoTrackingState.None -> AutoTrackingDisplayInfo.Disconnected
        is AutoTrackingState.Scanning -> AutoTrackingDisplayInfo.Scanning
        is AutoTrackingState.Tracking -> autoTrackingState.handle.displayInfo
        is AutoTrackingState.Failed -> {
          if (autoTrackingState.error is GameProcessFinderException.UnsupportedGame) {
            AutoTrackingDisplayInfo.UnsupportedGameVersion
          } else {
            AutoTrackingDisplayInfo.GeneralConnectError
          }
        }
      }
    },
    noSeedContent = {
      SeedDropTarget(
        onFileDropped = onFileDropped,
      )
    }
  )
}

private fun startAutoTracking(
  gameState: FullGameState,
  coroutineScope: CoroutineScope,
  onTrackingStateChange: (AutoTrackingState) -> Unit,
  onScanFinished: (Duration) -> Unit,
) {
  val job = AutoTracker(gameState, onScanFinished = onScanFinished).start(
    coroutineScope = coroutineScope,
    onAutoTrackingStateChange = { trackingState ->
      if (trackingState is AutoTrackingState.Tracking) {
        trackingState.handle.job.invokeOnCompletion {
          log { "Lost connection with the game" }
          onTrackingStateChange(AutoTrackingState.None)
        }
      }
      onTrackingStateChange(trackingState)
    },
  )
  job.invokeOnCompletion { error ->
    if (error != null) {
      onTrackingStateChange(AutoTrackingState.Failed(error))
    }
  }
}

private class InitialPreferences(
  val mainWindowSize: DpSize,
  val mainWindowPosition: WindowPosition,
  val showExtendedWindow: Boolean,
)
