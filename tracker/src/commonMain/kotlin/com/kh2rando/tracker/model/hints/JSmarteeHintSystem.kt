package com.kh2rando.tracker.model.hints

import androidx.compose.runtime.Immutable
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.hint_system_jsmartee
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.item.AnsemReport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

/**
 * A hint for the [JSmarteeHintSystem].
 */
@Immutable
@Serializable
data class JSmarteeHint(
  override val hintOrReportNumber: Int,
  /**
   * If using report mode, the location where the Ansem Report was found.
   */
  val reportLocation: Location?,
  /**
   * The location being hinted by this hint.
   */
  val hintedLocation: Location?,
  /**
   * The count of important checks for the [hintedLocation].
   */
  val importantCheckCount: Int?,

  override val journalText: String?,
) : Hint

/**
 * Hint system that reveals the number of "important checks" in a location.
 */
@Serializable
@SerialName("JSmartee")
class JSmarteeHintSystem(
  val hints: List<JSmarteeHint>,
  val progressionSettings: ProgressionSettings?,
) : HintSystem {

  override val displayName: StringResource
    get() = Res.string.hint_system_jsmartee

  override val basicProgressionSettings: BasicProgressionSettings?
    get() = progressionSettings?.basicSettings

  val hintInfoByHintOrReportNumber: Map<Int, HintInfo> = hints.associate { hint ->
    val hintOrReportNumber = hint.hintOrReportNumber
    val hintedLocation = hint.hintedLocation
    val importantCheckCount = hint.importantCheckCount
    val info = if (hintedLocation == null || importantCheckCount == null) {
      HintInfo.JournalTextOnly(
        hintOrReportNumber = hintOrReportNumber,
        journalText = hint.journalText,
      )
    } else {
      HintInfo.ImportantCheckCount(
        hintOrReportNumber = hintOrReportNumber,
        location = hintedLocation,
        count = importantCheckCount,
        journalText = hint.journalText,
      )
    }
    hintOrReportNumber to info
  }

  val hintsByHintedLocation: Map<Location, HintInfo.ImportantCheckCount> =
    hintInfoByHintOrReportNumber
      .values
      .filterIsInstance<HintInfo.ImportantCheckCount>()
      .associateBy { it.location }
  val reportLocationsByReport: Map<AnsemReport, Location>?

  init {
    if (progressionSettings == null) {
      val locationsByReport = hints.associate { hint ->
        val hintOrReportNumber = hint.hintOrReportNumber
        val location = checkNotNull(hint.reportLocation) {
          "Ansem Report $hintOrReportNumber is missing its location which is required for JSmartee hints"
        }
        val report = AnsemReport.ofReportNumber(hintOrReportNumber)
        report to location
      }

      check(locationsByReport.keys == AnsemReport.entries.toSet()) {
        "JSmartee hints requires a hint to be provided for every Ansem Report when in report mode"
      }

      this.reportLocationsByReport = locationsByReport
    } else {
      reportLocationsByReport = null
    }
  }

  override fun isValidReportLocation(location: Location, report: AnsemReport): Boolean {
    val locationsByReport = reportLocationsByReport ?: return true
    return locationsByReport.getValue(report) == location
  }

  override fun hintInfoForAcquiredReport(report: AnsemReport): HintInfo? {
    return hintInfoForHintOrReport(hintOrReportNumber = report.reportNumber)
  }

  fun hintInfoForHintOrReport(hintOrReportNumber: Int): HintInfo? {
    val fullInfo = hintInfoByHintOrReportNumber[hintOrReportNumber] ?: return null
    return if (progressionSettings == null) {
      fullInfo
    } else {
      HintInfo.JournalTextOnly(hintOrReportNumber, fullInfo.journalText)
    }
  }

  /**
   * Progression settings for [JSmarteeHintSystem].
   */
  @Serializable
  class ProgressionSettings(
    /**
     * Basic progression settings.
     */
    val basicSettings: BasicProgressionSettings,
  )

}
