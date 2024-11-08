package com.kh2rando.tracker.model.hints

import androidx.compose.runtime.Immutable
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.Proof
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Information that can be revealed to the player based on game state or other conditions.
 */
sealed interface Hint {

  /**
   * The number of this hint (either an Ansem Report number if report mode or just a 1-based index if progression mode).
   */
  val hintOrReportNumber: Int

  /**
   * Text that is displayed in Jiminy's Journal for the Ansem Report representing this hint, if any.
   */
  val journalText: String?

}

/**
 * Hint information that knows about a specific [Location].
 */
interface LocationAwareHintInfo {

  val location: Location

}

/**
 * Information that can be displayed on the tracker for a specific [Hint].
 */
@Serializable
sealed interface HintInfo {

  /**
   * The Ansem Report number of this hint if it's backed by an Ansem Report, or the index of this hint in the hint order
   * if backed by a progression hint.
   */
  val hintOrReportNumber: Int

  /**
   * The additional text displayed in Jiminy's Journal for this hint.
   */
  val journalText: String?

  /**
   * Indicates nothing outside of potentially containing [journalText].
   */
  @Immutable
  @Serializable
  @SerialName("JournalTextOnly")
  data class JournalTextOnly(
    override val hintOrReportNumber: Int,
    override val journalText: String? = null,
  ) : HintInfo

  /**
   * Indicates that a location has been revealed, with no other information.
   */
  @Immutable
  @Serializable
  @SerialName("GeneralRevealed")
  data class GeneralRevealed(
    override val hintOrReportNumber: Int,
    override val location: Location,
    override val journalText: String? = null,
  ) : HintInfo, LocationAwareHintInfo

  /**
   * Indicates that a location has a certain number of trackable items ("important checks").
   */
  @Immutable
  @Serializable
  @SerialName("ImportantCheckCount")
  data class ImportantCheckCount(
    override val hintOrReportNumber: Int,
    override val location: Location,
    val count: Int,
    override val journalText: String? = null,
  ) : HintInfo, LocationAwareHintInfo

  /**
   * Indicates that a location has a certain number of points remaining to obtain, based on items that have not yet been
   * acquired.
   */
  @Immutable
  @Serializable
  @SerialName("PointsCount")
  data class PointsCount(
    override val hintOrReportNumber: Int,
    override val location: Location,
    val points: Int,
    override val journalText: String? = null,
  ) : HintInfo, LocationAwareHintInfo

  /**
   * Indicates the location where a specific item can be found.
   */
  @Immutable
  @Serializable
  @SerialName("ItemLocation")
  data class ItemLocation(
    override val hintOrReportNumber: Int,
    override val location: Location,
    val item: ItemPrototype,
    override val journalText: String? = null,
  ) : HintInfo, LocationAwareHintInfo

  /**
   * Indicates that a location is on the path to one or more proofs, or that the location is not on the path to any.
   */
  @Immutable
  @Serializable
  @SerialName("PathToProofs")
  data class PathToProofs(
    override val hintOrReportNumber: Int,
    override val location: Location,
    val proofs: Set<Proof>,
    override val journalText: String? = null,
  ) : HintInfo, LocationAwareHintInfo, LocationAuxiliaryHintInfo

}


sealed interface LocationAuxiliaryHintInfo {

  /**
   * Use this if the hint system itself does not support auxiliary hint info at all.
   */
  @Immutable
  data object NotApplicableToHintSystem : LocationAuxiliaryHintInfo

  /**
   * Use this if the hint system itself supports auxiliary hint info, but a particular location doesn't have one.
   */
  @Immutable
  data object Blank : LocationAuxiliaryHintInfo

  /**
   * A JSmartee "hinted hint".
   */
  @Immutable
  data object HintedHint : LocationAuxiliaryHintInfo

  /**
   * Indicates that this location's count has been adjusted by the revealed items.
   */
  @Immutable
  data object CountAdjustedByRevealedItems : LocationAuxiliaryHintInfo

}
