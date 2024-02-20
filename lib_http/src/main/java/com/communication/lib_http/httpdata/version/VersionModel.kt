package com.communication.lib_http.httpdata.version

data class VersionModel(
    val appName: String?,       //应用名称
    val apkPath: String?,   //下载地址
    val forced: Int?,           //是否强升 1：是，0 否
    val md5: String?,
    val versionCode: Int?,       //版本号
    val versionName: String?,       //版本名称
    val instructions: String?,  //更新说明
)