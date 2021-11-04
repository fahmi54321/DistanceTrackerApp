package com.android.distancetrackerapp.utils

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import com.android.distancetrackerapp.utils.Constants.PERMISSION_LOCATION_REQUEST_CODE
import com.vmadalin.easypermissions.EasyPermissions

//todo 1 permission fine location
object Permission {

    //todo 2 permission fine location
    fun hasLocationPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    //todo 3 permission fine location (next fragment_permission.xml)
    fun requestLocationPermission(fragment:Fragment){
        EasyPermissions.requestPermissions(
            fragment,
            "Aplikasi ini tidak bisa berjalan tanpa perizin lokasi",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}