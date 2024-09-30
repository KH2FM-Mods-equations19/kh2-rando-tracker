package com.kh2rando.tracker.auto

import androidx.compose.ui.util.fastForEachIndexed
import com.kh2rando.tracker.model.DriveFormsState
import com.kh2rando.tracker.model.item.DriveForm
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.RealForm

/**
 * Reads levels and acquired state for all of the Drive Forms.
 */
class DriveFormsStateReader(private val gameProcess: GameProcess) {

  private val addresses = gameProcess.addresses
  private val currentDriveGaugeAddress = addresses.slot1 + 0x1B1
  private val maximumDriveGaugeAddress = addresses.slot1 + 0x1B2
  private val valorLevelAddress = DriveForm.ValorFormDummy.formLevelAddress(addresses)
  private val wisdomLevelAddress = DriveForm.WisdomForm.formLevelAddress(addresses)
  private val limitLevelAddress = DriveForm.LimitForm.formLevelAddress(addresses)
  private val masterLevelAddress = DriveForm.MasterForm.formLevelAddress(addresses)
  private val finalLevelAddress = DriveForm.FinalFormDummy.formLevelAddress(addresses)

  /**
   * Reads the [DriveFormsState] from memory. Uses the [acquiredInventory] to determine acquisition status of each form.
   *
   * Removes [RealForm.Final] from [acquiredInventory] if it was there.
   */
  fun readDriveFormsState(acquiredInventory: MutableList<ItemPrototype>): DriveFormsState {
    var valorAcquired = false
    var wisdomAcquired = false
    var limitAcquired = false
    var masterAcquired = false

    var realFinalFormIndex = -1

    // Final Form Dummy will be acquired _only_ if we found the dummy item.
    // We leave this in the list so that it can be tracked.
    var hasDummyFinalForm = false

    // We're very careful to loop the inventory exactly once here
    acquiredInventory.fastForEachIndexed { index, item ->
      when (item) {
        DriveForm.ValorFormDummy -> valorAcquired = true
        DriveForm.WisdomForm -> wisdomAcquired = true
        DriveForm.LimitForm -> limitAcquired = true
        DriveForm.MasterForm -> masterAcquired = true
        DriveForm.FinalFormDummy -> hasDummyFinalForm = true
        RealForm.Final -> realFinalFormIndex = index
        else -> {}
      }
    }

    // Real Final Form will be acquired if we forced _or_ found the dummy item.
    val hasRealFinalForm = realFinalFormIndex != -1
    if (hasRealFinalForm) {
      // We remove it from the list since we're not actually tracking real final in the tracker.
      acquiredInventory.removeAt(realFinalFormIndex)
    }

    return DriveFormsState(
      currentDriveGauge = gameProcess.readByteAsInt(currentDriveGaugeAddress),
      maximumDriveGauge = gameProcess.readByteAsInt(maximumDriveGaugeAddress),
      valorLevel = gameProcess.readByteAsInt(valorLevelAddress),
      valorAcquired = valorAcquired,
      wisdomLevel = gameProcess.readByteAsInt(wisdomLevelAddress),
      wisdomAcquired = wisdomAcquired,
      limitLevel = gameProcess.readByteAsInt(limitLevelAddress),
      limitAcquired = limitAcquired,
      masterLevel = gameProcess.readByteAsInt(masterLevelAddress),
      masterAcquired = masterAcquired,
      finalLevel = gameProcess.readByteAsInt(finalLevelAddress),
      finalAcquired = hasRealFinalForm,
      // Final is considered forced if we have the real item but not the dummy item
      finalForced = hasRealFinalForm && !hasDummyFinalForm,
    )
  }

}
