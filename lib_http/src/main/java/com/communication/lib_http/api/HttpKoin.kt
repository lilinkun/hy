package com.communication.lib_http.api

import android.util.Log
import com.communication.lib_http.BuildConfig
import com.communication.lib_http.base.BaseModel
import com.communication.lib_http.interceptor.AuthorizationInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by LG
 * on 2022/3/4  9:53
 * Description：
 */

const val SERVER_BASE_URL = "http://10.168.200.3:8080"
//const val SERVER_BASE_URL = "http://192.168.40.75:8080"
//const val SERVER_BASE_URL = "http://192.168.40.141:9001"
//const val SERVER_BASE_URL = "http://36.158.43.42:20091"
//const val SERVER_BASE_URL = "http://192.168.40.94:8080"
//const val SERVER_BASE_URL = "http://192.168.40.92:8888"
//const val SERVER_BASE_URL = "http://192.168.1.227:8080"
//const val SERVER_BASE_URL = "http://192.168.1.227:9200"
//const val SERVER_BASE_URL = "http://192.168.1.236:8080"
//const val SERVER_BASE_URL = "http://192.168.1.240:9201"

//const val WEB_EVENT = "http://192.168.1.204:8088/#/event/overview"
//const val WEB_RUNTIME = "http://192.168.1.204:8088/#/runtime/overview"
//const val WEB_MESSAGE = "http://192.168.1.204:8088/#/event/detail?id="
//const val WEB_EVENT = "http://192.168.40.94:8080/#/event/overview"
//const val WEB_RUNTIME = "http://192.168.40.94:8080/#/runtime/overview"
//const val WEB_MESSAGE = "http://192.168.40.94:8080/#/event/detail?id="
const val WEB_EVENT = "http://192.168.40.141:8200/#/event/overview"
const val WEB_RUNTIME = "http://192.168.40.141:8200/#/runtime/overview"
const val WEB_MESSAGE = "http://10.168.200.2:9883/#/eventAssignDetails?eventId="

const val TIME_OUT = 15L

var mBaseModel: BaseModel<Int>? = null


val httpModule = module {
    single {
        val httpLoggingInterceptor = HttpLoggingInterceptor { message ->
            Log.w(
                "httpLoggingInterceptor", message
            )

            if(message.contains("\"data\":401") && message.contains("\"code\":500")){
                var g = Gson().fromJson(message,BaseModel::class.java)
                mBaseModel = g as BaseModel<Int>
            }

        }
        httpLoggingInterceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        httpLoggingInterceptor
    }

    single {

        OkHttpClient.Builder()
//            .addInterceptor(HandleLoginInterceptor())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(AuthorizationInterceptor())
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build()


    }
    single<Retrofit>() {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(get<OkHttpClient>())
            .baseUrl(SERVER_BASE_URL)
            .build()
    }

    single<LoginApi> {
        get<Retrofit>().create(LoginApi::class.java)
    }

    single<MeApi> {
        get<Retrofit>().create(MeApi::class.java)
    }

    single<HomeApi> {
        get <Retrofit>().create(HomeApi::class.java)
    }

    single<MessageApi> {
        get <Retrofit>().create(MessageApi::class.java)
    }

    single<ContactApi> {
        get <Retrofit>().create(ContactApi::class.java)
    }

    single<ChangePwdApi> {
        get <Retrofit>().create(ChangePwdApi::class.java)
    }

    single<VersionApi> {
        get <Retrofit>().create(VersionApi::class.java)
    }
}