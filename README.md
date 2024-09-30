# kh2-rando-tracker

**Not for use in tournament races at this time!**

Tracker for Kingdom Hearts II Randomizer. Based on, and attempts to match most of the main features
of, the [KH2Tracker](https://github.com/Dee-Ayy/KH2Tracker).

## Supported

- Hint Systems
    - JSmartee
    - Shananas
    - Points
    - Path
    - Spoiler
    - Progression variants of all of the above
        - (World Complete Bonus is not available at this time due to complexity)
- Manual Tracking
    - Drag-and-drop or select a location and double-click items
    - "Three strikes" for Ansem Reports before you can no longer attempt to place them
- Auto Tracking
    - World progress and progression points
    - Items obtained in game locations
    - Objectives completed
        - (Puzzles is not yet supported)
    - Lucky Emblems obtained
    - Sora level, strength, magic, defense
    - Drive Form levels, growth ability levels, death counter
    - Extended information window
        - Next level check indicator, current and maximum drive gauge, munny
        - Currently playing song information
            - If using randomized music from the seed generator, displays the replacement song
              name (and
              optionally a song group)
- Tracker Progress saving
    - Manually
    - Automatically on a timer
- Customizable icons for most symbols
- Displaying the seed hash when a seed is loaded
- Displaying the journal ability hints for Ansem Reports
- Manually scroll-wheeling through a predefined set of mark icons for each location
- Extended information window
    - Mark each location as having no proofs, or any combination of the three
- Game Versions
    - Epic Games Global 1.0.0.9
    - Epic Games Global 1.0.0.10 (untested)
    - Steam Global 1.0.0.9 (untested)
    - Steam Global 1.0.0.10 (untested)
    - Steam JP 1.0.0.9 (untested)

## Planned

- Customization
    - Colors for certain components
    - Fonts?
- Automatic connect/reconnect for auto-tracking
- Custom icons for location scroll-wheel marking
- Icons and/or colors for marking objectives

## Not planned at this time (may reconsider at some point)

- Randomized bosses/enemies hinting
- Explicit Reverse Rando support
- Auto tracking for puzzle objective completion
- Battleship
- Bingo
- World Complete Bonus support for progression hints (depends on complexity)
- Custom tracker background image
- Emulator version support

## Changelog

### 1.9.0

Initial public (re)release.

## Acknowledgements

- [Dee-Ayy](https://github.com/Dee-Ayy), [Red-Buddha](https://github.com/Red-Buddha),
  [tommadness](https://github.com/tommadness), and all other contributors of the initial tracker
  implementation
- [Televo](https://github.com/Televo/) for many of the assets (icons, fonts, etc.)
- Everyone in the KH2 Rando community and OpenKH community that has helped with modding the game

## Open Source Licenses

- [KH2Tracker](https://github.com/Dee-Ayy/KH2Tracker/blob/v2.64/LICENSE)
  - Copyright (c) 2020 TrevorLuckey
- [androidx](https://github.com/androidx/androidx/blob/androidx-main/LICENSE.txt)
  - Copyright (C) 2017-2024 The Android Open Source Project
- [coil](https://github.com/coil-kt/coil/blob/3.0.0-rc02/LICENSE.txt)
  - Copyright 2024 Coil Contributors
- [compose-multiplatform](https://github.com/JetBrains/compose-multiplatform/blob/v1.7.0/LICENSE.txt)
  - Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
- [java-native-access](https://github.com/java-native-access/jna/blob/5.15.0/AL2.0)
  - Copyright (c) 2007-2015 JNA Contributors
- [kaml](https://github.com/charleskorn/kaml/blob/0.61.0/LICENSE)
  - Copyright 2018-2023 Charles Korn.
- [kotlin](https://github.com/JetBrains/kotlin/blob/v2.0.21/license/README.md)
  - Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
- [kotlinx.collections.immutable](https://github.com/Kotlin/kotlinx.collections.immutable/blob/v0.3.8/LICENSE.txt)
  - Copyright 2016-2024 JetBrains s.r.o. and contributors
- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines/blob/1.8.1/LICENSE.txt)
  - Copyright 2016-2024 JetBrains s.r.o. and contributors
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization/blob/v1.7.1/LICENSE.txt)
  - Copyright 2017-2019 JetBrains s.r.o. and respective authors and developers
- [okio](https://github.com/square/okio/blob/3.9.1/LICENSE.txt)
  - Copyright 2013 Square, Inc.
