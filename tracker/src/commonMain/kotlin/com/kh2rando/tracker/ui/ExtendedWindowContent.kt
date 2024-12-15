@file:OptIn(ExperimentalMaterial3Api::class)

package com.kh2rando.tracker.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.extended_journal_title
import com.kh2rando.tracker.generated.resources.extended_misc_drive_gauge_current_max
import com.kh2rando.tracker.generated.resources.extended_misc_munny
import com.kh2rando.tracker.generated.resources.extended_misc_next_level_check
import com.kh2rando.tracker.generated.resources.extended_misc_proof_information
import com.kh2rando.tracker.generated.resources.extended_misc_title
import com.kh2rando.tracker.generated.resources.extended_no_game_state
import com.kh2rando.tracker.generated.resources.extended_no_music_state
import com.kh2rando.tracker.generated.resources.extended_song
import com.kh2rando.tracker.generated.resources.location_symphony_of_sorcery
import com.kh2rando.tracker.model.DriveFormsState
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.LocationLayout
import com.kh2rando.tracker.model.MusicState
import com.kh2rando.tracker.model.SoraState
import com.kh2rando.tracker.model.gamestate.FullGameState
import com.kh2rando.tracker.model.item.MunnyPouch
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.preferences.TrackerPreferences
import com.kh2rando.tracker.model.preferences.collectAsState
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExtendedWindowContent(
  gameState: FullGameState?,
  preferences: TrackerPreferences,
  modifier: Modifier = Modifier,
) {
  Surface(modifier = modifier) {
    if (gameState == null) {
      NoGameState(Modifier.fillMaxSize())
    } else {
      ExtendedInformation(gameState, preferences, modifier = Modifier.fillMaxSize())
    }
  }
}

@Composable
private fun NoGameState(modifier: Modifier = Modifier) {
  Box(modifier, contentAlignment = Alignment.Center) {
    Text(
      stringResource(Res.string.extended_no_game_state),
      style = MaterialTheme.typography.titleMedium,
      fontFamily = khMenuFontFamily(),
    )
  }
}

@Composable
private fun ExtendedInformation(
  gameState: FullGameState,
  preferences: TrackerPreferences,
  modifier: Modifier = Modifier,
) {
  Column(modifier) {
    MainExtendedWindowContent(gameState, preferences, modifier = Modifier.weight(1.0f))

    val showSongInfo by preferences.showSongInfoExtendedWindow.collectAsState()
    val songFolderAsGroup by preferences.songFolderAsGroup.collectAsState()
    if (showSongInfo) {
      val musicState by gameState.musicStates.collectAsState()
      SongIndicator(musicState, songFolderAsGroup)
    }
  }
}

@Composable
private fun MainExtendedWindowContent(
  gameState: FullGameState,
  preferences: TrackerPreferences,
  modifier: Modifier = Modifier,
) {
  Row(modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
    Column(modifier = Modifier.weight(2.0f)) {
      SmallHeader(stringResource(Res.string.extended_journal_title))

      val updatedTextStyle = LocalTextStyle.current.copy(fontSize = 12.sp)
      CompositionLocalProvider(LocalTextStyle provides updatedTextStyle) {
        HintSummaryArea(
          showFullHints = true,
          hintInfoProvider = { gameState.revealedReportHintSets },
          modifier = Modifier.align(Alignment.CenterHorizontally),
          showIcons = false, // TODO: Preference? Maybe "standard" vs. "compact"?
        )
      }
    }

    Column(modifier = Modifier.weight(2.0f)) {
      SmallHeader(stringResource(Res.string.extended_misc_title))

      val soraState by gameState.soraStates.collectAsState()
      val driveFormsState by gameState.driveFormsStates.collectAsState()

      Spacer(Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth().height(36.dp).padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        IconCounterCell(
          text = if (soraState == SoraState.Unspecified) {
            "?"
          } else {
            val currentLevel = soraState.currentLevel
            val nextLevelWithChecks = gameState.seed.settings.levelSetting.nextLevelWithChecks(currentLevel)
            if (nextLevelWithChecks == null) {
              currentLevel.toString()
            } else {
              "$currentLevel → $nextLevelWithChecks"
            }
          },
          icon = SystemIcon.LevelStat,
          tooltip = stringResource(Res.string.extended_misc_next_level_check),
          modifier = Modifier.weight(1.0f),
        )

        IconCounterCell(
          text = if (driveFormsState == DriveFormsState.Unspecified) {
            "?"
          } else {
            "${driveFormsState.currentDriveGauge} / ${driveFormsState.maximumDriveGauge}"
          },
          icon = Location.DriveForms,
          tooltip = stringResource(Res.string.extended_misc_drive_gauge_current_max),
          modifier = Modifier.weight(1.0f),
        )

        IconCounterCell(
          text = if (soraState == SoraState.Unspecified) {
            "?"
          } else {
            "%,d".format(soraState.munny)
          },
          icon = MunnyPouch.Mickey,
          tooltip = stringResource(Res.string.extended_misc_munny),
          modifier = Modifier.weight(1.0f),
        )
      }

      Spacer(Modifier.height(16.dp))

      val locationLayout by preferences.locationLayout.collectAsState()
      Surface(color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(Res.string.extended_misc_proof_information), modifier = Modifier.padding(4.dp))
      }
      ProofInfoArea(
        eligibleLocations = gameState.seed.settings.enabledLocations - Location.GardenOfAssemblage,
        locationLayout = locationLayout,
        locationStatesProvider = { location -> gameState.locationUiStates.getValue(location) },
        onAdjustProof = { location, proof, delta -> gameState.adjustUserProofMark(location, proof, delta) },
        onMarkAllProofsImpossible = { location ->
          for (proof in Proof.entries) {
            gameState.markProofImpossible(location, proof)
          }
        },
        modifier = Modifier.fillMaxWidth().weight(1.0f)
      )
    }
  }
}

@Composable
private fun ProofInfoArea(
  eligibleLocations: Set<Location>,
  locationLayout: LocationLayout,
  locationStatesProvider: (Location) -> StateFlow<LocationUiState>,
  onAdjustProof: (Location, Proof, delta: Int) -> Unit,
  onMarkAllProofsImpossible: (Location) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    ProofInfoColumn(
      locations = locationLayout.leftLocations.filter { it in eligibleLocations },
      locationStatesProvider = locationStatesProvider,
      onAdjustProof = onAdjustProof,
      onMarkAllProofsImpossible = onMarkAllProofsImpossible,
      modifier = Modifier.weight(1.0f),
    )
    ProofInfoColumn(
      locations = locationLayout.rightLocations.filter { it in eligibleLocations },
      locationStatesProvider = locationStatesProvider,
      onAdjustProof = onAdjustProof,
      onMarkAllProofsImpossible = onMarkAllProofsImpossible,
      modifier = Modifier.weight(1.0f),
    )
  }
}

@Composable
private fun ProofInfoColumn(
  locations: List<Location>,
  locationStatesProvider: (Location) -> StateFlow<LocationUiState>,
  onAdjustProof: (Location, Proof, delta: Int) -> Unit,
  onMarkAllProofsImpossible: (Location) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    locations.forEach { location ->
      val locationState = locationStatesProvider(location)
      LocationProofInfoArea(
        locationState,
        onAdjustProof = { proof, delta -> onAdjustProof(location, proof, delta) },
        onMarkAllProofsImpossible = { onMarkAllProofsImpossible(location) },
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun LocationProofInfoArea(
  locationStates: StateFlow<LocationUiState>,
  onAdjustProof: (Proof, delta: Int) -> Unit,
  onMarkAllProofsImpossible: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val locationState by locationStates.collectAsState()
  val location = locationState.location
  val possibleProofs = locationState.possibleProofs
  val impossibleProofs = locationState.impossibleProofs
  val completed = locationState.counterState == LocationCounterState.Completed
  val noProofs = impossibleProofs.containsAll(Proof.entries)
  val colorScheme = MaterialTheme.colorScheme
  Surface(color = colorScheme.surfaceContainer) {
    Row(
      modifier = modifier.heightIn(max = 48.dp).padding(4.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
        Modifier.weight(1.0f)
          .onPointerEvent(PointerEventType.Scroll) { event ->
            val scrollDeltaY = event.changes.first().scrollDelta.y
            if (scrollDeltaY > 0) {
              onMarkAllProofsImpossible()
            }
          },
      ) {
        CustomizableIcon(
          location,
          contentDescription = location.localizedName,
          modifier = Modifier.align(Alignment.Center),
          alpha = if (noProofs || completed) GhostAlpha else DefaultAlpha,
        )

        if (completed) {
          CompletedIndicator(Modifier.fillMaxHeight(0.5f).align(Alignment.BottomEnd))
        }
      }

      for (proof in Proof.entries) {
        UserProofMark(
          proof,
          possible = proof in possibleProofs,
          impossible = proof in impossibleProofs,
          onAdjust = { delta -> onAdjustProof(proof, delta) },
          modifier = Modifier.weight(1.0f),
        )
      }
    }
  }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun UserProofMark(
  proof: Proof,
  possible: Boolean,
  impossible: Boolean,
  onAdjust: (delta: Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val tintOverride = if (impossible) {
    MaterialTheme.colorScheme.disabledItemTint
  } else {
    null
  }
  CustomizableIcon(
    proof,
    contentDescription = proof.localizedName,
    alpha = if (possible || tintOverride != null) DefaultAlpha else 0.15f,
    tintColorOverride = tintOverride,
    modifier = modifier
      .clickable {
        if (possible) {
          // Cycle back down to impossible by adjusting down twice
          onAdjust(-1)
          onAdjust(-1)
        } else {
          onAdjust(1)
        }
      }
      .onPointerEvent(PointerEventType.Scroll) { event ->
        val scrollDeltaY = event.changes.first().scrollDelta.y
        if (scrollDeltaY > 0) {
          onAdjust(-1)
        } else if (scrollDeltaY < 0) {
          onAdjust(1)
        }
      },
  )
}

@Composable
private fun SongIndicator(
  musicState: MusicState,
  songFolderAsGroup: Boolean,
  modifier: Modifier = Modifier,
) {
  Surface(
    color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor = musicState.battleStatus.color,
    modifier = modifier.fillMaxWidth().heightIn(min = 48.dp)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Icon(
        imageResource(Res.drawable.location_symphony_of_sorcery),
        contentDescription = stringResource(Res.string.extended_song),
        modifier = Modifier.size(32.dp)
      )

      AnimatedContent(targetState = musicState.song) { song ->
        Column(Modifier.weight(1.0f), verticalArrangement = Arrangement.Center) {
          Text(
            song.songName.ifEmpty { stringResource(Res.string.extended_no_music_state) },
            style = MaterialTheme.typography.titleMedium,
            fontFamily = khMenuFontFamily(),
          )

          if (songFolderAsGroup) {
            val group = song.group
            if (group.isNotEmpty()) {
              Text(group, style = MaterialTheme.typography.labelMedium)
            }
          }
        }
      }
    }
  }
}
