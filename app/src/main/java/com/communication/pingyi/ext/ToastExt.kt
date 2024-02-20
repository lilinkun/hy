package com.communication.pingyi.ext

import android.view.Gravity
import android.widget.Toast
import com.communication.pingyi.base.AppContext

/**
 * Created by LG
 * on 2022/3/10  17:28
 * Description：
 */

fun pyToast(str : String){
    val toast = Toast.makeText(AppContext, str, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.BOTTOM, 0, 2340 / 9)
    toast.show()
}

fun pyToastShort(str : String){
    val toast = Toast.makeText(AppContext, str, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.BOTTOM, 0, 2340 / 9)
    toast.show()
}
