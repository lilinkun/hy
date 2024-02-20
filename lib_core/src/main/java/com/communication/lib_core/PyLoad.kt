package com.communication.lib_core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout

/**
 * Created by LG
 * on 2022/3/15  11:33
 * Description：
 */
class PyLoad : RelativeLayout {


    constructor(context: Context) : super(context){
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }


    private fun init(context: Context) {
        (context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE
        ) as LayoutInflater).inflate(R.layout.py_load, this)
        findViewById<RelativeLayout>(R.id.layout).setOnClickListener { }
    }
}