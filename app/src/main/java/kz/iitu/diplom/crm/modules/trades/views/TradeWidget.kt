package kz.iitu.diplom.crm.modules.trades.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.AvatarWidget
import kz.iitu.diplom.crm.core.setEmployee
import kz.iitu.diplom.crm.modules.trades.bindings.TradeClickListener
import kz.iitu.diplom.crm.modules.trades.bindings.TradeStatusClickListener
import kz.iitu.diplom.crm.modules.trades.models.Task
import kz.iitu.diplom.crm.modules.trades.models.Trade
import kz.iitu.diplom.crm.modules.trades.models.TradeStatus
import kz.iitu.diplom.crm.utils.AppPreferences
import kz.iitu.diplom.crm.utils.isToday
import java.text.SimpleDateFormat
import java.util.*

class TradeWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttributeSet: Int = 0)
    : CardView(context, attrs, defStyleAttributeSet) {

    private val view = inflate(context, R.layout.trade_widget, this)
    private var titleView: TextView
    private var tradeStartDate: TextView
    private var tradeDeadline: TextView
    private var tasksCountLayout: ConstraintLayout
    private var statusWidget: TradeStatusWidget
    private var tradeEmployeeAvatar: AvatarWidget

    private var trade: Trade? = null
    private var tradeClickListener: TradeClickListener? = null
    private var tradeStatusClickListener: TradeStatusClickListener? = null

    init {
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
        radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7f, context.resources.displayMetrics)
        cardElevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, context.resources.displayMetrics)
        titleView = view.findViewById(R.id.tradeTitle)
        tradeStartDate = view.findViewById(R.id.tradeStartDate)
        tradeDeadline = view.findViewById(R.id.tradeDeadline)
        tasksCountLayout = view.findViewById(R.id.layout_tasks)
        statusWidget = view.findViewById(R.id.tradeStatus)
        tradeEmployeeAvatar = view.findViewById(R.id.tradeEmployeeAvatar)
        initAvatarWidget()
    }

    fun setTrade(trade: Trade) {
        setTitle(trade.title)
        setStartDate(trade.startDate)
        setDeadline(trade.deadline)
        setTradeStatus(trade.status)
        initTasksCount(trade.tasks)
        this.trade = trade
    }

    fun setTradeClickListener(listener: TradeClickListener) {
        this.tradeClickListener = listener
        this.setOnClickListener {
            val trade = this.trade ?: throw Exception("Trade cannot be null")
            tradeClickListener?.onClick(trade)
        }
    }

    fun setStatusClickListener(listener: TradeStatusClickListener) {
        tradeStatusClickListener = listener
        statusWidget.setOnClickListener {
            val docId = trade?.documentId ?: throw Exception("Document id cannot be null")
            tradeStatusClickListener?.onClick(docId, statusWidget.status)
        }
    }

    fun setTradeStatus(status: TradeStatus) {
        statusWidget.status = status
        this.trade = trade?.copy(status = status)
    }

    private fun setTitle(title: String) {
        titleView.text = title
    }

    private fun setStartDate(date: Date) {
        if(date.isToday()) {
            tradeStartDate.text = context.getString(R.string.trade_start_date, context.getString(R.string.trade_today))
        } else {
            tradeStartDate.text = context.getString(R.string.trade_start_date, getFormattedDateString(date))
        }
    }

    private fun setDeadline(date: Date) {
        if(date.isToday()) {
            tradeDeadline.text = context.getString(R.string.trade_deadline, context.getString(R.string.trade_today))
        } else {
            tradeDeadline.text = context.getString(R.string.trade_deadline, getFormattedDateString(date))
        }
        if(date.before(Date())) {
            tradeDeadline.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
    }

    private fun initTasksCount(tasks: List<Task>) {
        if(tasks.isNullOrEmpty()) {
            tasksCountLayout.visibility = View.GONE
        } else {
            tasksCountLayout.visibility = View.VISIBLE
            val tasksCountView = findViewById<TextView>(R.id.tasks_count)
            val tasksCountCompletedView = findViewById<TextView>(R.id.tasks_completed_count)
            val completedCount = tasks.count { it.isCompleted == true }
            tasksCountView.text = context.getString(R.string.trade_tasks_count, tasks.size)
            tasksCountCompletedView.text = context.getString(R.string.trade_tasks_completed_count, completedCount, tasks.size)
        }
    }

    private fun initAvatarWidget() {
        tradeEmployeeAvatar.setEmployeeLetters(AppPreferences.firstName, AppPreferences.lastName)
    }

    private fun getFormattedDateString(date: Date): String {
        return SimpleDateFormat("dd MMMM HH:mm", Locale.getDefault()).format(date)
    }
}