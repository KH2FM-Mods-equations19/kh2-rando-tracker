package com.kh2rando.tracker.model

import androidx.compose.runtime.Immutable

/**
 * Allows model objects to declare color preferences without direct references to UI code.
 */
@Immutable
enum class ColorToken {

  Red,
  Salmon,
  Orange,
  Gold,
  Green,
  LightBlue,
  DarkBlue,
  WhiteBlue,
  Magenta,
  Purple,
  White;

}
