package com.kh2rando.tracker.model

import androidx.compose.runtime.Immutable
import kotlin.jvm.JvmInline

/**
 * An in-game identifier.
 */
@Immutable
@JvmInline
value class GameId(val value: Int)

/**
 * An object that has a [GameId].
 */
interface HasGameId {

  /**
   * The [GameId] of this object.
   */
  val gameId: GameId

}
