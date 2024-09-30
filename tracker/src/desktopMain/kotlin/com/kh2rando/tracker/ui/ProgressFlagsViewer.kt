package com.kh2rando.tracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kh2rando.tracker.auto.GameProcessFinder
import com.kh2rando.tracker.auto.ProgressReader
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.progress.AgrabahProgress
import com.kh2rando.tracker.model.progress.AtlanticaProgress
import com.kh2rando.tracker.model.progress.BeastsCastleProgress
import com.kh2rando.tracker.model.progress.DisneyCastleProgress
import com.kh2rando.tracker.model.progress.HalloweenTownProgress
import com.kh2rando.tracker.model.progress.HollowBastionProgress
import com.kh2rando.tracker.model.progress.HundredAcreWoodProgress
import com.kh2rando.tracker.model.progress.LandOfDragonsProgress
import com.kh2rando.tracker.model.progress.OlympusColiseumProgress
import com.kh2rando.tracker.model.progress.PortRoyalProgress
import com.kh2rando.tracker.model.progress.PrideLandsProgress
import com.kh2rando.tracker.model.progress.ProgressFlag
import com.kh2rando.tracker.model.progress.SpaceParanoidsProgress
import com.kh2rando.tracker.model.progress.TwilightTownProgress
import com.kh2rando.tracker.model.progress.WorldThatNeverWasProgress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ProgressFlagsViewerContent(
  location: Location?,
  modifier: Modifier = Modifier,
) {
  if (location == null) {
    return
  }

  var values: List<Pair<ProgressFlag, Boolean>> by remember { mutableStateOf(emptyList()) }

  Surface(modifier = modifier.fillMaxSize()) {
    LazyHorizontalGrid(
      rows = GridCells.FixedSize(36.dp),
      contentPadding = PaddingValues(8.dp),
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      items(values) { (flag, value) ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(flag.toString(), textAlign = TextAlign.End)
          if (value) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ColorToken.Green.color)
          } else {
            Icon(Icons.Default.Clear, contentDescription = null)
          }
        }
      }
    }
  }

  LaunchedEffect(location) {
    launch(Dispatchers.Default) {
      val gameProcess = GameProcessFinder().findGame()
      val flagReader = ProgressReader(gameProcess)
      while (isActive) {
        delay(100.milliseconds)

        val flags = when (location) {
          Location.PrideLands -> PrideLandsProgress.Flag.entries
          Location.Agrabah -> AgrabahProgress.Flag.entries
          Location.WorldThatNeverWas -> WorldThatNeverWasProgress.Flag.entries
          Location.LandOfDragons -> LandOfDragonsProgress.Flag.entries
          Location.HalloweenTown -> HalloweenTownProgress.Flag.entries
          Location.OlympusColiseum -> OlympusColiseumProgress.Flag.entries
          Location.TwilightTown -> TwilightTownProgress.Flag.entries
          Location.PortRoyal -> PortRoyalProgress.Flag.entries
          Location.SpaceParanoids -> SpaceParanoidsProgress.Flag.entries
          Location.HundredAcreWood -> HundredAcreWoodProgress.Flag.entries
          Location.DisneyCastle -> DisneyCastleProgress.Flag.entries
          Location.HollowBastion -> HollowBastionProgress.Flag.entries
          Location.BeastsCastle -> BeastsCastleProgress.Flag.entries
          Location.Atlantica -> AtlanticaProgress.Flag.entries

          Location.SimulatedTwilightTown -> emptyList()
          Location.GardenOfAssemblage -> emptyList()

          Location.SoraLevels -> emptyList()
          Location.DriveForms -> emptyList()
          Location.Creations -> emptyList()
        }

        val result = flagReader.readRawFlags(flags)
        withContext(Dispatchers.Main) {
          values = result
        }
      }
    }
  }

}
