package com.sergeyrodin.kyivplaces

import androidx.annotation.VisibleForTesting
import com.sergeyrodin.kyivplaces.data.KyivPlacesDataSource
import com.sergeyrodin.kyivplaces.data.RemoteDataSource

object ServiceLocator {

    @Volatile
    var kyivPlacesDataSource: KyivPlacesDataSource? = null
        @VisibleForTesting set

    fun provideKyivPlacesDataSource(): KyivPlacesDataSource {
        return kyivPlacesDataSource ?: createDataSource()
    }

    private fun createDataSource(): KyivPlacesDataSource {
        val dataSource = RemoteDataSource()
        kyivPlacesDataSource = dataSource
        return dataSource
    }
}