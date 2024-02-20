package com.communication.lib_http.factory

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.communication.lib_http.base.BaseErrorModel
import com.communication.lib_http.base.BaseModelResponse
import com.communication.lib_http.exception.ResultException
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonToken
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created by LG
 * on 2022/5/17  16:32
 * Description：
 */
internal class GsonResponseBodyConverter<T>(private val gson : Gson,private val adapter: TypeAdapter<T>) : Converter<ResponseBody,T> {


    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T?{
        // 在这里通过 value 拿到 json 字符串进行解析
        // 判断状态码是失败的情况，就抛出异常

        val jsonReader = gson.newJsonReader(value.charStream())
        value.use {
            val result = adapter.read(jsonReader)
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw JsonIOException("JSON document was not fully consumed.")
            }
            return result
        }


        /*val response = value.string()

        val modelResponse = gson.fromJson(response,BaseModelResponse::class.java)

        if (modelResponse.code == 200){
            return gson.fromJson(response, type);
        }else{

            val errResponse = gson.fromJson(response,BaseErrorModel::class.java)

            if (errResponse.msg == "401"){
                throw ResultException(errResponse.code,errResponse.data)
            }else{
                throw ResultException(errResponse.code,errResponse.msg)
            }

        }*/

    }

}