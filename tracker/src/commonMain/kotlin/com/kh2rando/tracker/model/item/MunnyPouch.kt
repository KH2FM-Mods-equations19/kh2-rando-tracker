package com.kh2rando.tracker.model.item

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.aux_munny_pouch
import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.GameId
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.item.MunnyPouch.Mickey
import com.kh2rando.tracker.model.item.MunnyPouch.Olette
import org.jetbrains.compose.resources.DrawableResource

/**
 * Munny pouches. There are two distinct items in the game, one from [Olette] and one from [Mickey].
 */
enum class MunnyPouch(override val gameId: GameId) : ItemPrototype, RepeatableInventory {

  Olette(gameId = GameId(362)) {
    override val colorToken: ColorToken
      get() = Location.SimulatedTwilightTown.colorToken

    override fun inventoryCountAddress(addresses: GameAddresses): Address {
      return addresses.save + 0x363C
    }
  },

  Mickey(gameId = GameId(535)) {
    override val colorToken: ColorToken
      get() = Location.TwilightTown.colorToken

    override fun inventoryCountAddress(addresses: GameAddresses): Address {
      return addresses.save + 0x3695
    }
  };

  override val defaultIcon: DrawableResource
    get() = Res.drawable.aux_munny_pouch

  override val customIconIdentifier: String
    get() = "aux_munny_pouch"

}
