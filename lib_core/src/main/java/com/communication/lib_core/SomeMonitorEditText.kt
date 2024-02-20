package com.communication.lib_core

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.card.MaterialCardView
import java.util.*

/**
 * Created by LG
 * on 2022/3/23  15:17
 * Description：
 */
class SomeMonitorEditText : TextWatcher{

    lateinit var btn : PyButton
    lateinit var edits : LinkedList<EditTextYH>

    constructor(btn: PyButton, edits: LinkedList<EditTextYH>) {
        this.btn = btn
        this.edits = edits
        for (edit in edits){
            edit?.let {
                edit.addTextChangedListener(this)
            }
        }
    }


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    @SuppressLint("Range")
    override fun afterTextChanged(s: Editable?) {
        for(edit in edits){
            if(edit.text.toString().trim().length == 0){
                btn.alpha = 0.5F
                btn.isEnabled = false
                return
            }else{
                btn.alpha = 1F
                btn.isEnabled = true
            }
        }
    }
}