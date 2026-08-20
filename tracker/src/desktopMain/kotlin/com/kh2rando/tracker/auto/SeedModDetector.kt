package com.kh2rando.tracker.auto

import androidx.compose.runtime.Immutable
import com.kh2rando.tracker.io.TrackerFileHandler
import com.kh2rando.tracker.model.preferences.TrackerPreferences
import com.kh2rando.tracker.model.seed.RandomizerSeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Responsible for trying to detect randomizer seeds in OpenKH Mods Manager.
 */
class SeedModDetector(
  preferences: TrackerPreferences,
  private val fileHandler: TrackerFileHandler,
) {

  /**
   * Flow representing detection states. Only re-emits if the configured Mods Manager location changes.
   */
  val states: Flow<State> = preferences.modsManagerLocation.values
    .distinctUntilChanged()
    .map { modsManagerLocation ->
      if (modsManagerLocation == null) {
        State.ModsManagerNotFound
      } else {
        fileHandler.findSeedInModsManager(modsManagerLocation.toFile())
      }
    }

  /**
   * State of attempting to automatically detect a seed in Mods Manager.
   */
  @Immutable
  sealed interface State {

    /**
     * Could not find Mods Manager itself (or it was otherwise improperly configured).
     */
    @Immutable
    data object ModsManagerNotFound : State

    /**
     * In the process of trying to detect a seed.
     */
    @Immutable
    data object DetectingSeed : State

    /**
     * No single seed was found (may mean zero or multiple). May also indicate an error parsing a seed.
     */
    @Immutable
    data object SeedNotFound : State

    /**
     * A single seed was found and parsed.
     */
    @Immutable
    data class FoundSeed(val seed: RandomizerSeed) : State

  }

}
