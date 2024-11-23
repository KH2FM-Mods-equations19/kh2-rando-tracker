package com.kh2rando.tracker.model.item

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.lock_ancestorsword
import com.kh2rando.tracker.generated.resources.lock_battlefieldsofwar
import com.kh2rando.tracker.generated.resources.lock_beastclaw
import com.kh2rando.tracker.generated.resources.lock_bonefist
import com.kh2rando.tracker.generated.resources.lock_icecream
import com.kh2rando.tracker.generated.resources.lock_identitydisk
import com.kh2rando.tracker.generated.resources.lock_membershipcard
import com.kh2rando.tracker.generated.resources.lock_proudfang
import com.kh2rando.tracker.generated.resources.lock_royalsummons
import com.kh2rando.tracker.generated.resources.lock_scimitar
import com.kh2rando.tracker.generated.resources.lock_sketches
import com.kh2rando.tracker.generated.resources.lock_skillcrossbones
import com.kh2rando.tracker.generated.resources.lock_waytothedawn
import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.GameId
import com.kh2rando.tracker.model.Location
import org.jetbrains.compose.resources.DrawableResource

/**
 * Items that unlock visits.
 */
enum class VisitUnlock(
  override val gameId: GameId,
  /**
   * The location unlocked by this item.
   */
  val associatedLocation: Location,
  /**
   * Offset of this item's inventory address from the "save" location.
   */
  private val inventorySaveOffset: Int,
) : ItemPrototype, RepeatableInventory {

  BeastsClaw(
    gameId = GameId(59),
    associatedLocation = Location.BeastsCastle,
    inventorySaveOffset = 0x35B3
  ) {

    override val defaultIcon: DrawableResource
      get() = Res.drawable.lock_beastclaw

    override val customIconIdentifier: String
      get() = "lock_beastclaw"

  },

  BoneFist(
    gameId = GameId(60),
    associatedLocation = Location.HalloweenTown,
    inventorySaveOffset = 0x35B4
  ) {

    override val defaultIcon: DrawableResource
      get() = Res.drawable.lock_bonefist

    override val customIconIdentifier: String
      get() = "lock_bonefist"

  },

  ProudFang(
    gameId = GameId(61),
    associatedLocation = Location.PrideLands,
    inventorySaveOffset = 0x35B5
  ) {

    override val defaultIcon: DrawableResource
      get() = Res.drawable.lock_proudfang

    override val customIconIdentifier: String
      get() = "lock_proudfang"

  },

  BattlefieldsOfWar(
    gameId = GameId(54),
    associatedLocation = Location.OlympusColiseum,
    inventorySaveOffset = 0x35AE
  ) {

    override val defaultIcon: DrawableResource
      get() = Res.drawable.lock_battlefieldsofwar

    override val customIconIdentifier: String
      get() = "lock_battlefieldsofwar"

  },

  SwordOfTheAncestor(
    gameId = GameId(55),
    associatedLocation = Location.LandOfDragons,
    inventorySaveOffset = 0x35AF
  ) {

    override val defaultIcon: DrawableResource
      get() = Res.drawable.lock_ancestorsword

    override val customIconIdentifier: String
      get() = "lock_ancestorsword"

  },

  SkillAndCrossbones(
    gameId = GameId(62),
    associatedLocation = Location.PortRoyal,
    inventorySaveOffset = 0x35B6
  ) {

    override val defaultIcon: DrawableResource
      get() = Res.drawable.lock_skillcrossbones

    override val customIconIdentifier: String
      get() = "lock_skillcrossbones"

  },

  Scimitar(
    gameId = GameId(72),
    associatedLocation = Location.Agrabah,
    inventorySaveOffset = 0x35C0
  ) {

    override val defaultIcon: DrawableResource
      get() = Res.drawable.lock_scimitar

    override val customIconIdentifier: String
      get() = "lock_scimitar"

  },

  IdentityDisk(
    gameId = GameId(74),
    associatedLocation = Location.SpaceParanoids,
    inventorySaveOffset = 0x35C2
  ) {

    override val defaultIcon: DrawableResource
      get() = Res.drawable.lock_identitydisk

    override val customIconIdentifier: String
      get() = "lock_identitydisk"

  },

  WayToTheDawn(
    gameId = GameId(73),
    associatedLocation = Location.WorldThatNeverWas,
    inventorySaveOffset = 0x35C1
  ) {

    override val defaultIcon: DrawableResource
      get() = Res.drawable.lock_waytothedawn

    override val customIconIdentifier: String
      get() = "lock_waytothedawn"

  },

  MembershipCard(
    gameId = GameId(369),
    associatedLocation = Location.HollowBastion,
    inventorySaveOffset = 0x3643
  ) {

    override val defaultIcon: DrawableResource
      get() = Res.drawable.lock_membershipcard

    override val customIconIdentifier: String
      get() = "lock_membershipcard"

  },

  RoyalSummons(
    gameId = GameId(460),
    associatedLocation = Location.DisneyCastle,
    inventorySaveOffset = 0x365D
  ) {

    override val defaultIcon: DrawableResource
      get() = Res.drawable.lock_royalsummons

    override val customIconIdentifier: String
      get() = "lock_royalsummons"

  },

  IceCream(
    gameId = GameId(375),
    associatedLocation = Location.TwilightTown,
    inventorySaveOffset = 0x3649
  ) {

    override val defaultIcon: DrawableResource
      get() = Res.drawable.lock_icecream

    override val customIconIdentifier: String
      get() = "lock_icecream"

  },

  NaminesSketches(
    gameId = GameId(368),
    associatedLocation = Location.SimulatedTwilightTown,
    inventorySaveOffset = 0x3642
  ) {

    override val defaultIcon: DrawableResource
      get() = Res.drawable.lock_sketches

    override val customIconIdentifier: String
      get() = "lock_sketches"

  };

  override val colorToken: ColorToken
    get() = associatedLocation.colorToken

  override fun inventoryCountAddress(addresses: GameAddresses): Address {
    return addresses.save + inventorySaveOffset
  }

}
