package com.kh2rando.tracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

private class ComposeViewModelStoreOwner : ViewModelStoreOwner {

  override val viewModelStore: ViewModelStore = ViewModelStore()

  fun dispose() {
    viewModelStore.clear()
  }

}

/**
 * Return remembered [ViewModelStoreOwner] with the scope of current composable.
 */
@Composable
fun rememberViewModelStoreOwner(): ViewModelStoreOwner {
  val viewModelStoreOwner = remember { ComposeViewModelStoreOwner() }
  DisposableEffect(viewModelStoreOwner) {
    onDispose { viewModelStoreOwner.dispose() }
  }
  return viewModelStoreOwner
}
