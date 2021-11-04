package com.android.distancetrackerapp

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.distancetrackerapp.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

//todo 2 map_fragment_layout
class MapsFragment : Fragment(), OnMapReadyCallback {

    //todo 3 map_fragment_layout
    private var _binding:FragmentMapsBinding?=null
    private val binding get() = _binding!!

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

    override fun onMapReady(p0: GoogleMap?) {
    }

    //todo 5 map_fragment_layout
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}