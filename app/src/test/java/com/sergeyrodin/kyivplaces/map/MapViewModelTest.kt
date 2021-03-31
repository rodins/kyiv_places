package com.sergeyrodin.kyivplaces.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sergeyrodin.kyivplaces.MainCoroutineRule
import com.sergeyrodin.kyivplaces.data.FakeDataSource
import com.sergeyrodin.kyivplaces.network.KyivPlace
import com.sergeyrodin.kyivplaces.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private val PLACE1 = KyivPlace(1, "Independence Square", 50.450555, 30.5210808)
private val PLACE2 = KyivPlace(2, "Khreschatyk Street", 50.4475888, 30.5198317)
private val PLACE3 = KyivPlace(3, "National Opera of Ukraine", 50.44671, 30.5101755)

@ExperimentalCoroutinesApi
class MapViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var subject: MapViewModel
    private lateinit var dataSource: FakeDataSource

    @Before
    fun init() {
        dataSource = FakeDataSource()
    }

    @Test
    fun getPlaces_placesTitlesEquals() {
        dataSource.insertPlace(PLACE1)
        dataSource.insertPlace(PLACE2)
        dataSource.insertPlace(PLACE3)

        subject = MapViewModel(dataSource)

        val places = subject.places.getOrAwaitValue()
        assertThat(places.size, `is`(3))
        assertThat(places[0].name, `is`(PLACE1.name))
        assertThat(places[1].name, `is`(PLACE2.name))
        assertThat(places[2].name, `is`(PLACE3.name))
    }

    @Test
    fun getPlacesJosnError_placesSizeZero() {
        dataSource.insertPlace(PLACE1)
        dataSource.insertPlace(PLACE2)
        dataSource.insertPlace(PLACE3)
        dataSource.isJsonError = true

        subject = MapViewModel(dataSource)

        val places = subject.places.getOrAwaitValue()
        assertThat(places.size, `is`(0))
    }

    @Test
    fun loadingPlaces_loadingEventEquals() {
        dataSource.insertPlace(PLACE1)
        dataSource.insertPlace(PLACE2)
        dataSource.insertPlace(PLACE3)

        coroutineRule.pauseDispatcher()

        subject = MapViewModel(dataSource)

        val loadingBefore = subject.loadingEvent.getOrAwaitValue()
        assertThat(loadingBefore, `is`(true))

        coroutineRule.resumeDispatcher()

        val loadingAfter = subject.loadingEvent.getOrAwaitValue()
        assertThat(loadingAfter, `is`(false))
    }

    @Test
    fun errorMode_errorEventTrue() {
        dataSource.isError = true

        subject = MapViewModel(dataSource)

        val error = subject.errorEvent.getOrAwaitValue()
        assertThat(error, `is`(true))
    }

    @Test
    fun getData_errorEventFalse() {
        dataSource.insertPlace(PLACE1)
        dataSource.insertPlace(PLACE2)
        dataSource.insertPlace(PLACE3)

        subject = MapViewModel(dataSource)

        val error = subject.errorEvent.getOrAwaitValue()
        assertThat(error, `is`(false))
    }
}