package com.kh2rando.tracker.model.hints

import androidx.compose.runtime.Immutable
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.hint_system_disabled
import com.kh2rando.tracker.model.item.AnsemReport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

/**
 * A hint for the [DisabledHintSystem].
 */
@Immutable
@Serializable
data class DisabledHint(override val hintOrReportNumber: Int, override val journalText: String?) : Hint

/**
 * No hint system is being used.
 */
@Serializable
@SerialName("Disabled")
data class DisabledHintSystem(val hints: List<DisabledHint>) : HintSystem {

  private val hintInfos: Map<Int, HintInfo.JournalTextOnly> = hints.associate { hint ->
    val hintOrReportNumber = hint.hintOrReportNumber
    val info = HintInfo.JournalTextOnly(
      hintOrReportNumber = hintOrReportNumber,
      journalText = hint.journalText,
    )
    hintOrReportNumber to info
  }

  override val displayName: StringResource
    get() = Res.string.hint_system_disabled

  override val basicProgressionSettings: BasicProgressionSettings?
    get() = null

  override fun hintInfoForAcquiredReport(report: AnsemReport): HintInfo? {
    return hintInfos[report.reportNumber]
  }

}
