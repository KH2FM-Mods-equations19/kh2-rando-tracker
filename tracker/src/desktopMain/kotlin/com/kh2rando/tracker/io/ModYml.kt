package com.kh2rando.tracker.io

import kotlinx.serialization.Serializable

@Serializable
class ModYml(val assets: List<ModAsset>)

@Serializable
class ModAsset(val name: String, val source: List<AssetSource>)

@Serializable
class AssetSource(val name: String)
