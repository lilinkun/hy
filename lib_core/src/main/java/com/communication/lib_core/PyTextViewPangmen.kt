package com.communication.lib_core

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class PyTextViewPangmen : AppCompatTextView {

    constructor(context: Context?) : super(context!!) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init(context)
    }

    private fun init(context: Context?) {
        typeface = Typeface.createFromAsset(context?.resources?.assets, "fonts/PangMenZhengDao3.0.ttf");
    }
}