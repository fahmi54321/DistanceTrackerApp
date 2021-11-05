package com.android.distancetrackerapp.ui.map

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

object MapUtils {

    //todo 4 draw a polyline (next MapsFragment)
    fun setCameraPosition(location:LatLng):CameraPosition{
        return CameraPosition.Builder()
            .target(location)
            .zoom(18f)
            .build()
    }
}