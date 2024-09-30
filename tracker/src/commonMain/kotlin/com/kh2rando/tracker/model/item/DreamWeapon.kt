package com.kh2rando.tracker.model.item

import androidx.compose.ui.graphics.Color
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.dream_shield
import com.kh2rando.tracker.generated.resources.dream_staff
import com.kh2rando.tracker.generated.resources.dream_sword
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.HasColorToken
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.ui.color
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * The weapon that can be chosen to determine Sora's level reward order.
 */
enum class DreamWeapon : HasCustomizableIcon, HasColorToken {

  Sword {
    override val displayString: StringResource
      get() = Res.string.dream_sword
    override val defaultIcon: DrawableResource
      get() = Res.drawable.dream_sword
    override val customIconIdentifier: String
      get() = "sword"
  },
  Staff {
    override val displayString: StringResource
      get() = Res.string.dream_staff
    override val defaultIcon: DrawableResource
      get() = Res.drawable.dream_staff
    override val customIconIdentifier: String
      get() = "staff"
  },
  Shield {
    override val displayString: StringResource
      get() = Res.string.dream_shield
    override val defaultIcon: DrawableResource
      get() = Res.drawable.dream_shield
    override val customIconIdentifier: String
      get() = "shield"
  };

  abstract val displayString: StringResource

  override val defaultIconTint: Color
    get() = color

  override val customIconPath: List<String>
    get() = listOf("System", "stats")

  override val colorToken: ColorToken
    get() = ColorToken.WhiteBlue

}
