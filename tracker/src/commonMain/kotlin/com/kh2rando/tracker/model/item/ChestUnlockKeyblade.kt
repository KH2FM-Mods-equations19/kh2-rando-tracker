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
  val associatedLocation: Location,
  /**
   * Offset of this item's inventory address from the "save" location.
   */
  private val inventorySaveOffset: Int,
) : ItemPrototype, RepeatableInventory {

  Oathkeeper(gameId = GameId(42), associatedLocation = Location.TwilightTown, inventorySaveOffset = 0x35A2) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_tt
    override val customIconIdentifier: String
      get() = "chest_tt"
  },
  BondOfFlame(gameId = GameId(498), associatedLocation = Location.SimulatedTwilightTown, inventorySaveOffset = 0x368D) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_stt
    override val customIconIdentifier: String
      get() = "chest_stt"
  },
  SleepingLion(gameId = GameId(494), associatedLocation = Location.HollowBastion, inventorySaveOffset = 0x3689) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_hb
    override val customIconIdentifier: String
      get() = "chest_hb"
  },
  WinnersProof(gameId = GameId(544), associatedLocation = Location.GardenOfAssemblage, inventorySaveOffset = 0x3699) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_cor
    override val customIconIdentifier: String
      get() = "chest_cor"
    override val colorToken: ColorToken
      get() = ColorToken.Purple
  },
  WishingLamp(gameId = GameId(492), associatedLocation = Location.Agrabah, inventorySaveOffset = 0x3687) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_ag
    override val customIconIdentifier: String
      get() = "chest_ag"
  },
  RumblingRose(gameId = GameId(490), associatedLocation = Location.BeastsCastle, inventorySaveOffset = 0x3685) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_bc
    override val customIconIdentifier: String
      get() = "chest_bc"
  },
  Monochrome(gameId = GameId(485), associatedLocation = Location.DisneyCastle, inventorySaveOffset = 0x3680) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_dc
    override val customIconIdentifier: String
      get() = "chest_dc"
  },
  DecisivePumpkin(gameId = GameId(493), associatedLocation = Location.HalloweenTown, inventorySaveOffset = 0x3688) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_ht
    override val customIconIdentifier: String
      get() = "chest_ht"
  },
  HiddenDragon(gameId = GameId(481), associatedLocation = Location.LandOfDragons, inventorySaveOffset = 0x367C) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_lod
    override val customIconIdentifier: String
      get() = "chest_lod"
  },
  HerosCrest(gameId = GameId(484), associatedLocation = Location.OlympusColiseum, inventorySaveOffset = 0x367F) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_oc
    override val customIconIdentifier: String
      get() = "chest_oc"
  },
  CircleOfLife(gameId = GameId(487), associatedLocation = Location.PrideLands, inventorySaveOffset = 0x3682) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_pl
    override val customIconIdentifier: String
      get() = "chest_pl"
  },
  FollowTheWind(gameId = GameId(486), associatedLocation = Location.PortRoyal, inventorySaveOffset = 0x3681) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_pr
    override val customIconIdentifier: String
      get() = "chest_pr"
  },
  PhotonDebugger(gameId = GameId(488), associatedLocation = Location.SpaceParanoids, inventorySaveOffset = 0x3683) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_sp
    override val customIconIdentifier: String
      get() = "chest_sp"
  },
  TwoBecomeOne(gameId = GameId(543), associatedLocation = Location.WorldThatNeverWas, inventorySaveOffset = 0x3698) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_twtnw
    override val customIconIdentifier: String
      get() = "chest_twtnw"
  },
  SweetMemories(gameId = GameId(495), associatedLocation = Location.HundredAcreWood, inventorySaveOffset = 0x368A) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.chest_haw
    override val customIconIdentifier: String
      get() = "chest_haw"
  };

  override val colorToken: ColorToken
    get() = associatedLocation.colorToken

  override fun inventoryCountAddress(addresses: GameAddresses): Address {
    return addresses.save + inventorySaveOffset
  }

}
