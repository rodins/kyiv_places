package com.sergeyrodin.kyivplaces.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergeyrodin.kyivplaces.network.KyivPlace
import com.sergeyrodin.kyivplaces.data.KyivPlacesDataSource
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.launch
import java.io.IOException

class MapViewModel(private val dataSource: KyivPlacesDataSource) : ViewModel() {

    private val _errorEvent = MutableLiveData<Boolean>()
    val errorEvent: LiveData<Boolean>
        get() = _errorEvent

    private val _loadingEvent = MutableLiveData<Boolean>()
    val loadingEvent: LiveData<Boolean>
        get() = _loadingEvent

    private val _places = MutableLiveData<List<KyivPlace>>()
    val places: LiveData<List<KyivPlace>>
        get() = _places

    init {
        getPlacesFromNetwork()
    }

    private fun getPlacesFromNetwork() {
        _loadingEvent.value = true
        _errorEvent.value = false
        viewModelScope.launch {
            try {
                _places.value = dataSource.getPlaces()
            } catch (e: IOException) {
                _errorEvent.value = true
            } catch (e: JsonDataException) {
                _places.value = listOf()
            } finally {
                _loadingEvent.value = false
            }
        }
    }
}