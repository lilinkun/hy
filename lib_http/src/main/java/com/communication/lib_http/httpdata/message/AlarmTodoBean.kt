package com.communication.lib_http.httpdata.message


data class AlarmTodoBean(
    val alarmCause: Any,
    val alarmDescription: String,
    val alarmDirection: String,
    val alarmId: String,
    val alarmSource: String,
    val alarmStatus: String,
    val alarmType: String,
    val deviceIp: String,
    val deviceName: String,
    val facilityCode: String,
    val facilityName: String,
    val functionAreaType: String,
    val id: Int,
    val plateNumber: Any,
    val position: String,
    val reportedTime: String,
    val serviceAlarmFileList: List<ServiceAlarmFile>,
    val transportMedium: Any,
    val alarmStatusName: String,
    val alarmTypeName: String,
    val functionAreaTypeName: String,
    val transportMediumName: String,
    val alarmSourceName : String
)