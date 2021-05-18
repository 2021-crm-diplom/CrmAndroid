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
import kz.iitu.diplom.crm.model.Client
import kz.iitu.diplom.crm.model.Employee
import kz.iitu.diplom.crm.modules.profile.ProfileClientDialog
import kz.iitu.diplom.crm.modules.profile.ProfileEmployeesDialog
import kz.iitu.diplom.crm.modules.profile.ProfileFragment
import kz.iitu.diplom.crm.modules.profile.ProfileReportsFragment
import kz.iitu.diplom.crm.modules.settings.SettingsFragment
import kz.iitu.diplom.crm.modules.trades.*
import kz.iitu.diplom.crm.modules.trades.bindings.StatusChangedCallback
import kz.iitu.diplom.crm.modules.trades.details.TradeDetailFragment
import kz.iitu.diplom.crm.modules.trades.models.Task
import kz.iitu.diplom.crm.modules.trades.models.Trade
import kz.iitu.diplom.crm.modules.trades.models.TradeStatus
import kz.iitu.diplom.crm.modules.trades.views.TradeStatusDialog
import kz.iitu.diplom.crm.utils.AppPreferences
import kz.iitu.diplom.crm.utils.onFailure
import kz.iitu.diplom.crm.utils.onSuccess

abstract class NavigationActivity(@LayoutRes override val contentLayout: Int = R.layout.base_navigation_activity) :
    BaseActivity(contentLayout), TradeDetailFragment.Delegate, BaseTradesFragment.Delegate, ProfileFragment.Delegate {

    companion object {
        private const val TRADES = "trades"
        private const val EMPLOYEES = "employees"
        private const val TASKS = "tasks"
        private const val COMMENTS = "comments"
        private const val CLIENTS = "clients"
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

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount < 1) {
            finishGracefully()
        } else {
            super.onBackPressed()
        }
    }

    private fun finishGracefully() {
        AlertPopup(this, getString(R.string.finish_app_title), getString(R.string.finish_app_message))
            .setPositiveButton(getString(R.string.yes)) {
                super.onBackPressed()
            }
            .setNegativeButton(getString(R.string.cancel))
            .show()
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
                    WaitingTradesFragment::class.java.newInstance()
                }
                R.id.menu_trades_inwork ->  {
                    currentMenuItem = NavigationMenuItem.TRADES_INWORK
                    InWorkTradesFragment::class.java.newInstance()
                }
                R.id.menu_trades_completed ->  {
                    currentMenuItem = NavigationMenuItem.TRADES_COMPLETED
                    CompletedTradesFragment::class.java.newInstance()
                }
                R.id.menu_trades_paused ->  {
                    currentMenuItem = NavigationMenuItem.TRADES_PAUSED
                    PausedTradesFragment::class.java.newInstance()
                }
                R.id.menu_trades_rejected ->  {
                    currentMenuItem = NavigationMenuItem.TRADES_REJECTED
                    RejectedTradesFragment::class.java.newInstance()
                }
                R.id.menu_settings -> {
                    currentMenuItem = NavigationMenuItem.SETTINGS
                    SettingsFragment::class.java.newInstance()
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


    override fun loadAllTrades(callback: QuerySnapshotCallback) {
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

    override fun loadTradesByStatus(status: TradeStatus, callback: QuerySnapshotCallback) {
        firestoreDb.collection(TRADES)
            .whereEqualTo("status", status.title)
            .get()
            .onSuccess(this) { result ->
                callback.onSuccess(result)
            }
            .onFailure(this) { e ->
                Log.e(TAG, "loadTradesByStatus: $status failed", e)
                callback.onFailure(e)
            }
    }

    /**
     *
     *
     * Trade Details Fragment
     *
     *
     */

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

    override fun loadCommentsForTrade(tradeId: String, callback: QuerySnapshotCallback) {
        firestoreDb.collection(COMMENTS)
            .whereEqualTo("tradeId", tradeId)
            .get()
            .addOnSuccessListener { result ->
                callback.onSuccess(result)
                Log.i(TAG, "loadCommentsForTrade success")
            }
            .addOnFailureListener {
                callback.onFailure(it)
                Log.e(TAG, "loadCommentsForTrade failure", it)
            }
    }

    override fun addTradeComment(comment: Map<String, String?>, callback: DocumentReferenceCallback) {
        firestoreDb.collection(COMMENTS)
            .add(comment)
            .addOnSuccessListener { document ->
                callback.onSuccess(document)
                Log.i(TAG, "addTradeComment success with ID = ${document.id}")
            }
            .addOnFailureListener {
                Log.e(TAG, "addTradeComment failed", it)
            }
    }

    /**
     *
     *
     * Profile Fragment
     *
     *
     */


    override fun loadAllTradesForProfile(callback: QuerySnapshotCallback) {
        loadAllTrades(callback)
    }

    override fun loadAllTasks(callback: QuerySnapshotCallback) {
        firestoreDb.collection(TASKS)
            .get()
            .onSuccess(this) { result ->
                callback.onSuccess(result)
            }
            .onFailure(this) { e ->
                callback.onFailure(e)
            }
    }

    override fun loadEmployees(callback: QuerySnapshotCallback) {
        firestoreDb.collection(EMPLOYEES)
            .get()
            .onSuccess(this) { result ->
                callback.onSuccess(result)
            }
            .onFailure(this) { e ->
                callback.onFailure(e)
            }
    }

    override fun loadClients(callback: QuerySnapshotCallback) {
        firestoreDb.collection(CLIENTS)
            .get()
            .onSuccess(this) { result ->
                callback.onSuccess(result)
            }
            .onFailure(this) { e ->
                callback.onFailure(e)
            }
    }

    override fun onProfileEmployeesClicked(employees: List<Employee>) {
        pushDialog(ProfileEmployeesDialog(employees))
    }

    override fun onProfileClientsClicked(clients: List<Client>) {
        pushDialog(ProfileClientDialog(clients))
    }

    override fun onProfileReportsClicked() {
        addFragment(ProfileReportsFragment())
    }

    /**
     *
     *
     *
     * Common delegates
     *
     *
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

    override fun loadTasksForTrade(tradeId: String, callback: QuerySnapshotCallback) {
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
}