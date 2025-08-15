@file:OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)

package com.kh2rando.tracker.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferAction
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.DragAndDropTransferable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.kh2rando.tracker.model.gamestate.FullGameState
import com.kh2rando.tracker.model.gamestate.revealedButNotAcquiredItems
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.ChestUnlockKeyblade
import com.kh2rando.tracker.model.item.DriveForm
import com.kh2rando.tracker.model.item.HadesCupTrophy
import com.kh2rando.tracker.model.item.ImportantAbility
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.Magic
import com.kh2rando.tracker.model.item.MunnyPouch
import com.kh2rando.tracker.model.item.OlympusStone
import com.kh2rando.tracker.model.item.PromiseCharm
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.item.SummonCharm
import com.kh2rando.tracker.model.item.TornPage
import com.kh2rando.tracker.model.item.UniqueItem
import com.kh2rando.tracker.model.item.UnknownDisk
import com.kh2rando.tracker.model.item.VisitUnlock
import org.jetbrains.compose.resources.painterResource
import java.awt.datatransfer.StringSelection

@Composable
fun AvailableItemsLayout(gameState: FullGameState, modifier: Modifier = Modifier) {
  val enabledItemTypes = gameState.seed.settings.trackableItems
  val itemsByPrototype = gameState.allTrackableItems.groupBy(UniqueItem::prototype)

  val availableItems by gameState.availableItems.collectAsState()
  val availableByPrototype = availableItems.groupBy { it.prototype }

  val revealedButNotAcquiredItems by gameState.revealedButNotAcquiredItems.collectAsState(emptyList())
  val revealedButNotAcquiredByPrototype = revealedButNotAcquiredItems.groupBy { it }

  val ansemReportStrikes by gameState.ansemReportStrikes.collectAsState()

  @Composable
  fun ColumnScope.rowFromPrototypes(prototypes: List<ItemPrototype>) {
    val filteredPrototypes = prototypes.filter { it in enabledItemTypes }
    if (filteredPrototypes.isEmpty()) return

    AvailableItemsRow(
      entries = filteredPrototypes.map { prototype ->
        AvailableItemEntry(
          prototype = prototype,
          totalCopies = itemsByPrototype[prototype]!!.size,
          availableCount = availableByPrototype[prototype].orEmpty().size,
          revealedButNotAcquiredCount = revealedButNotAcquiredByPrototype[prototype].orEmpty().size,
          strikeCount = if (prototype is AnsemReport) ansemReportStrikes[prototype.ordinal] else 0,
        )
      },
      onTryAcquireItem = { prototype ->
        val selectedLocation = gameState.userSelectedLocations.value
        if (selectedLocation != null) {
          gameState.acquireItemManually(prototype, selectedLocation)
        }
      }
    )
  }

  Surface(modifier = modifier, color = MaterialTheme.colorScheme.surfaceContainer) {
    Column(modifier = Modifier) {
      rowFromPrototypes(AnsemReport.entries)
      rowFromPrototypes(Magic.entries + TornPage + MunnyPouch.entries)
      rowFromPrototypes(
        DriveForm.entries + SummonCharm.entries + ImportantAbility.entries + Proof.entries + PromiseCharm
      )
      rowFromPrototypes(ChestUnlockKeyblade.entries)
      rowFromPrototypes(
        listOf(
          VisitUnlock.BeastsClaw,
          VisitUnlock.BoneFist,
          VisitUnlock.ProudFang,
          VisitUnlock.BattlefieldsOfWar,
          VisitUnlock.SwordOfTheAncestor,
          VisitUnlock.SkillAndCrossbones,
          VisitUnlock.Scimitar,
          VisitUnlock.IdentityDisk,
        )
      )
      rowFromPrototypes(
        listOf(
          VisitUnlock.WayToTheDawn,
          VisitUnlock.MembershipCard,
          VisitUnlock.RoyalSummons,
          VisitUnlock.IceCream,
          VisitUnlock.NaminesSketches,
        ) + HadesCupTrophy + OlympusStone + UnknownDisk
      )
    }
  }
}

@Composable
private fun ColumnScope.AvailableItemsRow(
  entries: List<AvailableItemEntry>,
  onTryAcquireItem: (ItemPrototype) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.fillMaxWidth().weight(1.0f),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceEvenly,
  ) {
    for (entry in entries) {
      val prototype = entry.prototype
      Box(modifier = Modifier.weight(1.0f, fill = false), contentAlignment = Alignment.Center) {
        AvailableItemCell(
          entry = entry,
          onDoubleClick = { onTryAcquireItem(prototype) },
        )
      }
    }
  }
}

@Composable
private fun AvailableItemCell(
  entry: AvailableItemEntry,
  onDoubleClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val prototype = entry.prototype

  val customIconFile = prototype.findCustomIconFile()
  val defaultDragIconPainter = painterResource(prototype.defaultIcon)
  val dragIconPainter = if (customIconFile == null) {
    defaultDragIconPainter
  } else {
    rememberAsyncImagePainter(
      model = customIconFile,
      error = defaultDragIconPainter,
    )
  }

  val availableCount = entry.availableCount
  val strikeCount = entry.strikeCount

  val anyAvailable = availableCount > 0
  val interactable = anyAvailable && strikeCount < 3
  SimpleTooltipArea(tooltipText = entry.prototype.localizedName, modifier = modifier) {
    Box(
      modifier = Modifier.fillMaxHeight()
        .combinedClickable(
          enabled = interactable,
          interactionSource = null,
          indication = ripple(),
          onDoubleClick = onDoubleClick,
          onClick = {}
        )
        .itemDragAndDropSource(
          prototype = prototype,
          enabled = interactable,
          dragIconPainter = dragIconPainter,
          tint = if (customIconFile == null) prototype.color else Color.Unspecified,
        ),
      contentAlignment = Alignment.Center
    ) {
      val itemIconModifier = Modifier.heightIn(max = 36.dp)
      val totalCopies = entry.totalCopies
      when (totalCopies) {
        1 -> {
          val itemRenderState = if (anyAvailable) {
            if (entry.revealedButNotAcquiredCount > 0) ItemRenderState.Revealed else ItemRenderState.Default
          } else {
            ItemRenderState.Disabled
          }
          prototype.ItemIcon(modifier = itemIconModifier, renderState = itemRenderState, strikeCount = strikeCount)
        }

        else -> {
          val colorScheme = MaterialTheme.colorScheme
          val typography = MaterialTheme.typography

          val tintColor = if (anyAvailable) prototype.color else colorScheme.disabledItemTint
          BoxWithConstraints(Modifier.fillMaxHeight()) {
            val availableHeight = maxHeight

            Row(
              modifier = Modifier.align(Alignment.Center),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                availableCount.toString(),
                style = typography.headlineMedium.shrinkableToFitHeight(availableHeight),
                fontFamily = khMenuFontFamily(),
                color = tintColor,
              )

              val itemRenderState = if (anyAvailable) ItemRenderState.Default else ItemRenderState.Disabled
              prototype.ItemIcon(renderState = itemRenderState, modifier = itemIconModifier)
            }

            val revealedButNotAcquiredCount = entry.revealedButNotAcquiredCount
            if (revealedButNotAcquiredCount > 0) {
              OutlinedText(
                revealedButNotAcquiredCount.toString(),
                style = typography.titleMedium.shrinkableToFitHeight(availableHeight),
                color = colorScheme.onPrimary,
                outlineColor = LocalContentColor.current,
                outlineStroke = Stroke(4.0f, join = StrokeJoin.Round),
                fontFamily = khMenuFontFamily(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.TopCenter).offset(x = (-4).dp),
              )
            }
          }
        }
      }
    }
  }
}

private fun Modifier.itemDragAndDropSource(
  prototype: ItemPrototype,
  enabled: Boolean,
  dragIconPainter: Painter,
  tint: Color,
): Modifier {
  if (!enabled) {
    return this
  }
  return dragAndDropSource(
    drawDragDecoration = {
      drawIntoCanvas {
        val canvasSize = size
        with(dragIconPainter) {
          draw(
            size = canvasSize.copy(width = canvasSize.height),
            alpha = 0.9f,
            colorFilter = tint.tintFilterOrNull()
          )
        }
      }
    }
  ) {
    val stringData = prototype.gameId.value.toString()
    DragAndDropTransferData(
      transferable = DragAndDropTransferable(StringSelection(stringData)),
      supportedActions = listOf(DragAndDropTransferAction.Move),
    )
  }
}

private data class AvailableItemEntry(
  val prototype: ItemPrototype,
  val totalCopies: Int,
  val availableCount: Int,
  val revealedButNotAcquiredCount: Int,
  val strikeCount: Int,
)
