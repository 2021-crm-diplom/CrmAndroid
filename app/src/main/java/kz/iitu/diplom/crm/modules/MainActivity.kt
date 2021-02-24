package kz.iitu.diplom.crm.modules

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kz.iitu.diplom.crm.core.NavigationActivity

class MainActivity : NavigationActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}