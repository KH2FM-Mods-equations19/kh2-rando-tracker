package com.kh2rando.tracker.model.item

import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.ability_once_more
import com.kh2rando.tracker.generated.resources.ability_second_chance
import com.kh2rando.tracker.model.ColorToken
import com.kh2rando.tracker.model.GameId
import org.jetbrains.compose.resources.DrawableResource

/**
 * Abilities considered important enough to track.
 */
enum class ImportantAbility(override val gameId: GameId) : ItemPrototype {

  OnceMore(gameId = GameId(416)) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.ability_once_more

    override val customIconIdentifier: String
      get() = "once_more"

    override val colorToken: ColorToken
      get() = ColorToken.LightBlue
  },

  SecondChance(gameId = GameId(415)) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.ability_second_chance

    override val customIconIdentifier: String
      get() = "second_chance"

    override val colorToken: ColorToken
      get() = ColorToken.Green
  },

}
