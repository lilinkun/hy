package com.communication.lib_core

var lastClickTime = 0L
fun checkDoubleClick(interval: Int = 500): Boolean {
    val time = System.currentTimeMillis()
    val timeD = time - lastClickTime
    if (timeD < interval) {
        return false
    }
    lastClickTime = time
    return true
}

var lastExitTime = 0L
fun checkExit(): Boolean {
    val time = System.currentTimeMillis()
    val timeD = time - lastExitTime
    if (timeD < 1000) {
        return true
    }
    lastExitTime = time
    return false
}

var lastShortClickTime = 0L
fun checkShortClick(): Boolean {
    val time = System.currentTimeMillis()
    val timeD = time - lastShortClickTime
    if (timeD < 200) {
        return false
    }
    lastShortClickTime = time
    return true
}