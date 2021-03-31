package com.sergeyrodin.kyivplaces.data

import com.sergeyrodin.kyivplaces.network.KyivPlace

interface KyivPlacesDataSource {
    suspend fun getPlaces(): List<KyivPlace>
}