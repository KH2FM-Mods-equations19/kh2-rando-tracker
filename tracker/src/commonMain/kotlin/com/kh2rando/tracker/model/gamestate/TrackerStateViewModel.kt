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

  private val _gameStateState: MutableStateFlow<GameStateState> = MutableStateFlow(GameStateState.NotLoaded())

  /**
   * Current status of loading of the [FullGameState].
   */
  val gameStateState: StateFlow<GameStateState>
    get() = _gameStateState

  private val _latestAutoTrackerScanTime: MutableStateFlow<Duration> = MutableStateFlow(0.milliseconds)

  /**
   * How long the most recent auto-tracking scan took.
   */
  val latestAutoTrackerScanTime: StateFlow<Duration>
    get() = _latestAutoTrackerScanTime

  /**
   * Starts loading the [FullGameState] by delegating the load to [loader]. Marks the [gameStateState] as
   * [GameStateState.Loading] while loading and [GameStateState.Loaded] when done (with or without an error).
   */
  fun startLoadingGameState(loader: GameStateLoader) {
    viewModelScope.launch {
      _gameStateState.value = GameStateState.Loading
      try {
        val gameState = loader.loadGameState()
        _gameStateState.value = GameStateState.Loaded(gameState)
      } catch (e: Exception) {
        if (e is CancellationException) {
          throw e
        } else {
          _gameStateState.value = GameStateState.NotLoaded(e)
        }
      }
    }
  }

  /**
   * Sets the [gameStateState] to [GameStateState.NotLoaded].
   */
  fun resetGameState() {
    _gameStateState.value = GameStateState.NotLoaded()
  }

  /**
   * Records the [duration] of the latest auto-tracking scan.
   */
  fun publishAutoTrackerScanTime(duration: Duration) {
    // Is this worth the optimization of skipping it when in debug? Maybe, maybe not.
    if (debugMode) {
      _latestAutoTrackerScanTime.value = duration
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
