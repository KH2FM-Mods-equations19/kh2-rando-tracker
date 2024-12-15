package com.kh2rando.tracker.model.preferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.WindowPosition
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.LocationLayout
import com.kh2rando.tracker.ui.color
import okio.Path

/**
 * Typesafe access to user preferences.
 */
class TrackerPreferences(private val dataStore: DataStore<Preferences>) {

  /**
   * Size of the main window.
   */
  val mainWindowSize = DpSizePreference(
    dataStore,
    key = longPreferencesKey("mainWindow.windowSize")
  )

  /**
   * Position of the main window.
   */
  val mainWindowPosition = WindowPositionPreference(
    dataStore,
    key = longPreferencesKey("mainWindow.screenPosition"),
    defaultValue = WindowPosition.Aligned(Alignment.Center)
  )

  /**
   * Preferred [LocationLayout].
   */
  val locationLayout = object : StringBackedPreference<LocationLayout>(dataStore) {

    override val key: Preferences.Key<String> = stringPreferencesKey("mainWindow.locationLayout")

    override val defaultValue: LocationLayout
      get() = LocationLayout.GardenOfAssemblage

    override fun fromString(rawValue: String): LocationLayout {
      return LocationLayout.entries.firstOrNull { it.name == rawValue } ?: defaultValue
    }

    override fun toString(value: LocationLayout): String {
      return value.name
    }

  }

  /**
   * Whether or not to auto-save tracker progress.
   */
  val autoSaveTrackerProgress = BooleanPreference(
    dataStore,
    key = booleanPreferencesKey("tracker.autoSaveProgress"),
    defaultValue = true,
  )

  /**
   * Whether or not to automatically start auto tracking.
   */
  val autoTrackingAutoStart = BooleanPreference(
    dataStore,
    key = booleanPreferencesKey("tracker.autoTrackingAutoStart"),
    defaultValue = false,
  )

  /**
   * Whether or not to try to load custom image files.
   */
  val useCustomImages = BooleanPreference(
    dataStore,
    key = booleanPreferencesKey("tracker.useCustomImages"),
    defaultValue = false,
  )

  /**
   * Whether or not to update point counters based on revealed but not acquired items..
   */
  val pointsAutoMath = BooleanPreference(
    dataStore,
    key = booleanPreferencesKey("mainWindow.pointsAutoMath"),
    defaultValue = true,
  )

  /**
   * Whether or not to show the extended window upon app launch.
   */
  val showExtendedWindowOnLaunch = BooleanPreference(
    dataStore,
    key = booleanPreferencesKey("extendedWindow.showOnLaunch"),
    defaultValue = false,
  )

  /**
   * Size of the extended window.
   */
  val extendedWindowSize = DpSizePreference(
    dataStore,
    key = longPreferencesKey("extendedWindow.windowSize")
  )

  /**
   * Position of the extended window.
   */
  val extendedWindowPosition = WindowPositionPreference(
    dataStore,
    key = longPreferencesKey("extendedWindow.screenPosition"),
    defaultValue = WindowPosition.Aligned(Alignment.Center)
  )

  /**
   * Whether or not to show song information on the extended window.
   */
  val showSongInfoExtendedWindow = BooleanPreference(
    dataStore,
    key = booleanPreferencesKey("extendedWindow.showSongInfo"),
    defaultValue = true,
  )

  /**
   * Whether or not to display each song's containing folder as a group in the song info area.
   */
  val songFolderAsGroup = BooleanPreference(
    dataStore,
    key = booleanPreferencesKey("extendedWindow.songFolderAsGroup"),
    defaultValue = false,
  )

  /**
   * Size of the objective window.
   */
  val objectiveWindowSize = DpSizePreference(
    dataStore,
    key = longPreferencesKey("objectiveWindow.windowSize")
  )

  /**
   * Position of the objective window.
   */
  val objectiveWindowPosition = WindowPositionPreference(
    dataStore,
    key = longPreferencesKey("objectiveWindow.screenPosition"),
    defaultValue = WindowPosition.Aligned(Alignment.Center)
  )

  /**
   * Color for an grid cell that has been completed.
   */
  val gridCellCompleteColor = ColorPreference(
    dataStore,
    key = longPreferencesKey("grid.cellCompleteColor"),
    defaultValue = ColorToken.Red.color
  )

  /**
   * Color to use for marking a grid cell.
   */
  val gridCellMarkColor = ColorPreference(
    dataStore,
    key = longPreferencesKey("grid.cellMarkColor"),
    defaultValue = ColorToken.LightBlue.color
  )

  /**
   * Color to use to indicate grid completion.
   */
  val gridCompletionColor = ColorPreference(
    dataStore,
    key = longPreferencesKey("grid.completionColor"),
    defaultValue = ColorToken.Green.color
  )

  companion object {

    fun createDataStore(producePath: () -> Path): DataStore<Preferences> {
      return PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath() }
      )
    }

    /**
     * Allows access to [TrackerPreferences] without explicitly passing it everywhere.
     */
    val LocalPreferences = staticCompositionLocalOf<TrackerPreferences> { error("Preferences are not yet available") }

    val current: TrackerPreferences
      @Composable
      get() = LocalPreferences.current

  }

}

