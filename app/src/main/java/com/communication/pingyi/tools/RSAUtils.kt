package com.communication.pingyi.tools

import android.util.Base64
import java.lang.Exception
import java.security.Key
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

/**
 * Created by LG
 * on 2022/3/16  14:34
 * Description：
 */
object RSAUtils {

    val KEY_ALGORITHM = "RSA"
    val TRANSFORMATION = "RSA/None/PKCS1Padding"

    @Throws(Exception::class)
    fun encryptByPublicKey(data: String, key: String?): ByteArray? {
        // 对公钥解密
        val keyBytes: ByteArray = decryptBASE64(key)
        // 取得公钥
        val x509KeySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(KEY_ALGORITHM)
        val publicKey: Key = keyFactory.generatePublic(x509KeySpec)
        // 对数据加密
        val cipher = Cipher.getInstance(TRANSFORMATION)
        //        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return cipher.doFinal(data.toByteArray())
    }

    @Throws(Exception::class)
    fun decryptBASE64(key: String?): ByteArray {
        return Base64.decode(key, Base64.NO_WRAP)
    }

}