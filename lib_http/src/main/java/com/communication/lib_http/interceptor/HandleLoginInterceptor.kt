package com.communication.lib_http.interceptor

import com.communication.lib_http.exception.ResultException
import okhttp3.Response
import org.json.JSONObject

/**
 * Created by LG
 * on 2022/5/19  14:43
 * Description：
 */
class HandleLoginInterceptor : ResponseBodyInterceptor() {

    override fun intercept(response: Response, url: String, body: String): Response {
        var jsonObject : JSONObject? = null
        try {
            jsonObject = JSONObject(body)
        }catch (e : Exception){
            e.printStackTrace()
        }

        if (jsonObject != null){
            if (jsonObject.optInt("code",-1) != 200){
                throw ResultException(jsonObject.optString("data").toInt(),jsonObject.optString("msg"))
            }
        }
        return response

    }

}