package com.sergeyrodin.kyivplaces.map

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.sergeyrodin.kyivplaces.MainCoroutineRule
import com.sergeyrodin.kyivplaces.R
import com.sergeyrodin.kyivplaces.ServiceLocator
import com.sergeyrodin.kyivplaces.data.FakeDataSource
import com.sergeyrodin.kyivplaces.network.KyivPlace
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private val PLACE1 = KyivPlace(1, "Independence Square", 50.450555, 30.5210808)
private val PLACE2 = KyivPlace(2, "Khreschatyk Street", 50.4475888, 30.5198317)
private val PLACE3 = KyivPlace(3, "National Opera of Ukraine", 50.44671, 30.5101755)

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class MapFragmentTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var dataSource: FakeDataSource
    private lateinit var args: Bundle

    @Before
    fun initDataSource() {
        dataSource = FakeDataSource()
        ServiceLocator.kyivPlacesDataSource = dataSource
        args = Bundle().apply {
            putCharSequence("title", "Email")
        }
    }

    @Test
    fun placesNamesAreDisplayed() {
        dataSource.insertPlace(PLACE1)
        dataSource.insertPlace(PLACE2)
        dataSource.insertPlace(PLACE3)

        val text1 = "${PLACE1.id}. ${PLACE1.name}"
        val text2 = "${PLACE2.id}. ${PLACE2.name}"
        val text3 = "${PLACE3.id}. ${PLACE3.name}"


        launchFragmentInContainer<MapFragment>(args, R.style.Theme_KyivPlaces)

        onView(withText(text1)).check(matches(isDisplayed()))
        onView(withText(text2)).check(matches(isDisplayed()))
        onView(withText(text3)).check(matches(isDisplayed()))
    }

    @Test
    fun errorMode_errorMsgDisplayed() {
        val errorText = "Test IOException"
        dataSource.isError = true

        launchFragmentInContainer<MapFragment>(args, R.style.Theme_KyivPlaces)

        onView(withText(errorText)).check(matches(isDisplayed()))
    }

    @Test
    fun mapViewIsDisplayed() {
        launchFragmentInContainer<MapFragment>(args, R.style.Theme_KyivPlaces)

        onView(withId(R.id.map_view)).check(matches(isDisplayed()))
    }

    @Test
    fun errorMode_mapViewIsNotDisplayed() {
        dataSource.isError = true

        launchFragmentInContainer<MapFragment>(args, R.style.Theme_KyivPlaces)

        onView(withId(R.id.map_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun errorMode_retryButtonIsDisplayed() {
        dataSource.isError = true

        launchFragmentInContainer<MapFragment>(args, R.style.Theme_KyivPlaces)

        onView(withText(R.string.retry)).check(matches(isDisplayed()))
    }

    @Test
    fun errorMode_retryClick_placeNameDisplayed() {
        dataSource.insertPlace(PLACE1)
        val text1 = "${PLACE1.id}. ${PLACE1.name}"
        dataSource.isError = true

        launchFragmentInContainer<MapFragment>(args, R.style.Theme_KyivPlaces)

        dataSource.isError = false
        onView(withText(R.string.retry)).perform(click())

        onView(withText(text1)).check(matches(isDisplayed()))
    }

}