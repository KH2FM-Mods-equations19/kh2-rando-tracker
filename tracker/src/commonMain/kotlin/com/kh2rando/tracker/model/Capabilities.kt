package com.kh2rando.tracker.model

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource

/**
 * An object that provides a [ColorToken] for coloring.
 */
interface HasColorToken {

  /**
   * The tint [ColorToken] to use when applicable.
   */
  val colorToken: ColorToken?

}

/**
 * An object that provides a [defaultIcon] as well as identifying information to find a possible custom icon.
 */
interface HasCustomizableIcon {

  /**
   * The default icon for this object.
   */
  val defaultIcon: DrawableResource

  /**
   * The default tint for this icon, or [Color.Unspecified] if none.
   */
  val defaultIconTint: Color
    get() = Color.Unspecified

  /**
   * Path components for finding this object's custom icon.
   */
  val customIconPath: List<String>

  /**
   * The identifier of this object's custom icon.
   */
  val customIconIdentifier: String

}
