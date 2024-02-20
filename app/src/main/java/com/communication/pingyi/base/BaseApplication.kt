package com.communication.pingyi.base

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import cn.jpush.android.api.JPushInterface
import com.communication.lib_http.base.MMKVTool.initializeMMKV
import com.communication.pingyi.R
import com.communication.pingyi.di.allModule
import com.communication.pingyi.ext.initTimber
import com.jeremyliao.liveeventbus.LiveEventBus
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.scwang.smart.refresh.layout.wrapper.RefreshFooterWrapper
import com.scwang.smart.refresh.layout.wrapper.RefreshHeaderWrapper
import io.rong.imkit.RongIM
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created by LG
 * on 2022/3/4  15:30
 * Description：
 */
open class BaseApplication : Application(){


    override fun onCreate() {
        super.onCreate()
        mApplication = this
        initTimber()
        initKoin()
        initRefreshLayout()
        initLiveEventBus()
        initMMKV()
//        initJPush()
        initIm()
    }

    private fun initJPush() {
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
    }

    private fun initIm(){
        val appKey = "vnroth0kvgx5o"
        val enablePush = true
        RongIM.init(this,appKey, enablePush)
    }


    private fun initKoin() {

        startKoin {

            androidContext(this@BaseApplication)
            modules(allModule)

        }

    }


    private fun initRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.white, R.color.white)
            RefreshHeaderWrapper(
                MaterialHeader(context)
                    .setColorSchemeResources(R.color.blue)
            )
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator{ context, layout->
            layout.setPrimaryColorsId(R.color.white, R.color.white)
                //指定为经典Footer，默认是 BallPulseFooter
                RefreshFooterWrapper(
                    MaterialHeader(context)
                        .setColorSchemeResources(R.color.event_grey)
                )
        }
    }

    private fun initMMKV(){
        initializeMMKV(this)
    }


    private fun initLiveEventBus() {
        LiveEventBus.config().lifecycleObserverAlwaysActive(true).autoClear(true);
    }

}

lateinit var mApplication: Application

object AppContext : ContextWrapper(mApplication)