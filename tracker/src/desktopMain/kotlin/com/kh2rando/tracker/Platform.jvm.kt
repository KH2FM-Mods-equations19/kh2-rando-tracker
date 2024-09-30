package com.kh2rando.tracker

import okio.Path
import okio.Path.Companion.toPath

class JVMPlatform : Platform {

  override val name: String = "Java ${System.getProperty("java.version")}"

  override val trackerFilesRoot: Path
    get() {
      val userHome = System.getProperty("user.home").toPath(normalize = true)
      return userHome.resolve("kh2-rando-tracker")
    }

}

actual fun getPlatform(): Platform = JVMPlatform()
