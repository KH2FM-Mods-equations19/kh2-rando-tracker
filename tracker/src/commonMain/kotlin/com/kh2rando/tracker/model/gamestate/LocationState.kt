package com.kh2rando.tracker.model.gamestate

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.item.UniqueItem
import com.kh2rando.tracker.model.progress.ProgressCheckpoint
import com.kh2rando.tracker.ui.UserMarkIcon
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
   * Proofs that the user has marked as being possible for this location.
   */
  val possibleProofs: StateFlow<ImmutableSet<Proof>>

  /**
   * Proofs that the user has marked as being impossible for this location.
   */
  val impossibleProofs: StateFlow<ImmutableSet<Proof>>

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

  private val _possibleProofs: MutableStateFlow<PersistentSet<Proof>> = MutableStateFlow(persistentSetOf())
  override val possibleProofs: StateFlow<ImmutableSet<Proof>>
    get() = _possibleProofs

  private val _impossibleProofs: MutableStateFlow<PersistentSet<Proof>> = MutableStateFlow(persistentSetOf())
  override val impossibleProofs: StateFlow<ImmutableSet<Proof>>
    get() = _impossibleProofs

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
   * Marks [proof] as definitely possible for this location.
   */
  fun markProofPossible(proof: Proof) {
    _possibleProofs.update { previous -> previous + proof }
    _impossibleProofs.update { previous -> previous - proof }
  }

  /**
   * Marks [proof] as definitely impossible for this location.
   */
  fun markProofImpossible(proof: Proof) {
    _possibleProofs.update { previous -> previous - proof }
    _impossibleProofs.update { previous -> previous + proof }
  }

  /**
   * Marks [proof] as unknown possibility for this location.
   */
  fun markProofUnknown(proof: Proof) {
    _possibleProofs.update { previous -> previous - proof }
    _impossibleProofs.update { previous -> previous - proof }
  }

  /**
   * Adjusts the user proof mark for [proof] by a [delta].
   */
  fun adjustUserProofMark(proof: Proof, delta: Int) {
    val possible = _possibleProofs.value
    val impossible = _impossibleProofs.value
    if (delta < 0) {
      when (proof) {
        in possible -> _possibleProofs.update { previous -> previous - proof }
        in impossible -> { /* Nothing */ }
        else -> _impossibleProofs.update { previous -> previous + proof }
      }
    } else if (delta > 0) {
      when (proof) {
        in possible -> { /* Nothing */ }
        in impossible -> _impossibleProofs.update { previous -> previous - proof }
        else -> _possibleProofs.update { previous -> previous + proof }
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
