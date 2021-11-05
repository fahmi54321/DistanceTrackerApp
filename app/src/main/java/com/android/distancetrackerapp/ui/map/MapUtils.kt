package com.android.distancetrackerapp.ui.map

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.text.DecimalFormat

object MapUtils {

    //todo 4 draw a polyline (next MapsFragment)
    fun setCameraPosition(location:LatLng):CameraPosition{
        return CameraPosition.Builder()
            .target(location)
            .zoom(18f)
            .build()
    }

    //todo 5 calculate elapsed time (next MapsFragment)
    fun calculateElapsedTime(startTime: Long,stopTime: Long):String{
        val elapsedTime = stopTime - startTime

        val seconds = (elapsedTime / 1000).toInt() % 60
        val minutes = (elapsedTime / (1000*60) % 60)
        val hours = (elapsedTime / (1000 * 60 * 60) % 24)

        return "$hours:$minutes:$seconds"
    }

    //todo 1 calculate the distance (finish)
    fun calculateTheDistance(locationList: MutableList<LatLng>):String{
        if (locationList.size > 1){
            val meters = SphericalUtil.computeDistanceBetween(locationList.first(),locationList.last())
            val kilometers = meters / 1000
            return DecimalFormat("#.##").format(kilometers)
        }
        return "0.00"
    }
}