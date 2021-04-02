package com.sergeyrodin.kyivplaces.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergeyrodin.kyivplaces.data.KyivPlacesDataSource
import com.sergeyrodin.kyivplaces.network.KyivPlace
import kotlinx.coroutines.launch

class MapViewModel(private val dataSource: KyivPlacesDataSource) : ViewModel() {

    private val _dataEvent = MutableLiveData<Boolean>()
    val dataEvent: LiveData<Boolean>
        get() = _dataEvent

    private val _errorEvent = MutableLiveData<String>()
    val errorEvent: LiveData<String>
        get() = _errorEvent

    private val _loadingEvent = MutableLiveData<Boolean>()
    val loadingEvent: LiveData<Boolean>
        get() = _loadingEvent

    private val _places = MutableLiveData<List<KyivPlace>>()
    val places: LiveData<List<KyivPlace>>
        get() = _places

    fun loadPlacesOnce() {
        if(_places.value == null) {
            getPlacesFromNetwork()
        }
    }

    private fun getPlacesFromNetwork() {
        _loadingEvent.value = true
        _dataEvent.value = false
        _errorEvent.value = ""
        viewModelScope.launch {
            try {
                _places.value = dataSource.getPlaces()
                _dataEvent.value = true
            } catch (e: Exception) {
                _errorEvent.value = e.message
            } finally {
                _loadingEvent.value = false
            }
        }
    }

    fun refresh() {
        getPlacesFromNetwork()
    }
}