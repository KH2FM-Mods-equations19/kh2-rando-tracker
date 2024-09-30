package com.kh2rando.tracker.model.item

import androidx.compose.runtime.Immutable

/**
 * A unique instance of an [ItemPrototype].
 */
@Immutable
// Deliberately _not_ a data class so that we can get object identity
class UniqueItem(val prototype: ItemPrototype) {

  override fun toString(): String = prototype.toString()

}
