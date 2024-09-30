package com.kh2rando.tracker.model.gamestate

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.UniqueItem
import com.kh2rando.tracker.model.progress.ProgressCheckpoint
import com.kh2rando.tracker.ui.UserMarkIcon
import com.kh2rando.tracker.ui.UserProofMark
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.minus
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.plus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Read-only information about the state of a specific [Location].
 */
interface LocationStateApi {

  /**
   * The location represented.
   */
  val location: Location

  /**
   * The items acquired in this location.
   */
  val acquiredItems: StateFlow<ImmutableSet<UniqueItem>>

  /**
   * The [ProgressCheckpoint]s completed in this location.
   */
  val completedProgressCheckpoints: StateFlow<ImmutableSet<ProgressCheckpoint>>

  /**
   * The user mark counts for this location.
   */
  val userMarkCounts: StateFlow<Int>

  /**
   * [UserProofMark]s for this location.
   */
  val userProofMarks: StateFlow<ImmutableSet<UserProofMark>>

  /**
   * A set of items that have been manually rejected from this location by the user.
   *
   * Auto-tracking should not attempt to re-track any of these items to this location anymore.
   */
  val manuallyRejectedItems: Set<ItemPrototype>

}

/**
 * State of a [Location].
 */
class LocationState(override val location: Location) : LocationStateApi {

  private val _acquiredItems: MutableStateFlow<PersistentSet<UniqueItem>> = MutableStateFlow(persistentSetOf())
  override val acquiredItems: StateFlow<ImmutableSet<UniqueItem>>
    get() = _acquiredItems

  private val _completedProgressCheckpoints: MutableStateFlow<PersistentSet<ProgressCheckpoint>> =
    MutableStateFlow(persistentSetOf())
  override val completedProgressCheckpoints: StateFlow<ImmutableSet<ProgressCheckpoint>>
    get() = _completedProgressCheckpoints

  // Just start this value out really high to avoid negatives and modulo division
  private val _userMarks: MutableStateFlow<Int> = MutableStateFlow(UserMarkIcon.entries.size * 1_000)
  override val userMarkCounts: StateFlow<Int>
    get() = _userMarks

  private val _userProofMarks: MutableStateFlow<PersistentSet<UserProofMark>> = MutableStateFlow(persistentSetOf())
  override val userProofMarks: StateFlow<ImmutableSet<UserProofMark>>
    get() = _userProofMarks

  private val _manuallyRejectedItems: MutableSet<ItemPrototype> = mutableSetOf()
  override val manuallyRejectedItems: Set<ItemPrototype>
    get() = _manuallyRejectedItems

  /**
   * Acquires [item] into this location, without any sanity checks.
   */
  fun acquireItem(item: UniqueItem) {
    _acquiredItems.update { previous -> previous + item }
  }

  /**
   * Removes [item] from the acquired items for this location, without any sanity checks.
   */
  fun rejectItemManually(item: UniqueItem) {
    _acquiredItems.update { previous -> previous - item }
    _manuallyRejectedItems.add(item.prototype)
  }

  /**
   * Marks all of the items in [prototypes] as having been manually rejected for this location.
   */
  fun addManualRejections(prototypes: Collection<ItemPrototype>) {
    _manuallyRejectedItems.addAll(prototypes)
  }

  /**
   * Records a progress [checkpoint] for this location.
   */
  fun recordProgress(checkpoint: ProgressCheckpoint) {
    _completedProgressCheckpoints.update { previous -> previous + checkpoint }
  }

  /**
   * Removes a recorded progress [checkpoint] for this location.
   */
  fun removeProgress(checkpoint: ProgressCheckpoint) {
    _completedProgressCheckpoints.update { previous -> previous - checkpoint }
  }

  /**
   * Toggles the [userProofMark] on or off for this location.
   */
  fun toggleUserProofMark(userProofMark: UserProofMark) {
    _userProofMarks.update { previous ->
      if (userProofMark == UserProofMark.NoProofs) {
        if (userProofMark in previous) {
          // If it was just no proof before, now it should be empty
          persistentSetOf()
        } else {
          // Marking no proof should get rid of any marked proofs
          persistentSetOf(userProofMark)
        }
      } else {
        if (userProofMark in previous) {
          // Can just do a simple remove
          previous - userProofMark
        } else {
          // If a proof is now marked, it can't be no proofs
          previous - UserProofMark.NoProofs + userProofMark
        }
      }

    }
  }

  /**
   * Adjusts the user mark count for this location by a [delta].
   */
  fun adjustUserMark(delta: Int) {
    _userMarks.update { previous -> previous + delta }
  }

  /**
   * Sets the user mark count for this location to an exact [value].
   */
  fun setUserMark(value: Int) {
    _userMarks.value = value
  }

  override fun toString(): String {
    return location.toString()
  }

}
