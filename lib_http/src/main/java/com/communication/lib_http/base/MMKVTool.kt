package com.communication.lib_http.base

import android.content.Context
import com.tencent.mmkv.MMKV

/**
 * Created by LG
 * on 2022/3/4  11:22
 * Description：
 */
object MMKVTool {
    private val access_token = "access_token"
    private val username = "username"
    private val password = "password"
    private val nick_name = "nick_name"
    private val icon_url = "icon_url"

    private val case_cx = "case_cx"
    private val case_zf = "case_zf"
    private val case_hy = "case_hy"
    private val inspection_zf = "inspection_zf"
    private val inspection_hy = "inspection_hy"
    private val sysCacheMap = "sysCacheMap"

    private val brand = "brand"
    private val deviceId = "deviceId"
    private val reconnect = "reconnect"

    fun initializeMMKV(context: Context) {
        MMKV.initialize(context)
    }

    fun clearAll() {
        saveToken("")
        saveUsername("")
        savePassword("")
        saveNickName("")
        saveIconUrl("")
        saveReconnect(false)
    }

    fun saveToken(token: String) {
        MMKV.defaultMMKV().encode(access_token, token)
    }

    fun getToken(): String {
        return MMKV.defaultMMKV().decodeString(access_token, "")
    }

    fun saveUsername(uname: String) {
        MMKV.defaultMMKV().encode(username, uname)
    }

    fun getUsername(): String {
        return MMKV.defaultMMKV().decodeString(username, "")
    }

    fun saveIconUrl(iconUrl: String) {
        MMKV.defaultMMKV().encode(icon_url, iconUrl)
    }

    fun getIconUrl(): String {
        return MMKV.defaultMMKV().decodeString(icon_url, "")
    }

    fun saveNickName(nickname: String) {
        MMKV.defaultMMKV().encode(nick_name, nickname)
    }

    fun getNickName(): String {
        return MMKV.defaultMMKV().decodeString(nick_name, "")
    }

    fun savePassword(pwd: String) {
        MMKV.defaultMMKV().encode(password, pwd)
    }

    fun getPassword(): String {
        return MMKV.defaultMMKV().decodeString(password, "")
    }

    fun saveCaseCX(str: String) {
        MMKV.defaultMMKV().encode(case_cx, str)
    }

    fun getCaseCX(): String {
        return MMKV.defaultMMKV().decodeString(case_cx, "")
    }

    fun saveCaseZF(str: String) {
        MMKV.defaultMMKV().encode(case_zf, str)
    }

    fun getCaseZF(): String {
        return MMKV.defaultMMKV().decodeString(case_zf, "")
    }

    fun saveCaseHY(str: String) {
        MMKV.defaultMMKV().encode(case_hy, str)
    }

    fun getCaseHY(): String {
        return MMKV.defaultMMKV().decodeString(case_hy, "")
    }

    fun saveInspectionZF(str: String) {
        MMKV.defaultMMKV().encode(inspection_zf, str)
    }

    fun getInspectionZF(): String {
        return MMKV.defaultMMKV().decodeString(inspection_zf, "")
    }

    fun saveInspectionHY(str: String) {
        MMKV.defaultMMKV().encode(inspection_hy, str)
    }

    fun getInspectionHY(): String {
        return MMKV.defaultMMKV().decodeString(inspection_hy, "")
    }

    fun saveSysCacheMap(str : String){
        MMKV.defaultMMKV().encode(sysCacheMap,str)
    }

    fun saveReconnect(isConnect : Boolean){
        MMKV.defaultMMKV().encode(reconnect,isConnect)
    }

    fun getReconnect() : Boolean{
        return MMKV.defaultMMKV().decodeBool(reconnect,false)
    }

    fun saveBrand(str: String){
        MMKV.defaultMMKV().encode(brand,str)
    }

    fun getBrand() : String{
        return MMKV.defaultMMKV().decodeString(brand)
    }

    fun saveDeviceId(str : String){
        MMKV.defaultMMKV().encode(deviceId,str)
    }

    fun getDeviceId() : String{
        return MMKV.defaultMMKV().decodeString(deviceId)
    }


}