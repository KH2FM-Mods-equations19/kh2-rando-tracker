package com.kh2rando.tracker.model.item

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.summon_chicken_little
import com.kh2rando.tracker.generated.resources.summon_genie
import com.kh2rando.tracker.generated.resources.summon_peter_pan
import com.kh2rando.tracker.generated.resources.summon_stitch
import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.GameId
import org.jetbrains.compose.resources.DrawableResource

/**
 * Summon charms.
 */
enum class SummonCharm(
  override val gameId: GameId,
  /**
   * Offset of this item's inventory address from the "save" location.
   */
  private val inventorySaveOffset: Int,
  override val inventoryBitmask: Int,
) : ItemPrototype, BitmaskedInventory {

  BaseballCharm(gameId = GameId(383), inventorySaveOffset = 0x36C0, inventoryBitmask = 0x08) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.summon_chicken_little

    override val customIconIdentifier: String
      get() = "summon_chicken_little"

    override val colorToken: ColorToken
      get() = ColorToken.Gold
  },

  LampCharm(gameId = GameId(159), inventorySaveOffset = 0x36C4, inventoryBitmask = 0x10) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.summon_genie

    override val customIconIdentifier: String
      get() = "summon_genie"

    override val colorToken: ColorToken
      get() = ColorToken.Purple
  },

  UkuleleCharm(gameId = GameId(25), inventorySaveOffset = 0x36C0, inventoryBitmask = 0x01) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.summon_stitch

    override val customIconIdentifier: String
      get() = "summon_stitch"

    override val colorToken: ColorToken
      get() = ColorToken.WhiteBlue
  },

  FeatherCharm(gameId = GameId(160), inventorySaveOffset = 0x36C4, inventoryBitmask = 0x20) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.summon_peter_pan

    override val customIconIdentifier: String
      get() = "summon_peter_pan"

    override val colorToken: ColorToken
      get() = ColorToken.Red
  };

  override fun inventoryBitmaskAddress(addresses: GameAddresses): Address {
    return addresses.save + inventorySaveOffset
  }

}
