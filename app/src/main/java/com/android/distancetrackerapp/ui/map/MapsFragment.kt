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
import androidx.lifecycle.lifecycleScope
import com.android.distancetrackerapp.R
import com.android.distancetrackerapp.databinding.FragmentMapsBinding
import com.android.distancetrackerapp.service.TrackerService
import com.android.distancetrackerapp.ui.map.MapUtils.setCameraPosition
import com.android.distancetrackerapp.utils.Constants.ACTION_SERVICE_START
import com.android.distancetrackerapp.utils.Constants.ACTION_SERVICE_STOP
import com.android.distancetrackerapp.utils.ExtensionFunctions.disable
import com.android.distancetrackerapp.utils.ExtensionFunctions.enable
import com.android.distancetrackerapp.utils.ExtensionFunctions.hide
import com.android.distancetrackerapp.utils.ExtensionFunctions.show
import com.android.distancetrackerapp.utils.Permission.hasBackgroundLocationPermission
import com.android.distancetrackerapp.utils.Permission.requestBackgroundLocationPermission
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
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

    //todo 3 map_fragment_layout
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    //todo 1 enable my location
    private lateinit var map: GoogleMap

    //todo 5 update and observe location list
    private var locationList = mutableListOf<LatLng>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //todo 4 map_fragment_layout
        _binding = FragmentMapsBinding.inflate(inflater, container, false)


        //todo 6 map_fragment_layout (finish)
        binding.startButton.setOnClickListener {

            //todo 4 permission background location
            onStartButtonClicked()

        }
        binding.stopButton.setOnClickListener {
            //todo 2 stop foreground service
            onStopButtonClicked()
        }
        binding.resetButton.setOnClickListener {}

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
                if (locationList.size>1){
                    binding.stopButton.enable()
                }

                //todo 2 draw a polyline
                drawPolyline()

                //todo 5 draw a polyline (finish)
                followPolyline()
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