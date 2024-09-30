package com.kh2rando.tracker.model.hints

import com.kh2rando.tracker.model.item.AnsemReport
import com.kh2rando.tracker.model.item.DriveForm
import com.kh2rando.tracker.model.item.HadesCupTrophy
import com.kh2rando.tracker.model.item.ImportantAbility
import com.kh2rando.tracker.model.item.ItemPrototype
import com.kh2rando.tracker.model.item.Magic
import com.kh2rando.tracker.model.item.MunnyPouch
import com.kh2rando.tracker.model.item.PromiseCharm
import com.kh2rando.tracker.model.item.Proof
import com.kh2rando.tracker.model.item.SummonCharm
import com.kh2rando.tracker.model.item.UniqueItem
import com.kh2rando.tracker.model.item.VisitUnlock
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CheckAcquiredAllItemsTest {

  @Test
  fun tooManyForEmpty() {
    val result = checkAcquiredAllItems(
      acquiredItems = acquiredAll(PromiseCharm),
      allLocationItems = emptyList()
    )
    assertFalse(result)
  }

  @Test
  fun emptyIsNotEnough() {
    val result = checkAcquiredAllItems(
      acquiredItems = emptySet(),
      allLocationItems = listOf(HadesCupTrophy)
    )
    assertFalse(result)
  }

  @Test
  fun emptyIsCorrect() {
    val result = checkAcquiredAllItems(
      acquiredItems = emptySet(),
      allLocationItems = emptyList()
    )
    assertTrue(result)
  }

  @Test
  fun notEnoughCopiesForGroup() {
    val result = checkAcquiredAllItems(
      acquiredItems = acquiredAll(Magic.Fire),
      allLocationItems = listOf(Magic.Fire, Magic.Fire)
    )
    assertFalse(result)
  }

  @Test
  fun tooManyCopiesForGroup() {
    val result = checkAcquiredAllItems(
      acquiredItems = acquiredAll(Magic.Fire, Magic.Fire),
      allLocationItems = listOf(Magic.Fire)
    )
    assertFalse(result)
  }

  @Test
  fun correctCopiesForGroup() {
    val result = checkAcquiredAllItems(
      acquiredItems = acquiredAll(Magic.Fire, Magic.Fire),
      allLocationItems = listOf(Magic.Fire, Magic.Fire)
    )
    assertTrue(result)
  }

  @Test
  fun correctSizeButNotCorrectItem() {
    val result = checkAcquiredAllItems(
      acquiredItems = acquiredAll(Magic.Fire, Magic.Cure),
      allLocationItems = listOf(Magic.Fire, Magic.Fire)
    )
    assertFalse(result)
  }

  @Test
  fun correctTypeButIncorrectInstance() {
    val result = checkAcquiredAllItems(
      acquiredItems = acquiredAll(AnsemReport.Report4),
      allLocationItems = listOf(AnsemReport.Report7)
    )
    assertFalse(result)
  }

  @Test
  fun correctTypeAndInstance() {
    val result = checkAcquiredAllItems(
      acquiredItems = acquiredAll(AnsemReport.Report4),
      allLocationItems = listOf(AnsemReport.Report4)
    )
    assertTrue(result)
  }

  @Test
  fun practicalExample() {
    val result = checkAcquiredAllItems(
      acquiredItems = acquiredAll(
        ImportantAbility.OnceMore,
        DriveForm.MasterForm,
        AnsemReport.Report7,
        SummonCharm.FeatherCharm,
        AnsemReport.Report2,
        MunnyPouch.Mickey,
        Proof.ProofOfNonexistence,
        VisitUnlock.MembershipCard,
        Magic.Thunder,
        AnsemReport.Report12,
        VisitUnlock.SkillAndCrossbones,
      ),
      allLocationItems = listOf(
        Proof.ProofOfNonexistence,
        ImportantAbility.OnceMore,
        MunnyPouch.Mickey,
        VisitUnlock.SkillAndCrossbones,
        SummonCharm.FeatherCharm,
        AnsemReport.Report7,
        VisitUnlock.MembershipCard,
        AnsemReport.Report12,
        DriveForm.MasterForm,
        Magic.Thunder,
        AnsemReport.Report2
      )
    )
    assertTrue(result)
  }

  private fun acquiredAll(vararg prototypes: ItemPrototype): Set<UniqueItem> {
    return prototypes.mapTo(mutableSetOf()) { UniqueItem(it) }
  }

}
