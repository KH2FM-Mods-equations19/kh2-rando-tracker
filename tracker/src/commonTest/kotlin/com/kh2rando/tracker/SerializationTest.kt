package com.kh2rando.tracker

import com.kh2rando.tracker.model.SongEntry
import com.kh2rando.tracker.model.seed.testGameState
import com.kh2rando.tracker.model.seed.testPreferences
import com.kh2rando.tracker.model.seed.testSeed
import com.kh2rando.tracker.serialization.GameStateSerializedForm
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalSerializationApi::class)
class SerializationTest {

  @get:Rule
  val temporaryFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

  private val preferences = testPreferences(temporaryFolder)

  private val json: Json = Json {
    prettyPrint = true
  }

  private val cbor: Cbor = Cbor.Default

  @Test
  fun `can serialize to and from JSON if needed`() {
    runTest {
      val seed = testSeed(
        seedHashIcons = listOf("a", "b", "c"),
        musicReplacements = mapOf(
          123 to SongEntry(songName = "Simple and Clean", group = "Kingdom Hearts")
        )
      )
      val gameState = testGameState(seed, preferences)
      val serializedForm = gameState.toGameStateSerializedForm()
      val encodedData = json.encodeToString(serializedForm)
//      println(encodedData)
      val deserializedForm = json.decodeFromString<GameStateSerializedForm>(encodedData)
      assertEquals(expected = serializedForm, actual = deserializedForm)
    }
  }

  @Test
  fun `can serialize to and from CBOR if needed`() {
    runTest {
      val seed = testSeed(
        seedHashIcons = listOf("a", "b", "c"),
        musicReplacements = mapOf(
          123 to SongEntry(songName = "Simple and Clean", group = "Kingdom Hearts")
        )
      )
      val gameState = testGameState(seed, preferences)
      val serializedForm = gameState.toGameStateSerializedForm()
      val encodedData = cbor.encodeToByteArray(serializedForm)
      val deserializedForm = cbor.decodeFromByteArray<GameStateSerializedForm>(encodedData)
      assertEquals(expected = serializedForm, actual = deserializedForm)
    }
  }

}
