package com.kh2rando.tracker.auto

/**
 * Returns true if all bits in [mask] are set in this byte (represented as an int).
 *
 * Uses the appropriate endianness for the KH2 game.
 */
@Suppress("NOTHING_TO_INLINE") // This does seem to perform marginally better when inlined
inline fun Int.isSetByMask(mask: Int): Boolean = (this and mask) == mask

@OptIn(ExperimentalStdlibApi::class)
fun Int.hex(): String {
  return toHexString(HexFormat.UpperCase)
}

//inline fun Int.getBitAtPosition(position: Int): Int = (this shr position) and 1
