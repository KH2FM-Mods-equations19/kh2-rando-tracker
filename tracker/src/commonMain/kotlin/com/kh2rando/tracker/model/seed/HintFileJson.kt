package com.kh2rando.tracker.model.seed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Codifies the encoded form of the hint file included in a randomizer seed.
 */
@Serializable
data class HintFileJson(
  /**
   * Version of the seed generator.
   */
  val generatorVersion: String,

  /**
   * Type of hint system.
   */
  val hintsType: String,

  /**
   * A bucket of settings. Includes, among other things, the enabled locations.
   */
  val settings: Set<String>,

  /**
   * Point values for various item types as well as bonus point categories.
   */
  val checkValue: Map<String, Int>,

  /**
   * Item types that should be displayed in the tracker and tracked.
   */
  @SerialName("hintableItems")
  val trackableItems: List<String>? = null,

  /**
   * Item types that should be revealed when revealing a location's items. Primarily used for Spoiler hints.
   */
  @SerialName("reveal")
  val revealSettings: List<String>? = null,

  /**
   * Whether or not dummy items are being used to track Valor and Final Forms.
   */
  @SerialName("dummy_forms")
  val dummyForms: Boolean,

  @SerialName("level_data")
  val levelData: LevelData,

  /**
   * Items in each game location. Note that not all hint systems fill this in.
   */
  @SerialName("world")
  val itemsByLocation: Map<String, List<Int>>? = null,

  /**
   * Items that Sora starts the game with. Includes items as well as things like abilities.
   */
  val startingInventory: List<Int>,

  /**
   * Report information. Note that this can be bigger than 1-13 when using progression hints.
   */
  @SerialName("Reports")
  val reports: Map<String, ReportData>? = null,

  /**
   * General progression hint settings.
   */
  @SerialName("ProgressionSettings")
  val progressionSettings: ProgressionSettings? = null,

  /**
   * Used by Shananas and Points to indicate the order progression hints should be revealed in those hint systems.
   */
  @SerialName("world_order")
  val worldOrder: List<String>? = null,

  /**
   * The number of objectives needed to unlock the final door.
   *
   * Only required when the Final Door Requirement is Objectives.
   */
  @SerialName("num_objectives_needed")
  val numberOfObjectivesNeeded: Int? = null,

  /**
   * Information about each possible objective.
   */
  @SerialName("objective_locations")
  val objectiveLocations: List<ObjectiveLocation>? = null,

  /**
   * Settings for using Lucky Emblems as a Final Door Requirement, otherwise not present.
   */
  @SerialName("emblems")
  val emblemSettings: EmblemSettings? = null,

  ) {

  @Serializable
  class LevelData(
    @SerialName("Valor Level") val valorLevel: Map<String, Int>,
    @SerialName("Wisdom Level") val wisdomLevel: Map<String, Int>,
    @SerialName("Limit Level") val limitLevel: Map<String, Int>,
    @SerialName("Master Level") val masterLevel: Map<String, Int>,
    @SerialName("Final Level") val finalLevel: Map<String, Int>,
    @SerialName("Level") val level: SoraLevelData,
  )

  @Serializable
  class SoraLevelData(
    @SerialName("Sword") val sword: Map<String, Int>,
    @SerialName("Staff") val staff: Map<String, Int>,
    @SerialName("Shield") val shield: Map<String, Int>,
  )

  @Serializable
  class ReportData(
    // Different hint systems put the "hinted location" under a different key
    /**
     * The location hinted by this report.
     */
    @SerialName("World") val hintedLocation: String? = null,

    /**
     * The location hinted by this report.
     */
    @SerialName("HintedWorld") val hintedWorld: String? = null,

//    @SerialName("Text") val reportText: String? = null,

    /**
     * The text for Jiminy's Journal from this hint, if any.
     */
    @SerialName("JournalText") val journalText: String? = null,

    // JSmartee-specific
    /**
     * The number of Important Checks in the location hinted by this report (JSmartee-specific).
     */
    @SerialName("Count") val count: Int? = null,

    /**
     * The location this report was found in.
     */
    // Most systems don't really need this because they have full item location info.
    // But JSmartee at the least doesn't include full item location info (yet!).
    @SerialName("Location") val reportLocation: String? = null,

    // Points-specific
    /**
     * The game ID of the item revealed by this report, if any.
     */
    @SerialName("check") val revealedItemId: Int? = null,

//    @SerialName("itemName") val revealedItemDescription: String? = null,

    // Path-specific
    /**
     * The proof(s) that this report is relevant to (Path-specific).
     */
    @SerialName("ProofPath") val proofPath: List<String>? = null,
  ) {

    /**
     * The raw location hinted by this report.
     */
    val rawHintedLocation: String?
      get() = hintedWorld ?: hintedLocation

  }

  @Serializable
  class ProgressionSettings(
    @SerialName("Levels")
    val levels: List<Int>,
    @SerialName("Drives")
    val driveForms: List<Int>,
    @SerialName("SimulatedTwilightTown")
    val simulatedTwilightTown: List<Int>,
    @SerialName("TwilightTown")
    val twilightTown: List<Int>,
    @SerialName("HollowBastion")
    val hollowBastion: List<Int>,
    @SerialName("BeastsCastle")
    val beastsCastle: List<Int>,
    @SerialName("OlympusColiseum")
    val olympusColiseum: List<Int>,
    @SerialName("Agrabah")
    val agrabah: List<Int>,
    @SerialName("LandofDragons")
    val landOfDragons: List<Int>,
    @SerialName("HundredAcreWood")
    val hundredAcreWood: List<Int>,
    @SerialName("PrideLands")
    val prideLands: List<Int>,
    @SerialName("Atlantica")
    val atlantica: List<Int>,
    @SerialName("DisneyCastle")
    val disneyCastle: List<Int>,
    @SerialName("HalloweenTown")
    val halloweenTown: List<Int>,
    @SerialName("PortRoyal")
    val portRoyal: List<Int>,
    @SerialName("SpaceParanoids")
    val spaceParanoids: List<Int>,
    @SerialName("TWTNW")
    val worldThatNeverWas: List<Int>,
    @SerialName("CavernofRemembrance")
    val cavernOfRemembrance: List<Int>,
    @SerialName("HintCosts")
    val hintCosts: List<Int>,
    @SerialName("WorldCompleteBonus")
    val worldCompleteBonus: List<Int>,
    @SerialName("ReportBonus")
    val reportBonus: List<Int>,
    @SerialName("FinalXemnasReveal")
    val finalXemnasReveal: List<Int>,
  )

  @Serializable
  class ObjectiveLocation(
    val category: String,
    @SerialName("location_id")
    val locationId: Int
  )

  @Serializable
  class EmblemSettings(
    @SerialName("max_emblems_available")
    val maxEmblemsAvailable: Int,
    @SerialName("num_emblems_needed")
    val numberOfEmblemsNeeded: Int,
  )

}
