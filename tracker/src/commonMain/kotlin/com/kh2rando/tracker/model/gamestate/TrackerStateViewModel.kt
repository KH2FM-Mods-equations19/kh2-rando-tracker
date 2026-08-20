package com.kh2rando.tracker.model.gamestate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class TrackerStateViewModel(private val debugMode: Boolean) : ViewModel() {

  /**
   * Current status of loading of the [FullGameState].
   */
  val gameStateState: StateFlow<GameStateState>
    field: MutableStateFlow<GameStateState> = MutableStateFlow(GameStateState.NotLoaded())

  /**
   * How long the most recent auto-tracking scan took.
   */
  val latestAutoTrackerScanTime: StateFlow<Duration>
    field: MutableStateFlow<Duration> = MutableStateFlow(0.milliseconds)

  /**
   * Starts loading the [FullGameState] by delegating the load to [loader]. Marks the [gameStateState] as
   * [GameStateState.Loading] while loading and [GameStateState.Loaded] when done (with or without an error).
   */
  fun startLoadingGameState(loader: GameStateLoader) {
    viewModelScope.launch {
      gameStateState.value = GameStateState.Loading
      try {
        val gameState = loader.loadGameState()
        gameStateState.value = GameStateState.Loaded(gameState)
      } catch (e: Exception) {
        if (e is CancellationException) {
          throw e
        } else {
          gameStateState.value = GameStateState.NotLoaded(e)
        }
      }
    }
  }

  /**
   * Sets the [gameStateState] to [GameStateState.NotLoaded].
   */
  fun resetGameState() {
    gameStateState.value = GameStateState.NotLoaded()
  }

  /**
   * Records the [duration] of the latest auto-tracking scan.
   */
  fun publishAutoTrackerScanTime(duration: Duration) {
    // Is this worth the optimization of skipping it when in debug? Maybe, maybe not.
    if (debugMode) {
      latestAutoTrackerScanTime.value = duration
    }
  }

  fun interface GameStateLoader {

    suspend fun loadGameState(): FullGameState

  }

  /**
   * State of loading the [FullGameState].
   */
  sealed interface GameStateState {

    /**
     * The loaded [FullGameState], if available.
     */
    val gameState: FullGameState?
      get() = null

    /**
     * Game state is not currently loaded, with an optional [error] if there was an attempt to load that failed.
     */
    data class NotLoaded(val error: Exception? = null) : GameStateState

    /**
     * Game state is currently loading.
     */
    data object Loading : GameStateState

    /**
     * Game state is loaded and available.
     */
    data class Loaded(override val gameState: FullGameState) : GameStateState

  }

}
