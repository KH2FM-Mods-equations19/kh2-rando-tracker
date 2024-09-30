package com.kh2rando.tracker.model.item

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.aux_olympus_stone
import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.GameId
import org.jetbrains.compose.resources.DrawableResource

/**
 * The Olympus Stone.
 */
data object OlympusStone : ItemPrototype, RepeatableInventory {

  override val gameId: GameId
    get() = GameId(370)

  override val defaultIcon: DrawableResource
    get() = Res.drawable.aux_olympus_stone

  override val customIconIdentifier: String
    get() = "aux_olympus_stone"

  override val colorToken: ColorToken
    get() = ColorToken.Gold

  override fun inventoryCountAddress(addresses: GameAddresses): Address {
    return addresses.save + 0x3644
  }

}
