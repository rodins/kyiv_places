package com.sergeyrodin.kyivplaces

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.sergeyrodin.kyivplaces.data.FakeDataSource
import com.sergeyrodin.kyivplaces.network.KyivPlace
import com.sergeyrodin.kyivplaces.util.DataBindingIdlingResource
import com.sergeyrodin.kyivplaces.util.EspressoIdlingResource
import com.sergeyrodin.kyivplaces.util.monitorActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private val PLACE1 = KyivPlace(1, "Independence Square", 50.450555, 30.5210808)
private val PLACE2 = KyivPlace(2, "Khreschatyk Street", 50.4475888, 30.5198317)
private val PLACE3 = KyivPlace(3, "National Opera of Ukraine", 50.44671, 30.5101755)

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    private lateinit var dataSource: FakeDataSource

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Before
    fun initDataSource() {
        dataSource = FakeDataSource()
        ServiceLocator.kyivPlacesDataSource = dataSource
    }

    @Test
    fun enterEmail_submitClick_emailDisplayed() {
        val emailText = "Email"
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.email_edit)).perform(typeText(emailText), closeSoftKeyboard())
        onView(withId(R.id.submit_button)).perform(click())

        onView(withText(emailText)).check(matches(isDisplayed()))
        onView(withId(R.id.submit_button)).check(doesNotExist())

        activityScenario.close()
    }

    @Test
    fun enterEmail_submitClick_placesTextDisplayed() {
        val emailText = "Email"

        dataSource.insertPlace(PLACE1)
        dataSource.insertPlace(PLACE2)
        dataSource.insertPlace(PLACE3)

        val text1 = "${PLACE1.id}. ${PLACE1.name}"
        val text2 = "${PLACE2.id}. ${PLACE2.name}"
        val text3 = "${PLACE3.id}. ${PLACE3.name}"

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.email_edit)).perform(typeText(emailText), closeSoftKeyboard())
        onView(withId(R.id.submit_button)).perform(click())

        onView(withText(text1)).check(matches(isDisplayed()))
        onView(withText(text2)).check(matches(isDisplayed()))
        onView(withText(text3)).check(matches(isDisplayed()))

        activityScenario.close()
    }
}