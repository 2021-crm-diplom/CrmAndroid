package kz.iitu.diplom.crm.modules.trades.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.modules.trades.TradeClickListener
import kz.iitu.diplom.crm.modules.trades.TradeStatusClickListener
import kz.iitu.diplom.crm.modules.trades.models.Trade
import kz.iitu.diplom.crm.modules.trades.models.TradeStatus
import kz.iitu.diplom.crm.utils.isToday
import java.text.SimpleDateFormat
import java.util.*

class TradeWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttributeSet: Int = 0)
    : CardView(context, attrs, defStyleAttributeSet) {

    private val view = inflate(context, R.layout.widget_trade, this)
    private var titleView: TextView
    private var tradeStartDate: TextView
    private var tradeDeadline: TextView
    private var statusWidget: TradeStatusWidget

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
        statusWidget = view.findViewById(R.id.tradeStatus)
    }

    fun setTrade(trade: Trade) {
        setTitle(trade.title)
        setStartDate(trade.startDate)
        setDeadline(trade.deadline)
        setTradeStatus(trade.status)
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

    private fun getFormattedDateString(date: Date): String {
        return SimpleDateFormat("dd MMMM HH:mm", Locale.getDefault()).format(date)
    }
}