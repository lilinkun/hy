package com.communication.lib_core

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Created by LG
 * on 2022/3/10  17:55
 * Description：
 */
class TextViewYH : AppCompatTextView{
    constructor(context: Context?) : super(context!!) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init(context)
    }

    private fun init(context: Context?) {
//        typeface = BtiTypeface.getTypeface()
        setTypeface(null,Typeface.BOLD)
    }
}