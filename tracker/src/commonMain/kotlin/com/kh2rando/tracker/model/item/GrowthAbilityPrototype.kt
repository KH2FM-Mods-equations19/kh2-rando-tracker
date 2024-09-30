package com.kh2rando.tracker.model.item

import androidx.compose.ui.graphics.Color
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.growth_aerial_dodge
import com.kh2rando.tracker.generated.resources.growth_dodge_roll
import com.kh2rando.tracker.generated.resources.growth_glide
import com.kh2rando.tracker.generated.resources.growth_high_jump
import com.kh2rando.tracker.generated.resources.growth_quick_run
import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.HasColorToken
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.ui.color
import org.jetbrains.compose.resources.DrawableResource

/**
 * Growth abilities.
 */
enum class GrowthAbilityPrototype(
  /**
   * Offset of this item's inventory address from the "save" location.
   */
  private val inventorySaveOffset: Int,
  /**
   * Used when reading memory to determine obtained growth level.
   */
  val levelOffset: Int,
) : HasCustomizableIcon, HasColorToken {

  HighJump(inventorySaveOffset = 0x25CE, levelOffset = 93) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.growth_high_jump
    override val customIconIdentifier: String
      get() = "high_jump"
    override val colorToken: ColorToken?
      get() = DriveForm.ValorFormDummy.colorToken
  },

  QuickRun(inventorySaveOffset = 0x25D0, levelOffset = 97) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.growth_quick_run
    override val customIconIdentifier: String
      get() = "quick_run"
    override val colorToken: ColorToken?
      get() = DriveForm.WisdomForm.colorToken
  },

  DodgeRoll(inventorySaveOffset = 0x25D2, levelOffset = 563) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.growth_dodge_roll
    override val customIconIdentifier: String
      get() = "dodge_roll"
    override val colorToken: ColorToken?
      get() = DriveForm.LimitForm.colorToken
  },

  AerialDodge(inventorySaveOffset = 0x25D4, levelOffset = 101) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.growth_aerial_dodge
    override val customIconIdentifier: String
      get() = "aerial_dodge"
    override val colorToken: ColorToken?
      get() = DriveForm.MasterForm.colorToken
  },

  Glide(inventorySaveOffset = 0x25D6, levelOffset = 105) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.growth_glide
    override val customIconIdentifier: String
      get() = "glide"
    override val colorToken: ColorToken?
      get() = DriveForm.FinalFormDummy.colorToken
  };

  override val defaultIconTint: Color
    get() = color

  override val customIconPath: List<String>
    get() = listOf("System", "drive_growth")

  /**
   * Computes the memory address for this growth ability.
   */
  fun inventoryAddress(addresses: GameAddresses): Address {
    return addresses.save + inventorySaveOffset
  }

}
