package com.communication.lib_core

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 * Created by LG
 * on 2022/3/14  8:59
 * Description：
 */
class EditTextYH : AppCompatEditText {

    constructor(context : Context?) : super(context!!){
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init(context)
    }

    private fun init(context: Context?) {
//        typeface = BtiTypeface.getTypeface()
    }
}