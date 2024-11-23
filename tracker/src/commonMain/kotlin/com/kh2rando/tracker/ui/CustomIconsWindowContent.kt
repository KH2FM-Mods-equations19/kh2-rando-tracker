package com.kh2rando.tracker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.custom_icons_custom_icon
import com.kh2rando.tracker.generated.resources.custom_icons_custom_path
import com.kh2rando.tracker.generated.resources.custom_icons_default_icon
import com.kh2rando.tracker.log
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.item.DreamWeapon
import com.kh2rando.tracker.model.item.GrowthAbilityPrototype
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.objective.Objective
import com.kh2rando.tracker.model.progress.ProgressCheckpoint
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CustomIconsWindowContent(modifier: Modifier = Modifier) {
  Surface(modifier = modifier.fillMaxSize()) {
    val allCustomizableIcons = buildSet {
      addAll(ItemPrototype.fullList)
      addAll(ProgressCheckpoint.allCheckpoints)

      addAll(CreationsIcon.entries)
      addAll(DreamWeapon.entries)
      addAll(GrowthAbilityPrototype.entries)
      addAll(Location.entries)
      addAll(Objective.entries)
      addAll(SyntheticProgress.entries)
      addAll(SystemIcon.entries)
      addAll(UserProofMark.entries)
    }
    val sorted = allCustomizableIcons
      .sortedBy { it.fullCustomPath }
      .distinctBy { it.fullCustomPath }
    Column(Modifier.fillMaxWidth()) {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        SmallHeader(stringResource(Res.string.custom_icons_custom_path), modifier = Modifier.weight(3.0f))
        SmallHeader(stringResource(Res.string.custom_icons_default_icon), modifier = Modifier.weight(1.0f))
        SmallHeader(stringResource(Res.string.custom_icons_custom_icon), modifier = Modifier.weight(1.0f))
      }

      val alternateBackground = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
      LazyColumn {
        itemsIndexed(sorted) { index, item ->
          val defaultIcon = painterResource(item.defaultIcon)

          val background = if (index % 2 == 0) {
            alternateBackground
          } else {
            Modifier
          }
          Row(Modifier.fillMaxWidth().then(background), verticalAlignment = Alignment.CenterVertically) {
            Text("${item.fullCustomPath}.png", modifier = Modifier.weight(3.0f))

            Image(
              defaultIcon,
              contentDescription = null,
              colorFilter = item.defaultIconTint.tintFilterOrNull(),
              modifier = Modifier.size(48.dp).weight(1.0f),
            )

            val customIconFile = item.findCustomIconFile(force = true)
            if (customIconFile != null) {
              AsyncImage(
                customIconFile,
                contentDescription = null,
                error = defaultIcon,
                modifier = Modifier.size(48.dp).weight(1.0f),
                alpha = 1.0f,
                onError = { error ->
                  log { "Error loading custom icon - ${error.result.throwable.message}" }
                }
              )
            } else {
              Spacer(modifier = Modifier.weight(1.0f))
            }
          }
        }
      }
    }
  }
}

private val HasCustomizableIcon.fullCustomPath: String
  get() = (customIconPath + customIconIdentifier).joinToString("/")
