package kz.iitu.diplom.crm.core

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.modules.FirstFragment
import kz.iitu.diplom.crm.modules.SecondFragment
import kz.iitu.diplom.crm.modules.ThirdFragment
import kz.iitu.diplom.crm.modules.profile.ProfileFragment
import kz.iitu.diplom.crm.utils.AppPreferences

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
        initHeaderView()
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
        val initialMenuItem = navigationView.menu.findItem(R.id.menu_tasks_all)
        selectDrawerItem(initialMenuItem)
    }

    private fun selectDrawerItem(menuItem: MenuItem) {
        try {
            val fragment = when(menuItem.itemId) {
                R.id.menu_tasks_all -> FirstFragment::class.java.newInstance()
                R.id.menu_tasks_waiting -> SecondFragment::class.java.newInstance()
                R.id.menu_tasks_inwork ->  ThirdFragment::class.java.newInstance()
                R.id.menu_tasks_completed ->  ThirdFragment::class.java.newInstance()
                R.id.menu_tasks_paused ->  ThirdFragment::class.java.newInstance()
                R.id.menu_tasks_rejected ->  ThirdFragment::class.java.newInstance()
                R.id.menu_settings -> ThirdFragment::class.java.newInstance()
                else -> throw ClassNotFoundException("Cannot find class for navigation fragment")
            }
            pushFragment(fragment)
            setToolbarTitle(menuItem.title.toString())
            navigationView.checkedItem?.isChecked = false
            navigationView.setCheckedItem(menuItem)
            menuItem.isChecked = true
            closeDrawer()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun closeDrawer() {
        drawer.closeDrawers()
    }

    private fun initHeaderView() {
        val headerInfo = navigationViewHeader.findViewById<ConstraintLayout>(R.id.layout_header_info)
        headerInfo.findViewById<TextView>(R.id.header_phone_number).text = AppPreferences.phone
        headerInfo.findViewById<TextView>(R.id.header_username).text =
            getString(R.string.menu_username, AppPreferences.firstName, AppPreferences.lastName)
        headerInfo.setOnClickListener {
            closeDrawer()
            pushFragment(ProfileFragment())
            setToolbarTitle(getString(R.string.menu_profile))
            navigationView.checkedItem?.isChecked = false
        }
    }
}