package com.kh2rando.tracker.ui

import com.kh2rando.tracker.model.Location
import com.kh2rando.tracker.model.progress.DisneyCastleProgress
import com.kh2rando.tracker.model.progress.DisneyCastleProgress.LingeringWill
import com.kh2rando.tracker.model.progress.DisneyCastleProgress.MarluxiaData
import com.kh2rando.tracker.model.progress.HollowBastionProgress
import com.kh2rando.tracker.model.progress.HollowBastionProgress.DataDemyx
import com.kh2rando.tracker.model.progress.HollowBastionProgress.Sephiroth
import kotlin.test.Test
import kotlin.test.assertEquals

class ResolvedProgressTest {

  @Test
  fun `disney castle`() {
    val standardProgress = listOf(
      DisneyCastleProgress.Chests,
      DisneyCastleProgress.Minnie,
      DisneyCastleProgress.OldPete,
      DisneyCastleProgress.Windows,
      DisneyCastleProgress.BoatPete,
      DisneyCastleProgress.DCPete,
    )
    repeat(standardProgress.size) { index ->
      val completed = standardProgress.subList(0, index)
      assertEquals(
        expected = completed.lastOrNull()?.progressIndicator(),
        actual = Location.DisneyCastle.resolveProgressToDisplay(completed.toSet())
      )
    }

    val marluxia = standardProgress.toSet() + DisneyCastleProgress.Marluxia
    assertEquals(
      expected = DisneyCastleProgress.Marluxia.progressIndicator(),
      actual = Location.DisneyCastle.resolveProgressToDisplay(marluxia)
    )

    assertEquals(
      expected = MarluxiaData.progressIndicator(),
      actual = Location.DisneyCastle.resolveProgressToDisplay(marluxia + MarluxiaData)
    )

    val terra = standardProgress.toSet() + LingeringWill
    assertEquals(
      expected = LingeringWill.progressIndicator(),
      actual = Location.DisneyCastle.resolveProgressToDisplay(terra)
    )

    assertEquals(
      expected = SyntheticProgress.Marluxia_LingeringWill.progressIndicator(),
      actual = Location.DisneyCastle.resolveProgressToDisplay(marluxia + LingeringWill)
    )

    assertEquals(
      expected = SyntheticProgress.MarluxiaData_LingeringWill.progressIndicator(),
      actual = Location.DisneyCastle.resolveProgressToDisplay(marluxia + MarluxiaData + LingeringWill)
    )
  }

  @Test
  fun `hollow bastion doubles`() {
    val standardProgress = listOf(
      HollowBastionProgress.Chests,
      HollowBastionProgress.Bailey,
      HollowBastionProgress.AnsemStudy,
      HollowBastionProgress.Corridor,
      HollowBastionProgress.Dancers,
      HollowBastionProgress.HBDemyx,
      HollowBastionProgress.FinalFantasy,
      HollowBastionProgress.ThousandHeartless,
    )
    repeat(standardProgress.size) { index ->
      val completed = standardProgress.subList(0, index)
      assertEquals(
        expected = completed.lastOrNull()?.progressIndicator(),
        actual = Location.HollowBastion.resolveProgressToDisplay(completed.toSet())
      )
    }

    val sephiroth = standardProgress.toSet() + Sephiroth
    assertEquals(
      expected = Sephiroth.progressIndicator(),
      actual = Location.HollowBastion.resolveProgressToDisplay(sephiroth)
    )

    val dataDemyx = standardProgress.toSet() + DataDemyx
    assertEquals(
      expected = DataDemyx.progressIndicator(),
      actual = Location.HollowBastion.resolveProgressToDisplay(dataDemyx)
    )

    assertEquals(
      expected = SyntheticProgress.SephiDemyx.progressIndicator(),
      actual = Location.HollowBastion.resolveProgressToDisplay(sephiroth + DataDemyx)
    )
  }

}
