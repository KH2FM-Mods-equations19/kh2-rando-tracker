package com.kh2rando.tracker

import android.os.Build
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

class AndroidPlatform : Platform {

  override val name: String = "Android ${Build.VERSION.RELEASE}"

}

actual fun getPlatform(): Platform = AndroidPlatform()
