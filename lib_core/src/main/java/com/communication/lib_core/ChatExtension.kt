package com.communication.lib_core

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class ChatExtension : LinearLayout {


    constructor(context : Context?) : super(context!!){
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init(context)
    }

    fun init(context : Context) {

    }
}