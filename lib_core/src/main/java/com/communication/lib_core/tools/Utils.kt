package com.communication.lib_core.tools

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.*
import java.lang.Exception
import java.lang.StringBuilder
import java.text.DecimalFormat


object Utils {
    fun getVersionName(context: Context): String {
        return context.packageManager.getPackageInfo(context.packageName, 0).versionName
    }

    fun getVersionCode(context: Context): Long {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            context.packageManager.getPackageInfo(context.packageName, 0).longVersionCode
        } else {
            context.packageManager.getPackageInfo(context.packageName, 0).versionCode.toLong()
        }
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    val systemLanguage: String
        get() = Locale.getDefault().language

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return  语言列表
     */
    val systemLanguageList: Array<Locale>
        get() = Locale.getAvailableLocales()

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    val systemVersion: String
        get() = Build.VERSION.RELEASE

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    val systemModel: String
        get() = Build.MODEL

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    val deviceBrand: String
        get() = Build.BRAND

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return  手机IMEI
     */
    fun getIMEI(ctx: Context): String? {
        val tm = ctx.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
        return if (tm != null) {
            tm.deviceId
        } else null
    }


    fun getMacAddress(ctx: Context) : String?{
        var macAddress: String? = null
        val wifiManager =
            ctx.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = wifiManager?.connectionInfo
        if (!wifiManager.isWifiEnabled) {
            //必须先打开，才能获取到MAC地址
            wifiManager.isWifiEnabled = true
            wifiManager.isWifiEnabled = false
        }
        if (null != info) {
            if (ActivityCompat.checkSelfPermission(
                    ctx,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return ""
            }
            macAddress = info.macAddress
        }
        return macAddress
    }

    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID);
    }

    fun numberThousandseparator(num : Int) : String{
        return DecimalFormat.getNumberInstance().format(num)
    }


    fun isPasswordValid(password: String): Boolean {
        val regex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).{6,20}$"
        return password.matches(regex.toRegex())
    }

    fun serializeToString(obj: Serializable): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(obj)
        objectOutputStream.close()
        return byteArrayOutputStream.toString()
    }

    fun deserializeFromString(serializedString: String): Serializable? {
        val byteArrayInputStream = ByteArrayInputStream(serializedString.toByteArray(Charsets.UTF_8))
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as? Serializable
        objectInputStream.close()
        return obj
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun convertHtmlToSpannable(htmlContent: String, context : Context): Spannable {
        val spannableBuilder = SpannableStringBuilder()

        val htmlSpanned = Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT, object : Html.ImageGetter {
            override fun getDrawable(source: String?): Drawable? {
                return source?.let {
                    val localResourcePath = convertImagePathToLocalResource(it)
                    val resourceId = getResourceIdFromSource(localResourcePath,context)
                    val drawable = resourceId?.let { resId -> ContextCompat.getDrawable(context, resId) }
                    drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                    drawable
                }
            }
        }, null)

        spannableBuilder.append(htmlSpanned)

        return spannableBuilder
    }

    fun convertImagePathToLocalResource(imagePath: String): String {
        // 从路径中提取文件名
        val file = imagePath.substringAfterLast("/")
        var fileName = file.split(".")[0]
        if (!fileName.contains("emoji")){
            fileName = "emoji_" + fileName
        }
        // 构建本地资源路径
        return "R.raw.$fileName"
    }

    fun getResourceIdFromSource(source: String,context: Context): Int? {
        // 根据资源路径获取对应的资源 ID
        return try {
            val fieldName = source.substringAfterLast(".")
            val packageName = context.packageName
            val clazz = Class.forName(packageName + ".R\$raw")
            val field = clazz.getField(fieldName)
            field.getInt(null)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}