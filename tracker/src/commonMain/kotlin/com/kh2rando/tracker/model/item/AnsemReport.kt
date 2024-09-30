package com.kh2rando.tracker.model.item

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.ansem_report_blank
import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.GameId
import org.jetbrains.compose.resources.DrawableResource

/**
 * Secret Ansem Reports.
 */
enum class AnsemReport(
  override val gameId: GameId,
  /**
   * Offset of this item's inventory address from the "save" location.
   */
  private val inventorySaveOffset: Int,
  override val inventoryBitmask: Int,
) : ItemPrototype, BitmaskedInventory {

  Report1(gameId = GameId(226), inventorySaveOffset = 0x36C4, inventoryBitmask = 0x40),
  Report2(gameId = GameId(227), inventorySaveOffset = 0x36C4, inventoryBitmask = 0x80),
  Report3(gameId = GameId(228), inventorySaveOffset = 0x36C5, inventoryBitmask = 0x01),
  Report4(gameId = GameId(229), inventorySaveOffset = 0x36C5, inventoryBitmask = 0x02),
  Report5(gameId = GameId(230), inventorySaveOffset = 0x36C5, inventoryBitmask = 0x04),
  Report6(gameId = GameId(231), inventorySaveOffset = 0x36C5, inventoryBitmask = 0x08),
  Report7(gameId = GameId(232), inventorySaveOffset = 0x36C5, inventoryBitmask = 0x10),
  Report8(gameId = GameId(233), inventorySaveOffset = 0x36C5, inventoryBitmask = 0x20),
  Report9(gameId = GameId(234), inventorySaveOffset = 0x36C5, inventoryBitmask = 0x40),
  Report10(gameId = GameId(235), inventorySaveOffset = 0x36C5, inventoryBitmask = 0x80),
  Report11(gameId = GameId(236), inventorySaveOffset = 0x36C6, inventoryBitmask = 0x01),
  Report12(gameId = GameId(237), inventorySaveOffset = 0x36C6, inventoryBitmask = 0x02),
  Report13(gameId = GameId(238), inventorySaveOffset = 0x36C6, inventoryBitmask = 0x04);

  val reportNumber: Int = ordinal + 1

  override val defaultIcon: DrawableResource
    get() = Res.drawable.ansem_report_blank

  override val customIconIdentifier: String
    get() {
      val normalizedNumber = if (reportNumber < 10) "0$reportNumber" else reportNumber.toString()
      return "ansem_report$normalizedNumber"
    }

  override val colorToken: ColorToken
    get() = ColorToken.White

  override fun inventoryBitmaskAddress(addresses: GameAddresses): Address {
    return addresses.save + inventorySaveOffset
  }

  companion object {

    /**
     * Returns the [AnsemReport] whose report number matches the one provided. Throws an error if out of range.
     */
    fun ofReportNumber(reportNumber: Int): AnsemReport = entries[reportNumber - 1]

  }

}
