package com.sergeyrodin.kyivplaces.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sergeyrodin.kyivplaces.data.KyivPlacesDataSource
import java.lang.IllegalArgumentException

class MapViewModelFactory(private val dataSource: KyivPlacesDataSource): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Can't create ViewModel!")
    }
}