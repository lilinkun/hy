package com.communication.pingyi.ext

import com.communication.pingyi.BuildConfig
import timber.log.Timber


fun initTimber(){

    Timber.plant(Timber.DebugTree())

}

fun pyLog(string : String){
    if (BuildConfig.DEBUG){
        Timber.e("===================$string")
    }
}