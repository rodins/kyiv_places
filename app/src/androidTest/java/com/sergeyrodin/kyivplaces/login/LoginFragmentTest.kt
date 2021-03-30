package com.sergeyrodin.kyivplaces.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.sergeyrodin.kyivplaces.R
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert


import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun emailPasswordHintsAndSubmitButtonDisplayed() {
        launchFragmentInContainer<LoginFragment>(null, R.style.Theme_KyivPlaces)

        onView(withHint(R.string.enter_email)).check(matches(isDisplayed()))
        onView(withHint(R.string.enter_password)).check(matches(isDisplayed()))
        onView(withText(R.string.submit)).check(matches(isDisplayed()))
    }

    @Test
    fun submitClick_navigationCalled() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.navigation)

        val scenario = launchFragmentInContainer<LoginFragment>(null, R.style.Theme_KyivPlaces)
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }

        onView(withText(R.string.submit)).perform(click())

        Assert.assertThat(navController.currentDestination?.id, `is`(R.id.mapFragment))
    }

}