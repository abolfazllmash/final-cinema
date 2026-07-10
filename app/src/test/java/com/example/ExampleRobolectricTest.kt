package com.example

import android.app.Application
import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import com.example.data.FilmRepository
import com.example.ui.FilmScreen
import com.example.ui.FilmViewModel
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("فیلم‌بازی", appName)
  }

  @Test
  fun `test getFilms loads successfully`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = FilmRepository(context)
    val films = repository.getFilms()
    println("Loaded films size: ${films.size}")
    assertTrue("Should load films from assets or database", films.isNotEmpty())
  }

  @Test
  fun `test FilmScreen rendering`() {
    val application = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = FilmViewModel(application)
    
    composeTestRule.setContent {
      MyApplicationTheme {
        FilmScreen(vm = viewModel)
      }
    }
    
    composeTestRule.onRoot().assertExists()
    println("FilmScreen rendered successfully without crash!")
  }
}
