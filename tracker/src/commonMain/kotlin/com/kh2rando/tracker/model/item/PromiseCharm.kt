package com.kh2rando.tracker.model.item

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.misc_promise_charm
import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.GameId
import org.jetbrains.compose.resources.DrawableResource

/**
 * The Promise Charm.
 */
data object PromiseCharm : ItemPrototype, RepeatableInventory {

  override val gameId: GameId
    get() = GameId(524)

  override val defaultIcon: DrawableResource
    get() = Res.drawable.misc_promise_charm

  override val customIconIdentifier: String
    get() = "promise_charm"

  override val colorToken: ColorToken
    get() = ColorToken.Salmon

  override fun inventoryCountAddress(addresses: GameAddresses): Address {
    return addresses.save + 0x3694
  }

}
