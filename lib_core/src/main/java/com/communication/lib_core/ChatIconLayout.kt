package com.communication.lib_core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class ChatIconLayout : LinearLayout {

    private lateinit var iconIv : ImageView
    private lateinit var iconNameTv : TextView

    constructor(context : Context?) : super(context!!){
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init(context)
        val ta = context.obtainStyledAttributes(attrs,R.styleable.ChatIcon)
        val icon = ta.getDrawable(R.styleable.ChatIcon_icon)
        val iconName = ta.getString(R.styleable.ChatIcon_icon_name)

        iconIv.setImageDrawable(icon)
        iconNameTv.text = iconName

    }

    private fun init(context: Context) {

        (context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater).inflate(R.layout.chat_icon_layout,this)

        iconIv = findViewById(R.id.iv_icon)
        iconNameTv = findViewById(R.id.tv_icon_name)

    }

}