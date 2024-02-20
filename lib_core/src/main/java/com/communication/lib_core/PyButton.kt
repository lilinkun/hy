package com.communication.lib_core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.google.android.material.card.MaterialCardView

/**
 * Created by LG
 * on 2022/3/10  17:46
 * Description：
 */
class PyButton : MaterialCardView {

    private lateinit var txt: TextView


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
        val ta = context.obtainStyledAttributes(attrs,R.styleable.PyButton)
        val text = ta.getString(R.styleable.PyButton_py_button_name)
        val color = ta.getDrawable(R.styleable.PyButton_py_bg_color)
        ta.recycle()
        text?.apply {
            txt.text = this
        }
        color?.let { txt.background = it }
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        (context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE
        ) as LayoutInflater).inflate(R.layout.py_card_view, this)

        txt = findViewById(R.id.txt)
    }

    fun setName(str : String){
        txt.text = str
    }

    fun setBGColor(bgColor: Int) {
        txt.setBackgroundColor(bgColor)
    }


}