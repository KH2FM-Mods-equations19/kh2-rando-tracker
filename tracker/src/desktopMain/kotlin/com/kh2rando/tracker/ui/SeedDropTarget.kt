@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)

package com.kh2rando.tracker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.main_drop_seed_here
import com.kh2rando.tracker.generated.resources.randomizer_logo
import com.kh2rando.tracker.io.TrackerFileHandler
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.gamestate.BaseGameState
import com.kh2rando.tracker.model.hints.HintInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import java.io.File
import java.net.URI

enum class DropFileType {

  SeedZip,
  TrackerSave,
  Unknown,

}

object SeedDropTarget {

  fun categorizeDroppedFile(file: File): DropFileType {
    val extension = file.extension
    return if (extension.equals("zip", ignoreCase = true)) {
      DropFileType.SeedZip
    } else if (extension.equals(TrackerFileHandler.TRACKER_FILE_EXTENSION, ignoreCase = true)) {
      DropFileType.TrackerSave
    } else {
      DropFileType.Unknown
    }
  }

  suspend fun handleDroppedFile(
    file: File,
    type: DropFileType,
    handler: TrackerFileHandler,
  ): DropResult? {
    return when (type) {
      DropFileType.SeedZip -> {
        val randomizerSeed = handler.parseSeedZipFile(file) ?: return null
        DropResult(
          baseGameState = BaseGameState(randomizerSeed),
          previouslyRevealedHints = persistentListOf()
        )
      }

      DropFileType.TrackerSave -> {
        val gameStateSerializedForm = handler.readTrackerProgressFile(file)
        DropResult(
          baseGameState = gameStateSerializedForm.toBaseGameState(),
          previouslyRevealedHints = gameStateSerializedForm.revealedHints.toImmutableList()
        )
      }

      DropFileType.Unknown -> {
        null
      }
    }
  }

  data class DropResult(
    val baseGameState: BaseGameState,
    val previouslyRevealedHints: ImmutableList<HintInfo>,
  )

}

@Composable
fun SeedDropTarget(
  onFileDropped: (File, DropFileType) -> Unit,
  modifier: Modifier = Modifier,
) {
  var showTargetBorder by remember { mutableStateOf(false) }
  val dragAndDropTarget = remember {
    object : DragAndDropTarget {
      override fun onStarted(event: DragAndDropEvent) {
        showTargetBorder = true
      }

      override fun onEnded(event: DragAndDropEvent) {
        showTargetBorder = false
      }

      override fun onDrop(event: DragAndDropEvent): Boolean {
        val pair = parseDropEvent(event) ?: return false
        val (file, type) = pair
        return if (type != DropFileType.Unknown) {
          onFileDropped(file, type)
          true
        } else {
          false
        }
      }
    }
  }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = modifier.then(
      if (showTargetBorder) {
        val color = ColorToken.Green.color
        Modifier.border(BorderStroke(4.dp, color)).background(color.copy(alpha = 0.1f))
      } else
        Modifier
    )
      .padding(16.dp)
      .dragAndDropTarget(
        shouldStartDragAndDrop = { event ->
          val pair = parseDropEvent(event)
          pair != null && pair.second != DropFileType.Unknown
        },
        target = dragAndDropTarget
      ),
  ) {
    Image(
      imageResource(Res.drawable.randomizer_logo),
      contentDescription = null,
      modifier = Modifier.heightIn(max = 96.dp),
    )
    Text(
      stringResource(Res.string.main_drop_seed_here),
      style = MaterialTheme.typography.titleMedium,
      fontFamily = khMenuFontFamily(),
    )
  }
}

private fun parseDropEvent(event: DragAndDropEvent): Pair<File, DropFileType>? {
  return when (event.action) {
    DragAndDropTransferAction.Move, DragAndDropTransferAction.Copy -> {
      when (val dragData = event.dragData()) {
        is DragData.FilesList -> {
          val files = dragData.readFiles()
          if (files.size == 1) {
            val file = File(URI.create(files.first()))
            file to SeedDropTarget.categorizeDroppedFile(file)
          } else {
            null
          }
        }

        else -> {
          null
        }
      }
    }

    else -> {
      null
    }
  }
}
