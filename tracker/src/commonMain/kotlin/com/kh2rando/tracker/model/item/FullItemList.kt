package com.kh2rando.tracker.model.item

object FullItemList {

  /**
   * The list of all available [ItemPrototype]s, in their respective quantities.
   */
  val fullList: List<ItemPrototype> = buildList {
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
  }

}
