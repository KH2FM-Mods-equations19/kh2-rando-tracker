package com.kh2rando.tracker

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.kh2rando.tracker.model.GameState
import com.kh2rando.tracker.ui.App
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
  ComposeViewport(document.body!!) {
    App(GameState())
  }
}
