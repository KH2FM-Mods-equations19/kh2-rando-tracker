package com.kh2rando.tracker

import okio.Path

interface Platform {

  val name: String

  /**
   * Root directory where tracker files can be stored.
   */
  val trackerFilesRoot: Path

}

expect fun getPlatform(): Platform
