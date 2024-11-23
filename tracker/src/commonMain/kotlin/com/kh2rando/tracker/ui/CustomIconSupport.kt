package com.kh2rando.tracker.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import coil3.compose.AsyncImage
import com.kh2rando.tracker.TrackerFileSystem
import com.kh2rando.tracker.log
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.preferences.TrackerPreferences
import com.kh2rando.tracker.model.preferences.collectAsState
import okio.Path
import org.jetbrains.compose.resources.painterResource

@Immutable
data class CustomImagePath(val path: Path?) {

  companion object {

    val None = CustomImagePath(path = null)

  }

}

/**
 * Keeps track of the locations of custom icons so they don't need to be repeatedly searched.
 */
class CustomizableIconRegistry {

  private val cache = mutableMapOf<HasCustomizableIcon, CustomImagePath>()

  /**
   * Returns the [Path] to the customized icon to use for [icon], if it exists and if [useCustomImages] is true;
   * otherwise, returns null.
   */
  fun customPathOrNull(icon: HasCustomizableIcon, useCustomImages: Boolean): Path? {
    return if (useCustomImages) {
      cache.getOrPut(icon) {
        log { "lookup for $icon" }
        val folder = customImagesPath.resolve(icon.customIconPath.joinToString(Path.DIRECTORY_SEPARATOR))
        val iconIdentifier = icon.customIconIdentifier
        val foundFile = TrackerFileSystem.fileSystem.listOrNull(folder).orEmpty().firstOrNull { path ->
          val nameWithoutExtension = path.name.substringBeforeLast(".")
          nameWithoutExtension == iconIdentifier
        }
        if (foundFile == null) CustomImagePath.None else CustomImagePath(foundFile)
      }.path
    } else {
      null
    }
  }

  companion object {

    val customImagesPath: Path by lazy { TrackerFileSystem.customImagesDirectory }

    val LocalCustomizableIconRegistry = staticCompositionLocalOf { CustomizableIconRegistry() }

    val current: CustomizableIconRegistry
      @Composable
      get() = LocalCustomizableIconRegistry.current

  }

}

/**
 * Locates the custom icon file for this [HasCustomizableIcon], if it exists and if the user wants to use custom
 * images.
 */
@Composable
fun HasCustomizableIcon.findCustomIconFile(force: Boolean = false): Path? {
  val useCustomImages by TrackerPreferences.current.useCustomImages.collectAsState()
  return CustomizableIconRegistry.current.customPathOrNull(icon = this, useCustomImages = force || useCustomImages)
}

@Composable
fun CustomizableIcon(
  icon: HasCustomizableIcon,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  alpha: Float = DefaultAlpha,
  tintColorOverride: Color? = null,
) {
  val customIconFile = icon.findCustomIconFile()
  val defaultIcon = painterResource(icon.defaultIcon)
  if (customIconFile == null) {
    Image(
      defaultIcon,
      contentDescription = contentDescription,
      modifier = modifier,
      alpha = alpha,
      colorFilter = tintColorOverride?.tintFilterOrNull() ?: icon.defaultIconTint.tintFilterOrNull(),
    )
  } else {
    AsyncImage(
      customIconFile,
      contentDescription = contentDescription,
      error = defaultIcon,
      modifier = modifier,
      alpha = alpha,
      colorFilter = tintColorOverride?.tintFilterOrNull(),
      onError = { error ->
        log { "Error loading custom icon for $icon - ${error.result.throwable.message}" }
      }
    )
  }
}
