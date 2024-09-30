package com.kh2rando.tracker.model.hints

import androidx.compose.runtime.Immutable
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.hint_system_spoiler
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.ItemPrototype
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

/**
 * Hint for the [SpoilerHintSystem].
 */
@Immutable
@Serializable
data class SpoilerHint(
  override val hintOrReportNumber: Int,
  /**
   * The location being hinted by this hint.
   */
  val hintedLocation: Location,
  override val journalText: String?,
) : Hint

/**
 * Hint system that reveals all items in a location.
 */
@Serializable
@SerialName("Spoiler")
class SpoilerHintSystem(
  val hints: List<SpoilerHint>,
  val revealedItemTypes: Set<ItemPrototype>,
  val revealMode: RevealMode,
  val revealWorldCompletion: Boolean,
  val allItemsByLocation: Map<Location, List<ItemPrototype>>,
  val progressionSettings: ProgressionSettings?,
) : HintSystem {

  override val displayName: StringResource
    get() = Res.string.hint_system_spoiler

  override val basicProgressionSettings: BasicProgressionSettings?
    get() = progressionSettings?.basicSettings

  val hintInfoByHintOrReportNumber: Map<Int, HintInfo.GeneralRevealed> = hints.associate { hint ->
    val hintOrReportNumber = hint.hintOrReportNumber
    val info = HintInfo.GeneralRevealed(
      hintOrReportNumber = hintOrReportNumber,
      location = hint.hintedLocation,
      journalText = hint.journalText,
    )
    hintOrReportNumber to info
  }

  val hintsByHintedLocation: Map<Location, HintInfo.GeneralRevealed> =
    hintInfoByHintOrReportNumber.values.associateBy { it.location }

  override fun isValidReportLocation(location: Location, report: AnsemReport): Boolean {
    return report in allItemsByLocation[location].orEmpty()
  }

  override fun hintInfoForAcquiredReport(report: AnsemReport): HintInfo? {
    return hintInfoForHintOrReport(hintOrReportNumber = report.reportNumber)
  }

  fun hintInfoForHintOrReport(hintOrReportNumber: Int): HintInfo? {
    val fullInfo = hintInfoByHintOrReportNumber[hintOrReportNumber] ?: return null
    return when (revealMode) {
      RevealMode.Always -> {
        HintInfo.JournalTextOnly(hintOrReportNumber, fullInfo.journalText)
      }

      RevealMode.Gradual -> {
        if (progressionSettings == null) {
          fullInfo
        } else {
          HintInfo.JournalTextOnly(hintOrReportNumber, fullInfo.journalText)
        }
      }
    }
  }

  /**
   * When to reveal the items in a location.
   */
  enum class RevealMode {

    /**
     * Items are revealed immediately.
     */
    Always,

    /**
     * Items are revealed via Ansem report acquisition or earning progression points, depending on whether progression
     * mode is enabled.
     */
    Gradual,

  }

  /**
   * Progression settings for [SpoilerHintSystem].
   */
  @Serializable
  class ProgressionSettings(
    /**
     * Basic progression settings.
     */
    val basicSettings: BasicProgressionSettings,
  )

}
