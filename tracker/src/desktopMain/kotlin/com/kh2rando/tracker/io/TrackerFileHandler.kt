package com.kh2rando.tracker.io

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.charleskorn.kaml.decodeFromStream
import com.kh2rando.tracker.TrackerFileSystem
import com.kh2rando.tracker.auto.SeedModDetector
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
import okio.gzip
import okio.use
import java.io.File
import java.io.InputStream
import java.time.ZoneOffset
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
        blockingParseSeed(SeedZip(zipFile))
      }
    }
  }

  /**
   * Parses a seed mod directory into a [RandomizerSeed] object.
   *
   * Returns null if the directory does not contain the appropriate hints file.
   */
  suspend fun parseSeedModDirectory(hintFile: File): RandomizerSeed? {
    return withContext(ioDispatcher) {
      blockingParseSeed(SeedDirectory(directory = hintFile.parentFile))
    }
  }

  private fun blockingParseSeed(source: SeedStreamSource): RandomizerSeed? {
    val settings = source.hintFile()?.let { stream ->
      stream.bufferedReader().use { reader ->
        val encodedHintData = reader.readText()
        HintFileJsonParser().parseEncodedHintData(encodedHintData)
      }
    } ?: return null

    val seedHashIcons = source.seedHashIcons()?.let { stream ->
      stream.bufferedReader().useLines { lines ->
        lines.firstOrNull()?.split(",")
      }
    }.orEmpty().toImmutableList()

    val musicReplacements = source.modYml()?.let { stream ->
      val modYml = yaml.decodeFromStream<ModYml>(stream)

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
        } catch (_: Exception) {
          // Not worth failing an entire seed parse over this
          null
        }
      }.toMap().toImmutableMap()
    }.orEmpty()

    return RandomizerSeed(
      generatorVersion = settings.generatorVersion,
      settings = settings,
      seedHashIcons = seedHashIcons,
      musicReplacements = musicReplacements,
    )

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
      TrackerFileSystem.fileSystem.source(file.toOkioPath()).use { source ->
        source.gzip().buffer().use { buffer ->
          Cbor.decodeFromByteArray(buffer.readByteArray())
        }
      }
    }
  }

  /**
   * Writes [gameState] to the specified tracker progress file.
   */
  private fun writeTrackerProgressFile(path: Path, gameState: GameStateSerializedForm) {
    TrackerFileSystem.fileSystem.sink(path).use { sink ->
      sink.gzip().buffer().use { buffer ->
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
      .drop(50)
      .forEach(fileSystem::delete)
  }

  fun chooseModsManagerLocation(): File? {
    val chooser = JFileChooser()
    chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
    return if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
      chooser.selectedFile
    } else {
      null
    }
  }

  suspend fun findSeedInModsManager(modsManagerLocation: File): SeedModDetector.State {
    return withContext(ioDispatcher) {
      val configuration = try {
        parseModsManagerConfiguration(modsManagerLocation)
      } catch (e: Exception) {
        if (e is CancellationException) {
          throw e
        } else {
          null
        }
      } ?: return@withContext SeedModDetector.State.ModsManagerNotFound

      val modsPath = configuration.installedModsPath?.let { File(it) } ?: File(modsManagerLocation, "mods")
      if (!modsPath.isDirectory) {
        return@withContext SeedModDetector.State.ModsManagerNotFound
      }

      // Find exactly one mod with a hint file. If there aren't any, or if there are more than one, bail.
      val hintFile = modsPath.walk().singleOrNull { it.isFile && it.name == HINT_FILE_NAME }
      if (hintFile == null) {
        return@withContext SeedModDetector.State.SeedNotFound
      }

      val randomizerSeed = parseSeedModDirectory(hintFile)
      if (randomizerSeed == null) {
        SeedModDetector.State.SeedNotFound
      } else {
        SeedModDetector.State.FoundSeed(randomizerSeed)
      }
    }
  }

  private fun parseModsManagerConfiguration(modsManagerLocation: File): ModsManagerConfiguration? {
    if (!modsManagerLocation.isDirectory) {
      return null
    }

    val modsManagerConfigFile = File(modsManagerLocation, "mods-manager.yml")
    if (!modsManagerConfigFile.isFile) {
      return null
    }

    return TrackerFileSystem.fileSystem.source(modsManagerConfigFile.toOkioPath()).use { source ->
      source.buffer().use { source ->
        yaml.decodeFromSource<ModsManagerConfiguration>(source)
      }
    }
  }

  companion object {

    const val TRACKER_FILE_EXTENSION = "kh2tracker"

    private const val HINT_FILE_NAME = "HintFile.Hints"
    private const val HASH_ICONS_FILE_RELATIVE = "misc/randoseed-hash-icons.csv"
    private const val MOD_YML_FILE_RELATIVE = "mod.yml"

    private val trackerTimestampFormatter: DateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyyMMdd-AAAAAAAA").withZone(ZoneOffset.UTC)

    private val autoSaveInterval: Duration
      get() = 1.minutes

    fun autoSaveFileTimestamp(timestamp: ZonedDateTime = ZonedDateTime.now()): String {
      return trackerTimestampFormatter.format(timestamp)
    }

  }

  private interface SeedStreamSource {

    fun hintFile(): InputStream?

    fun seedHashIcons(): InputStream?

    fun modYml(): InputStream?

  }

  private class SeedZip(private val zipFile: ZipFile) : SeedStreamSource {

    override fun hintFile(): InputStream? {
      return zipFile.getEntry(HINT_FILE_NAME)?.let { zipFile.getInputStream(it) }
    }

    override fun seedHashIcons(): InputStream? {
      return zipFile.getEntry(HASH_ICONS_FILE_RELATIVE)?.let { zipFile.getInputStream(it) }
    }

    override fun modYml(): InputStream? {
      return zipFile.getEntry(MOD_YML_FILE_RELATIVE)?.let { zipFile.getInputStream(it) }
    }

  }

  private class SeedDirectory(private val directory: File) : SeedStreamSource {

    override fun hintFile(): InputStream? {
      return File(directory, HINT_FILE_NAME).takeIf { it.isFile }?.inputStream()
    }

    override fun seedHashIcons(): InputStream? {
      return File(directory, HASH_ICONS_FILE_RELATIVE).takeIf { it.isFile }?.inputStream()
    }

    override fun modYml(): InputStream? {
      return File(directory, MOD_YML_FILE_RELATIVE).takeIf { it.isFile }?.inputStream()
    }

  }

}
