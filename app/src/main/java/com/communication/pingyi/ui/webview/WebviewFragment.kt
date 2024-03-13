package com.communication.pingyi.ui.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.communication.lib_core.PyAppDialog
import com.communication.lib_core.SelectDialog
import com.communication.lib_core.tools.EVENTBUS_EVENT_BACK
import com.communication.lib_core.tools.EVENTBUS_EVENT_BOTTOM
import com.communication.lib_core.tools.GPSUtils
import com.communication.pingyi.R
import com.communication.pingyi.base.AppContext.getSystemService
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentWebviewBinding
import com.communication.pingyi.ext.pyToast
import com.communication.pingyi.ext.pyToastShort
import com.communication.pingyi.tools.AndroidJavascriptInterface
import com.communication.pingyi.tools.PhotoUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import java.io.File


/**
 * Created by LG
 * on 2022/4/11  15:59
 * Description：
 */
class WebviewFragment : BaseFragment<FragmentWebviewBinding>() {

    private var mSelectPhotoDialog: SelectDialog? = null
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null

    lateinit var webview : WebView

//    private val homeUrl = "http://192.168.120.40:9000/#/"
//    private val homeUrl1 = "http://192.168.120.40:9000/#/index"
    private val homeUrl = "http://10.168.200.2:9883/#/"
    private val homeUrl1 = "http://10.168.200.2:9883/#/index"

    var type : Int = 0

    private val args: WebviewFragmentArgs by navArgs()

    override fun getLayoutResId(): Int = R.layout.fragment_webview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LiveEventBus.get(EVENTBUS_EVENT_BACK,Boolean::class.java).observe(this) {
            if (it) {
                if (webview.canGoBack()) {
                    webview.goBack()
                }
            }
        }
    }

    override fun initView() {
//        val url = "http://www.baidu.com"

//        if (args.url.isNullOrEmpty()) {
//            url = "http://10.168.200.2:9883/#/"
//        } else {
//            url = args.url
//        }

//        url = "file:///android_asset/index.html"

        initWebView()


        if (!GPSUtils.isOPen(requireContext())) {
            showGPSDialog()
        }

    }

    override fun observeViewModels() {
    }


    private fun showGPSDialog(requestCode: Int = 888) {
        PyAppDialog(requireContext())
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
        if ((requestCode == PhotoUtils.RESULT_CODE_CAMERA || requestCode == PhotoUtils.RESULT_CODE_VIDEO_CAMERA) && resultCode == Activity.RESULT_OK) {
            //拍照并确定
            if (requestCode == PhotoUtils.RESULT_CODE_CAMERA){
                compressPicture(PhotoUtils.PATH_PHOTO)
            }else{
                val file = File(PhotoUtils.PATH_PHOTO)
                mFilePathCallback?.onReceiveValue(arrayOf(Uri.fromFile(file)))
            }

        }else if ((requestCode == PhotoUtils.RESULT_CODE_PHOTO || requestCode == PhotoUtils.RESULT_CODE_VIDEO) && resultCode == Activity.RESULT_OK) {
            //相册选择并确定
            val result = data?.data
            val path = result?.let { activity?.let { it1 -> PhotoUtils.getPath(it1, it) } }
            if (path == null) {
                mFilePathCallback?.onReceiveValue(null)
            } else {
                if (requestCode == PhotoUtils.RESULT_CODE_PHOTO){
                    compressPicture(path)
                }else{
                    val file = File(path)
                    mFilePathCallback?.onReceiveValue(arrayOf(Uri.fromFile(file)))
                }
            }
        } else {
            mFilePathCallback?.onReceiveValue(null)
        }
    }

    /**
     * 压缩图片
     */
    private fun compressPicture(path: String) {
        activity?.let {
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
        webview = binding.webView

        val settings = webview.settings
        settings.javaScriptEnabled = true
        //自适应屏幕
//        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN;
        settings.useWideViewPort = true;
        settings.loadWithOverviewMode = true;
        settings.loadWithOverviewMode = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE

        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        settings.setSupportZoom(true)
        webview.addJavascriptInterface(AndroidJavascriptInterface(requireActivity().applicationContext), "Android")
        webview.webViewClient = webviewClient
        webview.webChromeClient = webViewChromeClient

        webview.loadUrl(homeUrl)
    }

    private val webviewClient = object : WebViewClient(){

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
                if (url == homeUrl || url == homeUrl1){
                    LiveEventBus.get(EVENTBUS_EVENT_BOTTOM).post(true)
                }else {
                    LiveEventBus.get(EVENTBUS_EVENT_BOTTOM).post(false)
                }
        }

    }

    private val webViewChromeClient = object : WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            if (newProgress == 100) {
                binding.progressBar.setVisibility(View.GONE)
            } else {
                binding.progressBar.setVisibility(View.VISIBLE)
            }
        }

        override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>,
                                       fileChooserParams: FileChooserParams): Boolean {
            mFilePathCallback = filePathCallback
            val acceptTypes = fileChooserParams.acceptTypes
            if (acceptTypes.contains("image/*")) {
                type = 0
                showSelectDialog()
            }else if(acceptTypes.contains("video/*")){
                type = 1
                showSelectDialog()
            }
            return true
        }

//        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
//            super.onShowCustomView(view, callback)
//            // 进入全屏播放模式
//            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//            // 将全屏视图添加到视图中
//            fullscreenContainer.addView(view);
//            // 隐藏其他界面元素
//            webview.setVisibility(View.GONE);
//            // 设置全屏标志位
//            isFullscreen = true;
//        }
    }

    /**
     * 显示相册/拍照选择对话框
     */
    private fun showSelectDialog() {
        if (mSelectPhotoDialog == null) {
            mSelectPhotoDialog = SelectDialog(this.requireActivity(),type) { view ->
                when (view.id) {
                    R.id.tv_camera -> startCamera(type)
                    R.id.tv_photo -> startAlbum(type)
                    //不管选择还是不选择，必须有返回结果，否则就会调用一次
                    R.id.tv_cancel -> {
                        mFilePathCallback?.onReceiveValue(null)
                        mFilePathCallback = null
                    }
                }
            }
        }else{
            mSelectPhotoDialog!!.setType(type)
        }
        mSelectPhotoDialog?.show()
    }


    /**
     * 打开相册
     */
    private fun startAlbum(type: Int) {
//        activity?.let { PhotoUtils.startAlbum(it) }

        PhotoUtils.startAlbum(this,type)
    }

    /**
     * 拍照
     */
    fun startCamera(type: Int) {
//        activity?.let { PhotoUtils.startCamera(it) }
        if(type == 0){
            PhotoUtils.startCamera(this)
        }else{
            PhotoUtils.startVideoCamera(this)
        }
    }


    override fun onDestroy() {
        webview.stopLoading()
        webview.onPause()
        webview.settings.javaScriptEnabled = false
        webview.clearHistory()
        webview.clearView()
        webview.removeAllViews()
        webview.destroy()
        super.onDestroy()
    }

    fun getLocationLL() : String{
        val location: Location? = getLastKnownLocation()
        if (location != null) {
            val locationStr = """
            纬度：${location.latitude.toString()}
            经度：${location.longitude}
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
            getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
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

