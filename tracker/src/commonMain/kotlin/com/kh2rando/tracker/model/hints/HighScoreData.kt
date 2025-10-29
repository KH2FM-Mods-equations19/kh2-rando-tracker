package com.kh2rando.tracker.model.hints

import androidx.compose.runtime.Immutable
import com.kh2rando.tracker.model.item.ItemPrototype
import kotlinx.serialization.Serializable

/**
 * Data utilized when "high score mode" is enabled.
 */
@Immutable
@Serializable
data class HighScoreData(
  val pointsByItem: Map<ItemPrototype, Int>,
  val pointsByGoal: Map<HighScoreGoal, Int>,
)

/**
 * Goals that can be met to obtain bonus points in high score mode.
 */
enum class HighScoreGoal {

  AnsemReportSet,
  ProofSet,
  DriveFormSet,
  MagicSet,
  SummonSet,
  AbilitySet,
  TornPageSet,
  VisitUnlockSet,
  MunnyPouchSet,
  BonusLevel,
  WorldCompletion,
  FormLevel,
  NormalBossDefeated,
  FinalXemnasDefeated,
  AbsentSilhouetteDefeated,
  DataBossDefeated,
  SephirothDefeated,
  LingeringWillDefeated,
  DeathPenalty,

}
