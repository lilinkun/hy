package com.communication.lib_core

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 * Created by LG
 * on 2022/4/6  15:58
 * Description：
 */
class EditTextSearch : AppCompatEditText {

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