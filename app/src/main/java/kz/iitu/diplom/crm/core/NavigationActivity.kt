package kz.iitu.diplom.crm.core

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.modules.SecondFragment
import kz.iitu.diplom.crm.modules.ThirdFragment
import kz.iitu.diplom.crm.modules.trades.all_trades.AllTradesFragment
import kz.iitu.diplom.crm.modules.profile.ProfileFragment
import kz.iitu.diplom.crm.modules.trades.StatusChangedCallback
import kz.iitu.diplom.crm.modules.trades.TradeDetailFragment
import kz.iitu.diplom.crm.modules.trades.models.Task
import kz.iitu.diplom.crm.modules.trades.models.Trade
import kz.iitu.diplom.crm.modules.trades.models.TradeStatus
import kz.iitu.diplom.crm.modules.trades.views.TradeStatusDialog
import kz.iitu.diplom.crm.utils.AppPreferences
import kz.iitu.diplom.crm.utils.onFailure
import kz.iitu.diplom.crm.utils.onSuccess

abstract class NavigationActivity(@LayoutRes override val contentLayout: Int = R.layout.base_navigation_activity) :
    BaseActivity(contentLayout), AllTradesFragment.Delegate, TradeDetailFragment.Delegate {

    companion object {
        private const val TRADES = "trades"
        private const val EMPLOYEES = "employees"
        private const val TASKS = "tasks"
        private const val TAG = "NavigationActivity"
    }

    private lateinit var drawer: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var navigationViewHeader: View

    private var currentMenuItem: NavigationMenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        drawer = findViewById(R.id.drawer_layout)
        drawerToggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close)
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        navigationView = findViewById(R.id.navigation_view)
        navigationViewHeader = navigationView.getHeaderView(0)

        supportFragmentManager.addOnBackStackChangedListener {
            if(supportFragmentManager.backStackEntryCount >= 1) {
                drawerToggle.isDrawerIndicatorEnabled = false
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                lockDrawer()
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                drawerToggle.isDrawerIndicatorEnabled = true
                unlockDrawer()
            }
        }

        drawerToggle.setToolbarNavigationClickListener {
            if(supportFragmentManager.backStackEntryCount >= 1) {
                popFragment()
            }
        }

        setupNavigation()
        initHeaderView()
    }

    private fun setupNavigation() {
        selectInitialFragment()
        navigationView.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }

    private fun selectInitialFragment() {
        val initialMenuItem = navigationView.menu.findItem(R.id.menu_trades_all)
        selectDrawerItem(initialMenuItem)
    }

    private fun selectDrawerItem(menuItem: MenuItem) {
        if(menuItem.itemId == currentMenuItem?.id) {
            closeDrawer()
            return
        }
        try {
            val fragment = when(menuItem.itemId) {
                R.id.menu_trades_all -> {
                    currentMenuItem = NavigationMenuItem.ALL_TRADES
                    AllTradesFragment::class.java.newInstance()
                }
                R.id.menu_trades_waiting -> {
                    currentMenuItem = NavigationMenuItem.TRADES_WAITING
                    SecondFragment::class.java.newInstance()
                }
                R.id.menu_trades_inwork ->  {
                    currentMenuItem = NavigationMenuItem.TRADES_INWORK
                    ThirdFragment::class.java.newInstance()
                }
                R.id.menu_trades_completed ->  {
                    currentMenuItem = NavigationMenuItem.TRADES_COMPLETED
                    ThirdFragment::class.java.newInstance()
                }
                R.id.menu_trades_paused ->  {
                    currentMenuItem = NavigationMenuItem.TRADES_PAUSED
                    ThirdFragment::class.java.newInstance()
                }
                R.id.menu_trades_rejected ->  {
                    currentMenuItem = NavigationMenuItem.TRADES_REJECTED
                    ThirdFragment::class.java.newInstance()
                }
                R.id.menu_settings -> {
                    currentMenuItem = NavigationMenuItem.SETTINGS
                    ThirdFragment::class.java.newInstance()
                }
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

    private fun lockDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun unlockDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    private fun initHeaderView() {
        val headerInfo = navigationViewHeader.findViewById<ConstraintLayout>(R.id.layout_header_info)
        headerInfo.findViewById<TextView>(R.id.header_phone_number).text = AppPreferences.phone
        headerInfo.findViewById<TextView>(R.id.header_username).text =
            getString(R.string.menu_username, AppPreferences.firstName, AppPreferences.lastName)
        headerInfo.setOnClickListener {
            if(currentMenuItem == NavigationMenuItem.PROFILE) {
                closeDrawer()
                return@setOnClickListener
            }
            closeDrawer()
            pushFragment(ProfileFragment())
            setToolbarTitle(getString(R.string.menu_profile))
            currentMenuItem = NavigationMenuItem.PROFILE
            navigationView.checkedItem?.isChecked = false
        }
    }


    /**
     *
     *
     *
     *
     *   DELEGATES
     *
     *
     *
     *
     */

    /**
     *
     * All Trades Fragment
     *
     */
    override fun onStatusClicked(docId: String, currentStatus: TradeStatus, statusChangedCallback: StatusChangedCallback, position: Int) {
        pushDialog(TradeStatusDialog(docId, currentStatus,  statusChangedCallback, position))
    }

    override fun onStatusChanged(id: String, newStatus: TradeStatus) {
        firestoreDb.collection(TRADES)
            .document(id)
            .update("status", newStatus.title)
            .addOnFailureListener {
                Log.i(TAG, "onStatusChanged Error update status", it)
                AlertPopup(this,
                    getString(R.string.trade_update_status_error_title),
                    getString(R.string.trade_update_status_error_desc)
                ).show()
            }
    }

    override fun onTradeClicked(trade: Trade) {
        pushFragmentBackStack(TradeDetailFragment.create(trade))
    }

    override fun loadAllTrades(callback: AsyncCallback) {
        firestoreDb.collection(TRADES)
            .get()
            .onSuccess(this) { result ->
                callback.onSuccess(result)
            }
            .onFailure(this) { e ->
                Log.e(TAG, "loadAllTrades", e)
                callback.onFailure(e)
            }
    }

    override fun loadTasksForTrade(tradeId: String, callback: AsyncCallback) {
        firestoreDb.collection(TASKS)
            .whereEqualTo("tradeId", tradeId)
            .get()
            .onSuccess(this) { result ->
                callback.onSuccess(result)
            }
            .onFailure(this) { e ->
                Log.e(TAG, "loadTasksForTrade Failed to load tasks for tradeId $tradeId", e)
                callback.onFailure(e)
            }
    }

    override fun updateTasksCompleted(updatedTasks: List<Task>) {
        updatedTasks.forEach { task ->
            firestoreDb.collection(TASKS)
                .document(task.docId)
                .update("isCompleted", task.isCompleted)
                .addOnSuccessListener {
                    Log.i(TAG, "updateTasksCompleted success")
                }
                .addOnFailureListener {
                    Log.e(TAG, "updateTasksCompleted failure", it)
                }
        }
    }
}