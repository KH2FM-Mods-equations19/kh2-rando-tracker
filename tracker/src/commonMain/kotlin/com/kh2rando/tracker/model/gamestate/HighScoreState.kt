package com.kh2rando.tracker.model.gamestate

import androidx.compose.runtime.Immutable
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.LocationCounterState
import com.kh2rando.tracker.model.hints.HighScoreGoal
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.DriveForm
import com.kh2rando.tracker.model.item.ImportantAbility
import com.kh2rando.tracker.model.item.Magic
import com.kh2rando.tracker.model.item.MunnyPouch
import com.kh2rando.tracker.model.item.PromiseCharm
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.item.SummonCharm
import com.kh2rando.tracker.model.item.TornPage
import com.kh2rando.tracker.model.item.UniqueItem
import com.kh2rando.tracker.model.item.VisitUnlock
import com.kh2rando.tracker.model.progress.AgrabahProgress
import com.kh2rando.tracker.model.progress.BeastsCastleProgress
import com.kh2rando.tracker.model.progress.DisneyCastleProgress
import com.kh2rando.tracker.model.progress.HalloweenTownProgress
import com.kh2rando.tracker.model.progress.HollowBastionProgress
import com.kh2rando.tracker.model.progress.LandOfDragonsProgress
import com.kh2rando.tracker.model.progress.OlympusColiseumProgress
import com.kh2rando.tracker.model.progress.PortRoyalProgress
import com.kh2rando.tracker.model.progress.PrideLandsProgress
import com.kh2rando.tracker.model.progress.ProgressCheckpoint
import com.kh2rando.tracker.model.progress.SimulatedTwilightTownProgress
import com.kh2rando.tracker.model.progress.SpaceParanoidsProgress
import com.kh2rando.tracker.model.progress.TwilightTownProgress
import com.kh2rando.tracker.model.progress.WorldThatNeverWasProgress
import com.kh2rando.tracker.model.stateFlowOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import androidx.compose.ui.util.fastCoerceAtLeast as atLeast

/**
 * Points earned by acquiring individual items.
 */
val FullGameStateApi.individualItemPoints: Flow<Int>
  get() {
    val seedSettings = seed.settings
    val highScoreData = seedSettings.highScoreData ?: return stateFlowOf(0)
    val pointsByItem = highScoreData.pointsByItem.withDefault { 0 }

    return combine(locationUiStates.values) { uiStateList ->
      uiStateList.sumOf { locationState ->
        if (locationState.location == Location.GardenOfAssemblage) {
          // Items in GoA don't count for the score
          0
        } else {
          locationState.acquiredItems.sumOf { acquiredItem ->
            pointsByItem.getValue(acquiredItem.prototype)
          }
        }
      }
    }
  }

/**
 * Bonus points earned from items in inventory.
 */
val BaseGameStateApi.inventoryBonusPoints: Flow<Int>
  get() {
    val seedSettings = seed.settings
    val highScoreData = seedSettings.highScoreData ?: return stateFlowOf(0)
    val pointsByGoal = highScoreData.pointsByGoal.withDefault { 0 }

    val trackablePrototypes = seedSettings.trackableItems

    val requiredReports = AnsemReport.entries.toSet()
    val requiredProofs = buildSet {
      addAll(Proof.entries)
      if (PromiseCharm in trackablePrototypes) {
        add(PromiseCharm)
      }
    }
    val requiredForms = buildSet {
      addAll(DriveForm.entries)
      if (DriveForm.AntiForm !in trackablePrototypes) {
        remove(DriveForm.AntiForm)
      }
    }
    val requiredSummons = SummonCharm.entries.toSet()
    val requiredAbilities = ImportantAbility.entries.toSet()
    val requiredMunnyPouches = MunnyPouch.entries.toSet()

    val reportSetReward = pointsByGoal.getValue(HighScoreGoal.AnsemReportSet)
    val proofSetReward = pointsByGoal.getValue(HighScoreGoal.ProofSet)
    val formSetReward = pointsByGoal.getValue(HighScoreGoal.DriveFormSet)
    val magicSetReward = pointsByGoal.getValue(HighScoreGoal.MagicSet)
    val summonSetReward = pointsByGoal.getValue(HighScoreGoal.SummonSet)
    val abilitySetReward = pointsByGoal.getValue(HighScoreGoal.AbilitySet)
    val tornPageSetReward = pointsByGoal.getValue(HighScoreGoal.TornPageSet)
    val visitUnlockSetReward = pointsByGoal.getValue(HighScoreGoal.VisitUnlockSet)
    val munnyPouchSetReward = pointsByGoal.getValue(HighScoreGoal.MunnyPouchSet)

    return acquiredItems.map { acquiredItems ->
      val acquiredItemsByPrototype = acquiredItems.groupBy(UniqueItem::prototype)
      val acquiredPrototypes = acquiredItemsByPrototype.keys

      var sum = 0

      if (acquiredPrototypes.containsAll(requiredReports)) {
        sum += reportSetReward
      }
      if (acquiredPrototypes.containsAll(requiredProofs)) {
        sum += proofSetReward
      }
      if (acquiredPrototypes.containsAll(requiredForms)) {
        sum += formSetReward
      }
      for (magic in Magic.entries) {
        if (acquiredItemsByPrototype[magic]?.size == Magic.COPIES) {
          sum += magicSetReward
        }
      }
      if (acquiredPrototypes.containsAll(requiredSummons)) {
        sum += summonSetReward
      }
      if (acquiredPrototypes.containsAll(requiredAbilities)) {
        sum += abilitySetReward
      }
      if (acquiredItemsByPrototype[TornPage]?.size == TornPage.COPIES) {
        sum += tornPageSetReward
      }
      if (acquiredPrototypes.containsAll(requiredMunnyPouches)) {
        sum += munnyPouchSetReward
      }
      val allUnlocksAcquired = VisitUnlock.entries.all { unlock ->
        acquiredItemsByPrototype[unlock]?.size == unlock.associatedLocation.visitCount
      }
      if (allUnlocksAcquired) {
        sum += visitUnlockSetReward
      }

      sum
    }
  }

/**
 * Points earned for bonus levels.
 */
val BaseGameStateApi.bonusLevelPoints: Flow<Int>
  get() {
    val seedSettings = seed.settings
    val highScoreData = seedSettings.highScoreData ?: return stateFlowOf(0)
    val reward = highScoreData.pointsByGoal[HighScoreGoal.BonusLevel] ?: return stateFlowOf(0)

    return soraStates.map { soraState ->
      val bonusLevel = soraState.bonusLevel.atLeast(0)
      bonusLevel * reward
    }
  }

/**
 * Points earned for world completion.
 */
val FullGameStateApi.worldCompletionPoints: Flow<Int>
  get() {
    val seedSettings = seed.settings
    val highScoreData = seedSettings.highScoreData ?: return stateFlowOf(0)
    val reward = highScoreData.pointsByGoal[HighScoreGoal.WorldCompletion] ?: return stateFlowOf(0)

    return combine(locationUiStates.values) { uiStateList ->
      uiStateList.sumOf { locationState ->
        when (locationState.counterState) {
          LocationCounterState.None, LocationCounterState.Unrevealed, is LocationCounterState.Revealed -> {
            0
          }

          LocationCounterState.Completed -> {
            // Only counts for points if the location had any items to begin with
            if (locationState.acquiredItems.isEmpty()) 0 else reward
          }
        }
      }
    }
  }

/**
 * Points earned for form levels.
 */
val BaseGameStateApi.formLevelPoints: Flow<Int>
  get() {
    val seedSettings = seed.settings
    val highScoreData = seedSettings.highScoreData ?: return stateFlowOf(0)
    val reward = highScoreData.pointsByGoal[HighScoreGoal.FormLevel] ?: return stateFlowOf(0)

    return driveFormsStates.map { driveFormsState ->
      // Forms start at level 1, start earning points at level 2, so earned levels is (currentLevel - 1)
      val valorEarned = (driveFormsState.valorLevel - 1).atLeast(0)
      val wisdomEarned = (driveFormsState.wisdomLevel - 1).atLeast(0)
      val limitEarned = (driveFormsState.limitLevel - 1).atLeast(0)
      val masterEarned = (driveFormsState.masterLevel - 1).atLeast(0)
      val finalEarned = (driveFormsState.finalLevel - 1).atLeast(0)
      val totalEarnedLevels = (valorEarned + wisdomEarned + limitEarned + masterEarned + finalEarned)
      totalEarnedLevels * reward
    }
  }

/**
 * Points earned for defeating bosses.
 */
val BaseGameStateApi.bossDefeatedPoints: Flow<Int>
  get() {
    val seedSettings = seed.settings
    val highScoreData = seedSettings.highScoreData ?: return stateFlowOf(0)
    val pointsByGoal = highScoreData.pointsByGoal.withDefault { 0 }

    val normalBossReward = pointsByGoal.getValue(HighScoreGoal.NormalBossDefeated)
    val finalXemnasReward = pointsByGoal.getValue(HighScoreGoal.FinalXemnasDefeated)
    val absentSilhouetteReward = pointsByGoal.getValue(HighScoreGoal.AbsentSilhouetteDefeated)
    val dataBossReward = pointsByGoal.getValue(HighScoreGoal.DataBossDefeated)
    val sephirothReward = pointsByGoal.getValue(HighScoreGoal.SephirothDefeated)
    val lingeringWillReward = pointsByGoal.getValue(HighScoreGoal.LingeringWillDefeated)

    // TODO: These sets could live elsewhere, or the checkpoints could have tags on them or something.
    //       They could also potentially be dynamic, if we ever support randomized bosses.
    //       We'd need to look up which bosses are at which checkpoints to reorganize them.
    val normalBossCheckpoints = setOf<ProgressCheckpoint>(
      AgrabahProgress.Lords,
      AgrabahProgress.GenieJafar,
      BeastsCastleProgress.Beast,
      BeastsCastleProgress.Thresholder,
      BeastsCastleProgress.Shadowstalker,
      BeastsCastleProgress.DarkThorn,
      BeastsCastleProgress.Xaldin,
      DisneyCastleProgress.OldPete,
      DisneyCastleProgress.BoatPete,
      DisneyCastleProgress.DCPete,
      HalloweenTownProgress.PrisonKeeper,
      HalloweenTownProgress.OogieBoogie,
      HalloweenTownProgress.Experiment,
      HollowBastionProgress.HBDemyx,
      LandOfDragonsProgress.ShanYu,
      LandOfDragonsProgress.Riku,
      LandOfDragonsProgress.StormRider,
      OlympusColiseumProgress.Cerberus,
      OlympusColiseumProgress.OCPete,
      OlympusColiseumProgress.Hydra,
      OlympusColiseumProgress.Hades,
      PortRoyalProgress.Barbossa,
      PortRoyalProgress.GrimReaper1,
      PortRoyalProgress.GrimReaper,
      PrideLandsProgress.Scar,
      PrideLandsProgress.GroundShaker,
      SimulatedTwilightTownProgress.TwilightThorn,
      SimulatedTwilightTownProgress.Hayner,
      SimulatedTwilightTownProgress.Vivi,
      SimulatedTwilightTownProgress.Struggle, // aka Setzer
      SimulatedTwilightTownProgress.Axel1,
      SimulatedTwilightTownProgress.Axel,
      SpaceParanoidsProgress.HostileProgram,
      SpaceParanoidsProgress.Sark,
      SpaceParanoidsProgress.MCP,
      WorldThatNeverWasProgress.Roxas,
      WorldThatNeverWasProgress.Xigbar,
      WorldThatNeverWasProgress.Luxord,
      WorldThatNeverWasProgress.Saix,
      WorldThatNeverWasProgress.Xemnas1,
      WorldThatNeverWasProgress.ArmoredXemnas1,
      WorldThatNeverWasProgress.ArmoredXemnas2,

//      Blizzard Lord (Cups),
//      Cerberus (Cups),
//      Cloud,
//      Cloud (1),
//      Cloud (2),
//      Hades Cups,
//      Hades II (1),
//      Hercules,
//      Leon,
//      Leon (1),
//      Leon (2),
//      Leon (3),
//      Pete Cups,
//      Seifer,
//      Seifer (1),
//      Seifer (2),
//      Seifer (3),
//      Seifer (4),
//      Tifa,
//      Tifa (1),
//      Tifa (2),
//      Volcano Lord (Cups),
//      Yuffie,
//      Yuffie (1),
//      Yuffie (2),
//      Yuffie (3),
    )

    val absentSilhouetteCheckpoints = setOf(
      HalloweenTownProgress.Vexen,
      AgrabahProgress.Lexaeus,
      OlympusColiseumProgress.Zexion,
      DisneyCastleProgress.Marluxia,
      SpaceParanoidsProgress.Larxene,
    )

    val dataBossCheckpoints = setOf(
      WorldThatNeverWasProgress.DataXemnas,
      LandOfDragonsProgress.DataXigbar,
      BeastsCastleProgress.DataXaldin,
      HalloweenTownProgress.VexenData,
      AgrabahProgress.LexaeusData,
      OlympusColiseumProgress.ZexionData,
      TwilightTownProgress.DataAxel,
      PrideLandsProgress.DataSaix,
      HollowBastionProgress.DataDemyx,
      PortRoyalProgress.DataLuxord,
      DisneyCastleProgress.MarluxiaData,
      SpaceParanoidsProgress.LarxeneData,
      SimulatedTwilightTownProgress.DataRoxas,
    )

    val sephirothCheckpoint = HollowBastionProgress.Sephiroth
    val lingeringWillCheckpoint = DisneyCastleProgress.LingeringWill

    return allCompletedProgressCheckpoints.map { completedCheckpoints ->
      var sum = 0

      for (bossCheckpoint in normalBossCheckpoints) {
        if (bossCheckpoint in completedCheckpoints) {
          sum += normalBossReward

          // Needs to count Blizzard Lord and Volcano Lord separately.
          // May not be worth a more elegant solution because there's only one spot this matters.
          // TODO: If we ever support randomized bosses, give the bonuses separately for each boss that's there?
          if (bossCheckpoint == AgrabahProgress.Lords) {
            sum += normalBossReward
          }
        }
      }

      if (WorldThatNeverWasProgress.FinalXemnas in completedCheckpoints) {
        sum += finalXemnasReward
      }

      for (absentSilhouetteCheckpoint in absentSilhouetteCheckpoints) {
        if (absentSilhouetteCheckpoint in completedCheckpoints) {
          sum += absentSilhouetteReward
        }
      }

      for (dataBossCheckpoint in dataBossCheckpoints) {
        if (dataBossCheckpoint in completedCheckpoints) {
          sum += dataBossReward
        }
      }

      if (sephirothCheckpoint in completedCheckpoints) {
        sum += sephirothReward
      }

      if (lingeringWillCheckpoint in completedCheckpoints) {
        sum += lingeringWillReward
      }

      sum
    }
  }

/**
 * Points earned (or more likely lost) from deaths.
 */
val BaseGameStateApi.deathPoints: Flow<Int>
  get() {
    val seedSettings = seed.settings
    val highScoreData = seedSettings.highScoreData ?: return stateFlowOf(0)
    val reward = highScoreData.pointsByGoal[HighScoreGoal.DeathPenalty] ?: return stateFlowOf(0)

    return deaths.map { deathCount -> deathCount * reward }
  }

/**
 * Summary of all of the high score components.
 */
val FullGameStateApi.highScoreStates: Flow<HighScoreState>
  get() {
    return combine(
      individualItemPoints,
      inventoryBonusPoints,
      bonusLevelPoints,
      worldCompletionPoints,
      formLevelPoints,
      bossDefeatedPoints,
      deathPoints,
    ) { components ->
      HighScoreState(
        individualItems = components[0],
        inventoryBonuses = components[1],
        bonusLevel = components[2],
        worldCompletion = components[3],
        formLevels = components[4],
        bossesDefeated = components[5],
        deaths = components[6],
      )
    }
  }

/**
 * The total number of high score mode points that have been earned, based on settings and the current game state.
 */
val FullGameStateApi.totalHighScorePointsEarned: Flow<Int>
  get() = highScoreStates.map { it.total }.distinctUntilChanged()

@Immutable
data class HighScoreState(
  val individualItems: Int = 0,
  val inventoryBonuses: Int = 0,
  val bonusLevel: Int = 0,
  val worldCompletion: Int = 0,
  val formLevels: Int = 0,
  val bossesDefeated: Int = 0,
  val deaths: Int = 0,
) {

  val total: Int
    get() = individualItems + inventoryBonuses + bonusLevel + worldCompletion + formLevels + bossesDefeated + deaths

}
