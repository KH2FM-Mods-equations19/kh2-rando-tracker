package com.kh2rando.tracker.model

import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.progress.ProgressCheckpoint

/**
 * Snapshot of the state that can be updated by auto-tracking. Used for comparing the previous state to determine what,
 * if anything, has changed.
 */
class AutoTrackerSnapshot(
  val currentLocation: Location?,
  val soraState: SoraState,
  val driveFormsState: DriveFormsState,
  val growthState: GrowthState,
  val musicState: MusicState,
  val inventory: List<ItemPrototype>,
  val completedProgress: Set<ProgressCheckpoint>,
  val dead: Boolean,
  val emblemCount: Int,
  val inCreations: Boolean,
)
