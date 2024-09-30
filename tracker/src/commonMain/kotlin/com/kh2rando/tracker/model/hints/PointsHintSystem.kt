package com.kh2rando.tracker.model.hints

import androidx.compose.runtime.Immutable
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.hint_system_points
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.ItemPrototype
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

/**
 * A hint for the [PointsHintSystem]. Indicates the location of a single item.
 */
@Immutable
@Serializable
data class PointsHint(
  override val hintOrReportNumber: Int,
  /**
   * The location containing [revealedItem].
   */
  val hintedLocation: Location,
  /**
   * The item revealed by this hint.
   */
  val revealedItem: ItemPrototype,
  override val journalText: String?,
) : Hint

/**
 * Hint system that displays a remaining point count for each revealed location.
 */
@Serializable
@SerialName("Points")
class PointsHintSystem(
  val hints: List<PointsHint>,
  val allItemsByLocation: Map<Location, List<ItemPrototype>>,
  val pointValuesByPrototype: Map<ItemPrototype, Int>,
  val progressionSettings: ProgressionSettings?,
) : HintSystem {

  override val displayName: StringResource
    get() = Res.string.hint_system_points

  override val basicProgressionSettings: BasicProgressionSettings?
    get() = progressionSettings?.basicSettings

  val totalPointsByLocation = allItemsByLocation.entries.associate { (location, items) ->
    location to items.sumOf { pointValuesByPrototype[it] ?: 0 }
  }

  private val hintInfoByHintOrReportNumber: Map<Int, HintInfo.ItemLocation> = hints.associate { hint ->
    val hintOrReportNumber = hint.hintOrReportNumber
    val info = HintInfo.ItemLocation(
      hintOrReportNumber = hintOrReportNumber,
      location = hint.hintedLocation,
      item = hint.revealedItem,
      journalText = hint.journalText,
    )
    hintOrReportNumber to info
  }

  val hintsByHintedLocation: Map<Location, List<HintInfo.ItemLocation>> =
    hintInfoByHintOrReportNumber.values.groupBy { it.location }

  override fun isValidReportLocation(location: Location, report: AnsemReport): Boolean {
    return report in allItemsByLocation[location].orEmpty()
  }

  override fun hintInfoForAcquiredReport(report: AnsemReport): HintInfo? {
    return hintInfoForReport(reportNumber = report.reportNumber)
  }

  fun hintInfoForReport(reportNumber: Int): HintInfo.ItemLocation? {
    return hintInfoByHintOrReportNumber[reportNumber]
  }

  /**
   * Progression settings for [PointsHintSystem].
   */
  @Serializable
  class ProgressionSettings(
    /**
     * Basic progression settings.
     */
    val basicSettings: BasicProgressionSettings,
    /**
     * The order in which locations should be revealed.
     */
    val locationRevealOrder: List<Location>,
  )

}
