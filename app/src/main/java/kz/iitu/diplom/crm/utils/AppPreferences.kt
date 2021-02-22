package kz.iitu.diplom.crm.utils

import android.content.Context
import android.content.SharedPreferences
import java.lang.Exception

object AppPreferences {

    private const val NAME = "kz.iitu.diplom.crm#AppPreferences"
    private const val PREF_IS_LOGGED = "kz.iitu.diplom.crm#isLogged"
    private const val PREF_PHONE = "kz.iitu.diplom.crm#phone"
    private const val PREF_FIRST_NAME = "kz.iitu.diplom.crm#firstName"
    private const val PREF_LAST_NAME = "kz.iitu.diplom.crm#lastName"

    private var prefs: SharedPreferences? = null

    var isLogged: Boolean
        get() = getBoolean(PREF_IS_LOGGED, false)
        set(value) = putBoolean(PREF_IS_LOGGED, value)

    var phone: String?
        get() = getString(PREF_PHONE, null)
        set(value) = putString(PREF_PHONE, value)

    var firstName: String?
        get() = getString(PREF_FIRST_NAME, null)
        set(value) = putString(PREF_FIRST_NAME, value)

    var lastName: String?
        get() = getString(PREF_LAST_NAME, null)
        set(value) = putString(PREF_LAST_NAME, value)

    fun init(context: Context) {
        prefs = context.getSharedPreferences(NAME, Context.MODE_PRIVATE) ?: throw Exception("Couldn't get shared preferences")
    }

    fun clear() {
        apply {
            this?.clear()
        }
    }

    private fun putString(key: String, value: String?) {
        apply {
            this?.putString(key, value)
        }
    }

    private fun getString(key: String, defaultValue: String?): String? {
        return prefs?.getString(key, defaultValue) ?: defaultValue
    }

    private fun putBoolean(key: String, value: Boolean) {
        apply {
            this?.putBoolean(key, value)
        }
    }

    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs?.getBoolean(key, defaultValue) ?: defaultValue
    }

    private fun apply(action: SharedPreferences.Editor?.() -> Unit) {
        with(prefs?.edit()) {
            action.invoke(this)
            this?.apply()
        }
    }
}