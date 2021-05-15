package kz.iitu.diplom.crm

import android.app.Application
import android.content.Context
import kz.iitu.diplom.crm.utils.AppPreferences

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
    }

    //TODO: 1. ProfileFragment save to frirebase
    //TODO: 2. AvatarWidget dynamically change letters for name
    //TODO: 3.
    //TODO:
}