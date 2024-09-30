package com.kh2rando.tracker.auto

import com.kh2rando.tracker.model.Address
import com.sun.jna.Memory
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.WinNT

/**
 * An object that has access to reading memory.
 */
interface CanReadMemory {

  /**
   * Reads [byteCount] bytes, starting at [address].
   */
  fun readBytes(address: Address, byteCount: Int): ByteArray

  /**
   * Reads a single byte at [address].
   */
  fun readByte(address: Address): Byte

  /**
   * Reads two bytes at [address] and converts the result to int.
   */
  fun readShortAsInt(address: Address): Int

}

/**
 * Concrete implementation of [CanReadMemory].
 */
class MemoryReader(
  private val kernel32: Kernel32,
  private val processHandle: WinNT.HANDLE,
  private val baseAddress: Pointer,
): CanReadMemory {

  override fun readBytes(address: Address, byteCount: Int): ByteArray {
    return readMemory(address, byteCount) { getByteArray(0L, byteCount) }
  }

  override fun readByte(address: Address): Byte {
    return readMemory(address, Byte.SIZE_BYTES) { getByte(0L) }
  }

  override fun readShortAsInt(address: Address): Int {
    return readMemory(address, Short.SIZE_BYTES) { getShort(0L).toInt() }
  }

  private inline fun <T> readMemory(address: Address, size: Int, function: Memory.() -> T): T {
    return Memory(size.toLong()).use { memory ->
      val pointer = baseAddress.share(address.address)
      if (kernel32.ReadProcessMemory(processHandle, pointer, memory, size, null)) {
        memory.function()
      } else {
        error("Couldn't read")
      }
    }
  }

}

/**
 * Reads a single byte at [address] and converts to int.
 */
@Suppress("NOTHING_TO_INLINE") // We do want to micro-optimize the auto tracker code
inline fun CanReadMemory.readByteAsInt(address: Address): Int {
  return readByte(address).toInt()
}
