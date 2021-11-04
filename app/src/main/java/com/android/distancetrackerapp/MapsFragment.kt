package com.android.distancetrackerapp

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.distancetrackerapp.databinding.FragmentMapsBinding
import com.android.distancetrackerapp.utils.ExtensionFunctions.hide
import com.android.distancetrackerapp.utils.ExtensionFunctions.show

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//todo 2 map_fragment_layout
//todo 3 enable my location
class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    //todo 3 map_fragment_layout
    private var _binding:FragmentMapsBinding?=null
    private val binding get() = _binding!!

    //todo 1 enable my location
    private lateinit var map:GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //todo 4 map_fragment_layout
        _binding =  FragmentMapsBinding.inflate(inflater, container, false)


        //todo 6 map_fragment_layout (finish)
        binding.startButton.setOnClickListener{}
        binding.stopButton.setOnClickListener{}
        binding.resetButton.setOnClickListener{}

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {

        //todo 2 enable my location
        map = googleMap!!
        map.isMyLocationEnabled = true
        //todo 4 enable my location (next object ExtensionFunctions)
        map.setOnMyLocationButtonClickListener(this)

        //todo 2 enable my location
        map.uiSettings.apply {
            isZoomControlsEnabled = false
            isZoomGesturesEnabled = false
            isRotateGesturesEnabled = false
            isTiltGesturesEnabled = false
            isCompassEnabled = false
            isScrollGesturesEnabled = false
        }
    }

    //todo 5 map_fragment_layout
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMyLocationButtonClick(): Boolean {

        //todo 6 enable my location
        binding.hintTextview.animate().alpha(0f).duration = 1500
        lifecycleScope.launch {
            delay(2500)
            binding.hintTextview.hide()
            binding.startButton.show()
        }

        return false
    }
}