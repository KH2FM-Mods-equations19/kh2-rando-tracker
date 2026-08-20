package com.kh2rando.tracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.kh2rando.tracker.auto.SeedModDetector
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.main_configure_mods_manager_button
import com.kh2rando.tracker.generated.resources.main_configure_mods_manager_label
import com.kh2rando.tracker.generated.resources.main_mods_manager_no_seed_found_label
import com.kh2rando.tracker.generated.resources.main_mods_manager_use_found_seed_label
import com.kh2rando.tracker.generated.resources.main_use_mods_manager_seed_button
import com.kh2rando.tracker.io.TrackerFileHandler
import com.kh2rando.tracker.model.preferences.TrackerPreferences
import com.kh2rando.tracker.model.seed.RandomizerSeed
import org.jetbrains.compose.resources.stringResource

@Composable
fun SeedDetectionContent(
  preferences: TrackerPreferences,
  fileHandler: TrackerFileHandler,
  onChooseModsManagerLocation: () -> Unit,
  onUseExistingSeed: (RandomizerSeed) -> Unit,
  modifier: Modifier = Modifier,
) {
  val seedModDetector = remember { SeedModDetector(preferences, fileHandler) }
  val seedLoadState by seedModDetector.states.collectAsState(initial = SeedModDetector.State.DetectingSeed)
  val collectedState = seedLoadState
  Column(modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
    when (collectedState) {
      SeedModDetector.State.ModsManagerNotFound -> {
        TitleText(stringResource(Res.string.main_configure_mods_manager_label))
        Button(onClick = onChooseModsManagerLocation) {
          Text(stringResource(Res.string.main_configure_mods_manager_button))
        }
      }

      SeedModDetector.State.SeedNotFound -> {
        TitleText(stringResource(Res.string.main_mods_manager_no_seed_found_label))
      }

      is SeedModDetector.State.DetectingSeed -> {
        CircularProgressIndicator()
      }

      is SeedModDetector.State.FoundSeed -> {
        val seed = collectedState.seed
        TitleText(stringResource(Res.string.main_mods_manager_use_found_seed_label))
        Row {
          SeedHashIcons(seed.seedHashIcons)
        }
        Button(onClick = { onUseExistingSeed(seed) }) {
          Text(stringResource(Res.string.main_use_mods_manager_seed_button))
        }
      }
    }
  }
}

@Composable
private fun TitleText(text: String) {
  Text(
    text = text,
    style = MaterialTheme.typography.titleMedium,
    fontFamily = khMenuFontFamily(),
  )
}
