package com.kh2rando.tracker.model.hints

import androidx.compose.runtime.Immutable
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.hint_system_path
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.Proof
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

/**
 * A hint for the [PathHintSystem].
 */
@Immutable
@Serializable
data class PathHint(
  override val hintOrReportNumber: Int,
  /**
   * The location being hinted by this hint.
   */
  val hintedLocation: Location,
  /**
   * The proof(s) that this hint is relevant to.
   */
  val pathToProofs: Set<Proof>,
  override val journalText: String?,
) : Hint

/**
 * Hint system that indicates a path to one or more proofs for each location.
 */
@Serializable
@SerialName("Path")
class PathHintSystem(
  val hints: List<PathHint>,
  val allItemsByLocation: Map<Location, List<ItemPrototype>>,
  val progressionSettings: ProgressionSettings?,
) : HintSystem {

  override val displayName: StringResource
    get() = Res.string.hint_system_path

  override val basicProgressionSettings: BasicProgressionSettings?
    get() = progressionSettings?.basicSettings

  val hintInfoByHintOrReportNumber: Map<Int, HintInfo.PathToProofs> = hints.associate { hint ->
    val hintOrReportNumber = hint.hintOrReportNumber
    val info = HintInfo.PathToProofs(
      hintOrReportNumber = hintOrReportNumber,
      location = hint.hintedLocation,
      proofs = hint.pathToProofs,
      journalText = hint.journalText,
    )
    hintOrReportNumber to info
  }

  val hintsByHintedLocation: Map<Location, HintInfo.PathToProofs> =
    hintInfoByHintOrReportNumber.values.associateBy { it.location }

  override fun isValidReportLocation(location: Location, report: AnsemReport): Boolean {
    return report in allItemsByLocation[location].orEmpty()
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
   * Progression settings for [PathHintSystem].
   */
  @Serializable
  class ProgressionSettings(
    /**
     * Basic progression settings.
     */
    val basicSettings: BasicProgressionSettings,
  )

}
