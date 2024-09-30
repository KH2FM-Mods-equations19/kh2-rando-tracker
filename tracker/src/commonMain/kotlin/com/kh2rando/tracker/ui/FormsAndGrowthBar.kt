package com.kh2rando.tracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.unit.dp
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.aerial_dodge
import com.kh2rando.tracker.generated.resources.dodge_roll
import com.kh2rando.tracker.generated.resources.glide
import com.kh2rando.tracker.generated.resources.high_jump
import com.kh2rando.tracker.generated.resources.quick_run
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.gamestate.BaseGameStateApi
import com.kh2rando.tracker.model.item.DriveForm
import com.kh2rando.tracker.model.item.GrowthAbilityPrototype
import org.jetbrains.compose.resources.stringResource

@Composable
fun FormsAndGrowthBar(
  valorLevel: Int,
  valorAcquired: Boolean,
  wisdomLevel: Int,
  wisdomAcquired: Boolean,
  limitLevel: Int,
  limitAcquired: Boolean,
  masterLevel: Int,
  masterAcquired: Boolean,
  finalLevel: Int,
  finalAcquired: Boolean,
  highJumpLevel: Int,
  quickRunLevel: Int,
  dodgeRollLevel: Int,
  aerialDodgeLevel: Int,
  glideLevel: Int,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.fillMaxWidth().heightIn(max = 32.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceEvenly,
  ) {
    FormOrGrowthLevelCell(
      tooltip = DriveForm.ValorFormDummy.localizedName,
      level = valorLevel,
      acquired = valorAcquired,
      icon = DriveForm.ValorFormDummy,
      modifier = Modifier.weight(1.0f, fill = false),
    )
    FormOrGrowthLevelCell(
      tooltip = DriveForm.WisdomForm.localizedName,
      level = wisdomLevel,
      acquired = wisdomAcquired,
      icon = DriveForm.WisdomForm,
      modifier = Modifier.weight(1.0f, fill = false),
    )
    FormOrGrowthLevelCell(
      tooltip = DriveForm.LimitForm.localizedName,
      level = limitLevel,
      acquired = limitAcquired,
      icon = DriveForm.LimitForm,
      modifier = Modifier.weight(1.0f, fill = false),
    )
    FormOrGrowthLevelCell(
      tooltip = DriveForm.MasterForm.localizedName,
      level = masterLevel,
      acquired = masterAcquired,
      icon = DriveForm.MasterForm,
      modifier = Modifier.weight(1.0f, fill = false),
    )
    FormOrGrowthLevelCell(
      tooltip = DriveForm.FinalFormDummy.localizedName,
      level = finalLevel,
      acquired = finalAcquired,
      icon = DriveForm.FinalFormDummy,
      modifier = Modifier.weight(1.0f, fill = false),
    )
    FormOrGrowthLevelCell(
      tooltip = stringResource(Res.string.high_jump),
      level = highJumpLevel,
      acquired = highJumpLevel > 0,
      icon = GrowthAbilityPrototype.HighJump,
      modifier = Modifier.weight(1.0f, fill = false),
    )
    FormOrGrowthLevelCell(
      tooltip = stringResource(Res.string.quick_run),
      level = quickRunLevel,
      acquired = quickRunLevel > 0,
      icon = GrowthAbilityPrototype.QuickRun,
      modifier = Modifier.weight(1.0f, fill = false),
    )
    FormOrGrowthLevelCell(
      tooltip = stringResource(Res.string.dodge_roll),
      level = dodgeRollLevel,
      acquired = dodgeRollLevel > 0,
      icon = GrowthAbilityPrototype.DodgeRoll,
      modifier = Modifier.weight(1.0f, fill = false),
    )
    FormOrGrowthLevelCell(
      tooltip = stringResource(Res.string.aerial_dodge),
      level = aerialDodgeLevel,
      acquired = aerialDodgeLevel > 0,
      icon = GrowthAbilityPrototype.AerialDodge,
      modifier = Modifier.weight(1.0f, fill = false),
    )
    FormOrGrowthLevelCell(
      tooltip = stringResource(Res.string.glide),
      level = glideLevel,
      acquired = glideLevel > 0,
      icon = GrowthAbilityPrototype.Glide,
      modifier = Modifier.weight(1.0f, fill = false),
    )
  }
}

@Composable
fun FormsAndGrowthBar(
  gameState: BaseGameStateApi,
  modifier: Modifier = Modifier,
) {
  val driveFormsState by gameState.driveFormsStates.collectAsState()
  val growthState by gameState.growthStates.collectAsState()
  FormsAndGrowthBar(
    valorLevel = driveFormsState.valorLevel,
    valorAcquired = driveFormsState.valorAcquired,
    wisdomLevel = driveFormsState.wisdomLevel,
    wisdomAcquired = driveFormsState.wisdomAcquired,
    limitLevel = driveFormsState.limitLevel,
    limitAcquired = driveFormsState.limitAcquired,
    masterLevel = driveFormsState.masterLevel,
    masterAcquired = driveFormsState.masterAcquired,
    finalLevel = driveFormsState.finalLevel,
    finalAcquired = driveFormsState.finalAcquired,
    highJumpLevel = growthState.highJumpLevel,
    quickRunLevel = growthState.quickRunLevel,
    dodgeRollLevel = growthState.dodgeRollLevel,
    aerialDodgeLevel = growthState.aerialDodgeLevel,
    glideLevel = growthState.glideLevel,
    modifier = modifier,
  )
}

@Composable
private fun FormOrGrowthLevelCell(
  tooltip: String,
  level: Int,
  acquired: Boolean,
  icon: HasCustomizableIcon,
  modifier: Modifier = Modifier,
) {
  IconBadgeCell(
    badgeText = if (level == -1) "?" else level.toString(),
    icon = icon,
    tooltip = tooltip,
    modifier = modifier,
    iconAlpha = if (acquired) DefaultAlpha else GhostAlpha,
  )
}
