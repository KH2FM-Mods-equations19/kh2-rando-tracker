package com.kh2rando.tracker.model.hints

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.UniqueItem
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

/**
 * Collection of rules for how, when, and what information is revealed to the player to help them find their way through
 * the randomized game.
 */
@Serializable
sealed interface HintSystem {

  /**
   * Name to display for this hint system.
   */
  val displayName: StringResource

  /**
   * [BasicProgressionSettings] enabled for this system for this seed, if any.
   *
   * This can be used to distinguish whether or not progression mode is enabled.
   */
  val basicProgressionSettings: BasicProgressionSettings?

  /**
   * Determines if [location] is the correct location where [report] is found.
   *
   * Used to prevent players from brute forcing hints by guessing the locations of [AnsemReport]s.
   */
  fun isValidReportLocation(location: Location, report: AnsemReport): Boolean {
    return true
  }

  /**
   * Returns ths [HintInfo] to display, if any, for an [AnsemReport] that has been acquired.
   */
  fun hintInfoForAcquiredReport(report: AnsemReport): HintInfo? {
    return null
  }

}

/**
 * Verifies that all items in [allLocationItems] are acquired based on [acquiredItems].
 *
 * Compares using the [ItemPrototype]s to avoid needing to match on exact [UniqueItem] semantics.
 */
fun checkAcquiredAllItems(acquiredItems: Set<UniqueItem>, allLocationItems: List<ItemPrototype>): Boolean {
  // If the sizes don't match, can get out quick
  if (acquiredItems.size != allLocationItems.size) {
    return false
  }

  // Collect all the prototypes (into a list to allow for multiple instances)
  val remainingPrototypesToMatch = allLocationItems.mapTo(mutableListOf()) { it }

  // Remove an instance for each acquired item's prototype
  for (acquiredItem in acquiredItems) {
    remainingPrototypesToMatch.remove(acquiredItem.prototype)
  }

  // If there are none remaining to match, we've found everything
  return remainingPrototypesToMatch.isEmpty()
}
