package com.kh2rando.tracker.model.item

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.magic_blizzard
import com.kh2rando.tracker.generated.resources.magic_cure
import com.kh2rando.tracker.generated.resources.magic_fire
import com.kh2rando.tracker.generated.resources.magic_magnet
import com.kh2rando.tracker.generated.resources.magic_reflect
import com.kh2rando.tracker.generated.resources.magic_thunder
import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.GameId
import org.jetbrains.compose.resources.DrawableResource

/**
 * Magic elements.
 */
enum class Magic(
  override val gameId: GameId,
  /**
   * Offset of this item's inventory address from the "save" location.
   */
  private val inventorySaveOffset: Int,
) : ItemPrototype, RepeatableInventory {

  Fire(gameId = GameId(21), inventorySaveOffset = 0x3594) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.magic_fire

    override val customIconIdentifier: String
      get() = "magic_fire"

    override val colorToken: ColorToken
      get() = ColorToken.Orange
  },

  Blizzard(gameId = GameId(22), inventorySaveOffset = 0x3595) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.magic_blizzard

    override val customIconIdentifier: String
      get() = "magic_blizzard"

    override val colorToken: ColorToken
      get() = ColorToken.DarkBlue
  },

  Thunder(gameId = GameId(23), inventorySaveOffset = 0x3596) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.magic_thunder

    override val customIconIdentifier: String
      get() = "magic_thunder"

    override val colorToken: ColorToken
      get() = ColorToken.Gold
  },

  Cure(gameId = GameId(24), inventorySaveOffset = 0x3597) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.magic_cure

    override val customIconIdentifier: String
      get() = "magic_cure"

    override val colorToken: ColorToken
      get() = ColorToken.Green
  },

  Reflect(gameId = GameId(88), inventorySaveOffset = 0x35D0) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.magic_reflect

    override val customIconIdentifier: String
      get() = "magic_reflect"

    override val colorToken: ColorToken
      get() = ColorToken.WhiteBlue
  },

  Magnet(gameId = GameId(87), inventorySaveOffset = 0x35CF) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.magic_magnet

    override val customIconIdentifier: String
      get() = "magic_magnet"

    override val colorToken: ColorToken
      get() = ColorToken.Magenta
  };

  override fun inventoryCountAddress(addresses: GameAddresses): Address {
    return addresses.save + inventorySaveOffset
  }

  companion object {

    /**
     * The number of copies of each magic element in the game.
     */
    const val COPIES: Int = 3

  }

}
