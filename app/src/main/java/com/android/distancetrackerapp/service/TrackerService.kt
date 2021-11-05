package com.android.distancetrackerapp.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Intent
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.android.distancetrackerapp.utils.Constants.ACTION_SERVICE_START
import com.android.distancetrackerapp.utils.Constants.ACTION_SERVICE_STOP
import com.android.distancetrackerapp.utils.Constants.LOCATION_FASTEST_UPDATE_INTERVAL
import com.android.distancetrackerapp.utils.Constants.LOCATION_UPDATE_INTERVAL
import com.android.distancetrackerapp.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.android.distancetrackerapp.utils.Constants.NOTIFICATION_CHANNEL_NAME
import com.android.distancetrackerapp.utils.Constants.NOTIFICATION_ID
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//todo 1 create service (next android manifest)
//todo 2 create_notification (next NotificationModule)
@AndroidEntryPoint
class TrackerService : LifecycleService() {

    //todo 7 create_notification
    @Inject
    lateinit var notification: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    //    todo 2 start location update
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //todo 8 create service
    companion object {
        val started = MutableLiveData<Boolean>()
    }

    //todo 5 start location update
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)

            result?.locations?.let { locations ->
                for (location in locations) {
                    var newLatLng = LatLng(location.latitude, location.longitude)
                    Log.d("TrackerService", newLatLng.toString())
                }
            }
        }
    }

    //todo 9 create service
    private fun setInitialValues() {
        started.postValue(false)
    }

    override fun onCreate() {

        //todo 9 create service
        setInitialValues()

        //todo 3 start location update
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //todo 7 create service
        intent.let {
            when (it?.action) {
                ACTION_SERVICE_START -> {
                    //todo 10 create service (finish)
                    started.postValue(true)

                    //todo 2 start_foreground_service (finish)
                    startForegroundService()

                    //todo 6 start location update (finish)
                    startLocationUpdates()
                }
                ACTION_SERVICE_STOP -> {
                    //todo 10 create service (finish)
                    started.postValue(false)
                }
                else -> {

                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }


    //todo 1 start_foreground_service
    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(
                NOTIFICATION_ID,
                notification.build()
        )
    }

    //todo 4 start location update
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest().apply {
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = LOCATION_FASTEST_UPDATE_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
        )
    }

    //todo 8 create_notification (finish)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}