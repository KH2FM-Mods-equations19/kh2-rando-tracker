package com.kh2rando.tracker.ui

import androidx.compose.runtime.Composable
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.ansem_report_n
import com.kh2rando.tracker.generated.resources.anti_form
import com.kh2rando.tracker.generated.resources.baseball_charm
import com.kh2rando.tracker.generated.resources.battlefields_of_war
import com.kh2rando.tracker.generated.resources.beasts_claw
import com.kh2rando.tracker.generated.resources.blizzard
import com.kh2rando.tracker.generated.resources.bond_of_flame
import com.kh2rando.tracker.generated.resources.bone_fist
import com.kh2rando.tracker.generated.resources.circle_of_life
import com.kh2rando.tracker.generated.resources.cure
import com.kh2rando.tracker.generated.resources.decisive_pumpkin
import com.kh2rando.tracker.generated.resources.feather_charm
import com.kh2rando.tracker.generated.resources.final_form
import com.kh2rando.tracker.generated.resources.fire
import com.kh2rando.tracker.generated.resources.follow_the_wind
import com.kh2rando.tracker.generated.resources.hades_cup_trophy
import com.kh2rando.tracker.generated.resources.heros_crest
import com.kh2rando.tracker.generated.resources.hidden_dragon
import com.kh2rando.tracker.generated.resources.hint_item_path_all
import com.kh2rando.tracker.generated.resources.hint_item_path_connection
import com.kh2rando.tracker.generated.resources.hint_item_path_connection_nonexistence
import com.kh2rando.tracker.generated.resources.hint_item_path_connection_peace
import com.kh2rando.tracker.generated.resources.hint_item_path_none
import com.kh2rando.tracker.generated.resources.hint_item_path_nonexistence
import com.kh2rando.tracker.generated.resources.hint_item_path_nonexistence_peace
import com.kh2rando.tracker.generated.resources.hint_item_path_peace
import com.kh2rando.tracker.generated.resources.ice_cream
import com.kh2rando.tracker.generated.resources.identity_disk
import com.kh2rando.tracker.generated.resources.lamp_charm
import com.kh2rando.tracker.generated.resources.limit_form
import com.kh2rando.tracker.generated.resources.magnet
import com.kh2rando.tracker.generated.resources.master_form
import com.kh2rando.tracker.generated.resources.membership_card
import com.kh2rando.tracker.generated.resources.monochrome
import com.kh2rando.tracker.generated.resources.munny_pouch
import com.kh2rando.tracker.generated.resources.namines_sketches
import com.kh2rando.tracker.generated.resources.oathkeeper
import com.kh2rando.tracker.generated.resources.olympus_stone
import com.kh2rando.tracker.generated.resources.once_more
import com.kh2rando.tracker.generated.resources.photon_debugger
import com.kh2rando.tracker.generated.resources.promise_charm
import com.kh2rando.tracker.generated.resources.proof_of_connection
import com.kh2rando.tracker.generated.resources.proof_of_nonexistence
import com.kh2rando.tracker.generated.resources.proof_of_peace
import com.kh2rando.tracker.generated.resources.proud_fang
import com.kh2rando.tracker.generated.resources.reflect
import com.kh2rando.tracker.generated.resources.royal_summons
import com.kh2rando.tracker.generated.resources.rumbling_rose
import com.kh2rando.tracker.generated.resources.scimitar
import com.kh2rando.tracker.generated.resources.second_chance
import com.kh2rando.tracker.generated.resources.skill_and_crossbones
import com.kh2rando.tracker.generated.resources.sleeping_lion
import com.kh2rando.tracker.generated.resources.sweet_memories
import com.kh2rando.tracker.generated.resources.sword_of_the_ancestor
import com.kh2rando.tracker.generated.resources.thunder
import com.kh2rando.tracker.generated.resources.torn_pages
import com.kh2rando.tracker.generated.resources.two_become_one
import com.kh2rando.tracker.generated.resources.ukulele_charm
import com.kh2rando.tracker.generated.resources.unknown_disk
import com.kh2rando.tracker.generated.resources.valor_form
import com.kh2rando.tracker.generated.resources.way_to_the_dawn
import com.kh2rando.tracker.generated.resources.winners_proof
import com.kh2rando.tracker.generated.resources.wisdom_form
import com.kh2rando.tracker.generated.resources.wishing_lamp
import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.hints.HintInfo
import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.ChestUnlockKeyblade
import com.kh2rando.tracker.model.item.DriveForm
import com.kh2rando.tracker.model.item.HadesCupTrophy
import com.kh2rando.tracker.model.item.ImportantAbility
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.Magic
import com.kh2rando.tracker.model.item.MunnyPouch
import com.kh2rando.tracker.model.item.OlympusStone
import com.kh2rando.tracker.model.item.PromiseCharm
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.item.RealForm
import com.kh2rando.tracker.model.item.SummonCharm
import com.kh2rando.tracker.model.item.TornPage
import com.kh2rando.tracker.model.item.UnknownDisk
import com.kh2rando.tracker.model.item.VisitUnlock
import org.jetbrains.compose.resources.stringResource

val Location.localizedName: String
  @Composable get() = stringResource(displayName)

val ItemPrototype.localizedName: String
  @Composable get() {
    return when (this) {
      is AnsemReport -> stringResource(Res.string.ansem_report_n, reportNumber)
      DriveForm.ValorFormDummy, RealForm.Valor -> stringResource(Res.string.valor_form)
      DriveForm.WisdomForm -> stringResource(Res.string.wisdom_form)
      DriveForm.LimitForm -> stringResource(Res.string.limit_form)
      DriveForm.MasterForm -> stringResource(Res.string.master_form)
      DriveForm.FinalFormDummy, RealForm.Final -> stringResource(Res.string.final_form)
      DriveForm.AntiForm -> stringResource(Res.string.anti_form)
      HadesCupTrophy -> stringResource(Res.string.hades_cup_trophy)
      ImportantAbility.SecondChance -> stringResource(Res.string.second_chance)
      ImportantAbility.OnceMore -> stringResource(Res.string.once_more)
      Magic.Fire -> stringResource(Res.string.fire)
      Magic.Blizzard -> stringResource(Res.string.blizzard)
      Magic.Thunder -> stringResource(Res.string.thunder)
      Magic.Cure -> stringResource(Res.string.cure)
      Magic.Magnet -> stringResource(Res.string.magnet)
      Magic.Reflect -> stringResource(Res.string.reflect)
      is MunnyPouch -> stringResource(Res.string.munny_pouch)
      OlympusStone -> stringResource(Res.string.olympus_stone)
      PromiseCharm -> stringResource(Res.string.promise_charm)
      Proof.ProofOfConnection -> stringResource(Res.string.proof_of_connection)
      Proof.ProofOfNonexistence -> stringResource(Res.string.proof_of_nonexistence)
      Proof.ProofOfPeace -> stringResource(Res.string.proof_of_peace)
      SummonCharm.BaseballCharm -> stringResource(Res.string.baseball_charm)
      SummonCharm.LampCharm -> stringResource(Res.string.lamp_charm)
      SummonCharm.UkuleleCharm -> stringResource(Res.string.ukulele_charm)
      SummonCharm.FeatherCharm -> stringResource(Res.string.feather_charm)
      TornPage -> stringResource(Res.string.torn_pages)
      UnknownDisk -> stringResource(Res.string.unknown_disk)
      VisitUnlock.BattlefieldsOfWar -> stringResource(Res.string.battlefields_of_war)
      VisitUnlock.BeastsClaw -> stringResource(Res.string.beasts_claw)
      VisitUnlock.BoneFist -> stringResource(Res.string.bone_fist)
      VisitUnlock.IceCream -> stringResource(Res.string.ice_cream)
      VisitUnlock.IdentityDisk -> stringResource(Res.string.identity_disk)
      VisitUnlock.MembershipCard -> stringResource(Res.string.membership_card)
      VisitUnlock.NaminesSketches -> stringResource(Res.string.namines_sketches)
      VisitUnlock.ProudFang -> stringResource(Res.string.proud_fang)
      VisitUnlock.RoyalSummons -> stringResource(Res.string.royal_summons)
      VisitUnlock.Scimitar -> stringResource(Res.string.scimitar)
      VisitUnlock.SkillAndCrossbones -> stringResource(Res.string.skill_and_crossbones)
      VisitUnlock.SwordOfTheAncestor -> stringResource(Res.string.sword_of_the_ancestor)
      VisitUnlock.WayToTheDawn -> stringResource(Res.string.way_to_the_dawn)
      ChestUnlockKeyblade.Oathkeeper -> stringResource(Res.string.oathkeeper)
      ChestUnlockKeyblade.BondOfFlame -> stringResource(Res.string.bond_of_flame)
      ChestUnlockKeyblade.SleepingLion -> stringResource(Res.string.sleeping_lion)
      ChestUnlockKeyblade.WinnersProof -> stringResource(Res.string.winners_proof)
      ChestUnlockKeyblade.WishingLamp -> stringResource(Res.string.wishing_lamp)
      ChestUnlockKeyblade.RumblingRose -> stringResource(Res.string.rumbling_rose)
      ChestUnlockKeyblade.Monochrome -> stringResource(Res.string.monochrome)
      ChestUnlockKeyblade.DecisivePumpkin -> stringResource(Res.string.decisive_pumpkin)
      ChestUnlockKeyblade.HiddenDragon -> stringResource(Res.string.hidden_dragon)
      ChestUnlockKeyblade.HerosCrest -> stringResource(Res.string.heros_crest)
      ChestUnlockKeyblade.CircleOfLife -> stringResource(Res.string.circle_of_life)
      ChestUnlockKeyblade.FollowTheWind -> stringResource(Res.string.follow_the_wind)
      ChestUnlockKeyblade.PhotonDebugger -> stringResource(Res.string.photon_debugger)
      ChestUnlockKeyblade.TwoBecomeOne -> stringResource(Res.string.two_become_one)
      ChestUnlockKeyblade.SweetMemories -> stringResource(Res.string.sweet_memories)
    }
  }

@Composable
fun HintInfo.PathToProofs.proofPathText(location: Location): String {
  val locationDisplayName = stringResource(location.shortName)

  return when (proofs) {
    setOf(Proof.ProofOfConnection) -> {
      stringResource(Res.string.hint_item_path_connection, locationDisplayName)
    }

    setOf(Proof.ProofOfNonexistence) -> {
      stringResource(Res.string.hint_item_path_nonexistence, locationDisplayName)
    }

    setOf(Proof.ProofOfPeace) -> {
      stringResource(Res.string.hint_item_path_peace, locationDisplayName)
    }

    setOf(Proof.ProofOfConnection, Proof.ProofOfNonexistence) -> {
      stringResource(Res.string.hint_item_path_connection_nonexistence, locationDisplayName)
    }

    setOf(Proof.ProofOfConnection, Proof.ProofOfPeace) -> {
      stringResource(Res.string.hint_item_path_connection_peace, locationDisplayName)
    }

    setOf(Proof.ProofOfNonexistence, Proof.ProofOfPeace) -> {
      stringResource(Res.string.hint_item_path_nonexistence_peace, locationDisplayName)
    }

    setOf(Proof.ProofOfConnection, Proof.ProofOfNonexistence, Proof.ProofOfPeace) -> {
      stringResource(Res.string.hint_item_path_all, locationDisplayName)
    }

    else -> {
      stringResource(Res.string.hint_item_path_none, locationDisplayName)
    }
  }
}
