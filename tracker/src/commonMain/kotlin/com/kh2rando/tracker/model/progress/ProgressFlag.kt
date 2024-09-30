package com.kh2rando.tracker.model.progress

/**
 * Information that can be used to determine if certain game progress has occurred.
 */
interface ProgressFlag {

  /**
   * Index of the flag within a location's flags. Mostly just used for ordering.
   */
  val index: Int

  /**
   * Name of the flag, such as `EH_SCENARIO_1_OPEN`.
   */
  val flagName: String

  /**
   * Offset of this flag's byte from the "save" address.
   */
  val saveOffset: Int

  /**
   * The mask to use within this flag's byte to determine if the flag is set.
   */
  val mask: Int

}
