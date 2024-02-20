package com.communication.lib_http.httpdata.message

data class ServiceAlarmFile(
    val id : Long,//("主键id")
    val alarmId : String,//告警业务id
    val fileType : String,//文件类型
    val fileUrl : String,//照片/视频URL
    val uploadUser : String,//上传用户
    val isVaild : String//是否有效 0有效 1无效
)
