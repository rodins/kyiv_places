package com.sergeyrodin.kyivplaces

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @Test
    fun enterEmail_submitClick_emailDisplayed() {
        val emailText = "Email"
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.email_edit)).perform(typeText(emailText))
        onView(withId(R.id.submit_button)).perform(click())

        onView(withText(emailText)).check(matches(isDisplayed()))
        onView(withId(R.id.submit_button)).check(doesNotExist())

        activityScenario.close()
    }
}