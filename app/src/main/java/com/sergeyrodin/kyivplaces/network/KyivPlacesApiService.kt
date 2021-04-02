package com.sergeyrodin.kyivplaces.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://2fjd9l3x1l.api.quickmocker.com/"
//private const val BASE_URL = "https://my-json-server.typicode.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface KyivPlacesApiService {
    //@GET("rodins/kyiv_places/db")
    @GET("kyiv/places")
    suspend fun getKyivPlaces(): KyivPlaces
}

object KyivPlacesApi {
    val retrofitService by lazy {
        retrofit.create(KyivPlacesApiService::class.java)
    }
}