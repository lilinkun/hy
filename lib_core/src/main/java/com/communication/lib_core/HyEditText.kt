package com.communication.lib_core

import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.communication.lib_core.tools.Utils
import java.net.PasswordAuthentication

class HyEditText : ConstraintLayout{

    private lateinit var tvName : TextViewYH
    private lateinit var etPsd : EditText
    private lateinit var ivPsd : ImageView

    constructor(context : Context?) : super(context!!){
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init(context)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.HyEditText)
        val text = ta.getString(R.styleable.HyEditText_hy_tv_name)
        val evHintText = ta.getString(R.styleable.HyEditText_hy_ev_hint_name)
        val img = ta.getDrawable(R.styleable.HyEditText_hy_img_icon)

        ta.recycle()
        text?.apply {
            tvName.text = this
        }

        evHintText?.apply {
            etPsd.hint = this
        }

//        if (img == null) {
//            ivPsd.visibility = View.GONE
//        } else {
//            ivPsd.visibility = View.VISIBLE
//            ivPsd.setImageDrawable(img)
//        }

    }

    private fun init(context: Context) {
        (context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater).inflate(R.layout.hy_edittext,this)
        tvName = findViewById(R.id.tv_psd)
        etPsd = findViewById(R.id.ev_psd)
        ivPsd = findViewById(R.id.iv_psd_icon)

        ivPsd.setOnClickListener{
            getPsdVisible()
        }
    }

    fun getText() : String{
        return etPsd.text!!.toString()
    }

    fun getPsdVisible() {
//        etPsd.transformationMethod = PasswordTransformationMethod.getInstance()

        if(etPsd.transformationMethod == PasswordTransformationMethod.getInstance()){
            etPsd.transformationMethod = HideReturnsTransformationMethod.getInstance()
            ivPsd.setImageResource(R.drawable.ic_show_psd)
        }else if(etPsd.transformationMethod == HideReturnsTransformationMethod.getInstance()){
            etPsd.transformationMethod = PasswordTransformationMethod.getInstance()
            ivPsd.setImageResource(R.drawable.ic_hidden_psd)
        }

    }


    fun isBlank(): Boolean {
        if (etPsd.text!!.isBlank()){
            return true
        }else{
            return false
        }
    }

    fun isTip() : Boolean{
        if (Utils.isPasswordValid(etPsd.text!!.toString())){
            return true
        }else{
            return true
        }
    }
}