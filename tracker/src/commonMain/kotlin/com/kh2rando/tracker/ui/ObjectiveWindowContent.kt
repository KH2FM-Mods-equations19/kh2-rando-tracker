package com.kh2rando.tracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.desc_location_complete
import com.kh2rando.tracker.generated.resources.objectives_completed
import com.kh2rando.tracker.generated.resources.objectives_none_to_display
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.gamestate.BaseGameStateUpdateApi
import com.kh2rando.tracker.model.gamestate.allCompletedProgressCheckpoints
import com.kh2rando.tracker.model.objective.Objective
import com.kh2rando.tracker.model.progress.DriveFormProgress
import com.kh2rando.tracker.model.progress.SoraLevelProgress
import com.kh2rando.tracker.model.seed.FinalDoorRequirement
import org.jetbrains.compose.resources.stringResource

@Composable
fun ObjectiveWindowContent(
  gameState: BaseGameStateUpdateApi?,
  modifier: Modifier = Modifier,
) {
  Surface(modifier) {
    val finalDoorRequirement = gameState?.seed?.settings?.finalDoorRequirement
    if (gameState == null || finalDoorRequirement !is FinalDoorRequirement.Objectives) {
      NoObjectives(Modifier.fillMaxSize())
    } else {
      Objectives(
        gameState = gameState,
        objectivesRequirement = finalDoorRequirement,
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}

@Composable
private fun NoObjectives(modifier: Modifier = Modifier) {
  Box(modifier, contentAlignment = Alignment.Center) {
    Text(stringResource(Res.string.objectives_none_to_display))
  }
}

@Composable
private fun Objectives(
  gameState: BaseGameStateUpdateApi,
  objectivesRequirement: FinalDoorRequirement.Objectives,
  modifier: Modifier = Modifier,
) {
  val objectives = objectivesRequirement.objectiveList.sortedWith(
    compareBy<Objective> { it.location }.thenBy { it.difficulty }
  )
  if (objectives.isEmpty()) {
    NoObjectives(modifier)
    return
  }

  val manuallyCompletedObjectives by gameState.manuallyCompletedObjectives.collectAsState()
  val allCompletedProgress by gameState.allCompletedProgressCheckpoints.collectAsState(emptySet())
  fun Objective.isMet(): Boolean {
    return manuallyCompletedObjectives.contains(this) || allCompletedProgress.contains(checkpoint)
  }

  val obtained = objectives.count { objective -> objective.isMet() }
  val required = objectivesRequirement.objectivesNeeded
  val allMet = obtained >= required

  Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.headlineMedium.copy(fontFamily = khMenuFontFamily())
      ) {
        Text(
          stringResource(Res.string.objectives_completed),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.weight(1.0f, fill = false)
        )
        Text(
          "$obtained/$required",
          color = ColorToken.Gold.color,
          modifier = Modifier.weight(1.0f, fill = false)
        )
      }
    }

    LazyVerticalGrid(
      columns = GridCells.FixedSize(size = 72.dp),
      modifier = Modifier.weight(1.0f)
    ) {
      items(objectives, key = { it }) { objective ->
        val isMet = objective.isMet()

        val background = if (isMet && allMet) {
          ColorToken.Green.color.copy(alpha = 0.5f)
        } else if (isMet) {
          ColorToken.Red.color.copy(alpha = 0.5f)
        } else {
          Color.Transparent
        }

        ObjectiveCell(
          objective = objective,
          isMet = isMet,
          onClick = { gameState.manuallyToggleObjective(objective) },
          modifier = Modifier
            .background(background)
            .border(1.dp, MaterialTheme.colorScheme.outline)
            .padding(4.dp),
        )
      }
    }
  }
}

@Composable
private fun ObjectiveCell(
  objective: Objective,
  isMet: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val checkpoint = objective.checkpoint
  val objectiveText = stringResource(objective.description)

  Box(modifier = modifier.clickable(onClick = onClick)) {
    if (checkpoint is DriveFormProgress) {
      if (checkpoint.findCustomIconFile() == null) {
        IconBadgeCell(
          badgeText = checkpoint.formLevel.toString(),
          icon = checkpoint.driveForm,
          tooltip = objectiveText,
        )
      } else {
        SimpleTooltipArea(tooltipText = objectiveText) {
          CustomizableIcon(icon = checkpoint, contentDescription = objectiveText)
        }
      }
    } else if (checkpoint is SoraLevelProgress) {
      if (checkpoint.findCustomIconFile() == null) {
        IconBadgeCell(
          badgeText = checkpoint.level.toString(),
          icon = checkpoint,
          tooltip = objectiveText,
        )
      } else {
        SimpleTooltipArea(tooltipText = objectiveText) {
          CustomizableIcon(icon = checkpoint, contentDescription = objectiveText)
        }
      }
    } else {
      SimpleTooltipArea(tooltipText = objectiveText) {
        CustomizableIcon(icon = checkpoint, contentDescription = objectiveText)
      }
    }

    if (isMet) {
      Icon(
        Icons.Filled.CheckCircle,
        contentDescription = stringResource(Res.string.desc_location_complete),
        tint = ColorToken.LightBlue.color,
        modifier = Modifier.align(Alignment.TopEnd),
      )
    }
  }
}
