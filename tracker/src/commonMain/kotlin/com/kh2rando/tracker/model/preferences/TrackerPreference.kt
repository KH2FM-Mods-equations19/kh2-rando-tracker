package com.kh2rando.tracker.model.preferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.packFloats
import androidx.compose.ui.util.unpackFloat1
import androidx.compose.ui.util.unpackFloat2
import androidx.compose.ui.window.WindowPosition
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * A preference that can be persisted.
 */
interface TrackerPreference<Actual, Raw> {

  /**
   * Key for this preference.
   */
  val key: Preferences.Key<Raw>

  /**
   * Default value for this preference.
   */
  val defaultValue: Actual

  /**
   * Values of this preference over time.
   */
  val values: Flow<Actual>

  /**
   * Saves a value for this preference.
   */
  suspend fun save(value: Actual)

}

/**
 * Convenience for collecting with an explicit default value.
 */
@Composable
fun <T> TrackerPreference<T, *>.collectAsState(): State<T> {
  return values.collectAsState(initial = defaultValue)
}

/**
 * A preference stored as a boolean.
 */
class BooleanPreference(
  private val dataStore: DataStore<Preferences>,
  override val key: Preferences.Key<Boolean>,
  override val defaultValue: Boolean,
) : TrackerPreference<Boolean, Boolean> {

  override val values: Flow<Boolean> = dataStore.data.map { preferences -> preferences[key] ?: defaultValue }

  override suspend fun save(value: Boolean) {
    dataStore.edit { preferences -> preferences[key] = value }
  }

}

/**
 * A preference stored as a long.
 */
abstract class LongBackedPreference<T>(
  private val dataStore: DataStore<Preferences>,
) : TrackerPreference<T, Long> {

  override val values: Flow<T> = dataStore.data.map { preferences ->
    val rawValue = preferences[key]
    if (rawValue == null) defaultValue else fromLong(rawValue)
  }

  override suspend fun save(value: T) {
    dataStore.edit { preferences -> preferences[key] = toLong(value) }
  }

  abstract fun fromLong(rawValue: Long): T

  abstract fun toLong(value: T): Long

}

/**
 * A preference stored as a string.
 */
abstract class StringBackedPreference<T>(
  private val dataStore: DataStore<Preferences>,
) : TrackerPreference<T, String> {

  override val values: Flow<T> = dataStore.data.map { preferences ->
    val rawValue = preferences[key]
    if (rawValue == null) defaultValue else fromString(rawValue)
  }

  override suspend fun save(value: T) {
    dataStore.edit { preferences -> preferences[key] = toString(value) }
  }

  abstract fun fromString(rawValue: String): T

  abstract fun toString(value: T): String

}

/**
 * A preference stored as a (packed) [DpSize].
 */
class DpSizePreference(
  dataStore: DataStore<Preferences>,
  override val key: Preferences.Key<Long>,
  override val defaultValue: DpSize = DpSize.Unspecified,
) : LongBackedPreference<DpSize>(dataStore) {

  override fun fromLong(rawValue: Long): DpSize {
    return DpSize(width = unpackFloat1(rawValue).dp, height = unpackFloat2(rawValue).dp)
  }

  override fun toLong(value: DpSize): Long {
    return packFloats(value.width.value, value.height.value)
  }

}

/**
 * A preference stored as a (packed) [WindowPosition].
 */
class WindowPositionPreference(
  private val dataStore: DataStore<Preferences>,
  override val key: Preferences.Key<Long>,
  override val defaultValue: WindowPosition,
) : LongBackedPreference<WindowPosition>(dataStore) {

  override fun fromLong(rawValue: Long): WindowPosition {
    return WindowPosition(x = unpackFloat1(rawValue).dp, y = unpackFloat2(rawValue).dp)
  }

  override fun toLong(value: WindowPosition): Long {
    return packFloats(value.x.value, value.y.value)
  }

  override suspend fun save(value: WindowPosition) {
    when (value) {
      is WindowPosition.Absolute -> {
        super.save(value)
      }

      WindowPosition.PlatformDefault, is WindowPosition.Aligned -> {
        dataStore.edit { preferences -> preferences.remove(key) }
      }
    }
  }

}
