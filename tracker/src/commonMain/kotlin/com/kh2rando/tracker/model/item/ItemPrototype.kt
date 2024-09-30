package com.kh2rando.tracker.model.item

import androidx.compose.ui.graphics.Color
import com.kh2rando.tracker.model.Address
import com.kh2rando.tracker.model.GameAddresses
import com.kh2rando.tracker.model.GameId
import com.kh2rando.tracker.model.HasColorToken
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.HasGameId
import com.kh2rando.tracker.ui.color
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A prototype/category of game item.
 */
@Serializable(with = ItemPrototypeSerializer::class)
sealed interface ItemPrototype : HasGameId, HasCustomizableIcon, HasColorToken {

  override val defaultIconTint: Color
    get() = color

  override val customIconPath: List<String>
    get() = listOf("Checks")

  /**
   * Non-null if this item has a secondary [GameId] (such as the Drive Forms that have real and dummy items).
   */
  val secondaryGameId: GameId?
    get() = null

  companion object {

    /**
     * The list of all available [ItemPrototype]s, in their respective quantities.
     */
    val fullList: List<ItemPrototype> = run {
      val builder = mutableListOf<ItemPrototype>()

      fun add(prototype: ItemPrototype) {
        builder.add(prototype)
      }

      fun addAll(prototypes: Iterable<ItemPrototype>) {
        prototypes.forEach { add(it) }
      }

      addAll(AnsemReport.entries)
      for (magicPrototype in Magic.entries) {
        repeat(Magic.COPIES) { add(magicPrototype) }
      }
      repeat(TornPage.COPIES) { add(TornPage) }
      addAll(MunnyPouch.entries)
      addAll(DriveForm.entries)
      addAll(SummonCharm.entries)
      addAll(ImportantAbility.entries)
      addAll(Proof.entries)
      add(PromiseCharm)
      for (unlockPrototype in VisitUnlock.entries) {
        repeat(unlockPrototype.associatedLocation.visitCount) {
          add(unlockPrototype)
        }
      }
      add(HadesCupTrophy)
      add(OlympusStone)
      add(UnknownDisk)
      addAll(ChestUnlockKeyblade.entries)
      builder
    }

    /**
     * Returns true if [target] matches either [gameId] or [secondaryGameId].
     */
    fun ItemPrototype.checkGameIds(target: GameId): Boolean {
      return gameId == target || secondaryGameId == target
    }

  }

}

/**
 * An item that is represented in inventory by a count of items acquired.
 */
interface RepeatableInventory {

  /**
   * Computes the memory address where this item's inventory count can be found.
   */
  fun inventoryCountAddress(addresses: GameAddresses): Address

}

/**
 * An item that is represented in inventory as a single bit on/off.
 */
interface BitmaskedInventory {

  /**
   * The bitmask within this item's byte to use to determine if this item is acquired.
   */
  val inventoryBitmask: Int

  /**
   * Computes the memory address of this item's byte.
   */
  fun inventoryBitmaskAddress(addresses: GameAddresses): Address

}

/**
 * Represents an [ItemPrototype] using its [ItemPrototype.gameId].
 */
class ItemPrototypeSerializer : KSerializer<ItemPrototype> {

  private val allPrototypes: Set<ItemPrototype> = ItemPrototype.fullList.toSet()

  override val descriptor: SerialDescriptor
    get() = PrimitiveSerialDescriptor("ItemPrototype", PrimitiveKind.INT)

  override fun serialize(encoder: Encoder, value: ItemPrototype) {
    encoder.encodeInt(value.gameId.value)
  }

  override fun deserialize(decoder: Decoder): ItemPrototype {
    val gameId = GameId(decoder.decodeInt())
    return allPrototypes.first { it.gameId == gameId }
  }

}

/**
 * Returns a list comprised of [ItemPrototype]s in this list that are not contained in [acquiredItems].
 *
 * Useful, for example, to determine which items in a location have been revealed but not yet acquired.
 */
fun ImmutableList<ItemPrototype>.removeAcquired(acquiredItems: Iterable<UniqueItem>): ImmutableList<ItemPrototype> {
  return toPersistentList().mutate { builder ->
    for (acquiredItem in acquiredItems) {
      builder.remove(acquiredItem.prototype)
    }
  }
}
