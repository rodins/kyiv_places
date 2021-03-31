package com.sergeyrodin.kyivplaces

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.sergeyrodin.kyivplaces.data.KyivPlacesDataSource

class KyivPlacesApplication : MultiDexApplication() {
    val kyivPlacesDataSource: KyivPlacesDataSource
        get() = ServiceLocator.provideKyivPlacesDataSource()
}