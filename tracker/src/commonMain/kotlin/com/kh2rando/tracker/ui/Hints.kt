package com.kh2rando.tracker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.desc_emblems
import com.kh2rando.tracker.generated.resources.desc_objectives
import com.kh2rando.tracker.generated.resources.desc_proofs
import com.kh2rando.tracker.generated.resources.desc_reports
import com.kh2rando.tracker.generated.resources.hash_ability_unequip
import com.kh2rando.tracker.generated.resources.hash_accessory
import com.kh2rando.tracker.generated.resources.hash_ai_mode_frequent
import com.kh2rando.tracker.generated.resources.hash_ai_mode_moderate
import com.kh2rando.tracker.generated.resources.hash_ai_mode_rare
import com.kh2rando.tracker.generated.resources.hash_ai_settings
import com.kh2rando.tracker.generated.resources.hash_armor
import com.kh2rando.tracker.generated.resources.hash_button_circle
import com.kh2rando.tracker.generated.resources.hash_button_cross
import com.kh2rando.tracker.generated.resources.hash_button_l1
import com.kh2rando.tracker.generated.resources.hash_button_l2
import com.kh2rando.tracker.generated.resources.hash_button_r1
import com.kh2rando.tracker.generated.resources.hash_button_r2
import com.kh2rando.tracker.generated.resources.hash_button_square
import com.kh2rando.tracker.generated.resources.hash_button_triangle
import com.kh2rando.tracker.generated.resources.hash_exclamation_mark
import com.kh2rando.tracker.generated.resources.hash_form
import com.kh2rando.tracker.generated.resources.hash_gumi_block
import com.kh2rando.tracker.generated.resources.hash_gumi_blueprint
import com.kh2rando.tracker.generated.resources.hash_gumi_brush
import com.kh2rando.tracker.generated.resources.hash_gumi_gear
import com.kh2rando.tracker.generated.resources.hash_gumi_ship
import com.kh2rando.tracker.generated.resources.hash_item_consumable
import com.kh2rando.tracker.generated.resources.hash_item_key
import com.kh2rando.tracker.generated.resources.hash_item_tent
import com.kh2rando.tracker.generated.resources.hash_magic
import com.kh2rando.tracker.generated.resources.hash_material
import com.kh2rando.tracker.generated.resources.hash_party
import com.kh2rando.tracker.generated.resources.hash_question_mark
import com.kh2rando.tracker.generated.resources.hash_rank_a
import com.kh2rando.tracker.generated.resources.hash_rank_b
import com.kh2rando.tracker.generated.resources.hash_rank_c
import com.kh2rando.tracker.generated.resources.hash_rank_s
import com.kh2rando.tracker.generated.resources.hash_weapon_keyblade
import com.kh2rando.tracker.generated.resources.hash_weapon_shield
import com.kh2rando.tracker.generated.resources.hash_weapon_staff
import com.kh2rando.tracker.generated.resources.hint_general_reveal
import com.kh2rando.tracker.generated.resources.hint_important_checks_template
import com.kh2rando.tracker.generated.resources.hint_item_location_template
import com.kh2rando.tracker.generated.resources.hint_points_total_template
import com.kh2rando.tracker.generated.resources.hint_summary_no_hints
import com.kh2rando.tracker.generated.resources.hint_system_template
import com.kh2rando.tracker.generated.resources.progression_hint_info_template
import com.kh2rando.tracker.generated.resources.progression_hints_plus_points
import com.kh2rando.tracker.generated.resources.progression_total_points
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.gamestate.FullGameStateApi
import com.kh2rando.tracker.model.gamestate.acquiredReportSets
import com.kh2rando.tracker.model.gamestate.mostRecentRevealedPrimaryHint
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.hints.ProgressionSummary
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.seed.FinalDoorRequirement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Renders only the primary hint information from the given [hintInfo].
 */
@Composable
fun PrimaryHintInfoContent(
  hintInfo: HintInfo,
  modifier: Modifier = Modifier,
  showIcons: Boolean = true,
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    when (hintInfo) {
      is HintInfo.JournalTextOnly -> {

      }

      is HintInfo.GeneralRevealed -> {
        val location = hintInfo.location
        val locationShortName = stringResource(location.shortName)
        if (showIcons) {
          HintLocationContent(location, locationShortName)
        }
        Text(stringResource(Res.string.hint_general_reveal, locationShortName))
      }

      is HintInfo.ImportantCheckCount -> {
        val location = hintInfo.location
        val count = hintInfo.count
        val locationShortName = stringResource(location.shortName)
        if (showIcons) {
          HintLocationContent(location, locationShortName)
        }
        Text(
          pluralStringResource(Res.plurals.hint_important_checks_template, count, locationShortName, count)
        )
      }

      is HintInfo.PointsCount -> {
        val location = hintInfo.location
        val points = hintInfo.points
        val locationShortName = stringResource(location.shortName)
        if (showIcons) {
          HintLocationContent(location, locationShortName)
        }
        Text(
          pluralStringResource(Res.plurals.hint_points_total_template, points, locationShortName, points)
        )
      }

      is HintInfo.ItemLocation -> {
        val location = hintInfo.location
        val locationShortName = stringResource(location.shortName)
        if (showIcons) {
          HintLocationContent(location, locationShortName)
        }

        val itemDisplayName = hintInfo.item.localizedName
        val text = stringResource(Res.string.hint_item_location_template, locationShortName, itemDisplayName)
        Text(text)

        if (showIcons) {
        hintInfo.item.ItemIcon(modifier = Modifier.size(32.dp))
          }
      }

      is HintInfo.PathToProofs -> {
        val location = hintInfo.location
        val locationShortName = stringResource(location.shortName)
        if (showIcons) {
          HintLocationContent(location, locationShortName)
        }
        Text(hintInfo.proofPathText(location), modifier = Modifier.padding(4.dp))
      }
    }
  }
}

/**
 * Renders the journal text of [hintInfo], if any.
 */
@Composable
fun HintJournalTextContent(hintInfo: HintInfo, modifier: Modifier = Modifier) {
  Column(modifier = modifier) {
    val journalText = hintInfo.journalText.orEmpty()
    if (journalText.isNotEmpty()) {
      val lines = journalText.split("\n")
      for (line in lines) {
        if (line.isNotEmpty()) {
          Text(line)
        }
      }
    }
  }
}

/**
 * Renders the full contents of [hintInfo].
 */
@Composable
fun FullHintInfoContent(
  hintInfo: HintInfo,
  modifier: Modifier = Modifier,
  showIcons: Boolean = true
) {
  Column(modifier = modifier) {
    PrimaryHintInfoContent(hintInfo, showIcons = showIcons)
    HintJournalTextContent(hintInfo)
  }
}

@Composable
private fun HintLocationContent(location: Location, locationShortName: String, modifier: Modifier = Modifier) {
  Box(
    modifier.size(24.dp),
    contentAlignment = Alignment.Center
  ) {
    CustomizableIcon(icon = location, contentDescription = locationShortName)
  }
}

@Composable
fun HintStatusBar(
  gameState: FullGameStateApi,
  onShowReportDetails: () -> Unit,
  onShowProgressionDetails: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier.fillMaxWidth().height(72.dp).padding(horizontal = 8.dp)) {
    val seed = gameState.seed
    val seedSettings = seed.settings
    val hintSystem = seed.settings.hintSystem
    val typography = MaterialTheme.typography

    // Hint row 1
    val currHintInfo by gameState.mostRecentRevealedPrimaryHint.collectAsState(initial = null)
    val currentHintInfo = currHintInfo
    if (currentHintInfo == null) {
      CompositionLocalProvider(LocalTextStyle provides typography.titleLarge) {
        NoCurrentHintContent(
          generatorVersion = seed.generatorVersion,
          seedHashIconNames = seed.seedHashIcons,
          modifier = Modifier.weight(1.0f).align(Alignment.CenterHorizontally)
        )
      }
    } else {
      CompositionLocalProvider(LocalTextStyle provides typography.titleMedium) {
        PrimaryHintInfoContent(
          hintInfo = currentHintInfo,
          modifier = Modifier.weight(1.0f).align(Alignment.CenterHorizontally),
          showIcons = true,
        )
      }
    }

    // Hint row 2
    Row(
      modifier = Modifier.fillMaxWidth().weight(1.0f),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      @Composable
      fun SummaryDivider() {
        VerticalDivider(color = MaterialTheme.colorScheme.onSurface)
      }

      val hintSystemName = stringResource(Res.string.hint_system_template, stringResource(hintSystem.displayName))
      Text(hintSystemName, modifier = Modifier.weight(1.0f), style = typography.titleMedium)

      val summariesModifier = Modifier.heightIn(max = 24.dp)

      val basicProgressionSettings = hintSystem.basicProgressionSettings
      if (basicProgressionSettings != null) {
        val progressionSummaries = basicProgressionSettings.progressionSummaries(gameState)
        val progressionSummary by progressionSummaries.collectAsState(ProgressionSummary.Unspecified)
        ProgressionSummary(
          progressionSummary = progressionSummary,
          onShowProgressionDetails = onShowProgressionDetails,
          modifier = summariesModifier
        )
        SummaryDivider()
      }

      if (seedSettings.trackableItems.any { it is AnsemReport }) {
        val acquiredReports by gameState.acquiredReportSets.collectAsState(emptySet())
        ReportSummary(
          reportsAcquired = acquiredReports.size,
          onShowReportDetails = onShowReportDetails,
          modifier = summariesModifier
        )
        SummaryDivider()
      }

      when (val finalDoorRequirement = seedSettings.finalDoorRequirement) {
        FinalDoorRequirement.ThreeProofs -> {
          val acquiredItems by gameState.acquiredItems.collectAsState()
          val proofs = Proof.entries
          FinalDoorRequirementSummary(
            iconProvider = Proof.ProofOfNonexistence,
            acquired = acquiredItems.count { it.prototype is Proof },
            needed = proofs.size,
            tooltip = stringResource(Res.string.desc_proofs),
            modifier = summariesModifier,
          )
        }

        is FinalDoorRequirement.LuckyEmblems -> {
          val acquiredEmblemCount by gameState.acquiredEmblemCounts.collectAsState()
          FinalDoorRequirementSummary(
            iconProvider = SystemIcon.LuckyEmblem,
            acquired = acquiredEmblemCount,
            needed = finalDoorRequirement.emblemsNeeded,
            tooltip = stringResource(Res.string.desc_emblems, finalDoorRequirement.emblemsAvailable),
            modifier = summariesModifier,
          )
        }

        is FinalDoorRequirement.Objectives -> {
          val acquiredEmblemCount by gameState.acquiredEmblemCounts.collectAsState()
          FinalDoorRequirementSummary(
            iconProvider = SystemIcon.LuckyEmblem,
            acquired = acquiredEmblemCount,
            needed = finalDoorRequirement.objectivesNeeded,
            tooltip = stringResource(Res.string.desc_objectives),
            modifier = summariesModifier,
          )
        }
      }
    }
  }
}

@Composable
private fun ProgressionSummary(
  progressionSummary: ProgressionSummary,
  onShowProgressionDetails: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val nextHintRequiredPoints = progressionSummary.nextHintRequiredPoints
  if (nextHintRequiredPoints > 0) {
    val earnedHints = progressionSummary.totalEarnedHints
    IconCounterCell(
      text = stringResource(
        Res.string.progression_hint_info_template,
        earnedHints,
        progressionSummary.nextHintEarnedPoints,
        progressionSummary.nextHintRequiredPoints,
      ),
      icon = SystemIcon.ProgressionPoints,
      tooltip = stringResource(Res.string.progression_hints_plus_points),
      modifier = modifier.clickable(onClick = onShowProgressionDetails),
    )
  } else {
    IconCounterCell(
      text = progressionSummary.totalEarnedPoints.toString(),
      icon = SystemIcon.ProgressionPoints,
      tooltip = stringResource(Res.string.progression_total_points),
      modifier = modifier.clickable(onClick = onShowProgressionDetails),
    )
  }
}

@Composable
private fun ReportSummary(
  reportsAcquired: Int,
  onShowReportDetails: () -> Unit,
  modifier: Modifier = Modifier,
) {
  IconCounterCell(
    text = "$reportsAcquired/${AnsemReport.entries.size}",
    tooltip = stringResource(Res.string.desc_reports),
    icon = SystemIcon.AnsemReport,
    modifier = modifier.clickable(onClick = onShowReportDetails),
  )
}

@Composable
private fun FinalDoorRequirementSummary(
  iconProvider: HasCustomizableIcon,
  acquired: Int,
  needed: Int,
  tooltip: String,
  modifier: Modifier,
) {
  IconCounterCell(
    text = "$acquired/$needed",
    tooltip = tooltip,
    icon = iconProvider,
    modifier = modifier,
  )
}

@Composable
private fun NoCurrentHintContent(
  generatorVersion: String,
  seedHashIconNames: List<String>,
  modifier: Modifier = Modifier,
) {
  if (seedHashIconNames.isNotEmpty()) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
      Text(generatorVersion)
      for (iconName in seedHashIconNames) {
        val iconResource = seedHashIconResource(iconName) ?: continue
        Image(imageResource(iconResource), contentDescription = iconName)
      }
    }
  }
}

private fun seedHashIconResource(iconName: String): DrawableResource? {
  return when (iconName) {
    "item-consumable" -> Res.drawable.hash_item_consumable
    "item-tent" -> Res.drawable.hash_item_tent
    "item-key" -> Res.drawable.hash_item_key
    "ability-unequip" -> Res.drawable.hash_ability_unequip
    "weapon-keyblade" -> Res.drawable.hash_weapon_keyblade
    "weapon-staff" -> Res.drawable.hash_weapon_staff
    "weapon-shield" -> Res.drawable.hash_weapon_shield
    "armor" -> Res.drawable.hash_armor
    "magic" -> Res.drawable.hash_magic
    "material" -> Res.drawable.hash_material
    "exclamation-mark" -> Res.drawable.hash_exclamation_mark
    "question-mark" -> Res.drawable.hash_question_mark
    "accessory" -> Res.drawable.hash_accessory
    "party" -> Res.drawable.hash_party
    "ai-mode-frequent" -> Res.drawable.hash_ai_mode_frequent
    "ai-mode-moderate" -> Res.drawable.hash_ai_mode_moderate
    "ai-mode-rare" -> Res.drawable.hash_ai_mode_rare
    "ai-settings" -> Res.drawable.hash_ai_settings
    "rank-s" -> Res.drawable.hash_rank_s
    "rank-a" -> Res.drawable.hash_rank_a
    "rank-b" -> Res.drawable.hash_rank_b
    "rank-c" -> Res.drawable.hash_rank_c
    "gumi-brush" -> Res.drawable.hash_gumi_brush
    "gumi-blueprint" -> Res.drawable.hash_gumi_blueprint
    "gumi-ship" -> Res.drawable.hash_gumi_ship
    "gumi-block" -> Res.drawable.hash_gumi_block
    "gumi-gear" -> Res.drawable.hash_gumi_gear
    "form" -> Res.drawable.hash_form
    "button-r1" -> Res.drawable.hash_button_r1
    "button-r2" -> Res.drawable.hash_button_r2
    "button-l1" -> Res.drawable.hash_button_l1
    "button-l2" -> Res.drawable.hash_button_l2
    "button-triangle" -> Res.drawable.hash_button_triangle
    "button-cross" -> Res.drawable.hash_button_cross
    "button-square" -> Res.drawable.hash_button_square
    "button-circle" -> Res.drawable.hash_button_circle
    else -> null
  }
}

/**
 * Renders information about all of the acquired hints.
 */
@Composable
fun HintSummaryArea(
  showFullHints: Boolean,
  hintInfoProvider: () -> Flow<Set<HintInfo>>,
  modifier: Modifier = Modifier,
  showIcons: Boolean = true,
) {
  val hintInfoFlow = hintInfoProvider().map { hints ->
    hints
      .filterTo(mutableListOf()) { hint ->
        if (hint is HintInfo.JournalTextOnly) {
          !hint.journalText.isNullOrEmpty()
        } else {
          true
        }
      }
      .sortedBy(HintInfo::hintOrReportNumber)
  }
  val hintInfos by hintInfoFlow.collectAsState(emptyList())
  if (hintInfos.isEmpty()) {
    Box(modifier.sizeIn(minWidth = 300.dp, minHeight = 120.dp), contentAlignment = Alignment.Center) {
      Text(stringResource(Res.string.hint_summary_no_hints))
    }
  } else {
    val colorScheme = MaterialTheme.colorScheme
    LazyColumn(
      modifier = modifier,
      verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
      items(hintInfos, key = HintInfo::hintOrReportNumber) { hintInfo ->
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          modifier = Modifier.fillMaxWidth().background(colorScheme.surfaceContainer),
        ) {
          BoxWithConstraints(Modifier.size(32.dp), contentAlignment = Alignment.Center) {
            CounterText(hintInfo.hintOrReportNumber.toString(), maximumHeight = maxHeight)
          }
          if (showFullHints) {
            FullHintInfoContent(hintInfo, showIcons = showIcons)
          } else if (hintInfo is HintInfo.JournalTextOnly) {
            HintJournalTextContent(hintInfo)
          } else {
            PrimaryHintInfoContent(hintInfo, showIcons = showIcons)
          }
        }
      }
    }
  }
}

/**
 * Displays information about all of the acquired hints in a dialog.
 */
@Composable
fun HintSummaryDialog(
  hintInfoProvider: () -> Flow<Set<HintInfo>>,
  onDismissRequest: () -> Unit,
) {
  Dialog(onDismissRequest = onDismissRequest) {
    Surface {
      HintSummaryArea(showFullHints = false, hintInfoProvider = hintInfoProvider, showIcons = true)
    }
  }
}
