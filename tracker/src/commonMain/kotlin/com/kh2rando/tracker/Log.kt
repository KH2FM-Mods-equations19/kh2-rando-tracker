package com.kh2rando.tracker

var debugMode = false

inline fun log(lazyMessage: () -> Any) {
  if (debugMode) {
    println(lazyMessage())
  }
}
