package kz.iitu.diplom.crm.core

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kz.iitu.diplom.crm.R
import java.util.*

abstract class BaseActivity(@LayoutRes open val contentLayout: Int) : AppCompatActivity() {

    constructor(): this(R.layout.base_activity)

    protected val firestoreDb = Firebase.firestore
    protected lateinit var toolbar: Toolbar
    private var isLocked = false
    private lateinit var appBar: AppBarLayout
    private var progressPopup: ProgressPopup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle()
        setContentView(contentLayout)

        toolbar = findViewById(R.id.base_toolbar)
        appBar = findViewById(R.id.app_bar)
        setSupportActionBar(toolbar)
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount >= 1) {
            popFragment()
        } else super.onBackPressed()
    }

    fun setToolbarTitle(title: String?) {
        title?.let { this.title = it }
    }

    fun setToolbarSubtitle(subtitle: String?) {
        subtitle?.let { toolbar.subtitle = subtitle } ?: run { toolbar.subtitle = null }
    }

    fun hideToolbar() {
        supportActionBar?.hide()
        appBar.visibility = View.GONE
    }

    fun showToolbar() {
        supportActionBar?.show()
        appBar.visibility = View.VISIBLE
    }

    fun lock(action: () -> Unit) {
        if(isLocked) return
        isLocked = true
        progressPopup = ProgressPopup()
        progressPopup?.show(supportFragmentManager, "progressPopup")
        action.invoke()
    }

    fun unlock() {
        if(isLocked) {
            isLocked = false
            progressPopup?.dismiss()
            progressPopup = null
        }
    }

    protected fun pushFragment(fragment: Fragment) {
        val name = UUID.randomUUID().toString()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_container, fragment, name)
            .commit()
    }

    protected fun pushFragmentBackStack(fragment: Fragment) {
        val name = UUID.randomUUID().toString()
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(name)
            .replace(R.id.content_container, fragment, name)
            .commit()
    }

    protected fun popFragment() {
        supportFragmentManager.popBackStack()
    }

    protected fun pushDialog(dialog: DialogFragment, fullScreen: Boolean = false) {
        if(fullScreen) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction
                .add(android.R.id.content, dialog)
                .addToBackStack(null)
                .commit()
        } else {
            dialog.show(supportFragmentManager, dialog.tag)
        }
    }


    private fun setStyle() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }
}