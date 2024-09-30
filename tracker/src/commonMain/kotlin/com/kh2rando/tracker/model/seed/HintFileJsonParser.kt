@file:OptIn(ExperimentalEncodingApi::class)

package com.kh2rando.tracker.model.seed

import com.kh2rando.tracker.log
import com.kh2rando.tracker.model.CheckLocation
import com.kh2rando.tracker.model.CheckLocationCategory
import com.kh2rando.tracker.model.GameId
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.hints.BasicProgressionSettings
import com.kh2rando.tracker.model.hints.DisabledHint
import com.kh2rando.tracker.model.hints.DisabledHintSystem
import com.kh2rando.tracker.model.hints.Hint
import com.kh2rando.tracker.model.hints.HintSystem
import com.kh2rando.tracker.model.hints.JSmarteeHint
import com.kh2rando.tracker.model.hints.JSmarteeHintSystem
import com.kh2rando.tracker.model.hints.PathHint
import com.kh2rando.tracker.model.hints.PathHintSystem
import com.kh2rando.tracker.model.hints.PointsHint
import com.kh2rando.tracker.model.hints.PointsHintSystem
import com.kh2rando.tracker.model.hints.ShananasHint
import com.kh2rando.tracker.model.hints.ShananasHintSystem
import com.kh2rando.tracker.model.hints.SpoilerHint
import com.kh2rando.tracker.model.hints.SpoilerHintSystem
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.ChestUnlockKeyblade
import com.kh2rando.tracker.model.item.DreamWeapon
import com.kh2rando.tracker.model.item.DriveForm
import com.kh2rando.tracker.model.item.HadesCupTrophy
import com.kh2rando.tracker.model.item.ImportantAbility
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.ItemPrototype.Companion.checkGameIds
import com.kh2rando.tracker.model.item.Magic
import com.kh2rando.tracker.model.item.MunnyPouch
import com.kh2rando.tracker.model.item.OlympusStone
import com.kh2rando.tracker.model.item.PromiseCharm
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.item.SummonCharm
import com.kh2rando.tracker.model.item.TornPage
import com.kh2rando.tracker.model.item.UnknownDisk
import com.kh2rando.tracker.model.item.VisitUnlock
import com.kh2rando.tracker.model.objective.Objective
import com.kh2rando.tracker.model.progress.AgrabahProgress
import com.kh2rando.tracker.model.progress.AtlanticaProgress
import com.kh2rando.tracker.model.progress.BeastsCastleProgress
import com.kh2rando.tracker.model.progress.CavernOfRemembranceProgress
import com.kh2rando.tracker.model.progress.DisneyCastleProgress
import com.kh2rando.tracker.model.progress.DriveFormProgress
import com.kh2rando.tracker.model.progress.HalloweenTownProgress
import com.kh2rando.tracker.model.progress.HollowBastionProgress
import com.kh2rando.tracker.model.progress.HundredAcreWoodProgress
import com.kh2rando.tracker.model.progress.LandOfDragonsProgress
import com.kh2rando.tracker.model.progress.OlympusColiseumProgress
import com.kh2rando.tracker.model.progress.PortRoyalProgress
import com.kh2rando.tracker.model.progress.PrideLandsProgress
import com.kh2rando.tracker.model.progress.ProgressCheckpoint
import com.kh2rando.tracker.model.progress.SimulatedTwilightTownProgress
import com.kh2rando.tracker.model.progress.SoraLevelProgress
import com.kh2rando.tracker.model.progress.SpaceParanoidsProgress
import com.kh2rando.tracker.model.progress.TwilightTownProgress
import com.kh2rando.tracker.model.progress.WorldThatNeverWasProgress
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Parses a hint file into a [SeedSettings] object.
 */
class HintFileJsonParser {

  private val json: Json = Json { ignoreUnknownKeys = true }

  fun parseDecodedHintJson(jsonString: String): SeedSettings {
    val hintFileJson = json.decodeFromString<HintFileJson>(jsonString)

    val levelSetting = hintFileJson.parseLevelSetting()
    val creationsOptions = hintFileJson.parseCreationsOptions()
    val enabledLocations = hintFileJson.parseEnabledLocations(levelSetting, creationsOptions)
    val trackableItems = hintFileJson.parseTrackableItems()
    val settingsBucket = hintFileJson.settings
    val toggleSettings = buildSet {
      if ("Absent Silhouettes" in settingsBucket) {
        if ("Data Split" in settingsBucket) {
          add(ToggleSetting.AbsentSilhouettesSplit)
        } else {
          add(ToggleSetting.AbsentSilhouettes)
        }
      }
      if ("Data Organization XIII" in settingsBucket) add(ToggleSetting.DataOrganization)
      if ("Sephiroth" in settingsBucket) add(ToggleSetting.Sephiroth)
      if ("Lingering Will (Terra)" in settingsBucket) add(ToggleSetting.LingeringWill)
      if ("Olympus Cups" in settingsBucket) add(ToggleSetting.UnderdromeCups)
      if ("Hades Paradox Cup" in settingsBucket) add(ToggleSetting.HadesParadoxCup)
      if ("Cavern of Remembrance" in settingsBucket) add(ToggleSetting.CavernOfRemembrance)
      if ("Transport to Remembrance" in settingsBucket) add(ToggleSetting.TransportToRemembrance)
      if ("Dream Weapon Matters" in settingsBucket) add(ToggleSetting.DreamWeaponMatters)
      if ("better_stt" in settingsBucket) add(ToggleSetting.RoxasMovementEtc)
      if ("ScoreMode" in settingsBucket) add(ToggleSetting.HighScoreMode)
    }
    return SeedSettings(
      generatorVersion = hintFileJson.generatorVersion,
      enabledLocations = enabledLocations,
      trackableItems = trackableItems,
      hintSystem = hintFileJson.parseHintSystem(enabledLocations, trackableItems),
      finalDoorRequirement = hintFileJson.parseFinalDoorRequirement(),
      levelSetting = levelSetting,
      levelChecks = hintFileJson.parseLevelChecks(trackableItems),
      creationsOptions = creationsOptions,
      toggleSettings = toggleSettings,
    )
  }

  fun parseEncodedHintData(encodedData: String): SeedSettings {
    val decodedData = Base64.Default.decode(encodedData)
    return parseDecodedHintJson(decodedData.toString(Charsets.UTF_8))
  }

  /**
   * Determines the [LevelSetting].
   */
  private fun HintFileJson.parseLevelSetting(): LevelSetting {
    return if ("ExcludeFrom50" in settings) {
      LevelSetting.Level50
    } else if ("ExcludeFrom99" in settings) {
      LevelSetting.Level99
    } else {
      LevelSetting.Level1
    }
  }

  /**
   * Determines which [CreationsOption]s are active, if any.
   */
  private fun HintFileJson.parseCreationsOptions(): Set<CreationsOption> {
    return buildSet {
      if ("Puzzle" in settings) add(CreationsOption.Puzzle)
      if ("Synthesis" in settings) add(CreationsOption.Synthesis)
    }
  }

  /**
   * Builds a set of locations that can have randomized checks.
   *
   * Always includes [Location.GardenOfAssemblage] - serves as a catch-all for starting items, critical bonuses, etc.
   */
  private fun HintFileJson.parseEnabledLocations(
    levelSetting: LevelSetting,
    creationsOptions: Set<CreationsOption>,
  ): Set<Location> {
    return buildSet {
      // Garden of Assemblage is always on in the tracker for starting items
      add(Location.GardenOfAssemblage)

      when (levelSetting) {
        LevelSetting.Level1 -> {}
        LevelSetting.Level50, LevelSetting.Level99 -> add(Location.SoraLevels)
      }

      for (rawSetting in settings) {
        when (rawSetting) {
          "Land of Dragons" -> add(Location.LandOfDragons)
          "Beast's Castle" -> add(Location.BeastsCastle)
          "Hollow Bastion" -> add(Location.HollowBastion)
          "Twilight Town" -> add(Location.TwilightTown)
          "The World That Never Was" -> add(Location.WorldThatNeverWas)
          "Space Paranoids" -> add(Location.SpaceParanoids)
          "Port Royal" -> add(Location.PortRoyal)
          "Olympus Coliseum" -> add(Location.OlympusColiseum)
          "Agrabah" -> add(Location.Agrabah)
          "Halloween Town" -> add(Location.HalloweenTown)
          "Pride Lands" -> add(Location.PrideLands)
          "Disney Castle / Timeless River" -> add(Location.DisneyCastle)
          "Hundred Acre Wood" -> add(Location.HundredAcreWood)
          "Simulated Twilight Town" -> add(Location.SimulatedTwilightTown)
          "Form Levels" -> add(Location.DriveForms)
          "Atlantica" -> add(Location.Atlantica)
        }
      }

      if (creationsOptions.isNotEmpty()) {
        add(Location.Creations)
      }
    }
  }
}

/**
 * Builds a set of the item types considered to be trackable.
 */
private fun HintFileJson.parseTrackableItems(): Set<ItemPrototype> {
  val trackableItems = trackableItems ?: return emptySet()
  return buildSet {
    for (rawItemType in trackableItems) {
      when (rawItemType) {
        "magic" -> {
          addAll(Magic.entries)
        }

        "form" -> {
          addAll(DriveForm.entries)
          if ("Anti-Form" !in settings) {
            remove(DriveForm.AntiForm)
          }
        }

        "summon" -> {
          addAll(SummonCharm.entries)
        }

        "page" -> {
          add(TornPage)
        }

        "ability" -> {
          addAll(ImportantAbility.entries)
        }

        "visit" -> {
          addAll(VisitUnlock.entries)
        }

        "proof" -> {
          addAll(Proof.entries)
        }

        "report" -> {
          addAll(AnsemReport.entries)
        }

        "keyblade" -> {
          addAll(ChestUnlockKeyblade.entries)
        }

        "other" -> {
          addAll(MunnyPouch.entries)
          add(HadesCupTrophy)
          add(OlympusStone)
          add(UnknownDisk)
        }
      }
    }

    if ("PromiseCharm" in settings) {
      add(PromiseCharm)
    }
  }
}

/**
 * Builds an association between each enabled location and the trackable items in that location.
 *
 * Not available for all hint types.
 */
private fun parseItemsByLocation(
  hintFileJson: HintFileJson,
  enabledLocations: Set<Location>,
  trackableItems: Set<ItemPrototype>,
): Map<Location, List<ItemPrototype>>? {
  val rawItemsByLocation = hintFileJson.itemsByLocation ?: return null

  // Pre-populate all of these with an empty builder
  val itemsByLocation = mutableMapOf<Location, MutableList<ItemPrototype>>()
  for (location in enabledLocations) {
    itemsByLocation[location] = mutableListOf()
  }

  val unassignedItems = ItemPrototype.fullList.filterTo(mutableListOf()) { it in trackableItems }

  for ((rawLocation, itemIds) in rawItemsByLocation) {
    val location = checkNotNull(resolveItemLocation(rawLocation)) { "Unexpected location name $rawLocation" }

    // itemsByLocation is already filtered to enabled locations, so this can very well be null
    val locationItems = itemsByLocation[location] ?: continue
    for (itemId in itemIds) {
      val uniqueItemIndex = unassignedItems.indexOfFirst { it.checkGameIds(target = GameId(itemId)) }
      if (uniqueItemIndex == -1) {
        log { "Couldn't find item with ID $itemId in remaining unassigned items" }
      } else {
        val unassignedItem = unassignedItems.removeAt(uniqueItemIndex)
        locationItems.add(unassignedItem)
      }
    }
  }

  run {
    val locationItems = itemsByLocation.getValue(Location.GardenOfAssemblage)
    for (itemId in hintFileJson.startingInventory) {
      val uniqueItemIndex = unassignedItems.indexOfFirst { it.checkGameIds(target = GameId(itemId)) }
      if (uniqueItemIndex == -1) {
        // Starting inventory includes abilities and other things, so it's expected that not all match here
        continue
      } else {
        val unassignedItem = unassignedItems.removeAt(uniqueItemIndex)
        locationItems.add(unassignedItem)
      }
    }
  }

  for ((location, items) in itemsByLocation) {
    log { "$location : $items" }
  }

  return itemsByLocation
}

/**
 * Determines the type of and builds the [HintSystem] configured for this seed.
 */
private fun HintFileJson.parseHintSystem(
  enabledLocations: Set<Location>,
  trackableItems: Set<ItemPrototype>,
): HintSystem {
  val itemsByLocation = parseItemsByLocation(
    hintFileJson = this,
    enabledLocations = enabledLocations,
    trackableItems = trackableItems
  )
  return when (val rawHintsType = hintsType) {
    "Disabled" -> {
      parseDisabledHints()
    }

    "JSmartee" -> {
      parseJSmarteeHints()
    }

    "Shananas" -> {
      checkNotNull(itemsByLocation) { "Shananas hints expect itemsByLocation to be present" }
      parseShananasHints(enabledLocations, itemsByLocation)
    }

    "Points" -> {
      checkNotNull(itemsByLocation) { "Points hints expect itemsByLocation to be present" }
      parsePointsHints(enabledLocations, trackableItems, itemsByLocation)
    }

    "Path" -> {
      checkNotNull(itemsByLocation) { "Path hints expect itemsByLocation to be present" }
      parsePathHints(itemsByLocation)
    }

    "Spoiler" -> {
      checkNotNull(itemsByLocation) { "Spoiler hints expect itemsByLocation to be present" }
      parseSpoilerHints(itemsByLocation)
    }

    else -> {
      error("Unknown hintsType $rawHintsType")
    }
  }
}

/**
 * Extracts the relevant remaining hint file content into a [DisabledHintSystem].
 */
private fun HintFileJson.parseDisabledHints(): DisabledHintSystem {
  val reportData = reports.orEmpty()
  val hints = reportData.parseReportData(HintFileJson.ReportData::toDisabledHint)
  return DisabledHintSystem(hints = hints)
}

private fun HintFileJson.ReportData.toDisabledHint(number: Int): DisabledHint {
  return DisabledHint(hintOrReportNumber = number, journalText = journalText)
}

/**
 * Extracts the relevant remaining hint file content into a [JSmarteeHintSystem].
 */
private fun HintFileJson.parseJSmarteeHints(): JSmarteeHintSystem {
  val reportData = checkNotNull(reports) { "JSmartee hints expect report information to be supplied" }
  val hints = reportData.parseReportData(HintFileJson.ReportData::toJSmarteeHint)

  for (hint in hints) {
    log { "Report ${hint.hintOrReportNumber} in ${hint.reportLocation} hints ${hint.hintedLocation}" }
  }

  return JSmarteeHintSystem(hints = hints, progressionSettings = parseJSmarteeProgressionSettings())
}

private fun HintFileJson.ReportData.toJSmarteeHint(number: Int): JSmarteeHint {
  val hintedLocation = checkNotNull(resolveItemLocation(rawHintedLocation)) {
    "Could not resolve location $rawHintedLocation for report $number"
  }
  val count = checkNotNull(count) { "Report $number is missing an important check count" }
  return JSmarteeHint(
    hintOrReportNumber = number,
    // Note: the report location is not required when using progression mode
    reportLocation = resolveItemLocation(reportLocation),
    hintedLocation = hintedLocation,
    importantCheckCount = count,
    journalText = journalText,
  )
}

private fun HintFileJson.parseJSmarteeProgressionSettings(): JSmarteeHintSystem.ProgressionSettings? {
  val basicSettings = parseBasicProgressionSettings() ?: return null
  return JSmarteeHintSystem.ProgressionSettings(basicSettings)
}

private fun HintFileJson.parseShananasHints(
  enabledLocations: Set<Location>,
  itemsByLocation: Map<Location, List<ItemPrototype>>,
): ShananasHintSystem {
  // Report information is optional in Shananas hints
  val hints = reports.orEmpty().parseReportData(HintFileJson.ReportData::toShananasHint)
  return ShananasHintSystem(
    hints = hints,
    allItemsByLocation = itemsByLocation,
    progressionSettings = parseShananasProgressionSettings(enabledLocations),
  )
}

private fun HintFileJson.ReportData.toShananasHint(number: Int): ShananasHint {
  return ShananasHint(hintOrReportNumber = number, journalText = journalText)
}

private fun HintFileJson.parseShananasProgressionSettings(
  enabledLocations: Set<Location>,
): ShananasHintSystem.ProgressionSettings? {
  val basicSettings = parseBasicProgressionSettings() ?: return null
  val worldOrder = checkNotNull(worldOrder) { "Shananas progression mode requires a world order in the hint file" }
  return ShananasHintSystem.ProgressionSettings(
    basicSettings = basicSettings,
    locationRevealOrder = resolveProgressionLocationRevealOrder(enabledLocations, worldOrder)
  )
}

private fun HintFileJson.parsePointsHints(
  enabledLocations: Set<Location>,
  trackableItems: Set<ItemPrototype>,
  itemsByLocation: Map<Location, List<ItemPrototype>>,
): PointsHintSystem {
  val pointValuesByPrototype: Map<ItemPrototype, Int> = buildMap {
    for ((rawItemType, pointValue) in checkValue) {
      when (rawItemType) {
        "proof" -> {
          Proof.entries.forEach { put(it, pointValue) }
          put(PromiseCharm, pointValue)
        }

        "form" -> {
          DriveForm.entries.forEach { put(it, pointValue) }
        }

        "magic" -> {
          Magic.entries.forEach { put(it, pointValue) }
        }

        "summon" -> {
          SummonCharm.entries.forEach { put(it, pointValue) }
        }

        "ability" -> {
          ImportantAbility.entries.forEach { put(it, pointValue) }
        }

        "keyblade" -> {
          ChestUnlockKeyblade.entries.forEach { put(it, pointValue) }
        }

        "page" -> {
          put(TornPage, pointValue)
        }

        "report" -> {
          AnsemReport.entries.forEach { put(it, pointValue) }
        }

        "visit" -> {
          VisitUnlock.entries.forEach { put(it, pointValue) }
        }

        "other" -> {
          MunnyPouch.entries.forEach { put(it, pointValue) }
          put(HadesCupTrophy, pointValue)
          put(OlympusStone, pointValue)
          put(UnknownDisk, pointValue)
        }
      }
    }
  }

  val hints = reports.orEmpty().parseReportData { number -> toPointsHint(number, trackableItems) }
  return PointsHintSystem(
    hints = hints,
    allItemsByLocation = itemsByLocation,
    pointValuesByPrototype = pointValuesByPrototype,
    progressionSettings = this.parsePointsProgressionSettings(enabledLocations),
  )
}

private fun HintFileJson.ReportData.toPointsHint(number: Int, trackableItems: Set<ItemPrototype>): PointsHint {
  val hintedLocation = checkNotNull(resolveItemLocation(rawHintedLocation)) {
    "Could not resolve location $rawHintedLocation for report $number"
  }
  val revealedItemId = checkNotNull(revealedItemId) { "Report $number is missing an ID for the revealed item" }
  val revealedItem = checkNotNull(trackableItems.firstOrNull { it.checkGameIds(target = GameId(revealedItemId)) }) {
    "Could not find the revealed item $revealedItemId for report $number"
  }
  return PointsHint(
    hintOrReportNumber = number,
    hintedLocation = hintedLocation,
    revealedItem = revealedItem,
    journalText = journalText,
  )
}

private fun HintFileJson.parsePointsProgressionSettings(
  enabledLocations: Set<Location>,
): PointsHintSystem.ProgressionSettings? {
  val basicSettings = parseBasicProgressionSettings() ?: return null
  val worldOrder = checkNotNull(worldOrder) { "Points progression mode requires a world order in the hint file" }
  return PointsHintSystem.ProgressionSettings(
    basicSettings = basicSettings,
    locationRevealOrder = resolveProgressionLocationRevealOrder(enabledLocations, worldOrder)
  )
}

private fun HintFileJson.parsePathHints(itemsByLocation: Map<Location, List<ItemPrototype>>): PathHintSystem {
  val reportData = checkNotNull(reports) { "Path hints expect report information to be supplied" }
  val hints = reportData.parseReportData(HintFileJson.ReportData::toPathHint)
  return PathHintSystem(
    allItemsByLocation = itemsByLocation,
    hints = hints,
    progressionSettings = parsePathProgressionSettings(),
  )
}

private fun HintFileJson.ReportData.toPathHint(number: Int): PathHint {
  val hintedLocation = checkNotNull(resolveItemLocation(rawHintedLocation)) {
    "Could not resolve location $rawHintedLocation for report $number"
  }
  val paths = checkNotNull(proofPath) { "Report $number is missing the proof path" }
  val pathToProofs = if (paths == listOf("none")) {
    emptySet()
  } else {
    paths.mapTo(mutableSetOf()) { rawPath ->
      when (rawPath) {
        "Connection" -> Proof.ProofOfConnection
        "Nonexistence" -> Proof.ProofOfNonexistence
        "Peace" -> Proof.ProofOfPeace
        else -> throw IllegalStateException("Unexpected proof path name $rawPath in report $number")
      }
    }
  }

  return PathHint(
    hintOrReportNumber = number,
    hintedLocation = hintedLocation,
    pathToProofs = pathToProofs,
    journalText = journalText,
  )
}

private fun HintFileJson.parsePathProgressionSettings(): PathHintSystem.ProgressionSettings? {
  val basicSettings = parseBasicProgressionSettings() ?: return null
  return PathHintSystem.ProgressionSettings(basicSettings)
}

private fun HintFileJson.parseSpoilerHints(itemsByLocation: Map<Location, List<ItemPrototype>>): SpoilerHintSystem {
  val revealSettings = checkNotNull(revealSettings) { "Spoiler hints expect to have reveal settings filled in" }
  val revealedItemTypes = mutableSetOf<ItemPrototype>()
  for (revealSetting in revealSettings) {
    when (revealSetting) {
      "proof" -> {
        Proof.entries.forEach { revealedItemTypes.add(it) }
        revealedItemTypes.add(PromiseCharm)
      }

      "form" -> {
        DriveForm.entries.forEach { revealedItemTypes.add(it) }
      }

      "magic" -> {
        Magic.entries.forEach { revealedItemTypes.add(it) }
      }

      "summon" -> {
        SummonCharm.entries.forEach { revealedItemTypes.add(it) }
      }

      "ability" -> {
        ImportantAbility.entries.forEach { revealedItemTypes.add(it) }
      }

      "keyblade" -> {
        ChestUnlockKeyblade.entries.forEach { revealedItemTypes.add(it) }
      }

      "page" -> {
        revealedItemTypes.add(TornPage)
      }

      "report" -> {
        AnsemReport.entries.forEach { revealedItemTypes.add(it) }
      }

      "visit" -> {
        VisitUnlock.entries.forEach { revealedItemTypes.add(it) }
      }

      "other" -> {
        MunnyPouch.entries.forEach { revealedItemTypes.add(it) }
        revealedItemTypes.add(HadesCupTrophy)
        revealedItemTypes.add(OlympusStone)
        revealedItemTypes.add(UnknownDisk)
      }
    }
  }

  val reportData = checkNotNull(reports) { "Spoiler hints expect report information to be supplied" }
  val hints = reportData.parseReportData(HintFileJson.ReportData::toSpoilerHint)

  return SpoilerHintSystem(
    hints = hints,
    revealedItemTypes = revealedItemTypes,
    revealMode = if ("reportmode" in revealSettings) {
      SpoilerHintSystem.RevealMode.Gradual
    } else {
      SpoilerHintSystem.RevealMode.Always
    },
    revealWorldCompletion = "complete" in revealSettings,
    allItemsByLocation = itemsByLocation,
    progressionSettings = parseSpoilerProgressionSettings(),
  )
}

private fun HintFileJson.ReportData.toSpoilerHint(number: Int): SpoilerHint {
  val hintedLocation = checkNotNull(resolveItemLocation(rawHintedLocation)) {
    "Could not resolve location $rawHintedLocation for report $number"
  }
  return SpoilerHint(
    hintOrReportNumber = number,
    hintedLocation = hintedLocation,
    journalText = journalText,
  )
}

private fun HintFileJson.parseSpoilerProgressionSettings(): SpoilerHintSystem.ProgressionSettings? {
  val basicSettings = parseBasicProgressionSettings() ?: return null
  return SpoilerHintSystem.ProgressionSettings(basicSettings)
}

/**
 * Resolves in item [rawLocation] into a [Location], returning null if unable to do so.
 */
private fun resolveItemLocation(rawLocation: String?): Location? {
  return when (rawLocation) {
    "Level" -> Location.SoraLevels
    "Land of Dragons" -> Location.LandOfDragons
    "Beast's Castle" -> Location.BeastsCastle
    "Hollow Bastion" -> Location.HollowBastion
    "Twilight Town" -> Location.TwilightTown
    "The World That Never Was" -> Location.WorldThatNeverWas
    "Space Paranoids" -> Location.SpaceParanoids
    "Port Royal" -> Location.PortRoyal
    "Olympus Coliseum" -> Location.OlympusColiseum
    "Agrabah" -> Location.Agrabah
    "Halloween Town" -> Location.HalloweenTown
    "Pride Lands" -> Location.PrideLands
    "Disney Castle / Timeless River" -> Location.DisneyCastle
    "Hundred Acre Wood" -> Location.HundredAcreWood
    "Simulated Twilight Town" -> Location.SimulatedTwilightTown
    "Form Levels" -> Location.DriveForms
    "Atlantica" -> Location.Atlantica
    "Creations" -> Location.Creations
    "Critical Bonuses" -> Location.GardenOfAssemblage
    "Garden of Assemblage" -> Location.GardenOfAssemblage
    else -> null
  }
}

private fun HintFileJson.parseFinalDoorRequirement(): FinalDoorRequirement {
  val emblemSettings = emblemSettings
  return if ("objectives" in settings) {
    val objectivesNeeded = checkNotNull(numberOfObjectivesNeeded) {
      "Objective mode is missing the num_objectives_needed property"
    }
    FinalDoorRequirement.Objectives(
      objectiveList = parseObjectives(),
      objectivesNeeded = objectivesNeeded,
    )
  } else if (emblemSettings != null) {
    FinalDoorRequirement.LuckyEmblems(
      emblemsNeeded = emblemSettings.numberOfEmblemsNeeded,
      emblemsAvailable = emblemSettings.maxEmblemsAvailable,
    )
  } else {
    FinalDoorRequirement.ThreeProofs
  }
}

private fun HintFileJson.parseLevelChecks(trackableItems: Set<ItemPrototype>): LevelChecks {
  val soraLevels = LevelChecks.soraLevelsBuilder()
  val weaponPairings = listOf(
    DreamWeapon.Sword to levelData.level.sword,
    DreamWeapon.Staff to levelData.level.staff,
    DreamWeapon.Shield to levelData.level.shield,
  )
  for ((dreamWeapon, rawData) in weaponPairings) {
    val builder = soraLevels.getValue(dreamWeapon)
    for ((levelString, itemId) in rawData) {
      val levelNumber = checkNotNull(levelString.toIntOrNull()) { "Invalid level string $levelString for level checks" }
      val levelIndex = levelNumber - 1
      val item = trackableItems.firstOrNull { it.checkGameIds(target = GameId(itemId)) }
      if (item != null) {
        // If it's null maybe it's just something in the level data that's not trackable
        builder[levelIndex].add(item)
      }
    }
  }

  val formLevels = LevelChecks.formLevelsBuilder()
  val driveFormPairings = listOf(
    DriveForm.ValorFormDummy to levelData.valorLevel,
    DriveForm.WisdomForm to levelData.wisdomLevel,
    DriveForm.LimitForm to levelData.limitLevel,
    DriveForm.MasterForm to levelData.masterLevel,
    DriveForm.FinalFormDummy to levelData.finalLevel,
  )
  for ((driveForm, rawData) in driveFormPairings) {
    val builder = formLevels.getValue(driveForm)
    for ((levelString, itemId) in rawData) {
      val levelNumber = checkNotNull(levelString.toIntOrNull()) { "Invalid level string $levelString for level checks" }
      val levelIndex = levelNumber - 1
      val item = trackableItems.firstOrNull { it.checkGameIds(target = GameId(itemId)) }
      if (item != null) {
        // If it's null maybe it's just something in the level data that's not trackable
        builder[levelIndex].add(item)
      }
    }
  }

  return LevelChecks(soraLevels = soraLevels, formLevels = formLevels)
}

private inline fun <T : Hint> Map<String, HintFileJson.ReportData>.parseReportData(
  parseSingle: HintFileJson.ReportData.(reportNumber: Int) -> T,
): List<T> {
  return map { (rawReportNumber, rawReportData) ->
    val reportNumber = checkNotNull(rawReportNumber.toIntOrNull()) {
      "Found a non-numeric report number $rawReportNumber"
    }
    rawReportData.parseSingle(reportNumber)
  }.sortedBy { it.hintOrReportNumber }
}

private fun HintFileJson.parseBasicProgressionSettings(): BasicProgressionSettings? {
  val progression = "ProgressionHints" in settings
  if (!progression) {
    return null
  }

  val rawSettings = checkNotNull(progressionSettings) {
    "Progression hints mode requires ProgressionSettings in the hint file"
  }
  return rawSettings.resolveBasicProgressionSettings()
}

private fun HintFileJson.ProgressionSettings.resolveBasicProgressionSettings(): BasicProgressionSettings {
  val earnedPointsByCheckpoint: Map<ProgressCheckpoint, Int> = buildMap {
    putAll(SoraLevelProgress.pointsByCheckpoint(levels))
    putAll(DriveFormProgress.pointsByCheckpoint(driveForms))
    putAll(SimulatedTwilightTownProgress.pointsByCheckpoint(simulatedTwilightTown))
    putAll(TwilightTownProgress.pointsByCheckpoint(twilightTown))
    putAll(HollowBastionProgress.pointsByCheckpoint(hollowBastion))
    putAll(BeastsCastleProgress.pointsByCheckpoint(beastsCastle))
    putAll(OlympusColiseumProgress.pointsByCheckpoint(olympusColiseum))
    putAll(AgrabahProgress.pointsByCheckpoint(agrabah))
    putAll(LandOfDragonsProgress.pointsByCheckpoint(landOfDragons))
    putAll(HundredAcreWoodProgress.pointsByCheckpoint(hundredAcreWood))
    putAll(PrideLandsProgress.pointsByCheckpoint(prideLands))
    putAll(AtlanticaProgress.pointsByCheckpoint(atlantica))
    putAll(DisneyCastleProgress.pointsByCheckpoint(disneyCastle))
    putAll(HalloweenTownProgress.pointsByCheckpoint(halloweenTown))
    putAll(PortRoyalProgress.pointsByCheckpoint(portRoyal))
    putAll(SpaceParanoidsProgress.pointsByCheckpoint(spaceParanoids))
    putAll(WorldThatNeverWasProgress.pointsByCheckpoint(worldThatNeverWas))
    putAll(CavernOfRemembranceProgress.pointsByCheckpoint(cavernOfRemembrance))
  }

  return BasicProgressionSettings(
    pointsToAwardByCheckpoint = earnedPointsByCheckpoint,
    hintCosts = hintCosts,
    worldCompleteBonus = worldCompleteBonus.firstOrNull() ?: 0,
    reportBonus = reportBonus.firstOrNull() ?: 0,
    // Generator places a 1 here for true, 0 for false
    revealAllWhenDone = finalXemnasReveal.firstOrNull()?.let { it != 0 } ?: false,
  )
}

private fun HintFileJson.parseObjectives(): List<Objective> {
  val objectiveLocations = checkNotNull(objectiveLocations) {
    "Objective mode is missing the objective_locations property"
  }

  val locationCategories = CheckLocationCategory.entries.associateBy { it.hintFileJsonName }
  val objectivesByCheckLocation = Objective.entries.associateBy { it.checkLocation }

  val result = objectiveLocations.map { rawLocation ->
    val category = checkNotNull(locationCategories[rawLocation.category]) {
      "Unrecognized location category ${rawLocation.category} in objectives list"
    }
    val checkLocation = CheckLocation(category, rawLocation.locationId)
    checkNotNull(objectivesByCheckLocation[checkLocation]) { "Objective not found for $checkLocation" }
  }
  return result
}

// NOTE: The DA tracker doesn't take into account world_order?
// It seems to generate it itself by seeding a random number generator with a value based on the rando seed's hash
private fun resolveProgressionLocationRevealOrder(
  enabledLocations: Set<Location>,
  worldOrder: List<String>,
): List<Location> {
  // Filter revealable locations to everything enabled, but also take away GoA because it doesn't get a hint
  val revealableLocations = enabledLocations - Location.GardenOfAssemblage
  return worldOrder.mapNotNull { rawLocation ->
    val resolvedLocation = checkNotNull(resolveItemLocation(rawLocation)) {
      "Could not resolve location for world order $rawLocation"
    }
    resolvedLocation.takeIf { it in revealableLocations }
  }
}
