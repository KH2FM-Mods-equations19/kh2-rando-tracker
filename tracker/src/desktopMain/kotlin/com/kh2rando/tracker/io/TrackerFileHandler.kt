package com.kh2rando.tracker.io

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.charleskorn.kaml.decodeFromStream
import com.kh2rando.tracker.TrackerFileSystem
import com.kh2rando.tracker.log
import com.kh2rando.tracker.model.SongEntry
import com.kh2rando.tracker.model.gamestate.FullGameStateApi
import com.kh2rando.tracker.model.seed.HintFileJsonParser
import com.kh2rando.tracker.model.seed.RandomizerSeed
import com.kh2rando.tracker.serialization.GameStateSerializedForm
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import okio.Path
import okio.Path.Companion.toOkioPath
import okio.buffer
import okio.use
import java.io.File
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.zip.ZipFile
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalSerializationApi::class)
class TrackerFileHandler(private val ioDispatcher: CoroutineDispatcher) {

  private val yaml = Yaml(configuration = YamlConfiguration(strictMode = false))

  /**
   * Parses a seed zip file into a [RandomizerSeed] object.
   *
   * Returns null if the zip file does not contain the appropriate hints file.
   */
  suspend fun parseSeedZipFile(file: File): RandomizerSeed? {
    return withContext(ioDispatcher) {
      ZipFile(file).use { zipFile ->
        val settings = zipFile.getEntry("HintFile.Hints")?.let { entry ->
          zipFile.getInputStream(entry).use { stream ->
            stream.bufferedReader().use { reader ->
              val encodedHintData = reader.readText()
              HintFileJsonParser().parseEncodedHintData(encodedHintData)
            }
          }
        } ?: return@use null

        val seedHashIcons = zipFile.getEntry("misc/randoseed-hash-icons.csv")?.let { entry ->
          zipFile.getInputStream(entry).use { stream ->
            stream.bufferedReader().useLines { lines ->
              lines.firstOrNull()?.split(",")
            }
          }
        }.orEmpty().toImmutableList()

        val musicReplacements: Map<Int, SongEntry> = zipFile.getEntry("mod.yml")?.let { entry ->
          val modYml = zipFile.getInputStream(entry).use { stream -> yaml.decodeFromStream<ModYml>(stream) }

          val songReplacements = modYml.assets.filter { asset ->
            val name = asset.name
            name.startsWith("bgm") && name.endsWith(".scd")
          }

          songReplacements.mapNotNull { asset ->
            try {
              val gameSongFile = File(asset.name)
              val songId = gameSongFile.name
                .substringAfter("music")
                .substringBeforeLast(".win32.scd")
                .toInt()

              val replacementSongFile = File(asset.source.first().name)
              val songName = replacementSongFile.nameWithoutExtension

              if (replacementSongFile.isAbsolute) {
                songId to SongEntry(songName, group = replacementSongFile.parentFile.name)
              } else {
                songId to SongEntry(songName)
              }
            } catch (e: Exception) {
              // Not worth failing an entire seed parse over this
              null
            }
          }.toMap().toImmutableMap()
        }.orEmpty()

        RandomizerSeed(
          generatorVersion = settings.generatorVersion,
          settings = settings,
          seedHashIcons = seedHashIcons,
          musicReplacements = musicReplacements,
        )
      }
    }
  }

  /**
   * Shows a file chooser allowing the user to save tracker progress represented by [gameState] to a chosen file.
   */
  fun saveProgressWithPrompt(gameState: FullGameStateApi) {
    val chooser = JFileChooser()
    chooser.fileFilter = FileNameExtensionFilter("Tracker Progress Saves", TRACKER_FILE_EXTENSION)
    if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
      var selectedFile = chooser.selectedFile ?: return
      if (selectedFile.extension != TRACKER_FILE_EXTENSION) {
        selectedFile = selectedFile.resolveSibling("${selectedFile.name}.$TRACKER_FILE_EXTENSION")
      }
      writeTrackerProgressFile(selectedFile.toOkioPath(), gameState.toGameStateSerializedForm())
    }
  }

  /**
   * Reads a [GameStateSerializedForm] from the specified tracker progress file.
   */
  suspend fun readTrackerProgressFile(file: File): GameStateSerializedForm {
    return withContext(ioDispatcher) {
      file.inputStream().use { stream ->
        Cbor.decodeFromByteArray(stream.readAllBytes())
      }
    }
  }

  /**
   * Writes [gameState] to the specified tracker progress file.
   */
  fun writeTrackerProgressFile(path: Path, gameState: GameStateSerializedForm) {
    TrackerFileSystem.fileSystem.sink(path).use { sink ->
      sink.buffer().use { buffer ->
        val bytes = Cbor.encodeToByteArray(gameState)
        buffer.write(bytes)
      }
    }
  }

  fun launchAutoSaver(scope: CoroutineScope, gameState: FullGameStateApi) {
    scope.launch {
      while (isActive) {
        delay(autoSaveInterval)

        val serializedForm = gameState.toGameStateSerializedForm()
        withContext(ioDispatcher) {
          purgeOldAutoSaves()

          val autoSaveFile = autoSaveFile()
          try {
            writeTrackerProgressFile(gameState = serializedForm, path = autoSaveFile)
          } catch (e: Exception) {
            if (e is CancellationException) {
              throw e
            } else {
              log { e.message ?: "Error writing auto-save to $autoSaveFile" }
            }
          }
        }
      }
    }
  }

  private fun autoSaveFile(): Path {
    val timestamp = autoSaveFileTimestamp()
    return TrackerFileSystem.autoSavesDirectory.resolve("TrackerAutoSave-$timestamp.${TRACKER_FILE_EXTENSION}")
  }

  private fun purgeOldAutoSaves() {
    val fileSystem = TrackerFileSystem.fileSystem
    fileSystem.listOrNull(TrackerFileSystem.autoSavesDirectory).orEmpty()
      .filter { it.name.substringAfterLast(".") == TRACKER_FILE_EXTENSION }
      .sortedByDescending { it.name }
      .drop(2)
      .forEach(fileSystem::delete)
  }

  companion object {

    const val TRACKER_FILE_EXTENSION = "kh2tracker"

    private val trackerTimestampFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")

    private val autoSaveInterval: Duration
      get() = 1.minutes

    fun autoSaveFileTimestamp(timestamp: ZonedDateTime = ZonedDateTime.now()): String {
      return trackerTimestampFormatter.format(timestamp)
    }

  }

}
