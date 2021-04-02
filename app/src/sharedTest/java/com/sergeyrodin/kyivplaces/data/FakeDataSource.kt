package com.sergeyrodin.kyivplaces.data

import com.sergeyrodin.kyivplaces.network.KyivPlace
import com.squareup.moshi.JsonDataException
import java.io.IOException

class FakeDataSource: KyivPlacesDataSource {
    private val places = mutableListOf<KyivPlace>()
    var isError = false

    fun insertPlace(place: KyivPlace) {
        places.add(place)
    }

    override suspend fun getPlaces(): List<KyivPlace> {
        if(isError) throw IOException("Test IOException")
        return places
    }
}