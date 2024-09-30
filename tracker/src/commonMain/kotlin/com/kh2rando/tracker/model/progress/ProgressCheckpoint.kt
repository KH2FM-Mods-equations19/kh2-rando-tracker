package com.kh2rando.tracker.model.progress

import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.serialization.ProgressCheckpointSerializedForm
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.compose.resources.StringResource

/**
 * A checkpoint of progress made within the game.
 */
@Serializable(with = ProgressCheckpointSerializer::class)
interface ProgressCheckpoint : HasCustomizableIcon {

  /**
   * Index of this checkpoint within its [location], for ordering purposes.
   */
  val index: Int

  /**
   * Game location of this checkpoint.
   */
  val location: Location

  /**
   * Display representation of this checkpoint.
   */
  val displayString: StringResource

  /**
   * Whether or not this checkpoint should be shown in the main tracker UI for location progress.
   */
  val showInMainLocationProgress: Boolean
    get() = true

  /**
   * The progress flag that is associated with this checkpoint.
   */
  val associatedFlag: ProgressFlag?
    get() = null

  companion object {

    /**
     * A set of all known [ProgressCheckpoint]s.
     */
    val allCheckpoints: Set<ProgressCheckpoint>
      get() = Location.entries.flatMapTo(mutableSetOf()) { it.progressCheckpoints }

  }

}

/**
 * The list of [ProgressCheckpoint]s for this location.
 */
val Location.progressCheckpoints: List<ProgressCheckpoint>
  get() {
    return when (this) {
      Location.SoraLevels -> SoraLevelProgress.entries
      Location.SimulatedTwilightTown -> SimulatedTwilightTownProgress.entries
      Location.HollowBastion -> HollowBastionProgress.entries
      Location.OlympusColiseum -> OlympusColiseumProgress.entries
      Location.LandOfDragons -> LandOfDragonsProgress.entries
      Location.PrideLands -> PrideLandsProgress.entries
      Location.HalloweenTown -> HalloweenTownProgress.entries
      Location.SpaceParanoids -> SpaceParanoidsProgress.entries
      Location.GardenOfAssemblage -> CavernOfRemembranceProgress.entries
      Location.DriveForms -> DriveFormProgress.entries
      Location.TwilightTown -> TwilightTownProgress.entries
      Location.BeastsCastle -> BeastsCastleProgress.entries
      Location.Agrabah -> AgrabahProgress.entries
      Location.HundredAcreWood -> HundredAcreWoodProgress.entries
      Location.DisneyCastle -> DisneyCastleProgress.entries
      Location.PortRoyal -> PortRoyalProgress.entries
      Location.WorldThatNeverWas -> WorldThatNeverWasProgress.entries
      Location.Atlantica -> AtlanticaProgress.entries
      Location.Creations -> CreationsProgress.entries
    }
  }

/**
 * Represents a [ProgressCheckpoint] using a [ProgressCheckpointSerializedForm].
 */
class ProgressCheckpointSerializer : KSerializer<ProgressCheckpoint> {

  override val descriptor: SerialDescriptor
    get() = ProgressCheckpointSerializedForm.serializer().descriptor

  override fun serialize(encoder: Encoder, value: ProgressCheckpoint) {
    encoder.encodeSerializableValue(
      ProgressCheckpointSerializedForm.serializer(),
      ProgressCheckpointSerializedForm(value.location, value.index)
    )
  }

  override fun deserialize(decoder: Decoder): ProgressCheckpoint {
    val serializedForm = decoder.decodeSerializableValue(ProgressCheckpointSerializedForm.serializer())
    val location = serializedForm.location
    return location.progressCheckpoints.first { it.index == serializedForm.index }
  }

}
