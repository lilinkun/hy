package com.communication.pingyi.ui.webview

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.communication.lib_core.PyAppDialog
import com.communication.lib_core.SelectDialog
import com.communication.lib_core.tools.GPSUtils
import com.communication.pingyi.R
import com.communication.pingyi.base.AppContext.getSystemService
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentWebviewBinding
import com.communication.pingyi.ext.pyToastShort
import com.communication.pingyi.tools.AndroidJavascriptInterface
import com.communication.pingyi.tools.PhotoUtils
import java.io.File


/**
 * Created by LG
 * on 2022/4/11  15:59
 * Description：
 */
class WebviewFragment : BaseFragment<FragmentWebviewBinding>() {

    private var mSelectPhotoDialog: SelectDialog? = null
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null

    private val args: WebviewFragmentArgs by navArgs()

    override fun getLayoutResId(): Int = R.layout.fragment_webview

    override fun initView() {
//        val url = "http://www.baidu.com"
        var url = "http://10.168.200.2:9883/#/"

//        if (args.url.isNullOrEmpty()) {
//            url = "http://10.168.200.2:9883/#/"
//        } else {
//            url = args.url
//        }

//        url = "file:///android_asset/index.html"

        initWebView()


        binding.webView.apply {
            /*settings.defaultTextEncodingName = "utf-8"
            settings.allowFileAccess = true
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS;
            settings.supportZoom()
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            settings.useWideViewPort = true
            settings.supportMultipleWindows()
            settings.setAppCacheEnabled(true)
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            settings.setGeolocationEnabled(true)
            settings.setAppCachePath(context.cacheDir.absolutePath)*/

            loadUrl(url)
        }

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
        if (requestCode == PhotoUtils.RESULT_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            //拍照并确定
            compressPicture(PhotoUtils.PATH_PHOTO)
        } else if (requestCode == PhotoUtils.RESULT_CODE_PHOTO && resultCode == Activity.RESULT_OK) {
            //相册选择并确定
            val result = data?.data
            val path = result?.let { activity?.let { it1 -> PhotoUtils.getPath(it1, it) } }
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
        val settings = binding.webView.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        //自适应屏幕
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN;
        settings.loadWithOverviewMode = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.setSupportZoom(false)
        binding.webView.addJavascriptInterface(AndroidJavascriptInterface(requireActivity().applicationContext), "Android")
        binding.webView.isHorizontalScrollBarEnabled = false
        binding.webView.isVerticalScrollBarEnabled = false
        binding.webView.webChromeClient = webViewChromeClient
    }

    private val webViewChromeClient = object : WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            if (newProgress == 100) {
                if (binding.progressBar != null) {
                    binding.progressBar.setVisibility(View.GONE)
                }
            } else {
                if (binding.progressBar != null) {
                    binding.progressBar.setVisibility(View.VISIBLE)
                }
            }
        }

        override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>,
                                       fileChooserParams: FileChooserParams): Boolean {
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
            mSelectPhotoDialog = SelectDialog(this.requireActivity(), View.OnClickListener { view ->
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
        activity?.let { PhotoUtils.startAlbum(it) }
    }

    /**
     * 拍照
     */
    fun startCamera() {
        activity?.let { PhotoUtils.startCamera(it) }
    }


    override fun onDestroy() {
        if (binding.webView != null) {
            binding.webView.stopLoading()
            binding.webView.onPause()
            binding.webView.settings.javaScriptEnabled = false
            binding.webView.clearHistory()
            binding.webView.clearView()
            binding.webView.removeAllViews()
            binding.webView.destroy()
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

