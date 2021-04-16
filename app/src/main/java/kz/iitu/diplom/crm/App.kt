package kz.iitu.diplom.crm

import android.app.Application
import android.content.Context
import kz.iitu.diplom.crm.utils.AppPreferences

class App : Application() {

    var context: Context? = null
        private set

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
        context = applicationContext
    }

}