package com.communication.pingyi.base

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import com.communication.lib_core.tools.StatusBarUtil
import java.util.ArrayList

/**
 * Created by LG
 * on 2022/3/14  11:22
 * Description：
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.transparencyBar(this); //设置状态栏全透明
        StatusBarUtil.StatusBarLightMode(this); //设置白底黑字
    }

    override fun onStart() {
        super.onStart()
        requestPermission()
    }

    private var isPermissionRequested = false
    private fun requestPermission() {
        if (!isPermissionRequested) {
            isPermissionRequested = true
            val permissionsList = ArrayList<String>()
            val permissions = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.REQUEST_INSTALL_PACKAGES,
            )
            for (perm in permissions) {
                if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        PackageManager.PERMISSION_GRANTED != checkSelfPermission(perm)
                    } else {
                        TODO("VERSION.SDK_INT < M")
                    }
                ) {
                    permissionsList.add(perm)
                    // 进入到这里代表没有权限.
                }
            }
            if (permissionsList.isNotEmpty()) {
                val strings = arrayOfNulls<String>(permissionsList.size)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissionsList.toArray(strings), 0)
                }
            }
        }
    }

    private var bQuit = false
    open var handler: Handler? = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(paramAnonymousMessage: Message) {
            if (bQuit) return
            if (onMsg(paramAnonymousMessage)) return
        }
    }

    open fun onMsg(paramAnonymousMessage: Message?): Boolean {
        return false
    }

    override fun onDestroy() {
        bQuit = true
        handler = null
        super.onDestroy()
    }



}