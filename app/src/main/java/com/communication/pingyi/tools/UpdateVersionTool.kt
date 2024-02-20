package com.communication.pingyi.tools

import androidx.fragment.app.FragmentActivity
import com.communication.lib_core.PyUpdateDialog
import com.communication.lib_core.R
import com.communication.lib_core.tools.Constance.Companion.appFolderApkName
import com.communication.lib_core.tools.Utils
import com.communication.lib_http.httpdata.version.VersionModel
import listener.Md5CheckResultListener
import listener.UpdateDownloadListener
import model.UpdateConfig
import update.UpdateAppUtils

/**
 * Created by LG
 * on 2022/4/12  9:25
 * Description：
 */
object UpdateVersionTool {
    var dialog: PyUpdateDialog? = null

    fun hasNewVersion(context: FragmentActivity, info: VersionModel): Boolean {
        if (info.apkPath.isNullOrBlank()) {
            return false
        }
        if (info.versionCode == null) {
            return false
        }
        val currentVersionCode = Utils.getVersionCode(context)
        if (currentVersionCode >= info.versionCode!!.toLong()) {
            return false
        }
        return true
    }

    fun update(context: FragmentActivity, info: VersionModel) {
        if (hasNewVersion(context, info)) {
            val updateConfig = UpdateConfig().apply {
                force = false
                checkWifi = false
                needCheckMd5 = false
                isShowNotification = false
                notifyImgRes = R.mipmap.ic_launcher
                alwaysShowDownLoadDialog = false
                alwaysShow = false
                showDownloadingToast = false
                thisTimeShow = false
                apkSavePath = (context.externalCacheDir?.path ?: "") + "/" + appFolderApkName
                apkSaveName = appFolderApkName
            }

            val forceInstall = info.forced == 1
            var msg = ""
            val apkUrl = info.apkPath!!
            info.versionName?.let {
                msg += String.format(context.getString(R.string.update_version_code), it)
            }
//            Conver.getApkSizeShow(info.apkSize)?.let {
//                msg += String.format(context.getString(R.string.update_version_apk_size), it)
//            }
            info.instructions?.let {
                msg += String.format(context.getString(R.string.update_version_content), it)
            }
            dialog = PyUpdateDialog(context)
                .canCancel(!forceInstall)
                .setSingle(forceInstall)
                .setTitle(context.getString(R.string.update_version_title))
                .setMessage(msg)
                .setNegative(context.getString(R.string.update_version_cancel))
                .setPositive(context.getString(R.string.update_version_confirm))
                .setPositiveCallBack {
                    UpdateAppUtils.download()
                }
            dialog?.show()

            UpdateAppUtils
                .init(context)
                .getInstance()
                .apkUrl(apkUrl)
                .updateConfig(updateConfig)
//            .uiConfig(uiConfig)
                .setUpdateDownloadListener(object : UpdateDownloadListener {
                    override fun onDownload(progress: Int) {
                        dialog?.setProgress(progress)
                    }

                    override fun onError(e: Throwable) {
                        dialog?.dismiss()
                    }

                    override fun onFinish() {
                        dialog?.dismiss()
                    }

                    override fun onStart() {
                    }
                })
                .setMd5CheckResultListener(object : Md5CheckResultListener {
                    override fun onResult(result: Boolean) {
                        // true：检验通过，false：检验失败
                    }
                })
        }
    }

    fun clear() {
        dialog?.dismiss()
    }


}