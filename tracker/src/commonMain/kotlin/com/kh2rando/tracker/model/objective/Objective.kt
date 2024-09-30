package com.kh2rando.tracker.model.objective

import androidx.compose.ui.graphics.Color
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.objective_ag_abu
import com.kh2rando.tracker.generated.resources.objective_ag_as_lexaeus
import com.kh2rando.tracker.generated.resources.objective_ag_data_lexaeus
import com.kh2rando.tracker.generated.resources.objective_ag_elemental_lords
import com.kh2rando.tracker.generated.resources.objective_ag_jafar
import com.kh2rando.tracker.generated.resources.objective_ag_treasure_room
import com.kh2rando.tracker.generated.resources.objective_at_new_day
import com.kh2rando.tracker.generated.resources.objective_at_tutorial
import com.kh2rando.tracker.generated.resources.objective_at_ursula
import com.kh2rando.tracker.generated.resources.objective_bc_beast
import com.kh2rando.tracker.generated.resources.objective_bc_dark_thorn
import com.kh2rando.tracker.generated.resources.objective_bc_data_xaldin
import com.kh2rando.tracker.generated.resources.objective_bc_thresholder
import com.kh2rando.tracker.generated.resources.objective_bc_xaldin
import com.kh2rando.tracker.generated.resources.objective_dc_as_marluxia
import com.kh2rando.tracker.generated.resources.objective_dc_boat_pete
import com.kh2rando.tracker.generated.resources.objective_dc_data_marluxia
import com.kh2rando.tracker.generated.resources.objective_dc_lingering_will
import com.kh2rando.tracker.generated.resources.objective_dc_minnie
import com.kh2rando.tracker.generated.resources.objective_dc_pete
import com.kh2rando.tracker.generated.resources.objective_dc_windows
import com.kh2rando.tracker.generated.resources.objective_final_3
import com.kh2rando.tracker.generated.resources.objective_final_5
import com.kh2rando.tracker.generated.resources.objective_final_7
import com.kh2rando.tracker.generated.resources.objective_haw_spooky_cave
import com.kh2rando.tracker.generated.resources.objective_haw_starry_hill
import com.kh2rando.tracker.generated.resources.objective_hb_bailey
import com.kh2rando.tracker.generated.resources.objective_hb_data_demyx
import com.kh2rando.tracker.generated.resources.objective_hb_demyx
import com.kh2rando.tracker.generated.resources.objective_hb_last_chest_cor
import com.kh2rando.tracker.generated.resources.objective_hb_sephiroth
import com.kh2rando.tracker.generated.resources.objective_hb_thousand_heartless
import com.kh2rando.tracker.generated.resources.objective_hb_transport
import com.kh2rando.tracker.generated.resources.objective_ht_as_vexen
import com.kh2rando.tracker.generated.resources.objective_ht_children
import com.kh2rando.tracker.generated.resources.objective_ht_data_vexen
import com.kh2rando.tracker.generated.resources.objective_ht_experiment
import com.kh2rando.tracker.generated.resources.objective_ht_oogie_boogie
import com.kh2rando.tracker.generated.resources.objective_ht_presents_minigame
import com.kh2rando.tracker.generated.resources.objective_ht_prison_keeper
import com.kh2rando.tracker.generated.resources.objective_ht_stolen_presents
import com.kh2rando.tracker.generated.resources.objective_limit_3
import com.kh2rando.tracker.generated.resources.objective_limit_5
import com.kh2rando.tracker.generated.resources.objective_limit_7
import com.kh2rando.tracker.generated.resources.objective_lod_cave
import com.kh2rando.tracker.generated.resources.objective_lod_data_xigbar
import com.kh2rando.tracker.generated.resources.objective_lod_mountain
import com.kh2rando.tracker.generated.resources.objective_lod_shan_yu
import com.kh2rando.tracker.generated.resources.objective_lod_storm_rider
import com.kh2rando.tracker.generated.resources.objective_master_3
import com.kh2rando.tracker.generated.resources.objective_master_5
import com.kh2rando.tracker.generated.resources.objective_master_7
import com.kh2rando.tracker.generated.resources.objective_oc_as_zexion
import com.kh2rando.tracker.generated.resources.objective_oc_aurons_statue
import com.kh2rando.tracker.generated.resources.objective_oc_cerberus
import com.kh2rando.tracker.generated.resources.objective_oc_cups_cerberus
import com.kh2rando.tracker.generated.resources.objective_oc_cups_goddess
import com.kh2rando.tracker.generated.resources.objective_oc_cups_pain_panic
import com.kh2rando.tracker.generated.resources.objective_oc_cups_titan
import com.kh2rando.tracker.generated.resources.objective_oc_data_zexion
import com.kh2rando.tracker.generated.resources.objective_oc_hades
import com.kh2rando.tracker.generated.resources.objective_oc_hydra
import com.kh2rando.tracker.generated.resources.objective_oc_pete
import com.kh2rando.tracker.generated.resources.objective_oc_urns
import com.kh2rando.tracker.generated.resources.objective_pl_data_saix
import com.kh2rando.tracker.generated.resources.objective_pl_groundshaker
import com.kh2rando.tracker.generated.resources.objective_pl_hyenas_1
import com.kh2rando.tracker.generated.resources.objective_pl_hyenas_2
import com.kh2rando.tracker.generated.resources.objective_pl_scar
import com.kh2rando.tracker.generated.resources.objective_pl_simba
import com.kh2rando.tracker.generated.resources.objective_pr_barbossa
import com.kh2rando.tracker.generated.resources.objective_pr_data_luxord
import com.kh2rando.tracker.generated.resources.objective_pr_grim_reaper_1
import com.kh2rando.tracker.generated.resources.objective_pr_grim_reaper_2
import com.kh2rando.tracker.generated.resources.objective_pr_interceptor_barrels
import com.kh2rando.tracker.generated.resources.objective_pr_interceptor_pirates
import com.kh2rando.tracker.generated.resources.objective_pr_minute_fight
import com.kh2rando.tracker.generated.resources.objective_puzzle_awakening
import com.kh2rando.tracker.generated.resources.objective_puzzle_daylight
import com.kh2rando.tracker.generated.resources.objective_puzzle_duality
import com.kh2rando.tracker.generated.resources.objective_puzzle_frontier
import com.kh2rando.tracker.generated.resources.objective_puzzle_heart
import com.kh2rando.tracker.generated.resources.objective_puzzle_sunset
import com.kh2rando.tracker.generated.resources.objective_sp_as_larxene
import com.kh2rando.tracker.generated.resources.objective_sp_data_larxene
import com.kh2rando.tracker.generated.resources.objective_sp_hostile_program
import com.kh2rando.tracker.generated.resources.objective_sp_mcp
import com.kh2rando.tracker.generated.resources.objective_sp_screens
import com.kh2rando.tracker.generated.resources.objective_sp_solar_sailer
import com.kh2rando.tracker.generated.resources.objective_stt_axel_1
import com.kh2rando.tracker.generated.resources.objective_stt_axel_2
import com.kh2rando.tracker.generated.resources.objective_stt_data_roxas
import com.kh2rando.tracker.generated.resources.objective_stt_setzer
import com.kh2rando.tracker.generated.resources.objective_stt_twilight_thorn
import com.kh2rando.tracker.generated.resources.objective_tt_betwixt
import com.kh2rando.tracker.generated.resources.objective_tt_data_axel
import com.kh2rando.tracker.generated.resources.objective_tt_fairies
import com.kh2rando.tracker.generated.resources.objective_tt_sandlot
import com.kh2rando.tracker.generated.resources.objective_twtnw_data_xemnas
import com.kh2rando.tracker.generated.resources.objective_twtnw_luxord
import com.kh2rando.tracker.generated.resources.objective_twtnw_roxas
import com.kh2rando.tracker.generated.resources.objective_twtnw_saix
import com.kh2rando.tracker.generated.resources.objective_twtnw_xemnas
import com.kh2rando.tracker.generated.resources.objective_twtnw_xigbar
import com.kh2rando.tracker.generated.resources.objective_valor_3
import com.kh2rando.tracker.generated.resources.objective_valor_5
import com.kh2rando.tracker.generated.resources.objective_valor_7
import com.kh2rando.tracker.generated.resources.objective_wisdom_3
import com.kh2rando.tracker.generated.resources.objective_wisdom_5
import com.kh2rando.tracker.generated.resources.objective_wisdom_7
import com.kh2rando.tracker.model.CheckLocation
import com.kh2rando.tracker.model.CheckLocationCategory
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.progress.AgrabahProgress
import com.kh2rando.tracker.model.progress.AtlanticaProgress
import com.kh2rando.tracker.model.progress.BeastsCastleProgress
import com.kh2rando.tracker.model.progress.CavernOfRemembranceProgress
import com.kh2rando.tracker.model.progress.CreationsProgress
import com.kh2rando.tracker.model.progress.DisneyCastleProgress
import com.kh2rando.tracker.model.progress.DriveFormProgress
import com.kh2rando.tracker.model.progress.HalloweenTownProgress
import com.kh2rando.tracker.model.progress.HollowBastionProgress
import com.kh2rando.tracker.model.progress.HundredAcreWoodProgress
import com.kh2rando.tracker.model.progress.LandOfDragonsProgress
import com.kh2rando.tracker.model.progress.OlympusColiseumProgress
import com.kh2rando.tracker.model.progress.PortRoyalProgress
import com.kh2rando.tracker.model.progress.PrideLandsProgress
import com.kh2rando.tracker.model.progress.ProgressCheckpoint
import com.kh2rando.tracker.model.progress.SimulatedTwilightTownProgress
import com.kh2rando.tracker.model.progress.SpaceParanoidsProgress
import com.kh2rando.tracker.model.progress.TwilightTownProgress
import com.kh2rando.tracker.model.progress.WorldThatNeverWasProgress
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * An in-game objective. Used when [com.kh2rando.tracker.model.seed.FinalDoorRequirement.Objectives] is the seed's
 * final door requirement.
 */
enum class Objective(
  val checkpoint: ProgressCheckpoint,
  val location: Location = checkpoint.location,
  val checkLocation: CheckLocation,
  val type: Type,
  val description: StringResource,
  val difficulty: Difficulty = Difficulty.Early,
) : HasCustomizableIcon {

  // STT
  DefeatTwilightThorn(
    checkpoint = (SimulatedTwilightTownProgress.TwilightThorn),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 33),
    type = Type.Boss,
    description = Res.string.objective_stt_twilight_thorn,
  ),
  DefeatAxel1(
    checkpoint = (SimulatedTwilightTownProgress.Axel1),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 73),
    type = Type.Fight,
    description = Res.string.objective_stt_axel_1,
  ),
  FightSetzer(
    checkpoint = (SimulatedTwilightTownProgress.Struggle),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 519),
    type = Type.Fight,
    description = Res.string.objective_stt_setzer,
  ),
  DefeatAxel2(
    checkpoint = (SimulatedTwilightTownProgress.Axel),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 34),
    type = Type.Boss,
    description = Res.string.objective_stt_axel_2,
  ),

  // TT
  TalkTo3Fairies(
    checkpoint = (TwilightTownProgress.MysteriousTower),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 286),
    type = Type.WorldProgress,
    description = Res.string.objective_tt_fairies,
  ),
  DefeatSandlotBerserkers(
    checkpoint = (TwilightTownProgress.Sandlot),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 294),
    type = Type.Fight,
    description = Res.string.objective_tt_sandlot,
    difficulty = Difficulty.Middle,
  ),
  FightAlongsideAxel(
    checkpoint = (TwilightTownProgress.BetwixtAndBetween),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 63),
    type = Type.Fight,
    description = Res.string.objective_tt_betwixt,
    difficulty = Difficulty.Late,
  ),

  // HB
  DefendBailey(
    checkpoint = (HollowBastionProgress.Bailey),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 47),
    type = Type.Fight,
    description = Res.string.objective_hb_bailey,
  ),
  DefeatDemyx(
    checkpoint = (HollowBastionProgress.HBDemyx),
    checkLocation = CheckLocation(CheckLocationCategory.ItemAndStatBonus, 28),
    type = Type.Boss,
    description = Res.string.objective_hb_demyx,
    difficulty = Difficulty.Middle,
  ),
  Stop1000Heartless(
    checkpoint = (HollowBastionProgress.ThousandHeartless),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 60),
    type = Type.Fight,
    description = Res.string.objective_hb_thousand_heartless,
    difficulty = Difficulty.Late,
  ),

  // CoR
  ReachEndOfCavernOfRemembrance(
    checkpoint = (CavernOfRemembranceProgress.LastChest),
    location = Location.HollowBastion,
    checkLocation = CheckLocation(CheckLocationCategory.Chest, 579),
    type = Type.WorldProgress,
    description = Res.string.objective_hb_last_chest_cor,
    difficulty = Difficulty.Late,
  ),
  ReachEndOfTransportToRemembrance(
    checkpoint = (CavernOfRemembranceProgress.Transport),
    location = Location.HollowBastion,
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 72),
    type = Type.Fight,
    description = Res.string.objective_hb_transport,
    difficulty = Difficulty.Latest,
  ),

  // LoD
  ClimbMountainTrail(
    checkpoint = (LandOfDragonsProgress.Mountain),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 495),
    type = Type.WorldProgress,
    description = Res.string.objective_lod_mountain,
  ),
  FightEnemiesInVillageCave(
    checkpoint = (LandOfDragonsProgress.Cave),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 43),
    type = Type.Fight,
    description = Res.string.objective_lod_cave,
  ),
  DefeatShanYu(
    checkpoint = (LandOfDragonsProgress.ShanYu),
    checkLocation = CheckLocation(CheckLocationCategory.ItemAndStatBonus, 9),
    type = Type.Boss,
    description = Res.string.objective_lod_shan_yu,
    difficulty = Difficulty.Middle,
  ),
  DefeatStormRider(
    checkpoint = (LandOfDragonsProgress.StormRider),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 10),
    type = Type.Boss,
    description = Res.string.objective_lod_storm_rider,
    difficulty = Difficulty.Late,
  ),

  // BC
  DefeatThresholder(
    checkpoint = (BeastsCastleProgress.Thresholder),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 2),
    type = Type.Boss,
    description = Res.string.objective_bc_thresholder,
  ),
  HelpBeast(
    checkpoint = (BeastsCastleProgress.Beast),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 12),
    type = Type.WorldProgress,
    description = Res.string.objective_bc_beast,
  ),
  DefeatDarkThorn(
    checkpoint = (BeastsCastleProgress.DarkThorn),
    checkLocation = CheckLocation(CheckLocationCategory.ItemAndStatBonus, 3),
    type = Type.Boss,
    description = Res.string.objective_bc_dark_thorn,
    difficulty = Difficulty.Middle,
  ),
  DefeatXaldin(
    checkpoint = (BeastsCastleProgress.Xaldin),
    checkLocation = CheckLocation(CheckLocationCategory.ItemAndStatBonus, 4),
    type = Type.Boss,
    description = Res.string.objective_bc_xaldin,
    difficulty = Difficulty.Late,
  ),

  // DC
  EscortQueenMinnie(
    checkpoint = (DisneyCastleProgress.Minnie),
    checkLocation = CheckLocation(CheckLocationCategory.ItemAndStatBonus, 38),
    type = Type.WorldProgress,
    description = Res.string.objective_dc_minnie,
  ),
  FightThroughWindowsOfTime(
    checkpoint = (DisneyCastleProgress.Windows),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 368),
    type = Type.Fight,
    description = Res.string.objective_dc_windows,
    difficulty = Difficulty.Middle,
  ),
  StopPeteFromEscaping(
    checkpoint = (DisneyCastleProgress.BoatPete),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 16),
    type = Type.WorldProgress,
    description = Res.string.objective_dc_boat_pete,
    difficulty = Difficulty.Middle,
  ),
  DefeatFuturePete(
    checkpoint = (DisneyCastleProgress.DCPete),
    checkLocation = CheckLocation(CheckLocationCategory.ItemAndStatBonus, 17),
    type = Type.Boss,
    description = Res.string.objective_dc_pete,
    difficulty = Difficulty.Late,
  ),

  // PR
  StallForTimeOnIslaDeMuerta(
    checkpoint = (PortRoyalProgress.OneMinute),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 329),
    type = Type.WorldProgress,
    description = Res.string.objective_pr_minute_fight,
  ),
  DefendInterceptorFromPirates(
    checkpoint = (PortRoyalProgress.Medallions),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 62),
    type = Type.Fight,
    description = Res.string.objective_pr_interceptor_pirates,
  ),
  StopExplosiveBarrels(
    checkpoint = (PortRoyalProgress.Barrels),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 39),
    type = Type.WorldProgress,
    description = Res.string.objective_pr_interceptor_barrels,
  ),
  DefeatBarbossa(
    checkpoint = (PortRoyalProgress.Barbossa),
    checkLocation = CheckLocation(CheckLocationCategory.ItemAndStatBonus, 21),
    type = Type.Boss,
    description = Res.string.objective_pr_barbossa,
    difficulty = Difficulty.Middle,
  ),
  DefeatGrimReaper1(
    checkpoint = (PortRoyalProgress.GrimReaper1),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 59),
    type = Type.Boss,
    description = Res.string.objective_pr_grim_reaper_1,
    difficulty = Difficulty.Middle,
  ),
  DefeatGrimReaper2(
    checkpoint = (PortRoyalProgress.GrimReaper),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 22),
    type = Type.Boss,
    description = Res.string.objective_pr_grim_reaper_2,
    difficulty = Difficulty.Late,
  ),

  // AG
  EscortAbu(
    checkpoint = (AgrabahProgress.Abu),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 42),
    type = Type.WorldProgress,
    description = Res.string.objective_ag_abu,
  ),
  SurviveTreasureRoomAmbush(
    checkpoint = (AgrabahProgress.TreasureRoom),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 46),
    type = Type.Fight,
    description = Res.string.objective_ag_treasure_room,
    difficulty = Difficulty.Middle,
  ),
  DefeatElementalLords(
    checkpoint = (AgrabahProgress.Lords),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 37),
    type = Type.Boss,
    description = Res.string.objective_ag_elemental_lords,
    difficulty = Difficulty.Middle,
  ),
  DefeatGenieJafar(
    checkpoint = (AgrabahProgress.GenieJafar),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 15),
    type = Type.Boss,
    description = Res.string.objective_ag_jafar,
    difficulty = Difficulty.Late,
  ),

  // HT
  DefeatPrisonKeeper(
    checkpoint = (HalloweenTownProgress.PrisonKeeper),
    checkLocation = CheckLocation(CheckLocationCategory.ItemAndStatBonus, 18),
    type = Type.Boss,
    description = Res.string.objective_ht_prison_keeper,
  ),
  DefeatOogieBoogie(
    checkpoint = (HalloweenTownProgress.OogieBoogie),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 19),
    type = Type.Boss,
    description = Res.string.objective_ht_oogie_boogie,
    difficulty = Difficulty.Middle,
  ),
  CaptureLockShockBarrel(
    checkpoint = (HalloweenTownProgress.Children),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 40),
    type = Type.WorldProgress,
    description = Res.string.objective_ht_children,
    difficulty = Difficulty.Middle,
  ),
  FindStolenPresents(
    checkpoint = (HalloweenTownProgress.StolenPresents),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 297),
    type = Type.Fight,
    description = Res.string.objective_ht_stolen_presents,
    difficulty = Difficulty.Middle
  ),
  CreateDecoyPresents(
    checkpoint = (HalloweenTownProgress.PresentsMinigame),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 298),
    type = Type.WorldProgress,
    description = Res.string.objective_ht_presents_minigame,
    difficulty = Difficulty.Middle,
  ),
  DefeatExperiment(
    checkpoint = (HalloweenTownProgress.Experiment),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 20),
    type = Type.Boss,
    description = Res.string.objective_ht_experiment,
    difficulty = Difficulty.Late,
  ),

  // PL
  ReuniteWithSimba(
    checkpoint = (PrideLandsProgress.Simba),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 264),
    type = Type.WorldProgress,
    description = Res.string.objective_pl_simba,
  ),
  RescueTimonAndPumbaa(
    checkpoint = (PrideLandsProgress.Hyenas1),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 49),
    type = Type.WorldProgress,
    description = Res.string.objective_pl_hyenas_1,
  ),
  DefeatScar(
    checkpoint = (PrideLandsProgress.Scar),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 29),
    type = Type.Boss,
    description = Res.string.objective_pl_scar,
    difficulty = Difficulty.Middle,
  ),
  GetInfoAboutGhostOfScarFromHyenas(
    checkpoint = (PrideLandsProgress.Hyenas2),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 50),
    type = Type.WorldProgress,
    description = Res.string.objective_pl_hyenas_2,
    difficulty = Difficulty.Middle,
  ),
  DefeatGroundshaker(
    checkpoint = (PrideLandsProgress.GroundShaker),
    checkLocation = CheckLocation(CheckLocationCategory.ItemAndStatBonus, 30),
    type = Type.Boss,
    description = Res.string.objective_pl_groundshaker,
    difficulty = Difficulty.Late,
  ),

  // SP
  SurviveDataspaceAttack(
    checkpoint = (SpaceParanoidsProgress.Screens),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 45),
    type = Type.Fight,
    description = Res.string.objective_sp_screens,
  ),
  DefeatHostileProgram(
    checkpoint = (SpaceParanoidsProgress.HostileProgram),
    checkLocation = CheckLocation(CheckLocationCategory.ItemAndStatBonus, 31),
    type = Type.Boss,
    description = Res.string.objective_sp_hostile_program,
    difficulty = Difficulty.Middle,
  ),
  RideSolarSailer(
    checkpoint = (SpaceParanoidsProgress.SolarSailer),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 61),
    type = Type.Fight,
    description = Res.string.objective_sp_solar_sailer,
    difficulty = Difficulty.Middle,
  ),
  DefeatMCP(
    checkpoint = (SpaceParanoidsProgress.MCP),
    checkLocation = CheckLocation(CheckLocationCategory.ItemAndStatBonus, 32),
    type = Type.Boss,
    description = Res.string.objective_sp_mcp,
    difficulty = Difficulty.Late,
  ),

  // TWTNW
  DefeatRoxas(
    checkpoint = (WorldThatNeverWasProgress.Roxas),
    checkLocation = CheckLocation(CheckLocationCategory.ItemAndStatBonus, 69),
    type = Type.Boss,
    description = Res.string.objective_twtnw_roxas,
  ),
  DefeatXigbar(
    checkpoint = (WorldThatNeverWasProgress.Xigbar),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 23),
    type = Type.Boss,
    description = Res.string.objective_twtnw_xigbar,
    difficulty = Difficulty.Middle,
  ),
  DefeatLuxord(
    checkpoint = (WorldThatNeverWasProgress.Luxord),
    checkLocation = CheckLocation(CheckLocationCategory.ItemAndStatBonus, 24),
    type = Type.Boss,
    description = Res.string.objective_twtnw_luxord,
    difficulty = Difficulty.Middle,
  ),
  DefeatSaix(
    checkpoint = (WorldThatNeverWasProgress.Saix),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 25),
    type = Type.Boss,
    description = Res.string.objective_twtnw_saix,
    difficulty = Difficulty.Middle,
  ),
  DefeatXemnas(
    checkpoint = (WorldThatNeverWasProgress.Xemnas1),
    checkLocation = CheckLocation(CheckLocationCategory.DoubleStatBonus, 26),
    type = Type.Boss,
    description = Res.string.objective_twtnw_xemnas,
    difficulty = Difficulty.Late,
  ),

  // OC
  DefeatCerberus(
    checkpoint = (OlympusColiseumProgress.Cerberus),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 5),
    type = Type.Boss,
    description = Res.string.objective_oc_cerberus,
  ),
  TrainWithPhil(
    checkpoint = (OlympusColiseumProgress.Urns),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 57),
    type = Type.WorldProgress,
    description = Res.string.objective_oc_urns,
  ),
  DefeatPeteAtLock(
    checkpoint = (OlympusColiseumProgress.OCPete),
    checkLocation = CheckLocation(CheckLocationCategory.ItemBonus, 6),
    type = Type.Fight,
    description = Res.string.objective_oc_pete,
    difficulty = Difficulty.Middle,
  ),
  DefeatHydra(
    checkpoint = (OlympusColiseumProgress.Hydra),
    checkLocation = CheckLocation(CheckLocationCategory.ItemAndStatBonus, 7),
    type = Type.Boss,
    description = Res.string.objective_oc_hydra,
    difficulty = Difficulty.Middle,
  ),
  DefeatAmbushHadesChamber(
    checkpoint = (OlympusColiseumProgress.AuronStatue),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 295),
    type = Type.Fight,
    description = Res.string.objective_oc_aurons_statue,
    difficulty = Difficulty.Middle,
  ),
  DefeatHades(
    checkpoint = (OlympusColiseumProgress.Hades),
    checkLocation = CheckLocation(CheckLocationCategory.ItemAndStatBonus, 8),
    type = Type.Boss,
    description = Res.string.objective_oc_hades,
    difficulty = Difficulty.Late,
  ),

  // Cups
  WinPainPanicCup(
    checkpoint = (OlympusColiseumProgress.PainPanicCup),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 513),
    type = Type.Fight,
    description = Res.string.objective_oc_cups_pain_panic,
  ),
  WinCerberusCup(
    checkpoint = (OlympusColiseumProgress.CerberusCup),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 515),
    type = Type.Fight,
    description = Res.string.objective_oc_cups_cerberus,
    difficulty = Difficulty.Middle,
  ),
  WinTitanCup(
    checkpoint = (OlympusColiseumProgress.TitanCup),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 514),
    type = Type.Fight,
    description = Res.string.objective_oc_cups_titan,
    difficulty = Difficulty.Middle,
  ),
  WinGoddessOfFateCup(
    checkpoint = (OlympusColiseumProgress.GoddessOfFateCup),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 516),
    type = Type.Fight,
    description = Res.string.objective_oc_cups_goddess,
    difficulty = Difficulty.Late,
  ),

  // HAW
  RescuePoohFromSpookyCave(
    checkpoint = (HundredAcreWoodProgress.SpookyCave),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 284),
    type = Type.WorldProgress,
    description = Res.string.objective_haw_spooky_cave,
    difficulty = Difficulty.Late,
  ),
  HelpPoohOutOfHunnyPot(
    checkpoint = (HundredAcreWoodProgress.StarryHill),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 285),
    type = Type.WorldProgress,
    description = Res.string.objective_haw_starry_hill,
    difficulty = Difficulty.Latest,
  ),

  // AT
  RelearnToSwim(
    checkpoint = (AtlanticaProgress.Tutorial),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 367),
    type = Type.WorldProgress,
    description = Res.string.objective_at_tutorial,
  ),
  DefeatUrsula(
    checkpoint = (AtlanticaProgress.Ursula),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 287),
    type = Type.WorldProgress,
    description = Res.string.objective_at_ursula,
    difficulty = Difficulty.Late,
  ),
  ParticipateInNewDayMusical(
    checkpoint = (AtlanticaProgress.NewDay),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 279),
    type = Type.WorldProgress,
    description = Res.string.objective_at_new_day,
    difficulty = Difficulty.Latest,
  ),

  // Double check the valor dummy and final dummy ones
  ReachValor3(
    checkpoint = (DriveFormProgress.Valor3),
    checkLocation = CheckLocation(CheckLocationCategory.ValorLevel, 3),
    type = Type.WorldProgress,
    description = Res.string.objective_valor_3,
  ),
  ReachValor5(
    checkpoint = (DriveFormProgress.Valor5),
    checkLocation = CheckLocation(CheckLocationCategory.ValorLevel, 5),
    type = Type.WorldProgress,
    description = Res.string.objective_valor_5,
    difficulty = Difficulty.Middle,
  ),
  ReachValor7(
    checkpoint = (DriveFormProgress.Valor7),
    checkLocation = CheckLocation(CheckLocationCategory.ValorLevel, 7),
    type = Type.WorldProgress,
    description = Res.string.objective_valor_7,
    difficulty = Difficulty.Latest,
  ),
  ReachWisdom3(
    checkpoint = (DriveFormProgress.Wisdom3),
    checkLocation = CheckLocation(CheckLocationCategory.WisdomLevel, 3),
    type = Type.WorldProgress,
    description = Res.string.objective_wisdom_3,
  ),
  ReachWisdom5(
    checkpoint = (DriveFormProgress.Wisdom5),
    checkLocation = CheckLocation(CheckLocationCategory.WisdomLevel, 5),
    type = Type.WorldProgress,
    description = Res.string.objective_wisdom_5,
    difficulty = Difficulty.Middle,
  ),
  ReachWisdom7(
    checkpoint = (DriveFormProgress.Wisdom7),
    checkLocation = CheckLocation(CheckLocationCategory.WisdomLevel, 7),
    type = Type.WorldProgress,
    description = Res.string.objective_wisdom_7,
    difficulty = Difficulty.Latest,
  ),
  ReachLimit3(
    checkpoint = (DriveFormProgress.Limit3),
    checkLocation = CheckLocation(CheckLocationCategory.LimitLevel, 3),
    type = Type.WorldProgress,
    description = Res.string.objective_limit_3,
  ),
  ReachLimit5(
    checkpoint = (DriveFormProgress.Limit5),
    checkLocation = CheckLocation(CheckLocationCategory.LimitLevel, 5),
    type = Type.WorldProgress,
    description = Res.string.objective_limit_5,
    difficulty = Difficulty.Middle,
  ),
  ReachLimit7(
    checkpoint = (DriveFormProgress.Limit7),
    checkLocation = CheckLocation(CheckLocationCategory.LimitLevel, 7),
    type = Type.WorldProgress,
    description = Res.string.objective_limit_7,
    difficulty = Difficulty.Latest,
  ),
  ReachMaster3(
    checkpoint = (DriveFormProgress.Master3),
    checkLocation = CheckLocation(CheckLocationCategory.MasterLevel, 3),
    type = Type.WorldProgress,
    description = Res.string.objective_master_3,
  ),
  ReachMaster5(
    checkpoint = (DriveFormProgress.Master5),
    checkLocation = CheckLocation(CheckLocationCategory.MasterLevel, 5),
    type = Type.WorldProgress,
    description = Res.string.objective_master_5,
    difficulty = Difficulty.Middle,
  ),
  ReachMaster7(
    checkpoint = (DriveFormProgress.Master7),
    checkLocation = CheckLocation(CheckLocationCategory.MasterLevel, 7),
    type = Type.WorldProgress,
    description = Res.string.objective_master_7,
    difficulty = Difficulty.Latest,
  ),
  ReachFinal3(
    checkpoint = (DriveFormProgress.Final3),
    checkLocation = CheckLocation(CheckLocationCategory.FinalLevel, 3),
    type = Type.WorldProgress,
    description = Res.string.objective_final_3,
  ),
  ReachFinal5(
    checkpoint = (DriveFormProgress.Final5),
    checkLocation = CheckLocation(CheckLocationCategory.FinalLevel, 5),
    type = Type.WorldProgress,
    description = Res.string.objective_final_5,
    difficulty = Difficulty.Middle,
  ),
  ReachFinal7(
    checkpoint = (DriveFormProgress.Final7),
    checkLocation = CheckLocation(CheckLocationCategory.FinalLevel, 7),
    type = Type.WorldProgress,
    description = Res.string.objective_final_7,
    difficulty = Difficulty.Latest,
  ),

  // Puzzle
  CompleteAwakeningPuzzle(
    checkpoint = (CreationsProgress.AwakeningPuzzle),
    checkLocation = CheckLocation(CheckLocationCategory.Creation, 0),
    type = Type.WorldProgress,
    description = Res.string.objective_puzzle_awakening,
    difficulty = Difficulty.Middle,
  ),
  CompleteHeartPuzzle(
    checkpoint = (CreationsProgress.HeartPuzzle),
    checkLocation = CheckLocation(CheckLocationCategory.Creation, 1),
    type = Type.WorldProgress,
    description = Res.string.objective_puzzle_heart,
    difficulty = Difficulty.Middle,
  ),
  CompleteDualityPuzzle(
    checkpoint = (CreationsProgress.DualityPuzzle),
    checkLocation = CheckLocation(CheckLocationCategory.Creation, 2),
    type = Type.WorldProgress,
    description = Res.string.objective_puzzle_duality,
    difficulty = Difficulty.Late,
  ),
  CompleteFrontierPuzzle(
    checkpoint = (CreationsProgress.FrontierPuzzle),
    checkLocation = CheckLocation(CheckLocationCategory.Creation, 3),
    type = Type.WorldProgress,
    description = Res.string.objective_puzzle_frontier,
    difficulty = Difficulty.Late,
  ),
  CompleteDaylightPuzzle(
    checkpoint = (CreationsProgress.DaylightPuzzle),
    checkLocation = CheckLocation(CheckLocationCategory.Creation, 4),
    type = Type.WorldProgress,
    description = Res.string.objective_puzzle_daylight,
    difficulty = Difficulty.Late,
  ),
  CompleteSunsetPuzzle(
    checkpoint = (CreationsProgress.SunsetPuzzle),
    checkLocation = CheckLocation(CheckLocationCategory.Creation, 5),
    type = Type.WorldProgress,
    description = Res.string.objective_puzzle_sunset,
    difficulty = Difficulty.Late,
  ),

  // Superbosses
  DefeatSephiroth(
    checkpoint = (HollowBastionProgress.Sephiroth),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 35),
    type = Type.Boss,
    description = Res.string.objective_hb_sephiroth,
    difficulty = Difficulty.Latest,
  ),
  DefeatLingeringWill(
    checkpoint = (DisneyCastleProgress.LingeringWill),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 70),
    type = Type.Boss,
    description = Res.string.objective_dc_lingering_will,
    difficulty = Difficulty.Latest,
  ),
  DefeatDataRoxas(
    checkpoint = (SimulatedTwilightTownProgress.DataRoxas),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 558),
    type = Type.Boss,
    description = Res.string.objective_stt_data_roxas,
    difficulty = Difficulty.Latest,
  ),
  DefeatDataDemyx(
    checkpoint = (HollowBastionProgress.DataDemyx),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 560),
    type = Type.Boss,
    description = Res.string.objective_hb_data_demyx,
    difficulty = Difficulty.Latest,
  ),
  DefeatDataXigbar(
    checkpoint = (LandOfDragonsProgress.DataXigbar),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 555),
    type = Type.Boss,
    description = Res.string.objective_lod_data_xigbar,
    difficulty = Difficulty.Latest,
  ),
  DefeatDataSaix(
    checkpoint = (PrideLandsProgress.DataSaix),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 556),
    type = Type.Boss,
    description = Res.string.objective_pl_data_saix,
    difficulty = Difficulty.Latest,
  ),
  DefeatDataAxel(
    checkpoint = (TwilightTownProgress.DataAxel),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 561),
    type = Type.Boss,
    description = Res.string.objective_tt_data_axel,
    difficulty = Difficulty.Latest,
  ),
  DefeatDataXaldin(
    checkpoint = (BeastsCastleProgress.DataXaldin),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 559),
    type = Type.Boss,
    description = Res.string.objective_bc_data_xaldin,
    difficulty = Difficulty.Latest,
  ),
  DefeatDataLuxord(
    checkpoint = (PortRoyalProgress.DataLuxord),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 557),
    type = Type.Boss,
    description = Res.string.objective_pr_data_luxord,
    difficulty = Difficulty.Latest,
  ),
  DefeatDataXemnas(
    checkpoint = (WorldThatNeverWasProgress.DataXemnas),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 554),
    type = Type.Boss,
    description = Res.string.objective_twtnw_data_xemnas,
    difficulty = Difficulty.Latest,
  ),
  DefeatDataZexion(
    checkpoint = (OlympusColiseumProgress.ZexionData),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 551),
    type = Type.Boss,
    description = Res.string.objective_oc_data_zexion,
    difficulty = Difficulty.Latest,
  ),
  DefeatDataVexen(
    checkpoint = (HalloweenTownProgress.VexenData),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 549),
    type = Type.Boss,
    description = Res.string.objective_ht_data_vexen,
    difficulty = Difficulty.Latest,
  ),
  DefeatDataLarxene(
    checkpoint = (SpaceParanoidsProgress.LarxeneData),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 552),
    type = Type.Boss,
    description = Res.string.objective_sp_data_larxene,
    difficulty = Difficulty.Latest,
  ),
  DefeatDataLexaeus(
    checkpoint = (AgrabahProgress.LexaeusData),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 550),
    type = Type.Boss,
    description = Res.string.objective_ag_data_lexaeus,
    difficulty = Difficulty.Latest,
  ),
  DefeatDataMarluxia(
    checkpoint = (DisneyCastleProgress.MarluxiaData),
    checkLocation = CheckLocation(CheckLocationCategory.Popup, 553),
    type = Type.Boss,
    description = Res.string.objective_dc_data_marluxia,
    difficulty = Difficulty.Latest,
  ),

  // AS
  DefeatASZexion(
    checkpoint = (OlympusColiseumProgress.Zexion),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 66),
    type = Type.Boss,
    description = Res.string.objective_oc_as_zexion,
    difficulty = Difficulty.Latest,
  ),
  DefeatASVexen(
    checkpoint = (HalloweenTownProgress.Vexen),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 64),
    type = Type.Boss,
    description = Res.string.objective_ht_as_vexen,
    difficulty = Difficulty.Latest,
  ),
  DefeatASLarxene(
    checkpoint = (SpaceParanoidsProgress.Larxene),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 68),
    type = Type.Boss,
    description = Res.string.objective_sp_as_larxene,
    difficulty = Difficulty.Latest,
  ),
  DefeatASLexaeus(
    checkpoint = (AgrabahProgress.Lexaeus),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 65),
    type = Type.Boss,
    description = Res.string.objective_ag_as_lexaeus,
    difficulty = Difficulty.Latest,
  ),
  DefeatASMarluxia(
    checkpoint = (DisneyCastleProgress.Marluxia),
    checkLocation = CheckLocation(CheckLocationCategory.StatBonus, 67),
    type = Type.Boss,
    description = Res.string.objective_dc_as_marluxia,
    difficulty = Difficulty.Latest,
  );

  override val defaultIcon: DrawableResource
    get() = checkpoint.defaultIcon

  override val defaultIconTint: Color
    get() = checkpoint.defaultIconTint

  override val customIconPath: List<String>
    get() = checkpoint.customIconPath

  override val customIconIdentifier: String
    get() = checkpoint.customIconIdentifier

  enum class Type {

    Boss, WorldProgress, Fight

  }

  enum class Difficulty {

    Early, Middle, Late, Latest

  }

}
