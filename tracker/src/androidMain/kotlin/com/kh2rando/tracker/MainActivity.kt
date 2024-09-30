package com.kh2rando.tracker

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.tooling.preview.Preview
import com.kh2rando.tracker.ui.App

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(MainView(this))
  }

}

class MainView(context: Context) : AbstractComposeView(context) {

  @Composable
  override fun Content() {
    Tracker()
  }

}

@Composable
@Preview
fun Tracker() {
  App(gameState = null)
}
