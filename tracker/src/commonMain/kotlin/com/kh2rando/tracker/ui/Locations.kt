@file:OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)

package com.kh2rando.tracker.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferAction
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draganddrop.dragData
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.ansem_report
import com.kh2rando.tracker.generated.resources.desc_locked_visit
import com.kh2rando.tracker.generated.resources.desc_no_path_to_light
import com.kh2rando.tracker.generated.resources.hint_count_adjusted_by_reveals
import com.kh2rando.tracker.generated.resources.hint_hinted_hint
import com.kh2rando.tracker.generated.resources.max_form_level_text
import com.kh2rando.tracker.generated.resources.max_form_level_tooltip
import com.kh2rando.tracker.generated.resources.proof_blank
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.GameId
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.gamestate.FullGameState
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.hints.LocationAuxiliaryHintInfo
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.DreamWeapon
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.item.UniqueItem
import com.kh2rando.tracker.model.preferences.TrackerPreferences
import com.kh2rando.tracker.model.preferences.collectAsState
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.milliseconds

private const val LocationHeaderWidthDp = 80

private sealed interface LocationItem {

  data class Acquired(val item: UniqueItem) : LocationItem

  data class Revealed(val prototype: ItemPrototype) : LocationItem

}

@Immutable
private class LocationItemsArrangement(uiState: LocationUiState, itemsPerRow: Int) {

  /**
   * All of the items that should be shown, split into rows based on the configured number of items per row.
   */
  val arrangedItems: List<List<LocationItem>> = run {
    val combinedList = buildList {
      for (item in uiState.acquiredItems) {
        add(LocationItem.Acquired(item))
      }
      for (prototype in uiState.revealedButNotAcquiredPrototypes) {
        add(LocationItem.Revealed(prototype))
      }
    }
    combinedList.chunked(itemsPerRow)
  }

  /**
   * The number of rows in this arrangement.
   */
  val itemRowCount: Int = arrangedItems.size.coerceAtLeast(1)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is LocationItemsArrangement) return false
    return arrangedItems == other.arrangedItems
  }

  override fun hashCode(): Int = arrangedItems.hashCode()

  override fun toString(): String = arrangedItems.toString()

}

@Composable
fun LocationsLayout(
  gameState: FullGameState,
  preferences: TrackerPreferences,
  modifier: Modifier = Modifier,
) {
  BoxWithConstraints(modifier.fillMaxSize()) {
    val availableSize = (maxWidth / 2 - LocationHeaderWidthDp.dp).coerceAtLeast(0.dp)
    var itemsPerRow = 5
    if (availableSize > (36.dp * 7)) {
      itemsPerRow = 7
    }

    val seedSettings = gameState.seed.settings
    val enabledLocations = seedSettings.enabledLocations

    val progressContent: @Composable (LocationUiState) -> Unit = { locationUiState ->
      when (locationUiState.location) {
        Location.SoraLevels -> {
          val soraState by gameState.soraStates.collectAsState()
          DreamWeaponIndicator(dreamWeapon = soraState.dreamWeapon)
        }

        Location.DriveForms -> {
          val driveFormsState by gameState.driveFormsStates.collectAsState()
          DriveFormMaximumLevelIndicator(maximumLevel = driveFormsState.maximumDriveFormLevel())
        }

        else -> {
          ProgressCheckpointIndicator(progress = locationUiState.progressIndicator)
        }
      }
    }
    val reportHintInfoProvider: (AnsemReport) -> HintInfo? = { report ->
      seedSettings.hintSystem.hintInfoForAcquiredReport(report)
    }

    Row(Modifier.fillMaxSize()) {
      val trackerLayout by preferences.locationLayout.collectAsState()
      val provideLocationStates: (Location) -> StateFlow<LocationUiState> = { location ->
        gameState.locationUiStates.getValue(location)
      }
      val locationIconSupplier: @Composable (Location) -> HasCustomizableIcon = { location ->
        if (location == Location.Creations) {
          CreationsIcon.fromSeedSettings(seedSettings)
        } else {
          location
        }
      }
      LocationsColumn(
        itemsPerRow = itemsPerRow,
        locations = trackerLayout.leftLocations.filter { it in enabledLocations },
        locationUiStates = provideLocationStates,
        locationIconSupplier = locationIconSupplier,
        progressContent = progressContent,
        reportHintInfoProvider = reportHintInfoProvider,
        modifier = Modifier.weight(1.0f),
      )
      LocationsColumn(
        itemsPerRow = itemsPerRow,
        locations = trackerLayout.rightLocations.filter { it in enabledLocations },
        locationUiStates = provideLocationStates,
        locationIconSupplier = locationIconSupplier,
        progressContent = progressContent,
        reportHintInfoProvider = reportHintInfoProvider,
        modifier = Modifier.weight(1.0f),
      )
    }
  }
}

@Composable
private fun LocationsColumn(
  itemsPerRow: Int,
  locations: List<Location>,
  locationUiStates: (Location) -> StateFlow<LocationUiState>,
  locationIconSupplier: @Composable (Location) -> HasCustomizableIcon,
  progressContent: @Composable (LocationUiState) -> Unit,
  reportHintInfoProvider: (AnsemReport) -> HintInfo?,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(1.dp)) {
    for (location in locations) {
      val locationUiState by locationUiStates(location).collectAsState()
      // TODO: Try to be dynamic about itemsPerRow
      val itemsArrangement = LocationItemsArrangement(locationUiState, itemsPerRow = 5)
      val weight = locationRowWeight(thisLocationItemRowCount = itemsArrangement.itemRowCount)
      LocationRow(
        location = location,
        locationUiState = locationUiState,
        locationIconSupplier = locationIconSupplier,
        itemsPerRow = itemsPerRow,
        itemsArrangement = itemsArrangement,
        progressContent = { progressContent(locationUiState) },
        reportHintInfoProvider = reportHintInfoProvider,
        modifier = Modifier.weight(weight)
      )
    }
  }
}

@Composable
private fun LocationRow(
  location: Location,
  locationUiState: LocationUiState,
  itemsPerRow: Int,
  locationIconSupplier: @Composable (Location) -> HasCustomizableIcon,
  itemsArrangement: LocationItemsArrangement,
  progressContent: @Composable () -> Unit,
  reportHintInfoProvider: (AnsemReport) -> HintInfo?,
  modifier: Modifier = Modifier,
) {
  val isUserSelectedLocation = locationUiState.isUserSelectedLocation
  LocationRowContent(
    isUserSelectedLocation = isUserSelectedLocation,
    headerContent = {
      val counterState = locationUiState.counterState

      LocationHeader(
        location = location,
        locationIconSupplier = locationIconSupplier,
        selectedLocation = isUserSelectedLocation,
        lockedVisitContent = {
          LockArea(lockCount = locationUiState.lockedVisitCount)
        },
        progressContent = progressContent,
        counterState = counterState,
        counterContent = { CounterArea(counterState = counterState) },
        userMarkContent = {
          val userProofMarks = locationUiState.userProofMarks
          val markedProofCount = userProofMarks.size
          if (markedProofCount == 0) {
            AnimatedContent(locationUiState.userMarkIcon) { icon -> icon.Content(Modifier) }
          } else {
            BoxWithConstraints(contentAlignment = Alignment.Center) {
              if (markedProofCount > 1) {
                Image(
                  imageResource(Res.drawable.proof_blank),
                  contentDescription = null,
                  alpha = 0.8f,
                )
                CounterText(
                  markedProofCount.toString(),
                  maximumHeight = maxHeight / 2,
                  color = LocalContentColor.current.copy(alpha = 0.8f),
                )
              } else {
                val proofMark = userProofMarks.first()
                CustomizableIcon(
                  icon = proofMark,
                  contentDescription = stringResource(proofMark.displayString),
                  alpha = 0.8f,
                )
              }
            }
          }
        },
        onLocationClick = { locationUiState.toggleUserSelection() },
        onScrollLocationMark = { delta ->
          if (location != Location.GardenOfAssemblage) { // GoA doesn't need to support marking
            locationUiState.adjustUserMark(delta)
          }
        }
      )
    },
    hintsAreaContent = {
      HintsArea(
        hintInfo = locationUiState.auxiliaryHintInfo,
        surfaceColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier
      )
    },
    locationItemsContent = {
      LocationItemsArea(
        isDetectedLocation = locationUiState.isAutoDetectedLocation,
        itemsPerRow = itemsPerRow,
        itemsArrangement = itemsArrangement,
        reportHintInfoProvider = reportHintInfoProvider,
        onItemClick = { item -> locationUiState.rejectItemManually(item) },
        modifier = Modifier
      )
    },
    onItemDropped = locationUiState::tryAcquireItemManuallyByGameId,
    modifier = modifier,
  )
}

@Composable
private fun DreamWeaponIndicator(dreamWeapon: DreamWeapon, modifier: Modifier = Modifier) {
  val displayString = stringResource(dreamWeapon.displayString)
  SimpleTooltipArea(tooltipText = displayString, modifier = modifier) {
    CustomizableIcon(
      icon = dreamWeapon,
      contentDescription = displayString,
      modifier = Modifier.fillMaxSize(),
    )
  }
}

@Composable
private fun DriveFormMaximumLevelIndicator(maximumLevel: Int, modifier: Modifier = Modifier) {
  SimpleTooltipArea(tooltipText = stringResource(Res.string.max_form_level_tooltip), modifier = modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      val typography = MaterialTheme.typography
      val fontFamily = khMenuFontFamily()
      Text(
        text = stringResource(Res.string.max_form_level_text),
        style = typography.labelSmall.shrinkableToFitHeight(12.dp),
        fontFamily = fontFamily
      )
      OutlinedText(
        textString = maximumLevel.toString(),
        color = ColorToken.Gold.color,
        outlineColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        outlineStroke = Stroke(width = 2.0f, join = StrokeJoin.Round),
        style = typography.labelLarge.shrinkableToFitHeight(32.dp),
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
        modifier = Modifier.weight(1.0f)
      )
    }
  }
}

@Composable
private fun ProgressCheckpointIndicator(
  progress: LocationProgressIndicator?,
  modifier: Modifier = Modifier
) {
  if (progress != null) {
    val displayString = stringResource(progress.displayString)
    SimpleTooltipArea(tooltipText = displayString, modifier = modifier) {
      CustomizableIcon(
        icon = progress.icon,
        contentDescription = displayString,
        modifier = Modifier.fillMaxSize(),
      )
    }
  }
}

@Composable
private fun LocationRowContent(
  isUserSelectedLocation: Boolean,
  headerContent: @Composable () -> Unit,
  hintsAreaContent: @Composable () -> Unit,
  locationItemsContent: @Composable () -> Unit,
  onItemDropped: (GameId) -> Unit,
  modifier: Modifier = Modifier,
) {
  var showDropIndication by remember { mutableStateOf(false) }
  val dropTarget = remember {
    object : DragAndDropTarget {
      override fun onEntered(event: DragAndDropEvent) {
        showDropIndication = true
      }

      override fun onExited(event: DragAndDropEvent) {
        showDropIndication = false
      }

      override fun onDrop(event: DragAndDropEvent): Boolean {
        val gameId = event.parseGameId()
        return if (gameId == null) {
          false
        } else {
          onItemDropped(gameId)
          true
        }
      }

      override fun onEnded(event: DragAndDropEvent) {
        showDropIndication = false
      }
    }
  }

  val colorScheme = MaterialTheme.colorScheme
  val dropIndicationColor = colorScheme.onSurface
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .border(
        width = 1.dp,
        color = colorScheme.colorForSelectedOrNot(isUserSelectedLocation, defaultColor = Color.Transparent)
      )
      .dragAndDropTarget(
        shouldStartDragAndDrop = { event -> event.parseGameId() != null },
        target = dropTarget,
      )
      .drawWithContent {
        drawContent()

        if (showDropIndication) {
          drawRect(color = dropIndicationColor, alpha = 0.2f)
        }
      },
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      headerContent()
      hintsAreaContent()
      locationItemsContent()
    }
  }
}

@Composable
private fun LocationHeader(
  location: Location,
  locationIconSupplier: @Composable (Location) -> HasCustomizableIcon,
  selectedLocation: Boolean,
  lockedVisitContent: @Composable () -> Unit,
  progressContent: @Composable () -> Unit,
  counterState: LocationCounterState,
  counterContent: @Composable BoxWithConstraintsScope.() -> Unit,
  userMarkContent: @Composable () -> Unit,
  onLocationClick: () -> Unit,
  onScrollLocationMark: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val locationComplete = counterState == LocationCounterState.Completed

  val colorScheme = MaterialTheme.colorScheme
  Surface(
    onClick = onLocationClick,
    modifier = modifier
      .width(LocationHeaderWidthDp.dp)
      .fillMaxHeight()
      .onPointerEvent(PointerEventType.Scroll) { event ->
        val scrollDeltaY = event.changes.first().scrollDelta.y
        if (scrollDeltaY > 0) {
          onScrollLocationMark(1)
        } else if (scrollDeltaY < 0) {
          onScrollLocationMark(-1)
        }
      },
    // Using surface explicitly here on top of a surfaceContainer is a little hacky but it helps visually
    color = colorScheme.colorForSelectedOrNot(selectedLocation, defaultColor = colorScheme.surface)
  ) {
    Box(modifier = Modifier.padding(1.dp)) {
      // Bottom layer - just the image
      Box(
        modifier = Modifier.fillMaxSize().padding(PaddingValues(horizontal = 12.dp)),
        contentAlignment = Alignment.Center
      ) {
        CustomizableIcon(
          icon = locationIconSupplier(location),
          contentDescription = location.localizedName,
          alpha = if (locationComplete) GhostAlpha else DefaultAlpha,
          modifier = Modifier.fillMaxSize(),
        )
      }

      Box(Modifier.fillMaxHeight().align(Alignment.TopStart)) {
        Row {
          Box(Modifier.width(16.dp).fillMaxHeight()) {
            lockedVisitContent()
          }

          Column(Modifier.widthIn(max = 32.dp).fillMaxHeight()) {
            Box(Modifier.weight(2.0f)) {
              userMarkContent()
            }
            Spacer(Modifier.weight(1.0f))
          }
        }
      }

      Column(
        modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Box(
          modifier = Modifier.weight(6.0f).sizeIn(maxWidth = 32.dp, maxHeight = 32.dp),
          contentAlignment = Alignment.TopEnd
        ) {
          progressContent()
        }

        BoxWithConstraints(modifier = Modifier.weight(4.0f), contentAlignment = Alignment.BottomEnd) {
          counterContent()
        }
      }
    }

  }
}

@Composable
private fun LockArea(lockCount: Int, modifier: Modifier = Modifier) {
  Column(
    modifier = modifier.fillMaxHeight(),
    verticalArrangement = Arrangement.spacedBy(1.dp, Alignment.CenterVertically),
  ) {
    repeat(lockCount) {
      Icon(
        Icons.Filled.Lock,
        contentDescription = stringResource(Res.string.desc_locked_visit),
        modifier = Modifier.fillMaxWidth().weight(1.0f, fill = false),
        tint = ColorToken.Gold.color.copy(alpha = 0.75f),
      )
    }
  }
}

@Composable
private fun BoxWithConstraintsScope.CounterArea(
  counterState: LocationCounterState,
  modifier: Modifier = Modifier,
) {
  AnimatedContent(targetState = counterState) { targetState ->
    when (targetState) {
      LocationCounterState.None -> {

      }

      LocationCounterState.Unrevealed -> {
        val color = LocalContentColor.current.copy(alpha = 0.5f)
        CounterText(textString = "?", maximumHeight = maxHeight, modifier = modifier, color = color)
      }

      is LocationCounterState.Revealed -> {
        val value = targetState.value
        val color = if (value < 0) {
          ColorToken.Salmon.color
        } else if (targetState.adjustedByRevealedItems) {
          ColorToken.Green.color
        } else {
          Color.Unspecified
        }
        CounterText(textString = value.toString(), maximumHeight = maxHeight, color = color, modifier = modifier)
      }

      LocationCounterState.Completed -> {
        CompletedIndicator(modifier)
      }
    }
  }
}

@Composable
private fun HintsArea(
  hintInfo: LocationAuxiliaryHintInfo,
  surfaceColor: Color,
  modifier: Modifier = Modifier,
) {
  val childModifier = modifier.width(24.dp).fillMaxHeight()
  when (hintInfo) {
    is LocationAuxiliaryHintInfo.NotApplicableToHintSystem -> {
      // Actually do nothing here, hint system doesn't support them at all
    }

    is LocationAuxiliaryHintInfo.Blank -> {
      Surface(modifier = childModifier, color = surfaceColor) {

      }
    }

    is LocationAuxiliaryHintInfo.HintedHint -> {
      Surface(modifier = childModifier, color = surfaceColor) {
        HintedHintArea()
      }
    }

    is LocationAuxiliaryHintInfo.CountAdjustedByRevealedItems -> {
      Surface(modifier = childModifier, color = surfaceColor) {
        AdjustedCountArea()
      }
    }

    is HintInfo.PathToProofs -> {
      Surface(modifier = childModifier, color = surfaceColor) {
        PathHintsArea(hintInfo = hintInfo)
      }
    }
  }
}

@Composable
private fun HintedHintArea(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceAround
  ) {
    val displayText = stringResource(Res.string.hint_hinted_hint)
    SimpleTooltipArea(tooltipText = displayText) {
      Image(imageResource(Res.drawable.ansem_report), contentDescription = displayText)
    }
  }
}

@Composable
private fun AdjustedCountArea(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceAround
  ) {
    val displayText = stringResource(Res.string.hint_count_adjusted_by_reveals)
    SimpleTooltipArea(tooltipText = displayText) {
      Icon(
        Icons.Default.Add,
        contentDescription = displayText,
        tint = ColorToken.Green.color
      )
    }
  }
}

@Composable
private fun PathHintsArea(hintInfo: HintInfo.PathToProofs, modifier: Modifier = Modifier) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceAround
  ) {
    val pathToProofs = hintInfo.proofs
    if (pathToProofs.isEmpty()) {
      CustomizableIcon(
        icon = SystemIcon.Prohibition,
        contentDescription = stringResource(Res.string.desc_no_path_to_light),
        modifier = Modifier.padding(4.dp)
      )
    } else {
      for (proof in Proof.entries) {
        if (proof in pathToProofs) {
          CustomizableIcon(
            icon = proof,
            contentDescription = proof.localizedName,
            modifier = Modifier.weight(1.0f),
          )
        } else {
          Spacer(Modifier.weight(1.0f))
        }
      }
    }
  }
}

@Composable
private fun LocationItemsArea(
  isDetectedLocation: Boolean,
  itemsPerRow: Int,
  itemsArrangement: LocationItemsArrangement,
  reportHintInfoProvider: (AnsemReport) -> HintInfo?,
  onItemClick: (UniqueItem) -> Unit,
  modifier: Modifier = Modifier,
) {
  val colorScheme = MaterialTheme.colorScheme

  Surface(
    modifier = modifier.fillMaxSize(),
    color = if (isDetectedLocation) colorScheme.surfaceContainerHigh else colorScheme.surfaceContainer
  ) {
    Column {
      for (chunk in itemsArrangement.arrangedItems) {
        Row(Modifier.fillMaxWidth().weight(1.0f)) {
          val chunkSize = chunk.size
          for (locationItem in chunk) {
            when (locationItem) {
              is LocationItem.Acquired -> {
                val item = locationItem.item
                val prototype = item.prototype
                val reportHintInfo = if (prototype is AnsemReport) {
                  reportHintInfoProvider(prototype).takeUnless { hint ->
                    hint is HintInfo.JournalTextOnly && hint.journalText.isNullOrEmpty()
                  }
                } else {
                  null
                }
                if (reportHintInfo == null) {
                  SimpleTooltipArea(
                    tooltipText = prototype.localizedName,
                    modifier = Modifier.weight(1.0f).fillMaxHeight()
                  ) {
                    prototype.ItemIcon(
                      modifier = Modifier.fillMaxSize().clickable(onClick = { onItemClick(item) })
                    )
                  }
                } else {
                  ReportHintTooltipArea(reportHintInfo, modifier = Modifier.weight(1.0f).fillMaxHeight()) {
                    prototype.ItemIcon(
                      modifier = Modifier.fillMaxSize().clickable(onClick = { onItemClick(item) })
                    )
                  }
                }
              }

              is LocationItem.Revealed -> {
                SimpleTooltipArea(
                  tooltipText = locationItem.prototype.localizedName,
                  modifier = Modifier.weight(1.0f).fillMaxHeight()
                ) {
                  locationItem.prototype.ItemIcon(
                    renderState = ItemRenderState.Revealed,
                    modifier = Modifier.fillMaxSize()
                  )
                }
              }
            }
          }
          repeat(itemsPerRow - chunkSize) {
            Spacer(Modifier.weight(1.0f))
          }
        }
      }
    }
  }
}

@Composable
private fun ReportHintTooltipArea(
  hintInfo: HintInfo,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit,
) {
  val colorScheme = MaterialTheme.colorScheme
  val tooltipSurfaceShape = MaterialTheme.shapes.medium
  val tooltipBorderColor = colorScheme.onSurface
  TrackerTooltipArea(
    modifier = modifier,
    delay = 0.milliseconds,
    tooltip = {
      Surface(shape = tooltipSurfaceShape, color = colorScheme.surfaceContainerLow) {
        FullHintInfoContent(
          hintInfo = hintInfo,
          modifier = Modifier
            .border(1.dp, color = tooltipBorderColor, shape = tooltipSurfaceShape)
            .padding(horizontal = 8.dp, vertical = 4.dp)
        )
      }
    },
    content = content,
  )
}

private fun DragAndDropEvent.parseGameId(): GameId? {
  if (action != DragAndDropTransferAction.Move) {
    return null
  }

  when (val dragData = dragData()) {
    is DragData.Text -> {
      val gameId = dragData.readText().toIntOrNull()?.let { GameId(it) } ?: return null
      return gameId
    }
  }

  return null
}

private fun ColorScheme.colorForSelectedOrNot(selectedLocation: Boolean, defaultColor: Color): Color {
  return if (selectedLocation) secondaryContainer else defaultColor
}

private fun locationRowWeight(thisLocationItemRowCount: Int): Float {
  // The idea here is to give a good amount of weight for 1-2 rows, but start to constrict down
  // fairly rapidly once we reach 3+, with a cap at 3x the normal size
  return when (thisLocationItemRowCount) {
    1 -> 1.0f
    2 -> 1.7f
    3 -> 2.2f
    else -> 2.5f
  }
}

