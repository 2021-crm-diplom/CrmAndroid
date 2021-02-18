package kz.iitu.diplom.crm.core

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.appbar.AppBarLayout
import kz.iitu.diplom.crm.R
import java.util.*

abstract class BaseActivity(@LayoutRes open val contentLayout: Int) : AppCompatActivity() {

    constructor(): this(R.layout.base_activity)

    protected lateinit var toolbar: Toolbar
    private lateinit var appBar: AppBarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle()
        setContentView(contentLayout)

        toolbar = findViewById(R.id.base_toolbar)
        appBar = findViewById(R.id.app_bar)
        setSupportActionBar(toolbar)

        supportFragmentManager.addOnBackStackChangedListener {
            if(supportFragmentManager.backStackEntryCount >= 1) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount >= 1) {
            popFragment()
        } else super.onBackPressed()
    }

    fun setToolbarTitle(title: String?) {
        title?.let { this.title = it }
    }

    fun hideToolbar() {
        supportActionBar?.hide()
        appBar.visibility = View.GONE
    }

    fun showToolbar() {
        supportActionBar?.show()
        appBar.visibility = View.VISIBLE
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

    private fun setStyle() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }
}