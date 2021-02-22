package kz.iitu.diplom.crm

import android.app.Application
import kz.iitu.diplom.crm.utils.AppPreferences

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
    }
}