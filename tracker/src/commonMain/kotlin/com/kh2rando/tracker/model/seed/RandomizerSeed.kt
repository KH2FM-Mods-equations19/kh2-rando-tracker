package com.kh2rando.tracker.model.seed

import androidx.compose.runtime.Immutable
import com.kh2rando.tracker.model.SongEntry
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class RandomizerSeed(
  /**
   * The version of the seed generator used to generate this seed.
   */
  val generatorVersion: String,
  /**
   * Settings for this seed.
   */
  val settings: SeedSettings,
  /**
   * The hash icons for this seed.
   */
  val seedHashIcons: List<String> = persistentListOf(),
  /**
   * Replacement song files that are being used, if any.
   */
  val musicReplacements: Map<Int, SongEntry> = persistentMapOf(),
)
