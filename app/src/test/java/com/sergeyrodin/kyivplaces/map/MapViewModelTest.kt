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

    private lateinit var viewModel: MapViewModel
    private lateinit var dataSource: FakeDataSource

    @Before
    fun init() {
        dataSource = FakeDataSource()
        viewModel = MapViewModel(dataSource)
    }

    @Test
    fun getPlaces_placesTitlesEquals() {
        dataSource.insertPlace(PLACE1)
        dataSource.insertPlace(PLACE2)
        dataSource.insertPlace(PLACE3)

        viewModel.onMapCallback()

        val places = viewModel.places.getOrAwaitValue()
        assertThat(places.size, `is`(3))
        assertThat(places[0].name, `is`(PLACE1.name))
        assertThat(places[1].name, `is`(PLACE2.name))
        assertThat(places[2].name, `is`(PLACE3.name))
    }

    @Test
    fun getPlacesJosnError_errorEventIsTrue() {
        dataSource.isJsonError = true

        viewModel.onViewCreated()
        viewModel.onMapCallback()

        val error = viewModel.errorEvent.getOrAwaitValue()
        assertThat(error, `is`(true))
    }

    @Test
    fun loadingPlaces_loadingEventEquals() {
        dataSource.insertPlace(PLACE1)
        dataSource.insertPlace(PLACE2)
        dataSource.insertPlace(PLACE3)

        coroutineRule.pauseDispatcher()

        viewModel.onViewCreated()
        viewModel.onMapCallback()

        val loadingBefore = viewModel.loadingEvent.getOrAwaitValue()
        assertThat(loadingBefore, `is`(true))

        coroutineRule.resumeDispatcher()

        val loadingAfter = viewModel.loadingEvent.getOrAwaitValue()
        assertThat(loadingAfter, `is`(false))
    }

    @Test
    fun errorMode_errorEventTrue() {
        dataSource.isError = true

        viewModel.onMapCallback()

        val error = viewModel.errorEvent.getOrAwaitValue()
        assertThat(error, `is`(true))
    }

    @Test
    fun getData_errorEventFalse() {
        dataSource.insertPlace(PLACE1)
        dataSource.insertPlace(PLACE2)
        dataSource.insertPlace(PLACE3)

        viewModel.onMapCallback()

        val error = viewModel.errorEvent.getOrAwaitValue()
        assertThat(error, `is`(false))
    }

    @Test
    fun onViewCreated_loadingEventTrue() {
        viewModel.onViewCreated()

        val loadingBefore = viewModel.loadingEvent.getOrAwaitValue()
        assertThat(loadingBefore, `is`(true))
    }

    @Test
    fun onViewCreated_dataEventFalse() {
        viewModel.onViewCreated()

        val dataEvent = viewModel.dataEvent.getOrAwaitValue()
        assertThat(dataEvent, `is`(false))
    }

    @Test
    fun onGetData_dataEventTrue() {
        dataSource.insertPlace(PLACE1)
        dataSource.insertPlace(PLACE2)
        dataSource.insertPlace(PLACE3)

        viewModel.onViewCreated()
        viewModel.onMapCallback()

        val dataEvent = viewModel.dataEvent.getOrAwaitValue()
        assertThat(dataEvent, `is`(true))
    }

    @Test
    fun onError_dataEventFalse() {
        dataSource.isError = true

        viewModel.onViewCreated()
        viewModel.onMapCallback()

        val dataEvent = viewModel.dataEvent.getOrAwaitValue()
        assertThat(dataEvent, `is`(false))
    }

    @Test
    fun onJsonError_dataEventFalse() {
        dataSource.isJsonError = true

        viewModel.onViewCreated()
        viewModel.onMapCallback()

        val dataEvent = viewModel.dataEvent.getOrAwaitValue()
        assertThat(dataEvent, `is`(false))
    }
}