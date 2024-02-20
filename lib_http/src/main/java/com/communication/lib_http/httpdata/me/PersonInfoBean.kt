package com.communication.lib_http.httpdata.me

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Created by LG
 * on 2022/3/30  17:40
 * Description： 用户信息
 */
data class PersonInfoBean(
    val dept : DeptBean,
    val roles : MutableList<RolesBean>,
    val phonenumber : String,
    val userName : String,
    val nickName : String,
    val imToken : String,
) : Serializable




