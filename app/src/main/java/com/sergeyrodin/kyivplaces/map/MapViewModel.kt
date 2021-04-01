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

    private val _dataEvent = MutableLiveData<Boolean>()
    val dataEvent: LiveData<Boolean>
        get() = _dataEvent

    private val _errorEvent = MutableLiveData<Boolean>()
    val errorEvent: LiveData<Boolean>
        get() = _errorEvent

    private val _loadingEvent = MutableLiveData<Boolean>()
    val loadingEvent: LiveData<Boolean>
        get() = _loadingEvent

    private val _places = MutableLiveData<List<KyivPlace>>()
    val places: LiveData<List<KyivPlace>>
        get() = _places

    fun onMapCallback() {
        getPlacesFromNetwork()
    }

    private fun getPlacesFromNetwork() {
        _errorEvent.value = false
        viewModelScope.launch {
            try {
                _places.value = dataSource.getPlaces()
                _dataEvent.value = true
            } catch (e: IOException) {
                _errorEvent.value = true
            } catch (e: JsonDataException) {
                _errorEvent.value = true
            } finally {
                _loadingEvent.value = false
            }
        }
    }

    fun onViewCreated() {
        _loadingEvent.value = true
        _dataEvent.value = false
    }
}