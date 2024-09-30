import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
//    alias(libs.plugins.android.application)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.jetbrainsCompose)
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotlinSerialization)
}

val trackerVersion: String by rootProject
version = trackerVersion

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

kotlin {
  jvm("desktop")

//    androidTarget {
//    }

//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        moduleName = "composeApp"
//        browser {
//            val projectDirPath = project.projectDir.path
//            commonWebpackConfig {
//                outputFileName = "composeApp.js"
//                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
//                    static = (static ?: mutableListOf()).apply {
//                        // Serve sources to debug inside browser
//                        add(projectDirPath)
//                    }
//                }
//            }
//        }
//        binaries.executable()
//    }

  sourceSets {
    val desktopMain by getting

    commonMain.dependencies {
      implementation(compose.components.resources)
      implementation(compose.components.uiToolingPreview)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.runtime)
      implementation(compose.ui)
      implementation(libs.androidx.datastore)
      implementation(libs.androidx.datastore.preferences)
      implementation(libs.androidx.lifecycle.runtime.compose)
      implementation(libs.androidx.lifecycle.viewmodel)
      implementation(libs.androidx.lifecycle.viewmodel.compose)
      implementation(libs.coil)
      implementation(libs.kotlinx.collections.immutable)
      implementation(libs.kotlinx.serialization.cbor)
      implementation(libs.kotlinx.serialization.json)
    }
    desktopMain.dependencies {
      implementation(compose.desktop.currentOs)
      implementation(libs.jna.platform)
      implementation(libs.kaml)
      implementation(libs.kotlinx.coroutines.swing)
    }
//        androidMain.dependencies {
//            implementation(libs.material)
//            implementation(compose.uiTooling)
//            implementation(compose.preview)
//        }

    commonTest.dependencies {
      implementation(libs.kotlin.test)
      implementation(libs.kotlinx.coroutines.test)
      implementation(libs.turbine)
    }
  }
}

compose {
  resources {
    packageOfResClass = "com.kh2rando.tracker.generated.resources"
  }

  desktop {
    application {
      val trackerDebugMode: String by rootProject

      mainClass = "com.kh2rando.tracker.MainKt"

      args.add(trackerVersion)

      if (trackerDebugMode.toBoolean()) {
        args.add("debug")
      }

      nativeDistributions {
        targetFormats(TargetFormat.Msi)
        packageName = "KH2 Rando Tracker"
        description = "Item Tracker for Kingdom Hearts II Randomizer"
        copyright = "© 2024 equations19"
        packageVersion = trackerVersion
        modules("jdk.unsupported")

        windows {
          shortcut = true
          iconFile.set(file("replica_data.ico"))
        }
      }
    }
  }

  android {

  }
}

//android {
//    namespace = "com.kh2rando.tracker"
//    compileSdk = 34
//
//    buildFeatures {
//        compose = true
//    }
//
//    defaultConfig {
//        applicationId = "com.kh2rando.tracker"
//        minSdk = 29
//        targetSdk = 34
//        versionCode = 1
//        versionName = "1"
//    }
//}
