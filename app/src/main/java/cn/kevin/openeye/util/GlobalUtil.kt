package cn.kevin.openeye.util

import cn.kevin.openeye.OpenEyeApplication

object GlobalUtil {

    fun getString(resId : Int) :String {
        return OpenEyeApplication.context.resources.getString(resId)
    }
}