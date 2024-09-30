package com.kh2rando.tracker.model.item

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.proof_connection
import com.kh2rando.tracker.generated.resources.proof_nonexistence
import com.kh2rando.tracker.generated.resources.proof_peace
import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.GameId
import org.jetbrains.compose.resources.DrawableResource

/**
 * The Proof items.
 */
enum class Proof(override val gameId: GameId) : ItemPrototype, RepeatableInventory {

  ProofOfConnection(gameId = GameId(593)) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.proof_connection

    override val customIconIdentifier: String
      get() = "proof_connection"

    override fun inventoryCountAddress(addresses: GameAddresses): Address {
      return addresses.save + 0x36B2
    }
  },

  ProofOfNonexistence(gameId = GameId(594)) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.proof_nonexistence

    override val customIconIdentifier: String
      get() = "proof_nonexistence"

    override fun inventoryCountAddress(addresses: GameAddresses): Address {
      return addresses.save + 0x36B3
    }
  },

  ProofOfPeace(gameId = GameId(595)) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.proof_peace

    override val customIconIdentifier: String
      get() = "proof_peace"

    override fun inventoryCountAddress(addresses: GameAddresses): Address {
      return addresses.save + 0x36B4
    }
  };

  override val colorToken: ColorToken
    get() = ColorToken.White

}
