package com.communication.pingyi.tools

import android.app.Activity
import com.communication.lib_http.base.BaseModel
import java.util.ArrayList

/**
 * Created by LG
 * on 2022/3/31  17:27
 * Description：
 */
object ActivityUtil {

    lateinit var mBaseModel : BaseModel<Int>

    var activityList: MutableList<Activity> = ArrayList()

    // 添加Activity
    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }

    // 移除所有Activity
    fun finishAll() {
        for (activity in activityList) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
    }
}