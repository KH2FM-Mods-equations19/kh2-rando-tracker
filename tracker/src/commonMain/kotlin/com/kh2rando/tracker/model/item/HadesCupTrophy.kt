package com.kh2rando.tracker.model.item

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.aux_hades_cup
import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.GameId
import org.jetbrains.compose.resources.DrawableResource

/**
 * The Hades Cup Trophy.
 */
data object HadesCupTrophy : ItemPrototype, RepeatableInventory {

  override val gameId: GameId
    get() = GameId(537)

  override val defaultIcon: DrawableResource
    get() = Res.drawable.aux_hades_cup

  override val customIconIdentifier: String
    get() = "aux_hades_cup"

  override val colorToken: ColorToken
    get() = ColorToken.DarkBlue

  override fun inventoryCountAddress(addresses: GameAddresses): Address {
    return addresses.save + 0x3696
  }

}
