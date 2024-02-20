package com.communication.pingyi.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.communication.lib_core.tools.EVENTBUS_TOAST_STRING
import com.communication.lib_core.tools.EVENTBUS_TOKEN_INVALID
import com.communication.lib_http.api.mBaseModel
import com.communication.lib_http.base.MMKVTool
import com.communication.pingyi.activity.LoginActivity
import com.communication.pingyi.ext.pyToast
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * Created by LG
 * on 2022/3/10  16:15
 * Description：
 */
abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    lateinit var binding: T
    var showSuccessToast = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LiveEventBus.get(
            EVENTBUS_TOAST_STRING,
            String::class.java).observe(this,{
                str->if (isActive() && showSuccessToast){
                    str?.let {
                        if(!it.contains("解析错误")) {
                            pyToast(it)
                        }
                    }
        }
        })


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,getLayoutResId(),container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeViewModels()

    }

    abstract fun getLayoutResId(): Int
    abstract fun initView()
    abstract fun observeViewModels()

    fun isActive() : Boolean {
        if (lifecycle.currentState == Lifecycle.State.RESUMED){
            return true
        }
        return false
    }

}