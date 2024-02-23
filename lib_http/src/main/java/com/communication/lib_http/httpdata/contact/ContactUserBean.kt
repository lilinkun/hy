package com.communication.lib_http.httpdata.contact

import android.os.Parcelable
import com.communication.lib_http.httpdata.me.DeptBean
import java.io.Serializable

/**
 * Created by LG
 * on 2022/4/1  17:14
 * Description：
 */
data class ContactUserBean(
//    val userName : String,
//    val postJob : String,
//    val postDept : String,
//    val phoneNumber : String,
//    val dept : DeptBean


    val beginTime: Any,
    val createBy: String,
    val createTime: String,
    val delFlag: String,
    val dept: Dept,
    val deptId: Int,
    val endTime: Any,
    val params: ParamsX,
    val phoneNumber: String,
    val postJob: String,
    val remark: String,
    val searchValue: Any,
    val sex: Int,
    val status: String,
    val updateBy: Any,
    val updateTime: Any,
    val userId: Int,
    val userName: String,
    val imToken : String,
    val sysUserName : String
)

data class Dept(
    val ancestors: Any,
    val beginTime: Any,
    val children: List<Any>,
    val createBy: Any,
    val createTime: Any,
    val delFlag: String,
    val deptId: Int,
    val deptName: String,
    val endTime: Any,
    val leader: Any,
    val orderNum: Any,
    val params: ParamsX,
    val parentId: Any,
    val parentName: Any,
    val phone: Any,
    val remark: Any,
    val searchValue: Any,
    val status: Any,
    val updateBy: Any,
    val updateTime: Any
)

data class ParamsX(
    val beginTime: Any,
    val endTime: Any
)
