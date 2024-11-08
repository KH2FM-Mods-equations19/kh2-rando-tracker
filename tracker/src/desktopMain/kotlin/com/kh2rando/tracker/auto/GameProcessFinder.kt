package com.kh2rando.tracker.auto

import com.kh2rando.tracker.log
import com.kh2rando.tracker.model.GameAddresses
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.Kernel32Util
import com.sun.jna.platform.win32.Psapi
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.ptr.IntByReference
import kotlin.jvm.optionals.getOrNull
import kotlin.streams.asSequence

/**
 * Provides a way to read memory from the game's process.
 */
class GameProcess(memoryReader: MemoryReader, val addresses: GameAddresses) : CanReadMemory by memoryReader

/**
 * Can search for the KH2 game process.
 */
class GameProcessFinder(private val kernel32: Kernel32 = Kernel32.INSTANCE) {

  /**
   * Returns a [GameProcess] ready to read memory from the KH2 game, or throws an exception if unable to find or hook
   * into the game.
   */
  @Throws(GameProcessFinderException::class)
  fun findGame(): GameProcess {
    val processId = getGameProcessId() ?: throw GameProcessFinderException.GameProcessNotFound()

    val processHandle = kernel32.OpenProcess(
      WinNT.PROCESS_VM_READ or WinNT.PROCESS_VM_WRITE or WinNT.PROCESS_VM_OPERATION,
      false,
      processId.toInt()
    ) ?: throw GameProcessFinderException.UnableToHookGame(kernel32.GetLastError(), Kernel32Util.getLastErrorMessage())

    val processStatusApi = Psapi.INSTANCE

    val outModules = arrayOfNulls<WinDef.HMODULE>(5000)
    val outModulesFound = IntByReference(0)
    if (!processStatusApi.EnumProcessModules(processHandle, outModules, outModules.size, outModulesFound)) {
      throw GameProcessFinderException.UnableToHookGame(kernel32.GetLastError(), Kernel32Util.getLastErrorMessage())
    }

    val foundModuleHandle = outModules.asSequence()
      .take(outModulesFound.value)
      .filterNotNull()
      .mapNotNull { moduleHandle ->
        val outModulePath = CharArray(5000) { ' ' }
        val size = processStatusApi.GetModuleFileNameExW(processHandle, moduleHandle, outModulePath, outModulePath.size)
        if (size == 0) {
          // It's possible this one failed but the next one could succeed, in theory, so don't blow up
          null
        } else {
          val modulePath = outModulePath.take(size).joinToString("")
          if ("KINGDOM HEARTS II FINAL MIX.exe" in modulePath) moduleHandle else null
        }
      }
      .firstOrNull()

    return if (foundModuleHandle == null) {
      throw GameProcessFinderException.GameModuleNotFound()
    } else {
      val memoryReader = MemoryReader(kernel32, processHandle, foundModuleHandle.pointer)

      val candidateAddressCollections = listOf(
        EpicGlobal1009(),
//        EpicJp1009(),
        EpicShared10010(),
        SteamGlobal1009(),
        SteamJP1009(),
        SteamShared10010(),
      )
      val addressCollection = candidateAddressCollections.firstOrNull { candidate ->
        val versionCheckResult = memoryReader.readByteAsInt(candidate.versionCheckAddress)
        versionCheckResult == candidate.versionCheckExpectedValue
      } ?: throw GameProcessFinderException.UnsupportedGame()

      log { "Attached to KH2 $addressCollection" }

      GameProcess(memoryReader, addressCollection)
    }
  }

  private fun getGameProcessId(): Long? {
    return ProcessHandle.allProcesses().asSequence()
      .firstOrNull { "KINGDOM HEARTS II FINAL MIX" in it.info().command().getOrNull().orEmpty() }
      ?.pid()
  }

}

sealed class GameProcessFinderException : Exception() {

  /**
   * Unable to find the game process at all.
   */
  class GameProcessNotFound : GameProcessFinderException()

  /**
   * Unable to hook into the game via the process APIs.
   */
  class UnableToHookGame(
    val errorCode: Int,
    val errorMessage: String,
  ) : GameProcessFinderException()

  /**
   * Unable to find the game's module in the process modules. This is expected to be very unlikely to happen in
   * practice.
   */
  class GameModuleNotFound : GameProcessFinderException()

  /**
   * A KH2 game was found, but it is not one of the game versions that is supported by the tracker at this time.
   */
  class UnsupportedGame : GameProcessFinderException()

}
