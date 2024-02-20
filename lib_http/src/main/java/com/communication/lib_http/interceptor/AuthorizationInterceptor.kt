package com.communication.lib_http.interceptor

import com.communication.lib_http.api.SERVER_BASE_URL
import com.communication.lib_http.base.MMKVTool
import com.communication.lib_http.token.TokenService
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset

/**
 * Created by LG
 * on 2022/3/4  10:22
 * Description：
 */
class AuthorizationInterceptor : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.url.toString().contains("/app/login")) {
            return chain.proceed(request)
        }
        var response = chain.proceed(addTokenHeaders(request.newBuilder()))
        if (isTokenExpired(response)) {
            //处理过期
            if (getNewToken()) {
                response = chain.proceed(addTokenHeaders(request.newBuilder()))
            }
        }
        return response
    }

    private fun addTokenHeaders(builder: Request.Builder): Request {
        return builder.addHeader(
            "Authorization",
//            "123"
            MMKVTool.getToken()
        ).build()
    }

    private fun isTokenExpired(response: Response): Boolean {
        var b = false
        try {
            if (!response.isSuccessful) {
                val responseBody = response.body
                val source = responseBody?.source()

                source?.request(Long.MAX_VALUE)
                val buffer = source?.buffer
                val charset = Charset.defaultCharset()
                val bodyString = buffer?.clone()?.readString(charset)
                bodyString?.let {
                    if (it.contains("token expired")
                        || it.contains("token invalid")
                        || it.contains("token missing")
                    ) {
                        b = true
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            b = true
        }
        return b
    }

    private fun getNewToken(): Boolean {
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(TokenService::class.java)
        /*val response = api.getToken(
            LoginInfo(
                MMKVTool.getUsername(),
                MMKVTool.getPassword()
            )
        ).execute()*/
        val isSuccess = false
        /*response.body()?.code?.let { code ->
            if (code == "SUCCESS") {
                response.body()?.data?.let {
                    if (it.jwt != null) {
                        Log.e("====", "====重试获取的token：${it.jwt}")
                        MMKVTool.saveToken(it.jwt)
                        isSuccess = true
                    }
                }
            }
        }*/
        return isSuccess
    }

}