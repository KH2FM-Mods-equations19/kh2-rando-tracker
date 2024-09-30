package com.kh2rando.tracker.ui

import androidx.compose.runtime.Immutable
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.prog_dc_marluxia_data_lingering_will
import com.kh2rando.tracker.generated.resources.prog_dc_marluxia_lingering_will
import com.kh2rando.tracker.generated.resources.prog_hb_sephi_demyx
import com.kh2rando.tracker.generated.resources.progression_dc_marluxia_and_lingering_will
import com.kh2rando.tracker.generated.resources.progression_dc_marluxiadata_and_lingering_will
import com.kh2rando.tracker.generated.resources.progression_hb_demyx_and_sephiroth
import com.kh2rando.tracker.model.GameId
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.gamestate.BaseGameStateUpdateApi
import com.kh2rando.tracker.model.hints.LocationAuxiliaryHintInfo
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.UniqueItem
import com.kh2rando.tracker.model.item.removeAcquired
import com.kh2rando.tracker.model.progress.DisneyCastleProgress
import com.kh2rando.tracker.model.progress.HollowBastionProgress
import com.kh2rando.tracker.model.progress.ProgressCheckpoint
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import kotlin.math.abs

data class LocationUiState(
  private val gameStateUpdater: BaseGameStateUpdateApi,
  val location: Location,
  val isUserSelectedLocation: Boolean = false,
  val isAutoDetectedLocation: Boolean = false,
  val acquiredItems: ImmutableSet<UniqueItem> = persistentSetOf(),
  val revealedItems: ImmutableList<ItemPrototype> = persistentListOf(),
  val lockedVisitCount: Int = 0,
  val counterState: LocationCounterState = LocationCounterState.None,
  val completedProgressCheckpoints: ImmutableSet<ProgressCheckpoint> = persistentSetOf(),
  val userMarkCount: Int = 0,
  val auxiliaryHintInfo: LocationAuxiliaryHintInfo = LocationAuxiliaryHintInfo.NotApplicableToHintSystem,
) {

  val revealedButNotAcquiredPrototypes: ImmutableList<ItemPrototype> = revealedItems.removeAcquired(acquiredItems)

  val progressIndicator: LocationProgressIndicator? = location.resolveProgressToDisplay(completedProgressCheckpoints)

  val userMarkIcon = run {
    val userMarkIcons = UserMarkIcon.entries
    userMarkIcons.getOrElse(abs(userMarkCount) % userMarkIcons.size) { UserMarkIcon.None }
  }

  fun tryAcquireItemManuallyByGameId(gameId: GameId) {
    gameStateUpdater.tryAcquireItemManuallyByGameId(gameId = gameId, location = location)
  }

  fun rejectItemManually(item: UniqueItem) {
    gameStateUpdater.rejectItemManually(item = item, location = location)
  }

  fun toggleUserSelection() {
    gameStateUpdater.manuallyToggleLocation(location)
  }

  fun adjustUserMark(delta: Int) {
    gameStateUpdater.adjustUserMarkForLocation(location = location, userMarkDelta = delta)
  }

}

@Immutable
data class LocationProgressIndicator(
  val icon: HasCustomizableIcon,
  val displayString: StringResource,
)

fun ProgressCheckpoint.progressIndicator(): LocationProgressIndicator {
  return LocationProgressIndicator(icon = this, displayString = displayString)
}

/**
 * "Progress" that can be displayed on the tracker UI but isn't represented by specific in-game progress.
 *
 * Often this is a combination of independent bosses in a location that can be beaten in any order.
 */
enum class SyntheticProgress(
  val displayString: StringResource,
  override val defaultIcon: DrawableResource,
  override val customIconIdentifier: String,
) : HasCustomizableIcon {

  SephiDemyx(
    displayString = Res.string.prog_hb_sephi_demyx,
    defaultIcon = Res.drawable.progression_hb_demyx_and_sephiroth,
    customIconIdentifier = "demyx_and_sephiroth",
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression", "hollow_bastion")
  },

  Marluxia_LingeringWill(
    displayString = Res.string.prog_dc_marluxia_lingering_will,
    defaultIcon = Res.drawable.progression_dc_marluxia_and_lingering_will,
    customIconIdentifier = "marluxia_and_lingering_will",
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression", "disney_castle")
  },

  MarluxiaData_LingeringWill(
    displayString = Res.string.prog_dc_marluxia_data_lingering_will,
    defaultIcon = Res.drawable.progression_dc_marluxiadata_and_lingering_will,
    customIconIdentifier = "marluxiadata_and_lingering_will",
  ) {
    override val customIconPath: List<String>
      get() = listOf("Progression", "disney_castle")
  };

  fun progressIndicator(): LocationProgressIndicator {
    return LocationProgressIndicator(icon = this, displayString = displayString)
  }

}

/**
 * Resolves the progress which should be displayed in the tracker UI for this location based on the checkpoints that
 * have been completed.
 */
fun Location.resolveProgressToDisplay(
  checkpoints: Set<ProgressCheckpoint>,
): LocationProgressIndicator? {
  val syntheticProgress = when (this) {
    Location.HollowBastion -> {
      val sephiroth = HollowBastionProgress.Sephiroth in checkpoints
      val dataDemyx = HollowBastionProgress.DataDemyx in checkpoints
      if (sephiroth && dataDemyx) {
        SyntheticProgress.SephiDemyx
      } else {
        null
      }
    }

    Location.DisneyCastle -> {
      val absentMarluxia = DisneyCastleProgress.Marluxia in checkpoints
      val dataMarluxia = DisneyCastleProgress.MarluxiaData in checkpoints
      val lingeringWill = DisneyCastleProgress.LingeringWill in checkpoints
      if (dataMarluxia && lingeringWill) {
        SyntheticProgress.MarluxiaData_LingeringWill
      } else if (absentMarluxia && lingeringWill) {
        SyntheticProgress.Marluxia_LingeringWill
      } else {
        null
      }
    }

    else -> {
      null
    }
  }

  return syntheticProgress?.progressIndicator() ?: run {
    checkpoints.filter { it.showInMainLocationProgress }.maxByOrNull { it.index }?.progressIndicator()
  }
}
