package com.kh2rando.tracker.model.item

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.misc_torn_pages
import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.GameId
import org.jetbrains.compose.resources.DrawableResource

data object TornPage : ItemPrototype, RepeatableInventory {

  /**
   * The number of Torn Pages in the game.
   */
  const val COPIES: Int = 5

  override val gameId: GameId
    get() = GameId(32)

  override val defaultIcon: DrawableResource
    get() = Res.drawable.misc_torn_pages

  override val customIconIdentifier: String
    get() = "torn_pages"

  override val colorToken: ColorToken
    get() = ColorToken.Gold

  override fun inventoryCountAddress(addresses: GameAddresses): Address {
    return addresses.save + 0x3598
  }

}
