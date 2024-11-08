package com.kh2rando.tracker.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kh2rando.tracker.auto.GameProcessFinder
import com.kh2rando.tracker.auto.LocationReader
import com.kh2rando.tracker.model.hex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Composable
private fun AutoTrackingConsoleContent(
  state: LocationReader.LocationState?,
  autoTrackerLoopIterationAverage: Duration?,
  autoTrackerLoopIterationCount: Int,
  modifier: Modifier = Modifier,
) {
  Surface(modifier) {
    LazyVerticalGrid(GridCells.Fixed(3)) {
      item { Text(state?.location?.toString().orEmpty()) }
      item { Text(state?.worldId?.toString().orEmpty()) }
      item { Text(state?.worldId?.toHex().orEmpty()) }

      item { Text("Room") }
      item { Text(state?.roomId?.toString().orEmpty()) }
      item { Text(state?.roomId?.toHex().orEmpty()) }

      item { Text("Place") }
      item { Text(state?.placeId?.toString().orEmpty()) }
      item { Text(state?.placeId?.hex().orEmpty()) }

      item { Text("Door") } // Aka "Cup round"?
      item { Text(state?.doorId?.toString().orEmpty()) }
      item { Text(state?.doorId?.hex().orEmpty()) }

      item { Text("Map") }
      item { Text(state?.mapId?.toString().orEmpty()) }
      item { Text(state?.mapId?.toHex().orEmpty()) }

      item { Text("Btl") }
      item { Text(state?.battleId?.toString().orEmpty()) }
      item { Text(state?.battleId?.toHex().orEmpty()) }

      item { Text("Evt") }
      item { Text(state?.eventId?.toString().orEmpty()) }
      item { Text(state?.eventId?.toHex().orEmpty()) }

      item { Text("Event Complete") }
      item { Text(state?.eventComplete?.toString().orEmpty()) }
      item { Text(state?.eventComplete?.hex().orEmpty()) }

      item { Text("Auto-Tracker Scans") }
      item { Text("Count: $autoTrackerLoopIterationCount") }
      item { Text("Average: $autoTrackerLoopIterationAverage") }
    }
  }
}

@Composable
@OptIn(FlowPreview::class)
fun AutoTrackingConsoleContent(
  latestAutoTrackerScanTimes: Flow<Duration>,
  modifier: Modifier = Modifier,
) {
  var locationState: LocationReader.LocationState? by remember { mutableStateOf(null) }
  var averageInfo: Pair<Duration, Int>? by remember { mutableStateOf(null) }

  AutoTrackingConsoleContent(
    locationState,
    autoTrackerLoopIterationAverage = averageInfo?.first,
    autoTrackerLoopIterationCount = averageInfo?.second ?: 0,
    modifier = modifier.fillMaxSize(),
  )

  LaunchedEffect(Unit) {
    launch(Dispatchers.Default) {
      val gameProcess = GameProcessFinder().findGame()
      val flagReader = LocationReader(gameProcess)
      while (isActive) {
        delay(100.milliseconds)
        val result = flagReader.readLocationState()
        withContext(Dispatchers.Main) {
          locationState = result
        }
      }
    }
  }

  LaunchedEffect(Unit) {
    latestAutoTrackerScanTimes
      // The first emission will be possibly 0 milliseconds.
      // Either way, just skip one, not a big deal.
      .drop(1)
      .scan(Pair(0.milliseconds, 0)) { (totalTimeSoFar, count), scanTime ->
        Pair(totalTimeSoFar + scanTime, count + 1)
      }
      .sample(5.seconds)
      .onEach { (totalTime, count) ->
        val average = if (count == 0) 0.milliseconds else totalTime / count
        averageInfo = average to count
      }
      .launchIn(this)
  }
}
