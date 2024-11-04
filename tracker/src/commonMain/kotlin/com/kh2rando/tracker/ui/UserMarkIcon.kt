package com.kh2rando.tracker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.extended_misc_proof_information_none
import com.kh2rando.tracker.generated.resources.proof_of_connection
import com.kh2rando.tracker.generated.resources.proof_of_nonexistence
import com.kh2rando.tracker.generated.resources.proof_of_peace
import com.kh2rando.tracker.generated.resources.system_crossworld
import com.kh2rando.tracker.generated.resources.user_mark_connection
import com.kh2rando.tracker.generated.resources.user_mark_nonexistence
import com.kh2rando.tracker.generated.resources.user_mark_peace
import com.kh2rando.tracker.model.HasCustomizableIcon
import com.kh2rando.tracker.model.item.Proof
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.imageResource

/**
 * Icons that can be cycled by the user to mark each location.
 */
enum class UserMarkIcon {

  None {
    @Composable
    override fun Content(modifier: Modifier) {

    }
  },

  Connection {
    @Composable
    override fun Content(modifier: Modifier) {
      Image(
        imageResource(Res.drawable.user_mark_connection),
        contentDescription = null,
        alpha = ALPHA
      )
    }
  },

  Nonexistence {
    @Composable
    override fun Content(modifier: Modifier) {
      Image(
        imageResource(Res.drawable.user_mark_nonexistence),
        contentDescription = null,
        alpha = ALPHA
      )
    }
  },

  Peace {
    @Composable
    override fun Content(modifier: Modifier) {
      Image(
        imageResource(Res.drawable.user_mark_peace),
        contentDescription = null,
        alpha = ALPHA
      )
    }
  },

  Question {
    @Composable
    override fun Content(modifier: Modifier) {
      BoxWithConstraints(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        OutlinedText(
          "?",
          color = Color.White.copy(alpha = ALPHA),
          outlineColor = Color.Black.copy(alpha = ALPHA),
          style = MaterialTheme.typography.headlineMedium.shrinkableToFitHeight(maxHeight),
          fontFamily = khMenuFontFamily(),
        )
      }
    }
  },

  CrossWorld {
    @Composable
    override fun Content(modifier: Modifier) {
      Image(
        imageResource(Res.drawable.system_crossworld),
        contentDescription = null,
        alpha = ALPHA
      )
    }
  };

  @Composable
  abstract fun Content(modifier: Modifier)

  companion object {

    private const val ALPHA = 0.8f

  }

}

enum class UserProofMark(val displayString: StringResource) : HasCustomizableIcon {

  NoProofs(displayString = Res.string.extended_misc_proof_information_none) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.system_crossworld
    override val defaultIconTint: Color
      get() = Color.Unspecified
    override val customIconPath: List<String>
      get() = SystemIcon.Prohibition.customIconPath
    override val customIconIdentifier: String
      get() = SystemIcon.Prohibition.customIconIdentifier
  },

  Connection(displayString = Res.string.proof_of_connection) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.user_mark_connection
    override val customIconPath: List<String>
      get() = Proof.ProofOfConnection.customIconPath
    override val customIconIdentifier: String
      get() = Proof.ProofOfConnection.customIconIdentifier
  },

  Nonexistence(displayString = Res.string.proof_of_nonexistence) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.user_mark_nonexistence
    override val customIconPath: List<String>
      get() = Proof.ProofOfNonexistence.customIconPath
    override val customIconIdentifier: String
      get() = Proof.ProofOfNonexistence.customIconIdentifier
  },

  Peace(displayString = Res.string.proof_of_peace) {
    override val defaultIcon: DrawableResource
      get() = Res.drawable.user_mark_peace
    override val customIconPath: List<String>
      get() = Proof.ProofOfPeace.customIconPath
    override val customIconIdentifier: String
      get() = Proof.ProofOfPeace.customIconIdentifier
  },

}
