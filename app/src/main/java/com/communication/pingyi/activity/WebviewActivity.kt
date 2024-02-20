package com.communication.pingyi.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebSettings.LayoutAlgorithm.SINGLE_COLUMN
import android.webkit.WebView
import com.communication.lib_core.PyAppDialog
import com.communication.lib_core.PyLoad
import com.communication.lib_core.SelectDialog
import com.communication.lib_core.tools.GPSUtils
import com.communication.pingyi.R
import com.communication.pingyi.base.BaseActivity
import com.communication.pingyi.ext.pyToastShort
import com.communication.pingyi.tools.AndroidJavascriptInterface
import com.communication.pingyi.tools.PhotoUtils
import com.tencent.smtt.sdk.CacheManager
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.io.File


/**
 * Created by LG
 * on 2022/3/10  11:40
 * Description：
 */
@RuntimePermissions
class WebviewActivity : BaseActivity() {

    private var mSelectPhotoDialog: SelectDialog? = null
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null

    lateinit var webView : WebView
    lateinit var progressBar : PyLoad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        initView()
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    fun applyPermission() {

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)

    }

    fun initView() {
//        val url = "http://www.baidu.com"


        val url = intent.getStringExtra("url")

        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)

//        url = "file:///android_asset/index.html"

        initWebView()


        if (url != null) {
            webView.loadUrl(url)
        }

        if (!GPSUtils.isOPen(this)) {
            showGPSDialog()
        }

    }


    private fun showGPSDialog(requestCode: Int = 888) {
        PyAppDialog(this)
            .setSingle(false)
            .canCancel(true)
            .setTitle(getString(R.string.location_title))
            .setMessage(getString(R.string.location_message))
            .setNegative(getString(R.string.location_cancel))
            .setPositive(getString(R.string.location_ok))
            .setPositiveCallBack {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, requestCode)
            }
            .show()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PhotoUtils.RESULT_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            //拍照并确定
            compressPicture(PhotoUtils.PATH_PHOTO)
        } else if (requestCode == PhotoUtils.RESULT_CODE_PHOTO && resultCode == Activity.RESULT_OK) {
            //相册选择并确定
            val result = data?.data
            val path = result?.let { PhotoUtils.getPath(this, it) }
            if (path == null) {
                mFilePathCallback?.onReceiveValue(null)
            } else {
                compressPicture(path)
            }
        } else {
            mFilePathCallback?.onReceiveValue(null)
        }
    }

    /**
     * 压缩图片
     */
    private fun compressPicture(path: String) {
        this?.let {
            PhotoUtils.compressPicture(it, path, object : PhotoUtils.OnPictureCompressListener() {
                override fun onSuccess(file: File) {
                    mFilePathCallback?.onReceiveValue(arrayOf(Uri.fromFile(file)))
                }

                override fun onError(e: Throwable) {
                    mFilePathCallback?.onReceiveValue(null)
                }
            })
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(){
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        //自适应屏幕
        settings.layoutAlgorithm = SINGLE_COLUMN
        settings.loadWithOverviewMode = true
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.databaseEnabled = true
        settings.setSupportZoom(false)
        webView.addJavascriptInterface(AndroidJavascriptInterface(this), "Android")
        webView.isHorizontalScrollBarEnabled = false
        webView.isVerticalScrollBarEnabled = false
        webView.webChromeClient = webViewChromeClient
    }

    private val webViewChromeClient = object : WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            if (newProgress == 100) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE)
                }
            } else {
                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE)
                }
            }
        }

        override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>,
                                       fileChooserParams: FileChooserParams
        ): Boolean {
            mFilePathCallback = filePathCallback
            val acceptTypes = fileChooserParams.acceptTypes
            if (acceptTypes.contains("image/*")) {
                showSelectDialog()
            }
            return true
        }
    }

    /**
     * 显示相册/拍照选择对话框
     */
    private fun showSelectDialog() {
        if (mSelectPhotoDialog == null) {
            mSelectPhotoDialog = SelectDialog(this, View.OnClickListener { view ->
                when (view.id) {
                    R.id.tv_camera -> startCamera()
                    R.id.tv_photo -> startAlbum()
                    //不管选择还是不选择，必须有返回结果，否则就会调用一次
                    R.id.tv_cancel -> {
                        mFilePathCallback?.onReceiveValue(null)
                        mFilePathCallback = null
                    }
                }
            })
        }
        mSelectPhotoDialog?.show()
    }


    /**
     * 打开相册
     */
    private fun startAlbum() {
         PhotoUtils.startAlbum(this)
    }

    /**
     * 拍照
     */
    fun startCamera() {
        PhotoUtils.startCamera(this)
    }


    override fun onDestroy() {
        if (webView != null) {
            webView.stopLoading()
            webView.onPause()
            webView.settings.javaScriptEnabled = false
            webView.clearHistory()
            webView.clearView()
            webView.removeAllViews()
            webView.destroy()
            val file = CacheManager.getCacheFileBaseDir()
            if (file != null && file.exists() && file.isDirectory) {
                for (item in file.listFiles()) {
                    item.delete()
                }
                file.delete()
            }
            deleteDatabase("webview.db")
            deleteDatabase("webviewCache.db")
        }
        super.onDestroy()
    }

    fun getLocationLL() : String{
        val location: Location? = getLastKnownLocation()
        if (location != null) {
            val locationStr = """
            纬度：${location.getLatitude().toString()}
            经度：${location.getLongitude()}
            """.trimIndent()
            /*runOnUiThread {
                val method = "javascript:gpsResult('$locationStr')"
                webView.loadUrl(method)
            }*/
            return locationStr
        } else {
            pyToastShort("位置信息获取失败")
            return ""
        }
    }


    //得到位置对象
    private fun getLastKnownLocation(): Location? {
        //获取地理位置管理器
        val mLocationManager: LocationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager
        val providers: List<String> = mLocationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            @SuppressLint("MissingPermission") val l: Location =
                mLocationManager.getLastKnownLocation(provider)
                    ?: continue
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l
            }
        }
        return bestLocation
    }


}

