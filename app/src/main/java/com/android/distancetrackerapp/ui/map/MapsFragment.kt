package com.android.distancetrackerapp.ui.map

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.distancetrackerapp.R
import com.android.distancetrackerapp.databinding.FragmentMapsBinding
import com.android.distancetrackerapp.model.Result
import com.android.distancetrackerapp.service.TrackerService
import com.android.distancetrackerapp.ui.map.MapUtils.calculateElapsedTime
import com.android.distancetrackerapp.ui.map.MapUtils.calculateTheDistance
import com.android.distancetrackerapp.ui.map.MapUtils.setCameraPosition
import com.android.distancetrackerapp.utils.Constants.ACTION_SERVICE_START
import com.android.distancetrackerapp.utils.Constants.ACTION_SERVICE_STOP
import com.android.distancetrackerapp.utils.ExtensionFunctions.disable
import com.android.distancetrackerapp.utils.ExtensionFunctions.enable
import com.android.distancetrackerapp.utils.ExtensionFunctions.hide
import com.android.distancetrackerapp.utils.ExtensionFunctions.show
import com.android.distancetrackerapp.utils.Permission.hasBackgroundLocationPermission
import com.android.distancetrackerapp.utils.Permission.requestBackgroundLocationPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//todo 2 map_fragment_layout
//todo 3 enable my location
//todo 7 permission background location
class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        EasyPermissions.PermissionCallbacks {

    private var mapFragment: SupportMapFragment? = null

    //todo 1 create maps binding adapter class
    val started = MutableLiveData(false)

    //todo 3 map_fragment_layout
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    //todo 1 enable my location
    private lateinit var map: GoogleMap

    //todo 6 calculate elapsed time
    private var startTime = 0L
    private var stopTime = 0L

    //todo 5 update and observe location list
    private var locationList = mutableListOf<LatLng>()

    //todo 4 map reset
    private var polylinelist = mutableListOf<Polyline>()

    //todo 6 map reset
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        //todo 4 map_fragment_layout
        _binding = FragmentMapsBinding.inflate(inflater, container, false)

        //todo 4 create maps binding adapter class (next MapsBindingAdapter)
        binding.lifecycleOwner = this
        binding.tracking = this

        //todo 6 map_fragment_layout (finish)
        binding.startButton.setOnClickListener {

            //todo 4 permission background location
            onStartButtonClicked()

        }
        binding.stopButton.setOnClickListener {
            //todo 2 stop foreground service
            onStopButtonClicked()
        }
        binding.resetButton.setOnClickListener {

            //todo 1 map reset
            onResetButtonClicked()
        }

        //todo 7 map reset
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        return binding.root
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

        //todo 7 update and observe location list (finish)
        observeTrackerService()
    }

    //todo 6 update and observe location list
    private fun observeTrackerService() {
        TrackerService.locationList.observe(viewLifecycleOwner, {
            if (it != null) {
                locationList = it

                //todo 1 stop foreground service
                if (locationList.size > 1) {
                    binding.stopButton.enable()
                }

                //todo 2 draw a polyline
                drawPolyline()

                //todo 5 draw a polyline (finish)
                followPolyline()
            }
        })

        //todo 2 create maps binding adapter class (next fragment_maps.xml)
        TrackerService.started.observe(viewLifecycleOwner,{
            started.value = it
        })

        //todo 7 calculate elapsed time (finish)
        TrackerService.startTime.observe(viewLifecycleOwner, {
            startTime = it
        })
        TrackerService.stopTime.observe(viewLifecycleOwner, {
            stopTime = it

            //todo 1 show bigger picture
            if (stopTime != 0L) {
                showBiggerPicture()

                //todo 4 display result (next nav_graph untuk ngatur popUpTo dari result ke mapsFragment)
                displayResults()
            }
        })
    }

    //todo 1 draw a polyline
    private fun drawPolyline() {
        val polyline = map.addPolyline(
                PolylineOptions().apply {
                    width(10f)
                    color(Color.BLUE)
                    jointType(JointType.ROUND)
                    startCap(ButtCap())
                    endCap(ButtCap())
                    addAll(locationList)
                }
        )

        //todo 5 map reset
        polylinelist.add(polyline)
    }

    //todo 3 draw a polyline (next MapUtils)
    private fun followPolyline() {
        if (locationList.isNotEmpty()) {
            map.animateCamera(
                    (
                            CameraUpdateFactory.newCameraPosition(
                                    setCameraPosition(locationList.last())
                            )
                            ), 1000, null
            )
        }
    }

    //todo 5 permission background location
    private fun onStartButtonClicked() {
        if (hasBackgroundLocationPermission(requireContext())) {
            //todo 1 implement countdown
            startCountDown()
            binding.startButton.disable()
            binding.startButton.hide()
            binding.stopButton.show()
        } else {
            requestBackgroundLocationPermission(this)
        }
    }

    //todo 3 stop foreground service
    private fun onStopButtonClicked() {
        //todo 4 stop foreground service
        stopForegroundService()
        binding.stopButton.hide()
        binding.startButton.show()
    }

    //todo 2 map reset
    private fun onResetButtonClicked() {
        mapReset()
    }

    //todo 2 implement countdown (finish)
    private fun startCountDown() {
        binding.timerTextview.show()
        binding.stopButton.disable()
        val timer: CountDownTimer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val currentSecond = millisUntilFinished / 1000
                if (currentSecond.toString() == "0") {
                    binding.timerTextview.text = "GO"
                    binding.timerTextview.setTextColor(
                            ContextCompat.getColor(
                                    requireContext(),
                                    R.color.black
                            )
                    )
                } else {
                    binding.timerTextview.text = currentSecond.toString()
                    binding.timerTextview.setTextColor(
                            ContextCompat.getColor(
                                    requireContext(),
                                    R.color.red
                            )
                    )
                }
            }

            override fun onFinish() {
//                todo 5 create service
                sendActionCommandToService(ACTION_SERVICE_START)


                binding.timerTextview.hide()
            }
        }
        timer.start()
    }

    //todo 5 stop foreground service
    private fun stopForegroundService() {
        //todo 6 stop foreground service (next TrackerService)
        binding.startButton.disable()
        sendActionCommandToService(ACTION_SERVICE_STOP)
    }

    //todo 6 create service (next TrackerService)
    private fun sendActionCommandToService(action: String) {
        Intent(
                requireContext(),
                TrackerService::class.java
        ).apply {
            this.action = action
            requireContext().startService(this)
        }
    }

    //todo 2 show bigger picture (finish)
    private fun showBiggerPicture() {
        val bounds = LatLngBounds.Builder()
        for (location in locationList) {
            bounds.include(location)
        }
        map.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                        bounds.build(),
                        100
                ), 200, null
        )
    }

    //todo 3 display result
    private fun displayResults(){
        val result = Result(
                calculateTheDistance(locationList),
                calculateElapsedTime(startTime,stopTime)
        )
        lifecycleScope.launch {
            delay(2500)
            val directions = MapsFragmentDirections.actionMapsFragmentToResultFragment(result) // jangan lupa rebuild
            findNavController().navigate(directions)
            binding.startButton.apply {
                hide()
                enable()
            }
            binding.stopButton.hide()
            binding.resetButton.show()
        }
    }

    //todo 3 map reset
    @SuppressLint("MissingPermission")
    private fun mapReset() {
        //todo 8 map reset (finish)
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            val lastKnownLocation = LatLng(
                    it.result.latitude,
                    it.result.longitude
            )
            for (polyline in polylinelist){
                polyline.remove()
            }

            map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                            setCameraPosition(lastKnownLocation)
                    )
            )
            locationList.clear()
            binding.resetButton.hide()
            binding.startButton.show()
        }
    }

    override fun onMyLocationButtonClick(): Boolean {

        //todo 6 enable my location (finish)
        binding.hintTextview.animate().alpha(0f).duration = 1500
        lifecycleScope.launch {
            delay(2500)
            binding.hintTextview.hide()
            binding.startButton.show()
        }

        return false
    }

    //todo 6 permission background location
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    //todo 8 permission background location (finish)
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms[0])) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestBackgroundLocationPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        onStartButtonClicked()
    }

    //todo 5 map_fragment_layout
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}