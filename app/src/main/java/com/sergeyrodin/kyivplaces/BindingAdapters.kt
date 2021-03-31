package com.sergeyrodin.kyivplaces

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sergeyrodin.kyivplaces.network.KyivPlace
import com.sergeyrodin.kyivplaces.map.KyivPlaceAdapter

@BindingAdapter("placesData")
fun bindPlacesRecyclerView(view: RecyclerView, places: List<KyivPlace>?) {
    val adapter = view.adapter as KyivPlaceAdapter
    adapter.submitList(places)
}