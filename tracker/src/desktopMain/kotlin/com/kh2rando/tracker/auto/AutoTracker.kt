package com.kh2rando.tracker.auto

import com.kh2rando.tracker.model.AutoTrackerSnapshot
import com.kh2rando.tracker.model.AutoTrackingDisplayInfo
import com.kh2rando.tracker.model.gamestate.FullGameState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

/**
 * Current state of auto-tracking.
 */
sealed interface AutoTrackingState {

  /**
   * Not currently auto-tracking.
   */
  data object None : AutoTrackingState

  /**
   * Currently searching for the game.
   */
  class Scanning(val job: Job) : AutoTrackingState

  /**
   * Auto-tracking is happening.
   */
  class Tracking(val handle: AutoTrackingHandle) : AutoTrackingState

  /**
   * Auto-tracking has failed.
   */
  class Failed(val error: Throwable) : AutoTrackingState

}

/**
 * A handle to the currently connected game process as well as other relevant information.
 */
class AutoTrackingHandle(
  val gameProcess: GameProcess,
  val displayInfo: AutoTrackingDisplayInfo,
  val job: Job,
)

class AutoTracker(
  private val gameState: FullGameState,
  private val pollInterval: Duration = 200.milliseconds,
  private val onScanFinished: (Duration) -> Unit,
) {

  private val gameProcessFinder = GameProcessFinder()

  /**
   * Starts a coroutine in the [coroutineScope] that will repeatedly attempt to connect to the game and start
   * auto-tracking until successful. Returns a [Job] as a handle to the running task.
   */
  fun start(coroutineScope: CoroutineScope, onAutoTrackingStateChange: (AutoTrackingState) -> Unit): Job {
    return coroutineScope.launch(SupervisorJob()) {
      onAutoTrackingStateChange(AutoTrackingState.Scanning(coroutineContext.job))

      while (true) {
        delay(1.seconds)

        val gameProcess = try {
          gameProcessFinder.findGame()
        } catch (e: Exception) {
          when (e) {
            is CancellationException -> {
              // Cooperate with cancellation
              throw e
            }

            is GameProcessFinderException.UnsupportedGame -> {
              // If it's an unsupported game version, that's not worth retrying for.
              // Just report the error and get out.
              throw e
            }

            else -> null
          }
        }

        if (gameProcess != null) {
          // TODO: Do we need to delay for a little while?
          //       There's always been chatter about not starting auto tracking too early.
          val handle = launchAutoTracking(gameProcess, coroutineScope + SupervisorJob())
          onAutoTrackingStateChange(AutoTrackingState.Tracking(handle))
          break
        }
      }
    }
  }

  /**
   * Launches a coroutine that will continually read the game memory and update the tracker's [FullGameState] based on
   * the observed [GameProcess] state. Returns an [AutoTrackingHandle] to give the caller access to the running task.
   */
  private fun launchAutoTracking(gameProcess: GameProcess, coroutineScope: CoroutineScope): AutoTrackingHandle {
    val locationReader = LocationReader(gameProcess)
    val soraStateReader = SoraStateReader(gameProcess)
    val driveFormsStateReader = DriveFormsStateReader(gameProcess)
    val growthStateReader = GrowthStateReader(gameProcess)
    val musicReader = MusicReader(gameProcess, musicReplacements = gameState.seed.musicReplacements)
    val inventoryReader = InventoryReader(gameProcess, trackableItems = gameState.seed.settings.trackableItems)
    val progressReader = ProgressReader(gameProcess)
    val deathReader = DeathReader(gameProcess)
    val emblemsReader = EmblemsReader(gameProcess)

    val job = coroutineScope.launch {
      while (true) {
        // First we delay for a little bit. This is also how we'll cooperate with cancellation.
        delay(pollInterval)

        val time = measureTime {
          // Take a snapshot of the tracker's known game state.
          // We'll use this to determine what's changed.
          val previousSnapshot = gameState.takeAutoTrackerSnapshot()

          val newSnapshot = withContext(Dispatchers.Default) {
            val locationState = locationReader.readLocationState()
            val soraState = soraStateReader.readSoraState()
            val inventory = inventoryReader.readInventory()
            val driveFormsState = driveFormsStateReader.readDriveFormsState(inventory)
            AutoTrackerSnapshot(
              currentLocation = locationState.location,
              soraState = soraState,
              driveFormsState = driveFormsState,
              growthState = growthStateReader.readGrowthState(),
              musicState = musicReader.readMusicState(locationState),
              inventory = inventory,
              completedProgress = progressReader.readProgress(locationState, soraState, driveFormsState),
              dead = deathReader.isSoraDead(),
              emblemCount = emblemsReader.readEmblemCount(),
              inCreations = locationReader.inCreationsMenu(),
            )
          }

          // Update game state now back on the main thread
          gameState.applyAutoTrackerSnapshot(previousSnapshot, newSnapshot)
        }

        onScanFinished(time)
      }
    }

    return AutoTrackingHandle(
      gameProcess = gameProcess,
      displayInfo = AutoTrackingDisplayInfo.Connected(gameVersion = gameProcess.addresses.toString()),
      job = job
    )
  }

}
