package kz.iitu.diplom.crm.modules.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.QuerySnapshot
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.AlertPopup
import kz.iitu.diplom.crm.core.AvatarWidget
import kz.iitu.diplom.crm.core.BaseFragment
import kz.iitu.diplom.crm.core.QuerySnapshotCallback
import kz.iitu.diplom.crm.model.Client
import kz.iitu.diplom.crm.model.Employee
import kz.iitu.diplom.crm.modules.profile.widgets.CardTradeCountWidget
import kz.iitu.diplom.crm.modules.profile.widgets.ProfileEditTextWidget
import kz.iitu.diplom.crm.modules.startup.StartupActivity
import kz.iitu.diplom.crm.modules.trades.models.Task
import kz.iitu.diplom.crm.modules.trades.models.Trade
import kz.iitu.diplom.crm.modules.trades.models.TradeStatus
import kz.iitu.diplom.crm.modules.trades.views.TasksWidget
import kz.iitu.diplom.crm.utils.AppPreferences
import java.util.*

class ProfileFragment : BaseFragment() {

    private lateinit var tradeOverdueWidget: CardTradeCountWidget
    private lateinit var tradeCompletedTodayWidget: CardTradeCountWidget
    private lateinit var tradeAllCountWidget: CardTradeCountWidget
    private lateinit var tradeInWorkWidget: CardTradeCountWidget
    private lateinit var tasksWidget: TasksWidget
    private lateinit var employeesButton: MaterialCardView
    private lateinit var clientsButton: MaterialCardView
    private lateinit var reportsButton: MaterialCardView
    private lateinit var avatarWidget: AvatarWidget
    private lateinit var lastNameEditText: ProfileEditTextWidget
    private lateinit var firstNameEditText: ProfileEditTextWidget
    private lateinit var phoneEditText: ProfileEditTextWidget
    private lateinit var buttonSignOut: Button

    private var delegate: Delegate? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        delegate = context as Delegate
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        subtitle = null
        tradeOverdueWidget = view.findViewById(R.id.tradeOverdueWidget)
        tradeCompletedTodayWidget = view.findViewById(R.id.tradeCompletedTodayWidget)
        tradeAllCountWidget = view.findViewById(R.id.tradeAllCountWidget)
        tradeInWorkWidget = view.findViewById(R.id.tradeInWorkWidget)
        tasksWidget = view.findViewById(R.id.tasksWidget)
        employeesButton = view.findViewById(R.id.employees_button)
        clientsButton = view.findViewById(R.id.clients_button)
        reportsButton = view.findViewById(R.id.reports_button)
        avatarWidget = view.findViewById(R.id.profile_avatar)
        lastNameEditText = view.findViewById(R.id.edit_text_lastname)
        firstNameEditText = view.findViewById(R.id.edit_text_first_name)
        phoneEditText = view.findViewById(R.id.edit_text_phone)
        buttonSignOut = view.findViewById(R.id.button_signout)
        initTradesWidget()
        initTasksWidget()
        initFields()
        initButtons()
        avatarWidget.setEmployeeLetters(AppPreferences.firstName, AppPreferences.lastName)
        return view
    }

    private fun initTradesWidget() {
        tradeAllCountWidget.setTitle(getString(R.string.profile_all_count))
        tradeOverdueWidget.setTitle(getString(R.string.profile_overdue))
        tradeCompletedTodayWidget.setTitle(getString(R.string.profile_completed))
        tradeInWorkWidget.setTitle(getString(R.string.profile_in_work))

        tradeOverdueWidget.setTextColor(R.color.red)
        tradeCompletedTodayWidget.setTextColor(R.color.green)

        delegate?.loadAllTradesForProfile(object: QuerySnapshotCallback {

            override fun onSuccess(result: QuerySnapshot) {
                if(result.isEmpty) {
                    tradeAllCountWidget.setCount(0)
                    tradeOverdueWidget.setCount(0)
                    tradeCompletedTodayWidget.setCount(0)
                    tradeInWorkWidget.setCount(0)
                } else {
                    val trades = mutableListOf<Trade>()
                    result.forEach { tradeDoc ->
                        trades.add(Trade(tradeDoc))
                    }
                    val allTradesCount = trades.size
                    val inWorkTrades = trades.filter { it.status == TradeStatus.IN_WORK}.size
                    val completedTrades = trades.filter { it.status == TradeStatus.COMPLETED }.size
                    val overdueTrades = getOverdueTradesCount(trades)
                    tradeAllCountWidget.setCount(allTradesCount)
                    tradeOverdueWidget.setCount(overdueTrades)
                    tradeCompletedTodayWidget.setCount(completedTrades)
                    tradeInWorkWidget.setCount(inWorkTrades)
                }
            }

            override fun onFailure(e: Exception) {
                tradeAllCountWidget.setCount(0)
                tradeOverdueWidget.setCount(0)
                tradeCompletedTodayWidget.setCount(0)
                tradeInWorkWidget.setCount(0)
            }
        })
    }

    private fun initTasksWidget() {
        tasksWidget.isTasksClickable = false
        delegate?.loadAllTasks(object: QuerySnapshotCallback {
            override fun onSuccess(result: QuerySnapshot) {
                val tasks = mutableListOf<Task>()
                result.forEach {
                    tasks.add(Task(it))
                }
                tasksWidget.setTasks(tasks)
            }

            override fun onFailure(e: Exception) {
                Log.e("ProfileFragment", "${e.message}", e)
            }
        })
    }

    private fun initFields() {
        lastNameEditText.text = AppPreferences.lastName
        firstNameEditText.text = AppPreferences.firstName
        phoneEditText.text = AppPreferences.phone
    }

    private fun initButtons() {
        initEmployeesButton()
        initClientsButton()
        initReportsButton()
        buttonSignOut.setOnClickListener {
            signOut()
        }
    }

    private fun initEmployeesButton() {
        val employees = mutableListOf<Employee>()
        delegate?.loadEmployees(object: QuerySnapshotCallback {
            override fun onSuccess(result: QuerySnapshot) {
                result.forEach {
                    employees.add(Employee(it))
                }
                employeesButton.setOnClickListener {
                    delegate?.onProfileEmployeesClicked(employees)
                }
            }
            override fun onFailure(e: Exception) {}
        })
    }

    private fun initClientsButton() {
        val clients = mutableListOf<Client>()
        delegate?.loadClients(object: QuerySnapshotCallback {
            override fun onSuccess(result: QuerySnapshot) {
                result.forEach {
                    clients.add(Client(it))
                }
                clientsButton.setOnClickListener {
                    delegate?.onProfileClientsClicked(clients)
                }
            }
            override fun onFailure(e: Exception) {}
        })
    }

    private fun initReportsButton() {
        reportsButton.setOnClickListener {
            delegate?.onProfileReportsClicked()
        }
    }

    private fun getOverdueTradesCount(trades: List<Trade>): Int {
        var count = 0
        trades.forEach {
            if(it.deadline.before(Date())) count ++
        }
        return count
    }

    private fun signOut() {
        AlertPopup(
            requireContext(),
            getString(R.string.profile_sign_out_title),
            getString(R.string.profile_sign_out_description))
            .setPositiveButton(getString(R.string.exit)) { signOutConfirmed() }
            .setNegativeButton(getString(R.string.cancel)).show()
    }

    private fun signOutConfirmed() {
        AppPreferences.clear()
        val intent = Intent(context, StartupActivity::class.java)
        context?.startActivity(intent)
    }

    interface Delegate {
        fun loadAllTradesForProfile(callback: QuerySnapshotCallback)
        fun loadAllTasks(callback: QuerySnapshotCallback)
        fun loadEmployees(callback: QuerySnapshotCallback)
        fun loadClients(callback: QuerySnapshotCallback)
        fun onProfileEmployeesClicked(employees: List<Employee>)
        fun onProfileClientsClicked(clients: List<Client>)
        fun onProfileReportsClicked()
    }
}