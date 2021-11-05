package com.android.distancetrackerapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


//todo 1 display result (next nav_graph untuk menambahkan argument pada ResultFragment)
@Parcelize
data class Result(
        var distance: String,
        var time: String,
):Parcelable