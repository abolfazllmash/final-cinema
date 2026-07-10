package com.example

import org.junit.Assert.*
import org.junit.Test
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.example.data.Film
import java.io.File

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testParseFilmsJson() {
    var file = File("app/src/main/assets/films.json")
    if (!file.exists()) {
        file = File("src/main/assets/films.json")
    }
    if (!file.exists()) {
        println("Current working directory: " + File(".").absolutePath)
        println("Parent directory files: " + File("..").listFiles()?.map { it.name })
    }
    assertTrue("films.json must exist at expected path", file.exists())
    
    val jsonString = file.readText()
    
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        
    val listType = Types.newParameterizedType(List::class.java, Film::class.java)
    val listAdapter = moshi.adapter<List<Film>>(listType)
    
    try {
        val films = listAdapter.fromJson(jsonString)
        assertNotNull("Parsed films list should not be null", films)
        println("Successfully parsed ${films?.size} films!")
    } catch (e: Exception) {
        e.printStackTrace()
        fail("Failed to parse films.json: ${e.message}")
    }
  }
}
