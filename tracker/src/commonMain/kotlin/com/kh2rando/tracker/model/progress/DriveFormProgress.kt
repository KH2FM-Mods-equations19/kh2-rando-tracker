package com.kh2rando.tracker.model.progress

import androidx.compose.ui.graphics.Color
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_drive_final_2
import com.kh2rando.tracker.generated.resources.prog_drive_final_3
import com.kh2rando.tracker.generated.resources.prog_drive_final_4
import com.kh2rando.tracker.generated.resources.prog_drive_final_5
import com.kh2rando.tracker.generated.resources.prog_drive_final_6
import com.kh2rando.tracker.generated.resources.prog_drive_final_7
import com.kh2rando.tracker.generated.resources.prog_drive_limit_2
import com.kh2rando.tracker.generated.resources.prog_drive_limit_3
import com.kh2rando.tracker.generated.resources.prog_drive_limit_4
import com.kh2rando.tracker.generated.resources.prog_drive_limit_5
import com.kh2rando.tracker.generated.resources.prog_drive_limit_6
import com.kh2rando.tracker.generated.resources.prog_drive_limit_7
import com.kh2rando.tracker.generated.resources.prog_drive_master_2
import com.kh2rando.tracker.generated.resources.prog_drive_master_3
import com.kh2rando.tracker.generated.resources.prog_drive_master_4
import com.kh2rando.tracker.generated.resources.prog_drive_master_5
import com.kh2rando.tracker.generated.resources.prog_drive_master_6
import com.kh2rando.tracker.generated.resources.prog_drive_master_7
import com.kh2rando.tracker.generated.resources.prog_drive_valor_2
import com.kh2rando.tracker.generated.resources.prog_drive_valor_3
import com.kh2rando.tracker.generated.resources.prog_drive_valor_4
import com.kh2rando.tracker.generated.resources.prog_drive_valor_5
import com.kh2rando.tracker.generated.resources.prog_drive_valor_6
import com.kh2rando.tracker.generated.resources.prog_drive_valor_7
import com.kh2rando.tracker.generated.resources.prog_drive_wisdom_2
import com.kh2rando.tracker.generated.resources.prog_drive_wisdom_3
import com.kh2rando.tracker.generated.resources.prog_drive_wisdom_4
import com.kh2rando.tracker.generated.resources.prog_drive_wisdom_5
import com.kh2rando.tracker.generated.resources.prog_drive_wisdom_6
import com.kh2rando.tracker.generated.resources.prog_drive_wisdom_7
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.item.DriveForm
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class DriveFormProgress(
  val driveForm: DriveForm,
  val formLevel: Int,
  override val displayString: StringResource,
  override val customIconIdentifier: String,
) : ProgressCheckpoint {
  Valor2(
    driveForm = DriveForm.ValorFormDummy,
    formLevel = 2,
    displayString = Res.string.prog_drive_valor_2,
    customIconIdentifier = "Drives_Valor2",
  ),
  Valor3(
    driveForm = DriveForm.ValorFormDummy,
    formLevel = 3,
    displayString = Res.string.prog_drive_valor_3,
    customIconIdentifier = "Drives_Valor3",
  ),
  Valor4(
    driveForm = DriveForm.ValorFormDummy,
    formLevel = 4,
    displayString = Res.string.prog_drive_valor_4,
    customIconIdentifier = "Drives_Valor4",
  ),
  Valor5(
    driveForm = DriveForm.ValorFormDummy,
    formLevel = 5,
    displayString = Res.string.prog_drive_valor_5,
    customIconIdentifier = "Drives_Valor5",
  ),
  Valor6(
    driveForm = DriveForm.ValorFormDummy,
    formLevel = 6,
    displayString = Res.string.prog_drive_valor_6,
    customIconIdentifier = "Drives_Valor6",
  ),
  Valor7(
    driveForm = DriveForm.ValorFormDummy,
    formLevel = 7,
    displayString = Res.string.prog_drive_valor_7,
    customIconIdentifier = "Drives_Valor7",
  ),
  Wisdom2(
    driveForm = DriveForm.WisdomForm,
    formLevel = 2,
    displayString = Res.string.prog_drive_wisdom_2,
    customIconIdentifier = "Drives_Wisdom2",
  ),
  Wisdom3(
    driveForm = DriveForm.WisdomForm,
    formLevel = 3,
    displayString = Res.string.prog_drive_wisdom_3,
    customIconIdentifier = "Drives_Wisdom3",
  ),
  Wisdom4(
    driveForm = DriveForm.WisdomForm,
    formLevel = 4,
    displayString = Res.string.prog_drive_wisdom_4,
    customIconIdentifier = "Drives_Wisdom4",
  ),
  Wisdom5(
    driveForm = DriveForm.WisdomForm,
    formLevel = 5,
    displayString = Res.string.prog_drive_wisdom_5,
    customIconIdentifier = "Drives_Wisdom5",
  ),
  Wisdom6(
    driveForm = DriveForm.WisdomForm,
    formLevel = 6,
    displayString = Res.string.prog_drive_wisdom_6,
    customIconIdentifier = "Drives_Wisdom6",
  ),
  Wisdom7(
    driveForm = DriveForm.WisdomForm,
    formLevel = 7,
    displayString = Res.string.prog_drive_wisdom_7,
    customIconIdentifier = "Drives_Wisdom7",
  ),
  Limit2(
    driveForm = DriveForm.LimitForm,
    formLevel = 2,
    displayString = Res.string.prog_drive_limit_2,
    customIconIdentifier = "Drives_Limit2",
  ),
  Limit3(
    driveForm = DriveForm.LimitForm,
    formLevel = 3,
    displayString = Res.string.prog_drive_limit_3,
    customIconIdentifier = "Drives_Limit3",
  ),
  Limit4(
    driveForm = DriveForm.LimitForm,
    formLevel = 4,
    displayString = Res.string.prog_drive_limit_4,
    customIconIdentifier = "Drives_Limit4",
  ),
  Limit5(
    driveForm = DriveForm.LimitForm,
    formLevel = 5,
    displayString = Res.string.prog_drive_limit_5,
    customIconIdentifier = "Drives_Limit5",
  ),
  Limit6(
    driveForm = DriveForm.LimitForm,
    formLevel = 6,
    displayString = Res.string.prog_drive_limit_6,
    customIconIdentifier = "Drives_Limit6",
  ),
  Limit7(
    driveForm = DriveForm.LimitForm,
    formLevel = 7,
    displayString = Res.string.prog_drive_limit_7,
    customIconIdentifier = "Drives_Limit7",
  ),
  Master2(
    driveForm = DriveForm.MasterForm,
    formLevel = 2,
    displayString = Res.string.prog_drive_master_2,
    customIconIdentifier = "Drives_Master2",
  ),
  Master3(
    driveForm = DriveForm.MasterForm,
    formLevel = 3,
    displayString = Res.string.prog_drive_master_3,
    customIconIdentifier = "Drives_Master3",
  ),
  Master4(
    driveForm = DriveForm.MasterForm,
    formLevel = 4,
    displayString = Res.string.prog_drive_master_4,
    customIconIdentifier = "Drives_Master4",
  ),
  Master5(
    driveForm = DriveForm.MasterForm,
    formLevel = 5,
    displayString = Res.string.prog_drive_master_5,
    customIconIdentifier = "Drives_Master5",
  ),
  Master6(
    driveForm = DriveForm.MasterForm,
    formLevel = 6,
    displayString = Res.string.prog_drive_master_6,
    customIconIdentifier = "Drives_Master6",
  ),
  Master7(
    driveForm = DriveForm.MasterForm,
    formLevel = 7,
    displayString = Res.string.prog_drive_master_7,
    customIconIdentifier = "Drives_Master7",
  ),
  Final2(
    driveForm = DriveForm.FinalFormDummy,
    formLevel = 2,
    displayString = Res.string.prog_drive_final_2,
    customIconIdentifier = "Drives_Final2",
  ),
  Final3(
    driveForm = DriveForm.FinalFormDummy,
    formLevel = 3,
    displayString = Res.string.prog_drive_final_3,
    customIconIdentifier = "Drives_Final3",
  ),
  Final4(
    driveForm = DriveForm.FinalFormDummy,
    formLevel = 4,
    displayString = Res.string.prog_drive_final_4,
    customIconIdentifier = "Drives_Final4",
  ),
  Final5(
    driveForm = DriveForm.FinalFormDummy,
    formLevel = 5,
    displayString = Res.string.prog_drive_final_5,
    customIconIdentifier = "Drives_Final5",
  ),
  Final6(
    driveForm = DriveForm.FinalFormDummy,
    formLevel = 6,
    displayString = Res.string.prog_drive_final_6,
    customIconIdentifier = "Drives_Final6",
  ),
  Final7(
    driveForm = DriveForm.FinalFormDummy,
    formLevel = 7,
    displayString = Res.string.prog_drive_final_7,
    customIconIdentifier = "Drives_Final7",
  );

  override val defaultIcon: DrawableResource
    get() = driveForm.defaultIcon

  override val defaultIconTint: Color
    get() = driveForm.defaultIconTint

  override val customIconPath: List<String>
    get() = listOf("Grid", "Checks")

  override val index: Int
    get() = ordinal

  override val location: Location
    get() = Location.DriveForms

  companion object {

    fun pointsByCheckpoint(pointsList: List<Int>): Map<DriveFormProgress, Int> {
      val valor = listOf(Valor2, Valor3, Valor4, Valor5, Valor6, Valor7).zip(pointsList).toMap()
      val wisdom = listOf(Wisdom2, Wisdom3, Wisdom4, Wisdom5, Wisdom6, Wisdom7).zip(pointsList).toMap()
      val limit = listOf(Limit2, Limit3, Limit4, Limit5, Limit6, Limit7).zip(pointsList).toMap()
      val master = listOf(Master2, Master3, Master4, Master5, Master6, Master7).zip(pointsList).toMap()
      val final = listOf(Final2, Final3, Final4, Final5, Final6, Final7).zip(pointsList).toMap()
      return valor + wisdom + limit + master + final
    }

  }

}
