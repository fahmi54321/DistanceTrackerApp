package com.android.distancetrackerapp.utils

import android.view.View
import android.widget.Button

//todo 5 enable my location (next MapsFragment)
object ExtensionFunctions {

    fun View.show(){
        this.visibility = View.VISIBLE
    }

    fun View.hide(){
        this.visibility = View.INVISIBLE
    }

    fun Button.enable(){
        this.isEnabled = true
    }

    fun Button.disable(){
        this.isEnabled = false
    }

}