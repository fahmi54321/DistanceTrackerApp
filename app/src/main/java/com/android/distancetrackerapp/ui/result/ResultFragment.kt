package com.android.distancetrackerapp.ui.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.android.distancetrackerapp.R
import com.android.distancetrackerapp.databinding.FragmentResultBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

//todo 3 design result fragment (finish)
class ResultFragment : BottomSheetDialogFragment() {

    //todo 10 display result
    private val args: ResultFragmentArgs by navArgs()

    //todo 7 display result
    private var _binding: FragmentResultBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //todo 8 display result
        _binding =  FragmentResultBinding.inflate(inflater, container, false)

        //todo 11 display result (finish)
        binding.distanceValueTextview.text = getString(R.string.result,args.result.distance) // jangan lupa setting pada values.xml bagian result
        binding.timeValueTextview.text = args.result.time

        return binding.root
    }

    //todo 9 display result
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}