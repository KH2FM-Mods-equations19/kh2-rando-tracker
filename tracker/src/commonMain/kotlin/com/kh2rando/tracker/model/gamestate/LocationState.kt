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

  override val acquiredItems: StateFlow<ImmutableSet<UniqueItem>>
    field: MutableStateFlow<PersistentSet<UniqueItem>> = MutableStateFlow(persistentSetOf())

  override val completedProgressCheckpoints: StateFlow<ImmutableSet<ProgressCheckpoint>>
    field: MutableStateFlow<PersistentSet<ProgressCheckpoint>> = MutableStateFlow(persistentSetOf())

  // Just start this value out really high to avoid negatives and modulo division
  override val userMarkCounts: StateFlow<Int>
    field: MutableStateFlow<Int> = MutableStateFlow(UserMarkIcon.entries.size * 1_000)

  override val possibleProofs: StateFlow<ImmutableSet<Proof>>
    field: MutableStateFlow<PersistentSet<Proof>> = MutableStateFlow(persistentSetOf())

  override val impossibleProofs: StateFlow<ImmutableSet<Proof>>
    field: MutableStateFlow<PersistentSet<Proof>> = MutableStateFlow(persistentSetOf())

  override val manuallyRejectedItems: Set<ItemPrototype>
    field: MutableSet<ItemPrototype> = mutableSetOf()

  /**
   * Acquires [item] into this location, without any sanity checks.
   */
  fun acquireItem(item: UniqueItem) {
    acquiredItems.update { previous -> previous + item }
  }

  /**
   * Removes [item] from the acquired items for this location, without any sanity checks.
   */
  fun rejectItemManually(item: UniqueItem) {
    acquiredItems.update { previous -> previous - item }
    manuallyRejectedItems.add(item.prototype)
  }

  /**
   * Marks all of the items in [prototypes] as having been manually rejected for this location.
   */
  fun addManualRejections(prototypes: Collection<ItemPrototype>) {
    manuallyRejectedItems.addAll(prototypes)
  }

  /**
   * Records a progress [checkpoint] for this location.
   */
  fun recordProgress(checkpoint: ProgressCheckpoint) {
    completedProgressCheckpoints.update { previous -> previous + checkpoint }
  }

  /**
   * Removes a recorded progress [checkpoint] for this location.
   */
  fun removeProgress(checkpoint: ProgressCheckpoint) {
    completedProgressCheckpoints.update { previous -> previous - checkpoint }
  }

  /**
   * Marks [proof] as definitely possible for this location.
   */
  fun markProofPossible(proof: Proof) {
    possibleProofs.update { previous -> previous + proof }
    impossibleProofs.update { previous -> previous - proof }
  }

  /**
   * Marks [proof] as definitely impossible for this location.
   */
  fun markProofImpossible(proof: Proof) {
    possibleProofs.update { previous -> previous - proof }
    impossibleProofs.update { previous -> previous + proof }
  }

  /**
   * Marks [proof] as unknown possibility for this location.
   */
  fun markProofUnknown(proof: Proof) {
    possibleProofs.update { previous -> previous - proof }
    impossibleProofs.update { previous -> previous - proof }
  }

  /**
   * Adjusts the user proof mark for [proof] by a [delta].
   */
  fun adjustUserProofMark(proof: Proof, delta: Int) {
    val possible = possibleProofs.value
    val impossible = impossibleProofs.value
    if (delta < 0) {
      when (proof) {
        in possible -> possibleProofs.update { previous -> previous - proof }
        in impossible -> { /* Nothing */ }
        else -> impossibleProofs.update { previous -> previous + proof }
      }
    } else if (delta > 0) {
      when (proof) {
        in possible -> { /* Nothing */ }
        in impossible -> impossibleProofs.update { previous -> previous - proof }
        else -> possibleProofs.update { previous -> previous + proof }
      }
    }
  }

  /**
   * Adjusts the user mark count for this location by a [delta].
   */
  fun adjustUserMark(delta: Int) {
    userMarkCounts.update { previous -> previous + delta }
  }

  /**
   * Sets the user mark count for this location to an exact [value].
   */
  fun setUserMark(value: Int) {
    userMarkCounts.value = value
  }

  override fun toString(): String {
    return location.toString()
  }

}
