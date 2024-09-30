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
import androidx.compose.ui.unit.dp
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.stat_acquired_items
import com.kh2rando.tracker.generated.resources.stat_deaths
import com.kh2rando.tracker.generated.resources.stat_defense
import com.kh2rando.tracker.generated.resources.stat_level
import com.kh2rando.tracker.generated.resources.stat_magic
import com.kh2rando.tracker.generated.resources.stat_strength
import com.kh2rando.tracker.model.SoraState
import com.kh2rando.tracker.model.gamestate.BaseGameStateApi
import org.jetbrains.compose.resources.stringResource

@Composable
fun SoraStatsBar(
  level: Int,
  strength: Int,
  magic: Int,
  defense: Int,
  deaths: Int,
  acquiredItemCounter: Pair<Int, Int>,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.fillMaxWidth().heightIn(max = 24.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceEvenly,
  ) {
    IconCounterCell(
      text = if (level == -1) "?" else level.toString(),
      icon = SystemIcon.LevelStat,
      tooltip = stringResource(Res.string.stat_level),
      modifier = Modifier.weight(1.0f, fill = false)
    )
    IconCounterCell(
      text = if (strength == -1) "?" else strength.toString(),
      icon = SystemIcon.StrengthStat,
      tooltip = stringResource(Res.string.stat_strength),
      modifier = Modifier.weight(1.0f, fill = false)
    )
    IconCounterCell(
      text = if (magic == -1) "?" else magic.toString(),
      icon = SystemIcon.MagicStat,
      tooltip = stringResource(Res.string.stat_magic),
      modifier = Modifier.weight(1.0f, fill = false)
    )
    IconCounterCell(
      text = if (defense == -1) "?" else defense.toString(),
      icon = SystemIcon.DefenseStat,
      tooltip = stringResource(Res.string.stat_defense),
      modifier = Modifier.weight(1.0f, fill = false)
    )
    IconCounterCell(
      text = if (deaths == -1) "?" else deaths.toString(),
      icon = SystemIcon.DeathCount,
      tooltip = stringResource(Res.string.stat_deaths),
      modifier = Modifier.weight(1.0f, fill = false)
    )
    IconCounterCell(
      text = "${acquiredItemCounter.first}/${acquiredItemCounter.second}",
      icon = SystemIcon.ItemCount,
      tooltip = stringResource(Res.string.stat_acquired_items),
      modifier = Modifier.weight(2.0f, fill = false)
    )
  }
}

@Composable
fun SoraStatsBar(
  soraState: SoraState,
  deaths: Int,
  acquiredItemCounter: Pair<Int, Int>,
  modifier: Modifier = Modifier,
) {
  SoraStatsBar(
    level = soraState.currentLevel,
    strength = soraState.strengthStat,
    magic = soraState.magicStat,
    defense = soraState.defenseStat,
    deaths = deaths,
    acquiredItemCounter = acquiredItemCounter,
    modifier = modifier
  )
}

@Composable
fun SoraStatsBar(
  gameState: BaseGameStateApi,
  modifier: Modifier = Modifier,
) {
  val soraState by gameState.soraStates.collectAsState()
  val deaths by gameState.deaths.collectAsState()
  val acquiredItems by gameState.acquiredItems.collectAsState()
  val allTrackableItems = gameState.allTrackableItems
  SoraStatsBar(
    soraState = soraState,
    deaths = deaths,
    acquiredItemCounter = acquiredItems.size to allTrackableItems.size,
    modifier = modifier
  )
}
