package com.sergeyrodin.kyivplaces.network

import com.squareup.moshi.Json

data class KyivPlace(
    val id: Int,
    val name: String,
    @Json(name = "lat") val latitude: Double,
    @Json(name = "lng") val longitude: Double
)


