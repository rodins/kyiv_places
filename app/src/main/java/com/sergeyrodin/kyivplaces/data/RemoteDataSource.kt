package com.sergeyrodin.kyivplaces.data

import com.sergeyrodin.kyivplaces.network.KyivPlace
import com.sergeyrodin.kyivplaces.network.KyivPlacesApi
import com.sergeyrodin.kyivplaces.util.wrapEspressoIdlingResource

class RemoteDataSource : KyivPlacesDataSource {
    override suspend fun getPlaces(): List<KyivPlace> {
        wrapEspressoIdlingResource {
            return KyivPlacesApi.retrofitService.getKyivPlaces().places
        }
    }
}