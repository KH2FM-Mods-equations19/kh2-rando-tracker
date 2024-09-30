package com.kh2rando.tracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.kh2rando.tracker.generated.resources.Res
import com.kh2rando.tracker.generated.resources.about_acknowledgements
import com.kh2rando.tracker.generated.resources.about_licenses
import com.kh2rando.tracker.model.ColorToken
import org.jetbrains.compose.resources.stringResource

@Composable
fun AboutWindowContent(
  version: String,
  modifier: Modifier = Modifier,
) {
  Surface(modifier = modifier.fillMaxSize()) {
    val linkStyles = TextLinkStyles(style = SpanStyle(ColorToken.LightBlue.color))
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(2.dp),
      contentPadding = PaddingValues(2.dp),
      modifier = Modifier.padding(8.dp),
    ) {
      item {
        Text(buildAnnotatedString {
          append("KH2 Rando Tracker $version by ")
          withLink(LinkAnnotation.Url("https://github.com/KH2FM-Mods-equations19/kh2-rando-tracker", linkStyles)) {
            append("equations19")
          }
        })
      }

      item { Spacer(Modifier.height(16.dp)) }

      item {
        SmallHeader(stringResource(Res.string.about_acknowledgements))
      }

      item {
        Text(buildAnnotatedString {
          append("Thanks to ")
          withLink(LinkAnnotation.Url("https://github.com/Dee-Ayy", linkStyles)) { append("Dee-Ayy") }
          append(", ")
          withLink(LinkAnnotation.Url("https://github.com/Red-Buddha", linkStyles)) { append("Red-Buddha") }
          append(", ")
          withLink(LinkAnnotation.Url("https://github.com/tommadness", linkStyles)) { append("tommadness") }
          append(", and all other contributors of the initial tracker implementation.")
          append("\n\n")
          append("Thanks to ")
          withLink(LinkAnnotation.Url("https://github.com/Televo", linkStyles)) { append("Televo") }
          append(" for many of the assets (icons, fonts, etc.).")
          append("\n\n")
          append("Thanks to everyone in the KH2 Rando community and OpenKH community that has helped with modding the game.")
        })
      }

      item { Spacer(Modifier.height(16.dp)) }

      item {
        SmallHeader(stringResource(Res.string.about_licenses))
      }
      items(thirdPartyLibraries, key = ThirdPartyLibrary::name) { library ->
        ThirdPartyLibraryContent(library)
      }
    }
  }
}

@Composable
private fun ThirdPartyLibraryContent(
  library: ThirdPartyLibrary,
  modifier: Modifier = Modifier,
) {
  val linkStyles = TextLinkStyles(style = SpanStyle(ColorToken.LightBlue.color))
  Column(modifier = modifier) {
    val string = buildAnnotatedString {
      withLink(LinkAnnotation.Url(library.link, styles = linkStyles)) {
        append(library.name)
      }
    }
    Text(string)

    Text(library.copyright)
  }
}

private data class ThirdPartyLibrary(
  val name: String,
  val link: String,
  val copyright: String,
)

private val thirdPartyLibraries: List<ThirdPartyLibrary>
  get() {
    return listOf(
      ThirdPartyLibrary(
        name = "KH2Tracker",
        link = "https://github.com/Dee-Ayy/KH2Tracker/blob/v2.64/LICENSE",
        copyright = "Copyright (c) 2020 TrevorLuckey",
      ),
      ThirdPartyLibrary(
        name = "androidx",
        link = "https://github.com/androidx/androidx/blob/androidx-main/LICENSE.txt",
        copyright = "Copyright (C) 2017-2024 The Android Open Source Project",
      ),
      ThirdPartyLibrary(
        name = "coil",
        link = "https://github.com/coil-kt/coil/blob/3.0.0-rc02/LICENSE.txt",
        copyright = "Copyright 2024 Coil Contributors",
      ),
      ThirdPartyLibrary(
        name = "compose-multiplatform",
        link = "https://github.com/JetBrains/compose-multiplatform/blob/v1.7.0/LICENSE.txt",
        copyright = "Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.",
      ),
      ThirdPartyLibrary(
        name = "java-native-access",
        link = "https://github.com/java-native-access/jna/blob/5.15.0/AL2.0",
        copyright = "Copyright (c) 2007-2015 JNA Contributors",
      ),
      ThirdPartyLibrary(
        name = "kaml",
        link = "https://github.com/charleskorn/kaml/blob/0.61.0/LICENSE",
        copyright = "Copyright 2018-2023 Charles Korn.",
      ),
      ThirdPartyLibrary(
        name = "kotlin",
        link = "https://github.com/JetBrains/kotlin/blob/v2.0.21/license/README.md",
        copyright = "Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.",
      ),
      ThirdPartyLibrary(
        name = "kotlinx.collections.immutable",
        link = "https://github.com/Kotlin/kotlinx.collections.immutable/blob/v0.3.8/LICENSE.txt",
        copyright = "Copyright 2016-2024 JetBrains s.r.o. and contributors",
      ),
      ThirdPartyLibrary(
        name = "kotlinx.coroutines",
        link = "https://github.com/Kotlin/kotlinx.coroutines/blob/1.8.1/LICENSE.txt",
        copyright = "Copyright 2016-2024 JetBrains s.r.o. and contributors",
      ),
      ThirdPartyLibrary(
        name = "kotlinx.serialization",
        link = "https://github.com/Kotlin/kotlinx.serialization/blob/v1.7.1/LICENSE.txt",
        copyright = "Copyright 2017-2019 JetBrains s.r.o. and respective authors and developers",
      ),
      ThirdPartyLibrary(
        name = "okio",
        link = "https://github.com/square/okio/blob/3.9.1/LICENSE.txt",
        copyright = "Copyright 2013 Square, Inc.",
      ),
    )
  }
