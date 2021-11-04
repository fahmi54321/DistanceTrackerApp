package com.android.distancetrackerapp.service

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.android.distancetrackerapp.utils.Constants.ACTION_SERVICE_START
import com.android.distancetrackerapp.utils.Constants.ACTION_SERVICE_STOP

//todo 1 create service (next android manifest)
class TrackerService:LifecycleService() {

    //todo 8 create service
    companion object{
        val started = MutableLiveData<Boolean>()
    }

    //todo 9 create service
    private fun setInitialValues(){
        started.postValue(false)
    }

    override fun onCreate() {

        //todo 9 create service
        setInitialValues()

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //todo 7 create service
        intent.let {
            when(it?.action){
                ACTION_SERVICE_START ->{
                    //todo 10 create service (finish)
                    started.postValue(true)
                }
                ACTION_SERVICE_STOP ->{
                    //todo 10 create service (finish)
                    started.postValue(false)
                }else->{

                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }
}