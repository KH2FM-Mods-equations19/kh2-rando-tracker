package com.kh2rando.tracker.model.hints

import androidx.compose.runtime.Immutable
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.hint_system_shananas
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.ItemPrototype
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

/**
 * A hint for the [ShananasHintSystem].
 */
@Immutable
@Serializable
data class ShananasHint(
  override val hintOrReportNumber: Int,
  override val journalText: String?,
) : Hint

/**
 * Hint system that only indicates whether or not all trackable items in a location have been found.
 */
@Serializable
@SerialName("Shananas")
class ShananasHintSystem(
  val hints: List<ShananasHint>,
  val allItemsByLocation: Map<Location, List<ItemPrototype>>,
  val progressionSettings: ProgressionSettings?,
) : HintSystem {

  private val hintInfoByHintOrReportNumber: Map<Int, HintInfo.JournalTextOnly> = hints.associate { hint ->
    val hintOrReportNumber = hint.hintOrReportNumber
    val info = HintInfo.JournalTextOnly(
      hintOrReportNumber = hintOrReportNumber,
      journalText = hint.journalText,
    )
    hintOrReportNumber to info
  }

  override val displayName: StringResource
    get() = Res.string.hint_system_shananas

  override val basicProgressionSettings: BasicProgressionSettings?
    get() = progressionSettings?.basicSettings

  override fun isValidReportLocation(location: Location, report: AnsemReport): Boolean {
    return report in allItemsByLocation[location].orEmpty()
  }

  override fun hintInfoForAcquiredReport(report: AnsemReport): HintInfo? {
    return hintInfoByHintOrReportNumber[report.reportNumber]
  }

  /**
   * Progression settings for [ShananasHintSystem].
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
