package com.communication.lib_core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView

/**
 * Created by LG
 * on 2022/3/28  16:16
 * Description：
 */
class PyMessageRed : RelativeLayout {

    private lateinit var countView: TextView

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        (context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE
        ) as LayoutInflater).inflate(R.layout.py_message_red, this)

        countView = findViewById(R.id.count)
    }

    fun setCount(count: String) {
        countView.text = count
    }
}