package com.kh2rando.tracker.model.item

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.aux_unknowndisk
import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.GameId
import org.jetbrains.compose.resources.DrawableResource

/**
 * The Unknown Disk.
 */
data object UnknownDisk : ItemPrototype, RepeatableInventory {

  override val gameId: GameId
    get() = GameId(462)

  override val defaultIcon: DrawableResource
    get() = Res.drawable.aux_unknowndisk

  override val customIconIdentifier: String
    get() = "aux_unknowndisk"

  override val colorToken: ColorToken
    get() = ColorToken.White

  override fun inventoryCountAddress(addresses: GameAddresses): Address {
    return addresses.save + 0x365F
  }

}
