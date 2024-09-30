package com.kh2rando.tracker.model

/**
 * Displayable information about the game's auto-tracking state.
 */
sealed interface AutoTrackingDisplayInfo {

  /**
   * Not currently auto tracking.
   */
  data object Disconnected: AutoTrackingDisplayInfo

  /**
   * Searching for the running game.
   */
  data object Scanning : AutoTrackingDisplayInfo

  /**
   * Connected to the running game.
   */
  class Connected(val gameVersion: String) : AutoTrackingDisplayInfo

  /**
   * Able to connect, but the detected game version is not currently supported.
   */
  data object UnsupportedGameVersion : AutoTrackingDisplayInfo

  /**
   * An error occurred connecting to the game.
   */
  data object GeneralConnectError : AutoTrackingDisplayInfo

}
