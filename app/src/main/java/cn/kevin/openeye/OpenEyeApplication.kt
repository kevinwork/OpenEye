package cn.kevin.openeye

import android.app.Application
import android.content.Context

class OpenEyeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        lateinit var context : Context
    }
}