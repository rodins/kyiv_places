package com.sergeyrodin.kyivplaces.map

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sergeyrodin.kyivplaces.KyivPlacesApplication
import com.sergeyrodin.kyivplaces.databinding.FragmentMapBinding
import com.sergeyrodin.kyivplaces.network.KyivPlace

private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private lateinit var map: GoogleMap

    private val viewModel by viewModels<MapViewModel> {
        MapViewModelFactory(
            (requireContext().applicationContext as KyivPlacesApplication).kyivPlacesDataSource
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)

        setupBinding()
        setupMapView(savedInstanceState)
        observePlaces()

        return binding.root
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.places.adapter = KyivPlaceAdapter()
    }

    private fun setupMapView(savedInstanceState: Bundle?) {
        val mapViewBundle = savedInstanceState?.getBundle(MAPVIEW_BUNDLE_KEY)
        binding.mapView.onCreate(mapViewBundle)
    }

    private fun observePlaces() {
        viewModel.places.observe(viewLifecycleOwner) {
            if (::map.isInitialized) {
                addMarkersOnMap(it)
            }
        }
    }

    private fun addMarkersOnMap(places: List<KyivPlace>) {
        if (places.isEmpty()) return
        moveCamera(places)
        addMarkers(places)
    }

    private fun moveCamera(places: List<KyivPlace>) {
        val cameraLatLng = getCameraLatLngWithAvgValues(places)
        val zoomLevel = 14f
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraLatLng, zoomLevel))
    }

    private fun getCameraLatLngWithAvgValues(places: List<KyivPlace>): LatLng {
        val avgLatitude = places.map {
            it.latitude
        }.average()
        val avgLongitude = places.map {
            it.longitude
        }.average()
        return LatLng(avgLatitude, avgLongitude)
    }

    private fun addMarkers(places: List<KyivPlace>) {
        places.forEach { place ->
            val latLng = LatLng(place.latitude, place.longitude)
            map.addMarker(MarkerOptions().position(latLng))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onViewCreated()
        getMapAsync()
    }

    private fun getMapAsync() {
        binding.mapView.getMapAsync { googleMap ->
            map = googleMap
            viewModel.onMapCallback()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY) ?: Bundle().also {
            outState.putBundle(MAPVIEW_BUNDLE_KEY, it)
        }
        binding.mapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}