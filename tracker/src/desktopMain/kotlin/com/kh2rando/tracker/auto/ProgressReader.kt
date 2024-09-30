package com.kh2rando.tracker.auto

import androidx.compose.ui.util.fastForEach
import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.DriveFormsState
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.SoraState
import com.kh2rando.tracker.model.item.DriveForm
import com.kh2rando.tracker.model.progress.CavernOfRemembranceProgress
import com.kh2rando.tracker.model.progress.DriveFormProgress
import com.kh2rando.tracker.model.progress.HundredAcreWoodProgress
import com.kh2rando.tracker.model.progress.ProgressCheckpoint
import com.kh2rando.tracker.model.progress.ProgressFlag
import com.kh2rando.tracker.model.progress.SoraLevelProgress
import com.kh2rando.tracker.model.progress.SoraLevelProgress.Level10
import com.kh2rando.tracker.model.progress.SoraLevelProgress.Level20
import com.kh2rando.tracker.model.progress.SoraLevelProgress.Level30
import com.kh2rando.tracker.model.progress.SoraLevelProgress.Level40
import com.kh2rando.tracker.model.progress.SoraLevelProgress.Level50
import com.kh2rando.tracker.model.progress.progressCheckpoints

/**
 * Reads and interprets progress in each game location.
 */
class ProgressReader(private val gameProcess: GameProcess) {

  private val save = gameProcess.addresses.save

  private val checkpointFlagsByAddress: Map<Address, List<Pair<ProgressCheckpoint, ProgressFlag>>> = run {
    val result = mutableMapOf<Address, MutableList<Pair<ProgressCheckpoint, ProgressFlag>>>()
    Location.entries.forEach { location ->
      for (checkpoint in location.progressCheckpoints) {
        val flag = checkpoint.associatedFlag ?: continue
        val address = save + flag.saveOffset
        result.getOrPut(address) { mutableListOf() }.add(checkpoint to flag)
      }
    }
    result
  }

  private val corLastChestsAddress = save + 0x23DE

  /**
   * Reads and interprets progress in each game location.
   *
   * Uses [soraState] and [driveFormsState] to interpret progress for [Location.SoraLevels] and [Location.DriveForms]
   * respectively.
   *
   * Uses [locationState] to interpret progress that is difficult to otherwise determine by saved progress flags alone.
   */
  fun readProgress(
    locationState: LocationReader.LocationState,
    soraState: SoraState,
    driveFormsState: DriveFormsState,
  ): Set<ProgressCheckpoint> {
    val result = mutableSetOf<ProgressCheckpoint>()
    for ((address, checkpoints) in checkpointFlagsByAddress) {
      val byte = gameProcess.readByteAsInt(address)
      checkpoints.fastForEach { (checkpoint, flag) ->
        if (byte.isSetByMask(flag.mask)) {
          result.add(checkpoint)
        }
      }
    }
    populateLevelProgress(soraState.currentLevel, result)
    populateDriveFormProgress(driveFormsState, result)
    updateAdditionalProgress(locationState, result)
    return result
  }

  /**
   * Reads and returns the raw flag values for each of [progressFlags].
   */
  fun readRawFlags(progressFlags: List<ProgressFlag>): List<Pair<ProgressFlag, Boolean>> {
    return progressFlags.map { flag ->
      val byte = gameProcess.readByteAsInt(save + flag.saveOffset)
      val value = byte.isSetByMask(flag.mask)
      flag to value
    }
  }

  private fun updateAdditionalProgress(
    locationState: LocationReader.LocationState,
    progress: MutableSet<ProgressCheckpoint>,
  ) {
    val location = locationState.location
    if (location == Location.HollowBastion) {
      // No progress flag for last chest of CoR so we read whether or not the chest was opened
      val lastChestsByte = gameProcess.readByteAsInt(corLastChestsAddress)
      if (lastChestsByte.isSetByMask(0x08)) {
        progress.add(CavernOfRemembranceProgress.LastChest)
      }
    } else if (location == Location.HundredAcreWood) {
      // No progress flag for Pooh's Howse so we just try to notice when we walked in the door
      if (locationState.roomId == HundredAcreWoodIds.PoohBearsHowse) {
        progress.add(HundredAcreWoodProgress.Chests)
      }
    }
  }

  companion object {

    // We do a lot of up front work so that we can do quick list lookups by index when performance matters
    private val soraLevelProgressIndex = buildSoraLevelProgressIndex()
    private val valorProgressIndex = buildDriveFormProgressIndex(DriveForm.ValorFormDummy)
    private val wisdomProgressIndex = buildDriveFormProgressIndex(DriveForm.WisdomForm)
    private val limitProgressIndex = buildDriveFormProgressIndex(DriveForm.LimitForm)
    private val masterProgressIndex = buildDriveFormProgressIndex(DriveForm.MasterForm)
    private val finalProgressIndex = buildDriveFormProgressIndex(DriveForm.FinalFormDummy)

    fun populateLevelProgress(currentLevel: Int, progress: MutableSet<ProgressCheckpoint>) {
      progress.addAll(soraLevelProgressIndex.getOrEmpty(currentLevel))
    }

    fun populateDriveFormProgress(driveFormsState: DriveFormsState, progress: MutableSet<ProgressCheckpoint>) {
      progress.addAll(valorProgressIndex.getOrEmpty(driveFormsState.valorLevel))
      progress.addAll(wisdomProgressIndex.getOrEmpty(driveFormsState.wisdomLevel))
      progress.addAll(limitProgressIndex.getOrEmpty(driveFormsState.limitLevel))
      progress.addAll(masterProgressIndex.getOrEmpty(driveFormsState.masterLevel))
      progress.addAll(finalProgressIndex.getOrEmpty(driveFormsState.finalLevel))
    }

    @Suppress("NOTHING_TO_INLINE") // Trying to squeeze performance
    private inline fun <T> List<List<T>>.getOrEmpty(index: Int): List<T> {
      return getOrElse(index) { emptyList() }
    }

    private fun buildSoraLevelProgressIndex(): List<List<SoraLevelProgress>> {
      val ten = listOf(Level10)
      val twenty = ten + Level20
      val thirty = twenty + Level30
      val forty = thirty + Level40
      val fifty = forty + Level50
      val result = mutableListOf<List<SoraLevelProgress>>()
      // 0-9
      repeat(10) { result.add(emptyList()) }
      // 10-19
      repeat(10) { result.add(ten) }
      // 20-29
      repeat(10) { result.add(twenty) }
      // 30-39
      repeat(10) { result.add(thirty) }
      // 40-49
      repeat(10) { result.add(forty) }
      // 50-99
      repeat(50) { result.add(fifty) }
      return result
    }

    private fun buildDriveFormProgressIndex(driveForm: DriveForm): List<List<DriveFormProgress>> {
      val checkpoints = DriveFormProgress.entries.filter { it.driveForm == driveForm }
      val level2 = listOf(checkpoints[0])
      val level3 = level2 + checkpoints[1]
      val level4 = level3 + checkpoints[2]
      val level5 = level4 + checkpoints[3]
      val level6 = level5 + checkpoints[4]
      val level7 = level6 + checkpoints[5]
      return listOf(emptyList(), emptyList(), level2, level3, level4, level5, level6, level7)
    }

  }

}
