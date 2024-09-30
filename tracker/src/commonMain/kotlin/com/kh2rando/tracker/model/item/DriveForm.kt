package com.kh2rando.tracker.model.item

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.form_anti
import com.kh2rando.tracker.generated.resources.form_final
import com.kh2rando.tracker.generated.resources.form_limit
import com.kh2rando.tracker.generated.resources.form_master
import com.kh2rando.tracker.generated.resources.form_valor
import com.kh2rando.tracker.generated.resources.form_wisdom
import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.GameId
import org.jetbrains.compose.resources.DrawableResource

/**
 * Drive Forms.
 *
 * Note that Valor and Final are represented in this enum by their dummy items. See [RealForm] for a more detailed
 * explanation.
 */
enum class DriveForm(
  override val gameId: GameId,
  /**
   * Offset of this item's inventory address from the "save" location.
   */
  private val inventorySaveOffset: Int,
  override val inventoryBitmask: Int,
  /**
   * Offset of this drive form's level address from the "save" location.
   */
  private val levelSaveOffset: Int,
) : ItemPrototype, BitmaskedInventory {

  ValorFormDummy(
    gameId = GameId(89),
    inventorySaveOffset = 0x36C0,
    inventoryBitmask = 0x80,
    levelSaveOffset = 0x32F6
  ) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.form_valor

    override val customIconIdentifier: String
      get() = "form_valor"

    override val colorToken: ColorToken
      get() = ColorToken.Red

    override val secondaryGameId: GameId
      get() = RealForm.Valor.gameId
  },

  WisdomForm(
    gameId = GameId(27),
    inventorySaveOffset = 0x36C0,
    inventoryBitmask = 0x04,
    levelSaveOffset = 0x332E
  ) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.form_wisdom

    override val customIconIdentifier: String
      get() = "form_wisdom"

    override val colorToken: ColorToken
      get() = ColorToken.LightBlue
  },

  LimitForm(
    gameId = GameId(563),
    inventorySaveOffset = 0x36CA,
    inventoryBitmask = 0x08,
    levelSaveOffset = 0x3366
  ) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.form_limit

    override val customIconIdentifier: String
      get() = "form_limit"

    override val colorToken: ColorToken
      get() = ColorToken.Orange
  },

  MasterForm(
    gameId = GameId(31),
    inventorySaveOffset = 0x36C0,
    inventoryBitmask = 0x40,
    levelSaveOffset = 0x339E
  ) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.form_master

    override val customIconIdentifier: String
      get() = "form_master"

    override val colorToken: ColorToken
      get() = ColorToken.Gold
  },

  FinalFormDummy(
    gameId = GameId(115),
    inventorySaveOffset = 0x36C2,
    inventoryBitmask = 0x02,
    levelSaveOffset = 0x33D6
  ) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.form_final

    override val customIconIdentifier: String
      get() = "form_final"

    override val colorToken: ColorToken
      get() = ColorToken.White

    override val secondaryGameId: GameId
      get() = RealForm.Final.gameId
  },

  AntiForm(
    gameId = GameId(30),
    inventorySaveOffset = 0x36C0,
    inventoryBitmask = 0x20,
    levelSaveOffset = 0x340C
  ) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.form_anti

    override val customIconIdentifier: String
      get() = "form_anti"

    override val colorToken: ColorToken
      get() = ColorToken.Purple
  };

  override fun inventoryBitmaskAddress(addresses: GameAddresses): Address {
    return addresses.save + inventorySaveOffset
  }

  /**
   * Computes the memory address of this drive form's level.
   */
  fun formLevelAddress(addresses: GameAddresses): Address {
    return addresses.save + levelSaveOffset
  }

}

/**
 * These are the real form inventory items for Valor and Final Forms. However, the GoA mod uses dummy items to represent
 * Valor and Final in order to allow the tracker to differentiate if either of those forms were activated for other
 * reasons (in Valor's case for Valor Genie reasons, and in Final's case for forcing Final).
 *
 * In most cases, the tracker will only care about the dummy items, so these real form items are kept as a separate
 * category that should never be added to the list of trackable items.
 */
enum class RealForm(
  override val gameId: GameId,
  /**
   * Offset of this item's inventory address from the "save" location.
   */
  private val inventorySaveOffset: Int,
  override val inventoryBitmask: Int,
) : ItemPrototype, BitmaskedInventory {

  Valor(
    gameId = GameId(26),
    inventorySaveOffset = 0x36C0,
    inventoryBitmask = 0x02,
  ) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.form_valor

    override val customIconIdentifier: String
      get() = "form_valor"

    override val colorToken: ColorToken
      get() = ColorToken.Gold
  },

  Final(
    gameId = GameId(29),
    inventorySaveOffset = 0x36C0,
    inventoryBitmask = 0x10,
  ) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.form_final

    override val customIconIdentifier: String
      get() = "form_final"

    override val colorToken: ColorToken
      get() = ColorToken.Gold
  };

  override fun inventoryBitmaskAddress(addresses: GameAddresses): Address {
    return addresses.save + inventorySaveOffset
  }

}
