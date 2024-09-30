package com.kh2rando.tracker.model.item

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.chest_ag
import com.kh2rando.tracker.generated.resources.chest_bc
import com.kh2rando.tracker.generated.resources.chest_cor
import com.kh2rando.tracker.generated.resources.chest_dc
import com.kh2rando.tracker.generated.resources.chest_haw
import com.kh2rando.tracker.generated.resources.chest_hb
import com.kh2rando.tracker.generated.resources.chest_ht
import com.kh2rando.tracker.generated.resources.chest_lod
import com.kh2rando.tracker.generated.resources.chest_oc
import com.kh2rando.tracker.generated.resources.chest_pl
import com.kh2rando.tracker.generated.resources.chest_pr
import com.kh2rando.tracker.generated.resources.chest_sp
import com.kh2rando.tracker.generated.resources.chest_stt
import com.kh2rando.tracker.generated.resources.chest_tt
import com.kh2rando.tracker.generated.resources.chest_twtnw
import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.GameId
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource

/**
 * Keyblades that can be used to unlock chests in various locations.
 */
enum class ChestUnlockKeyblade(
  override val gameId: GameId,
  /**
   * Offset of this item's inventory address from the "save" location.
   */
  private val inventorySaveOffset: Int,
) : ItemPrototype, RepeatableInventory {

  Oathkeeper(gameId = GameId(42), inventorySaveOffset = 0x35A2) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_tt
    override val customIconIdentifier: String
      get() = "chest_tt"
    override val colorToken: ColorToken
      get() = Location.TwilightTown.colorToken
  },
  BondOfFlame(gameId = GameId(498), inventorySaveOffset = 0x368D) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_stt
    override val customIconIdentifier: String
      get() = "chest_stt"
    override val colorToken: ColorToken
      get() = Location.SimulatedTwilightTown.colorToken
  },
  SleepingLion(gameId = GameId(494), inventorySaveOffset = 0x3689) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_hb
    override val customIconIdentifier: String
      get() = "chest_hb"
    override val colorToken: ColorToken
      get() = Location.HollowBastion.colorToken
  },
  WinnersProof(gameId = GameId(544), inventorySaveOffset = 0x3699) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_cor
    override val customIconIdentifier: String
      get() = "chest_cor"
    override val colorToken: ColorToken
      get() = ColorToken.Purple
  },
  WishingLamp(gameId = GameId(492), inventorySaveOffset = 0x3687) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_ag
    override val customIconIdentifier: String
      get() = "chest_ag"
    override val colorToken: ColorToken
      get() = Location.Agrabah.colorToken
  },
  RumblingRose(gameId = GameId(490), inventorySaveOffset = 0x3685) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_bc
    override val customIconIdentifier: String
      get() = "chest_bc"
    override val colorToken: ColorToken
      get() = Location.BeastsCastle.colorToken
  },
  Monochrome(gameId = GameId(485), inventorySaveOffset = 0x3680) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_dc
    override val customIconIdentifier: String
      get() = "chest_dc"
    override val colorToken: ColorToken
      get() = Location.DisneyCastle.colorToken
  },
  DecisivePumpkin(gameId = GameId(493), inventorySaveOffset = 0x3688) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_ht
    override val customIconIdentifier: String
      get() = "chest_ht"
    override val colorToken: ColorToken
      get() = Location.HalloweenTown.colorToken
  },
  HiddenDragon(gameId = GameId(481), inventorySaveOffset = 0x367C) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_lod
    override val customIconIdentifier: String
      get() = "chest_lod"
    override val colorToken: ColorToken
      get() = Location.LandOfDragons.colorToken
  },
  HerosCrest(gameId = GameId(484), inventorySaveOffset = 0x367F) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_oc
    override val customIconIdentifier: String
      get() = "chest_oc"
    override val colorToken: ColorToken
      get() = Location.OlympusColiseum.colorToken
  },
  CircleOfLife(gameId = GameId(487), inventorySaveOffset = 0x3682) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_pl
    override val customIconIdentifier: String
      get() = "chest_pl"
    override val colorToken: ColorToken
      get() = Location.PrideLands.colorToken
  },
  FollowTheWind(gameId = GameId(486), inventorySaveOffset = 0x3681) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_pr
    override val customIconIdentifier: String
      get() = "chest_pr"
    override val colorToken: ColorToken
      get() = Location.PortRoyal.colorToken
  },
  PhotonDebugger(gameId = GameId(488), inventorySaveOffset = 0x3683) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_sp
    override val customIconIdentifier: String
      get() = "chest_sp"
    override val colorToken: ColorToken
      get() = Location.SpaceParanoids.colorToken
  },
  TwoBecomeOne(gameId = GameId(543), inventorySaveOffset = 0x3698) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_twtnw
    override val customIconIdentifier: String
      get() = "chest_twtnw"
    override val colorToken: ColorToken
      get() = Location.WorldThatNeverWas.colorToken
  },
  SweetMemories(gameId = GameId(495), inventorySaveOffset = 0x368A) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_haw
    override val customIconIdentifier: String
      get() = "chest_haw"
    override val colorToken: ColorToken
      get() = Location.HundredAcreWood.colorToken
  };

  abstract override val colorToken: ColorToken

  override fun inventoryCountAddress(addresses: GameAddresses): Address {
    return addresses.save + inventorySaveOffset
  }

}
