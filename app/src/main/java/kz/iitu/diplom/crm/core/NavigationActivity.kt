package kz.iitu.diplom.crm.core

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.modules.FirstFragment
import kz.iitu.diplom.crm.modules.SecondFragment
import kz.iitu.diplom.crm.modules.ThirdFragment
import java.lang.Exception
import kotlin.reflect.KClass

abstract class NavigationActivity(@LayoutRes override val contentLayout: Int = R.layout.base_navigation_activity) : BaseActivity(contentLayout) {

    private lateinit var drawer: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var navigationViewHeader: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        drawer = findViewById(R.id.drawer_layout)
        drawerToggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close)
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        navigationView = findViewById(R.id.navigation_view)
        navigationViewHeader = navigationView.getHeaderView(0)

        setupNavigation()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupNavigation() {
        selectInitialFragment()
        navigationView.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }

    private fun selectInitialFragment() {
        val initialMenuItem = navigationView.menu.getItem(0)
        selectDrawerItem(initialMenuItem)
    }

    private fun selectDrawerItem(menuItem: MenuItem) {
        try {
            val fragment = when(menuItem.itemId) {
                R.id.first_fragment -> FirstFragment::class.java.newInstance()
                R.id.second_fragment ->  SecondFragment::class.java.newInstance()
                R.id.third ->  ThirdFragment::class.java.newInstance()
                else -> throw ClassNotFoundException("Cannot find class for navigation fragment")
            }
            pushFragment(fragment)
            setToolbarTitle(menuItem.title.toString())
            menuItem.isChecked = true
            closeDrawer()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun closeDrawer() {
        drawer.closeDrawers()
    }
}