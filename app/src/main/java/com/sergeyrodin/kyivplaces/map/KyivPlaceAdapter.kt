package com.sergeyrodin.kyivplaces.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sergeyrodin.kyivplaces.R
import com.sergeyrodin.kyivplaces.network.KyivPlace

class KyivPlaceAdapter: ListAdapter<KyivPlace, KyivPlaceAdapter.ViewHolder>(KyivPlaceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

        fun bind(place: KyivPlace) {
            val text = "${place.id}. ${place.name}"
            val textView: TextView = view as TextView
            textView.text = text
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.kyiv_place_item, parent, false)
                return ViewHolder(view)
            }
        }
    }
}

class KyivPlaceDiffCallback: DiffUtil.ItemCallback<KyivPlace>() {

    override fun areItemsTheSame(oldItem: KyivPlace, newItem: KyivPlace): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: KyivPlace, newItem: KyivPlace): Boolean {
        return oldItem == newItem
    }

}