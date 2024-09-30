package com.kh2rando.tracker.model

/**
 * State to show for a location's counter in the tracker UI.
 */
sealed interface LocationCounterState {

  /**
   * Display no counter information.
   */
  data object None : LocationCounterState

  /**
   * Display an indicator that this location's counter information has not yet been revealed.
   */
  data object Unrevealed : LocationCounterState

  /**
   * Display an indicator that this location has been completed for purposes of the counter.
   */
  data object Completed : LocationCounterState

  /**
   * Displays an indicator that this location's count has been revealed and is [value]. Additionally indicates if the
   * value has been adjusted by revealed items in the location, so that the UI can display the counter differently if
   * needed.
   */
  data class Revealed(val value: Int, val adjustedByRevealedItems: Boolean = false) : LocationCounterState

}
