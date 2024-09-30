package com.kh2rando.tracker.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.status_err_connecting
import com.kh2rando.tracker.generated.resources.status_err_unsupported_game_version
import com.kh2rando.tracker.generated.resources.status_not_connected
import com.kh2rando.tracker.generated.resources.status_searching_for_game
import com.kh2rando.tracker.model.AutoTrackingDisplayInfo
import com.kh2rando.tracker.model.seed.SeedSettings
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StatusBar(
  seedSettings: SeedSettings,
  autoTrackingDisplayInfo: () -> AutoTrackingDisplayInfo?,
  modifier: Modifier = Modifier,
) {
  val tooltipPlacement = Alignment.TopCenter

  Row(
    modifier.fillMaxWidth().height(36.dp).padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    run {
      val levelSetting = seedSettings.levelSetting
      val displayString = stringResource(levelSetting.displayString)
      SimpleTooltipArea(tooltipText = displayString, tooltipPlacement = tooltipPlacement) {
        Image(painterResource(levelSetting.defaultIcon), contentDescription = displayString)
      }
    }

    for (toggleSetting in seedSettings.toggleSettings) {
      val displayString = toggleSetting.displayString?.let { stringResource(it) }
      val defaultIcon = toggleSetting.defaultIcon?.let { painterResource(it) }
      if (displayString != null && defaultIcon != null) {
        SimpleTooltipArea(tooltipText = displayString, tooltipPlacement = tooltipPlacement) {
          Image(defaultIcon, contentDescription = displayString)
        }
      }
    }

    Spacer(Modifier.weight(1.0f))

    AutoTrackingStatusArea(autoTrackingDisplayInfo(), modifier = Modifier.size(36.dp))
  }
}

@Composable
private fun AutoTrackingStatusArea(
  autoTrackingDisplayInfo: AutoTrackingDisplayInfo?,
  modifier: Modifier = Modifier
) {
  val text = when (autoTrackingDisplayInfo) {
    null -> return
    AutoTrackingDisplayInfo.Disconnected -> stringResource(Res.string.status_not_connected)
    AutoTrackingDisplayInfo.Scanning -> stringResource(Res.string.status_searching_for_game)
    is AutoTrackingDisplayInfo.Connected -> autoTrackingDisplayInfo.gameVersion
    is AutoTrackingDisplayInfo.UnsupportedGameVersion -> stringResource(Res.string.status_err_unsupported_game_version)
    is AutoTrackingDisplayInfo.GeneralConnectError -> stringResource(Res.string.status_err_connecting)
  }
  SimpleTooltipArea(
    tooltipText = text,
    modifier = modifier,
    tooltipPlacement = Alignment.TopCenter,
  ) {
    when (autoTrackingDisplayInfo) {
      AutoTrackingDisplayInfo.Disconnected -> {
        CustomizableIcon(icon = SystemIcon.AutoTrackerDisconnected, contentDescription = text)
      }

      AutoTrackingDisplayInfo.Scanning -> {
        val systemIcon = SystemIcon.AutoTrackerScanning
        if (systemIcon.findCustomIconFile() == null) {
          val transition = rememberInfiniteTransition()
          val rotation by transition.animateFloat(
            initialValue = 0.0f,
            targetValue = 360.0f,
            animationSpec = infiniteRepeatable(
              animation = tween(durationMillis = 1000, easing = LinearEasing)
            )
          )
          Icon(
            painterResource(systemIcon.defaultIcon),
            contentDescription = text,
            tint = systemIcon.color,
            modifier = Modifier.rotate(rotation)
          )
        } else {
          CustomizableIcon(icon = systemIcon, contentDescription = text)
        }
      }

      is AutoTrackingDisplayInfo.Connected -> {
        CustomizableIcon(icon = SystemIcon.AutoTrackerConnected, contentDescription = text)
      }

      AutoTrackingDisplayInfo.GeneralConnectError, AutoTrackingDisplayInfo.UnsupportedGameVersion -> {
        CustomizableIcon(icon = SystemIcon.AutoTrackerError, contentDescription = text)
      }
    }
  }
}
