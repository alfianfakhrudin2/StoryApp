package com.example.storyapp.view.login

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.storyapp.JsonConverter
import com.example.storyapp.data.remote.retrofit.ApiConfig
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.example.storyapp.R
import com.example.storyapp.view.main.MainActivity
import com.example.storyapp.view.utils.EspressoIdlingResource
import com.example.storyapp.view.welcome.WelcomeActivity
import okhttp3.mockwebserver.MockResponse


@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        ActivityScenario.launch(LoginActivity::class.java)
        mockWebServer.start(8080)
        ApiConfig.base_url = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun emailAndPassword_Error_inEnglish() {

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success.response.json"))
        mockWebServer.enqueue(mockResponse)

        Intents.init()

        onView(withId(R.id.titleTextViewLogin))
            .check(matches(isDisplayed()))

        onView(withId(R.id.emailEditTextLogin))
            .perform(typeText("mangeak@gmail.com"), closeSoftKeyboard())

        onView(withId(R.id.passwordEditTextLogin))
            .perform(typeText("mangeak12"), closeSoftKeyboard())

        onView(withId(R.id.loginButton))
            .perform(click())

        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))

        onView(withId(R.id.rvMain))
            .check(matches(isDisplayed()))

        onView(withId(R.id.logout_button))
            .perform(click())

        Intents.intended(IntentMatchers.hasComponent(WelcomeActivity::class.java.name))

        onView(withId(R.id.titleTextViewWelcome))
            .check(matches(isDisplayed()))
    }
}