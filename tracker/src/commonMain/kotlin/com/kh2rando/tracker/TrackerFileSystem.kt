package com.kh2rando.tracker

import okio.FileSystem
import okio.Path

/**
 * Convenience accessors for common tracker files.
 */
object TrackerFileSystem {

  /**
   * [FileSystem] to use.
   */
  val fileSystem: FileSystem = FileSystem.SYSTEM
  private val platform: Platform = getPlatform()

  private val trackerHomeDirectory: Path
    get() {
      val trackerFilesRoot = platform.trackerFilesRoot
      fileSystem.createDirectories(trackerFilesRoot)
      return trackerFilesRoot
    }

  private fun resolveAndCreateDirectory(name: String): Path {
    val directory = trackerHomeDirectory.resolve(name)
    fileSystem.createDirectories(directory)
    return directory
  }

  /**
   * Tracker log file.
   */
  val logFile: Path
    get() = trackerHomeDirectory.resolve("kh2-rando-tracker.log")

  /**
   * Directory that contains auto-save tracker progress.
   */
  val autoSavesDirectory: Path
    get() = resolveAndCreateDirectory("auto-saves")

  /**
   * Directory into which the user can place custom images.
   */
  val customImagesDirectory: Path
    get() = resolveAndCreateDirectory("CustomImages")

  /**
   * Tracker preferences file.
   */
  val preferencesFile: Path
    get() = resolveAndCreateDirectory("preferences").resolve("tracker.preferences_pb")

}
