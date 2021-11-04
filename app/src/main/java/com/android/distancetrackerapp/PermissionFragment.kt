package com.android.distancetrackerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.distancetrackerapp.Permission.hasLocationPermission
import com.android.distancetrackerapp.Permission.requestLocationPermission
import com.android.distancetrackerapp.databinding.FragmentPermissionBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

//todo 10 permission fine location
class PermissionFragment : Fragment(),EasyPermissions.PermissionCallbacks {

    //todo 5 permission fine location
    private var _binding: FragmentPermissionBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //todo 6 permission fine location
        _binding =  FragmentPermissionBinding.inflate(inflater, container, false)

        //todo 8 permission fine location
        binding.continueButton.setOnClickListener {
            if (hasLocationPermission(requireContext())){
                findNavController().navigate(R.id.action_permissionFragment_to_mapsFragment)
            }else{
                requestLocationPermission(this)
            }
        }


        return binding.root
    }


    //todo 9 permission fine location
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    //todo 10 permission fine location (next MainActivity.kt)
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms[0])){
            SettingsDialog.Builder(requireActivity()).build().show()
        }else{
            requestLocationPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        findNavController().navigate(R.id.action_permissionFragment_to_mapsFragment)
    }

    //todo 7 permission fine location
    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        _binding = null
    }
}