@file:OptIn(ExperimentalMaterial3Api::class)

package com.kh2rando.tracker.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.util.fastCoerceAtMost
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.expended_path_cheat_sheet
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
import com.kh2rando.tracker.model.hints.PathHintSystem
import com.kh2rando.tracker.model.item.DriveForm
import com.kh2rando.tracker.model.item.ImportantAbility
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.Magic
import com.kh2rando.tracker.model.item.MunnyPouch
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.item.SummonCharm
import com.kh2rando.tracker.model.item.TornPage
import com.kh2rando.tracker.model.item.VisitUnlock
import com.kh2rando.tracker.model.preferences.TrackerPreferences
import com.kh2rando.tracker.model.preferences.collectAsState
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.milliseconds

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
  val enabledLocations = gameState.seed.settings.enabledLocations

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
      val proofEligibleLocations = enabledLocations - Location.GardenOfAssemblage
      Surface(color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
          Text(stringResource(Res.string.extended_misc_proof_information), modifier = Modifier.padding(4.dp))

          Row(Modifier.background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f))) {
            for (proof in Proof.entries) {
              HeaderProofShortcut(proof, onInteract = {
                for (location in proofEligibleLocations) {
                  gameState.markProofImpossible(location, proof)
                }
              })
            }
          }
        }
      }

      val pathHints = gameState.seed.settings.hintSystem is PathHintSystem
      ProofInfoArea(
        eligibleLocations = proofEligibleLocations,
        locationLayout = locationLayout,
        locationStatesProvider = { location -> gameState.locationUiStates.getValue(location) },
        onAdjustProof = { location, proof, delta -> gameState.adjustUserProofMark(location, proof, delta) },
        onMarkAllProofsImpossible = { location ->
          for (proof in Proof.entries) {
            gameState.markProofImpossible(location, proof)
          }
        },
        pathHints = pathHints,
        modifier = Modifier.fillMaxWidth(),
      )

      Spacer(Modifier.height(8.dp))

      if (pathHints) {
        SmallHeader(stringResource(Res.string.expended_path_cheat_sheet))
        PathHintsCheatSheet(
          enabledLocations = enabledLocations,
          modifier = Modifier.fillMaxWidth(),
        )
      }
    }
  }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun HeaderProofShortcut(
  proof: Proof,
  onInteract: () -> Unit,
  modifier: Modifier = Modifier,
) {
  CustomizableIcon(
    proof,
    contentDescription = proof.localizedName(),
    modifier = modifier.size(36.dp)
      .clickable { onInteract() }
      .onPointerEvent(PointerEventType.Scroll) { event ->
        val scrollDeltaY = event.changes.first().scrollDelta.y
        if (scrollDeltaY != 0.0f) {
          onInteract()
        }
      },
  )
}

@Composable
private fun ProofInfoArea(
  eligibleLocations: Set<Location>,
  locationLayout: LocationLayout,
  locationStatesProvider: (Location) -> StateFlow<LocationUiState>,
  onAdjustProof: (Location, Proof, delta: Int) -> Unit,
  onMarkAllProofsImpossible: (Location) -> Unit,
  pathHints: Boolean,
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
      pathHints = pathHints,
      modifier = Modifier.weight(1.0f),
    )
    ProofInfoColumn(
      locations = locationLayout.rightLocations.filter { it in eligibleLocations },
      locationStatesProvider = locationStatesProvider,
      onAdjustProof = onAdjustProof,
      onMarkAllProofsImpossible = onMarkAllProofsImpossible,
      pathHints = pathHints,
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
  pathHints: Boolean,
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
        pathHints = pathHints,
        modifier = Modifier.fillMaxWidth(),
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
  pathHints: Boolean,
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
        ProofLocationsLocation(location = location, pathHints = pathHints) {
          CustomizableIcon(
            location,
            contentDescription = location.localizedName,
            modifier = Modifier.align(Alignment.Center),
            alpha = if (noProofs || completed) GhostAlpha else DefaultAlpha,
          )
        }

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
    contentDescription = proof.localizedName(),
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
private fun PathHintsCheatSheet(
  enabledLocations: Set<Location>,
  modifier: Modifier = Modifier,
) {
  fun enabled(vararg locations: Location): List<Location> {
    return locations.filter { it in enabledLocations }
  }

  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Column(modifier = Modifier.weight(4.0f)) {
      PathHintBox(
        item = Magic.Fire,
        locations = enabled(Location.HollowBastion, Location.Agrabah, Location.PrideLands),
      )
      PathHintBox(
        item = Magic.Blizzard,
        locations = enabled(Location.HollowBastion, Location.Atlantica),
      )
      PathHintBox(
        item = Magic.Thunder,
        locations = enabled(Location.OlympusColiseum, Location.LandOfDragons, Location.PrideLands),
      )
      PathHintBox(
        item = Magic.Cure,
        locations = enabled(Location.HundredAcreWood, Location.BeastsCastle, Location.HollowBastion),
      )
      PathHintBox(
        item = Magic.Magnet,
        locations = enabled(Location.WorldThatNeverWas, Location.PortRoyal, Location.HalloweenTown),
      )
      PathHintBox(
        item = Magic.Reflect,
        locations = enabled(Location.DisneyCastle, Location.SpaceParanoids, Location.BeastsCastle),
      )
    }
    Column(modifier = Modifier.weight(4.0f)) {
      PathHintBox(
        item = TornPage,
        locations = enabled(
          Location.HollowBastion,
          Location.Agrabah,
          Location.PrideLands,
          Location.DisneyCastle,
          Location.LandOfDragons,
          Location.HundredAcreWood,
        ),
      )
      PathHintBox(
        item = DriveForm.ValorFormDummy,
        locations = enabled(Location.TwilightTown, Location.SimulatedTwilightTown, Location.DriveForms),
      )
      PathHintBox(
        item = DriveForm.WisdomForm,
        locations = enabled(Location.DisneyCastle, Location.DriveForms),
      )
      PathHintBox(
        item = DriveForm.LimitForm,
        locations = enabled(Location.TwilightTown, Location.SimulatedTwilightTown, Location.DriveForms),
      )
      PathHintBox(
        item = DriveForm.MasterForm,
        locations = enabled(Location.HollowBastion, Location.DriveForms),
      )
      PathHintBox(
        item = DriveForm.FinalFormDummy,
        locations = enabled(Location.DriveForms),
      )
    }
    Column(modifier = Modifier.weight(2.0f)) {
      PathHintBox(
        item = ImportantAbility.OnceMore,
        locations = enabled(Location.SoraLevels),
      )
      PathHintBox(
        item = ImportantAbility.SecondChance,
        locations = enabled(Location.SoraLevels),
      )
      PathHintBox(
        item = SummonCharm.BaseballCharm,
        locations = enabled(Location.HollowBastion),
      )
      PathHintBox(
        item = SummonCharm.UkuleleCharm,
        locations = enabled(Location.HollowBastion),
      )
      PathHintBox(
        item = SummonCharm.LampCharm,
        locations = enabled(Location.Agrabah),
      )
      PathHintBox(
        item = SummonCharm.FeatherCharm,
        locations = enabled(Location.PortRoyal),
      )
    }
    Column(modifier = Modifier.weight(2.0f)) {
      for (unlock in VisitUnlock.entries.subList(0, 7)) {
        PathHintBox(
          item = unlock,
          locations = enabled(unlock.associatedLocation),
        )
      }
    }
    Column(modifier = Modifier.weight(2.0f)) {
      for (unlock in VisitUnlock.entries.subList(7, 13)) {
        PathHintBox(
          item = unlock,
          locations = enabled(unlock.associatedLocation),
        )
      }
    }
  }
}

@Composable
private fun PathHintBox(
  item: ItemPrototype,
  locations: List<Location>,
  modifier: Modifier = Modifier,
) {
  if (locations.isEmpty()) {
    return
  }
  Box(
    modifier = modifier.heightIn(max = 64.dp)
      .border(width = 1.dp, color = MaterialTheme.colorScheme.surfaceContainerHigh),
  ) {
    Row(
      modifier = Modifier.padding(vertical = 2.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      CustomizableIcon(item, contentDescription = item.localizedName(), modifier = Modifier.size(24.dp))
      LazyVerticalGrid(
        columns = GridCells.Fixed(locations.size.fastCoerceAtMost(3)),
        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer),
      ) {
        items(locations) { location ->
          CustomizableIcon(
            icon = location,
            contentDescription = location.localizedName,
            modifier = Modifier.size(24.dp),
          )
        }
      }
    }
  }
}

@Composable
private fun ProofLocationsLocation(
  location: Location,
  modifier: Modifier = Modifier,
  pathHints: Boolean,
  content: @Composable () -> Unit,
) {
  if (pathHints) {
    val colorScheme = MaterialTheme.colorScheme
    val tooltipBorderColor = colorScheme.onSurface
    TrackerTooltipArea(
      modifier = modifier,
      delay = 500.milliseconds,
      tooltip = {
        val items = location.pathHintItems()
        val columns = items.size.fastCoerceAtMost(5)
        Surface(
          modifier = Modifier.widthIn(max = 32.dp * columns),
          color = colorScheme.surfaceContainerLow,
          border = BorderStroke(width = 1.dp, color = tooltipBorderColor)
        ) {
          LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
          ) {
            items(items) { item ->
              CustomizableIcon(
                icon = item,
                contentDescription = item.localizedName(),
                modifier = Modifier.size(24.dp),
              )
            }
          }
        }
      },
      content = content,
    )
  } else {
    content()
  }
}

private fun Location.pathHintItems(): List<ItemPrototype> {
  return when (this) {
    Location.SoraLevels -> {
      listOf(ImportantAbility.SecondChance, ImportantAbility.OnceMore)
    }

    Location.SimulatedTwilightTown -> {
      listOf(VisitUnlock.NaminesSketches, DriveForm.ValorFormDummy, DriveForm.LimitForm)
    }

    Location.HollowBastion -> {
      listOf(
        VisitUnlock.MembershipCard,
        Magic.Blizzard,
        Magic.Fire,
        SummonCharm.BaseballCharm,
        VisitUnlock.MembershipCard,
        DriveForm.MasterForm,
        SummonCharm.UkuleleCharm,
        Magic.Blizzard,
        Magic.Cure,
        TornPage,
      )
    }

    Location.OlympusColiseum -> {
      listOf(VisitUnlock.BattlefieldsOfWar, Magic.Thunder, VisitUnlock.BattlefieldsOfWar)
    }

    Location.LandOfDragons -> {
      listOf(VisitUnlock.SwordOfTheAncestor, VisitUnlock.SwordOfTheAncestor, TornPage, Magic.Thunder)
    }

    Location.PrideLands -> {
      listOf(VisitUnlock.ProudFang, TornPage, Magic.Fire, VisitUnlock.ProudFang, Magic.Thunder)
    }

    Location.HalloweenTown -> {
      listOf(VisitUnlock.BoneFist, Magic.Magnet, VisitUnlock.BoneFist)
    }

    Location.SpaceParanoids -> {
      listOf(VisitUnlock.IdentityDisk, VisitUnlock.IdentityDisk, Magic.Reflect)
    }

    Location.GardenOfAssemblage -> {
      emptyList()
    }

    Location.DriveForms -> {
      listOf(
        DriveForm.ValorFormDummy,
        DriveForm.WisdomForm,
        DriveForm.LimitForm,
        DriveForm.MasterForm,
        DriveForm.FinalFormDummy,
      )
    }

    Location.TwilightTown -> {
      listOf(
        VisitUnlock.IceCream,
        DriveForm.ValorFormDummy,
        VisitUnlock.IceCream,
        DriveForm.LimitForm,
        VisitUnlock.IceCream,
      )
    }

    Location.BeastsCastle -> {
      listOf(VisitUnlock.BeastsClaw, Magic.Cure, VisitUnlock.BeastsClaw, Magic.Reflect)
    }

    Location.Agrabah -> {
      listOf(VisitUnlock.Scimitar, SummonCharm.LampCharm, VisitUnlock.Scimitar, TornPage, Magic.Fire)
    }

    Location.HundredAcreWood -> {
      listOf(Magic.Cure, TornPage)
    }

    Location.DisneyCastle -> {
      listOf(VisitUnlock.RoyalSummons, TornPage, VisitUnlock.RoyalSummons, Magic.Reflect, DriveForm.WisdomForm)
    }

    Location.PortRoyal -> {
      listOf(VisitUnlock.SkillAndCrossbones, VisitUnlock.SkillAndCrossbones, SummonCharm.FeatherCharm, Magic.Magnet)
    }

    Location.WorldThatNeverWas -> {
      listOf(VisitUnlock.WayToTheDawn, Magic.Magnet, VisitUnlock.WayToTheDawn)
    }

    Location.Atlantica -> {
      listOf(Magic.Blizzard)
    }

    Location.Creations -> {
      emptyList()
    }
  }
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
