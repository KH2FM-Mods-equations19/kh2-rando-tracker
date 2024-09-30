package com.kh2rando.tracker.serialization

import com.kh2rando.tracker.model.Location
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ProgressCheckpoint")
class ProgressCheckpointSerializedForm(val location: Location, val index: Int)
