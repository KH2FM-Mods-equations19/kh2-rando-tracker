package com.kh2rando.tracker.model.seed

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import app.cash.turbine.turbineScope
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.SongEntry
import com.kh2rando.tracker.model.gamestate.BaseGameState
import com.kh2rando.tracker.model.gamestate.FullGameState
import com.kh2rando.tracker.model.gamestate.FullGameStateApi
import com.kh2rando.tracker.model.gamestate.GameStateFactory
import com.kh2rando.tracker.model.gamestate.mostRecentRevealedPrimaryHint
import com.kh2rando.tracker.model.hints.BasicProgressionSettings
import com.kh2rando.tracker.model.hints.DisabledHintSystem
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.hints.HintSystem
import com.kh2rando.tracker.model.hints.LocationAuxiliaryHintInfo
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.preferences.TrackerPreferences
import com.kh2rando.tracker.model.progress.ProgressCheckpoint
import com.kh2rando.tracker.ui.LocationUiState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals

/**
 * Creates a [RandomizerSeed] with reasonable defaults, to be used in tests.
 */
fun testSeed(
  enabledLocations: Set<Location> = Location.entries.toSet(),
  trackableItems: Set<ItemPrototype> = ItemPrototype.fullList.toSet(),
  hintSystem: HintSystem = DisabledHintSystem(hints = emptyList()),
  finalDoorRequirement: FinalDoorRequirement = FinalDoorRequirement.ThreeProofs,
  levelChecks: LevelChecks = LevelChecks(
    soraLevels = LevelChecks.soraLevelsBuilder(),
    formLevels = LevelChecks.formLevelsBuilder()
  ),
  levelSetting: LevelSetting = LevelSetting.Level50,
  creationsOptions: Set<CreationsOption> = CreationsOption.entries.toSet(),
  toggleSettings: Set<ToggleSetting> = ToggleSetting.entries.toSet(),
  generatorVersion: String = "test",
  seedHashIcons: List<String> = emptyList(),
  musicReplacements: Map<Int, SongEntry> = emptyMap(),
): RandomizerSeed {
  return RandomizerSeed(
    generatorVersion = generatorVersion,
    settings = SeedSettings(
      generatorVersion = generatorVersion,
      enabledLocations = enabledLocations,
      trackableItems = trackableItems,
      hintSystem = hintSystem,
      finalDoorRequirement = finalDoorRequirement,
      levelChecks = levelChecks,
      levelSetting = levelSetting,
      creationsOptions = creationsOptions,
      toggleSettings = toggleSettings,
    ),
    seedHashIcons = seedHashIcons,
    musicReplacements = musicReplacements,
  )
}

/**
 * Creates a [BasicProgressionSettings] with reasonable defaults, to be used in tests.
 */
fun testProgressionSettings(
  pointsToAwardByCheckpoint: Map<ProgressCheckpoint, Int> = ProgressCheckpoint.allCheckpoints.associateWith { 1 },
  hintCosts: List<Int> = List(100) { 1 },
  worldCompleteBonus: Int = 0,
  reportBonus: Int = 0,
  revealAllWhenDone: Boolean = false,
): BasicProgressionSettings {
  return BasicProgressionSettings(
    pointsToAwardByCheckpoint = pointsToAwardByCheckpoint,
    hintCosts = hintCosts,
    worldCompleteBonus = worldCompleteBonus,
    reportBonus = reportBonus,
    revealAllWhenDone = revealAllWhenDone,
  )
}

@OptIn(ExperimentalCoroutinesApi::class)
fun testPreferences(temporaryFolder: TemporaryFolder): TrackerPreferences {
  return TrackerPreferences(
    dataStore = PreferenceDataStoreFactory.create(
      scope = TestScope(UnconfinedTestDispatcher() + Job()),
      produceFile = { temporaryFolder.newFile("test.preferences_pb") }
    )
  )
}

@OptIn(ExperimentalCoroutinesApi::class)
fun TestScope.testGameState(
  seed: RandomizerSeed,
  preferences: TrackerPreferences,
  previouslyRevealedHints: List<HintInfo> = emptyList(),
): FullGameState {
  return GameStateFactory(
    preferences = preferences,
    scope = backgroundScope,
    backgroundDispatcher = UnconfinedTestDispatcher(testScheduler)
  ).create(
    baseGameState = BaseGameState(seed),
    previouslyRevealedHints = previouslyRevealedHints.toImmutableList(),
  )
}

fun <T> assertEmpty(list: List<T>) {
  assertEquals(expected = emptyList(), actual = list)
}

fun <T> assertEmpty(set: Set<T>) {
  assertEquals(expected = emptySet(), actual = set)
}

// (This used to be actually on LocationUiState, but it was only used in tests)
val LocationUiState.acquiredPrototypes: List<ItemPrototype>
  get() = acquiredItems.map { it.prototype }

fun LocationUiState.assertAcquired(vararg prototypes: ItemPrototype) {
  assertEquals(expected = prototypes.toList(), actual = acquiredPrototypes)
}

fun LocationUiState.assertRevealed(vararg prototypes: ItemPrototype) {
  assertEquals(expected = prototypes.toList(), actual = revealedItems)
}

fun LocationUiState.assertProgress(vararg checkpoints: ProgressCheckpoint) {
  assertEquals(expected = checkpoints.toSet(), actual = completedProgressCheckpoints)
}

fun LocationUiState.assertCounter(counterState: LocationCounterState) {
  assertEquals(expected = counterState, actual = this.counterState)
}

fun LocationUiState.assertAuxiliary(auxiliaryHintInfo: LocationAuxiliaryHintInfo) {
  assertEquals(expected = auxiliaryHintInfo, actual = this.auxiliaryHintInfo)
}

/**
 * Allows for calling various convenience test functions that can be used to test [LocationUiState]s and hints.
 */
interface LocationHintsTestScope {

  /**
   * Skips one or more flow emissions for [location].
   */
  suspend fun skip(location: Location, count: Int)

  /**
   * Awaits a flow emission for [location] and calls [block] allowing assertions to be performed against the
   * [LocationUiState].
   */
  suspend fun awaitLocationState(location: Location, block: LocationUiState.() -> Unit)

  /**
   * Awaits a flow emission for revealed hints and asserts that the value equals [expectedHints], in order.
   */
  suspend fun awaitRevealedHints(vararg expectedHints: HintInfo)

  /**
   * Awaits a flow emission for the last revealed hint and asserts that it equals [expectedHint].
   */
  suspend fun awaitLastRevealedHint(expectedHint: HintInfo?)

}

/**
 * Establishes a scope that can be used to test location hints and [LocationUiState]s.
 */
suspend fun TestScope.testLocationHints(
  gameState: FullGameStateApi,
  block: suspend LocationHintsTestScope.() -> Unit,
) {
  turbineScope {
    val locationStates = gameState.locationUiStates.mapValues { (_, value) -> value.testIn(backgroundScope) }
    val revealedHints = gameState.revealedPrimaryHintsInOrder.testIn(backgroundScope)
    val lastRevealedHint = gameState.mostRecentRevealedPrimaryHint.testIn(backgroundScope)

    val scope = object : LocationHintsTestScope {
      override suspend fun skip(location: Location, count: Int) {
        locationStates.getValue(location).skipItems(count)
      }

      override suspend fun awaitLocationState(location: Location, block: LocationUiState.() -> Unit) {
        locationStates.getValue(location).awaitItem().block()
      }

      override suspend fun awaitRevealedHints(vararg expectedHints: HintInfo) {
        assertEquals(expected = expectedHints.toList(), actual = revealedHints.awaitItem())
      }

      override suspend fun awaitLastRevealedHint(expectedHint: HintInfo?) {
        assertEquals(expected = expectedHint, actual = lastRevealedHint.awaitItem())
      }
    }
    scope.block()
  }
}
