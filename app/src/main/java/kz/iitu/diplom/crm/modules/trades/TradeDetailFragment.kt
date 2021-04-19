package kz.iitu.diplom.crm.modules.trades

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.BaseFragment
import kz.iitu.diplom.crm.modules.trades.models.Task
import kz.iitu.diplom.crm.modules.trades.models.Trade
import kz.iitu.diplom.crm.modules.trades.models.TradeStatus
import kz.iitu.diplom.crm.modules.trades.views.TasksWidget
import kz.iitu.diplom.crm.utils.format
import kz.iitu.diplom.crm.utils.formatLocal
import java.util.*

class TradeDetailFragment : BaseFragment() {

    companion object {
        private const val ARG_TRADE = "kz.iitu.diplom.crm.modules.trades.TradeDetailFragment"
        fun create(trade: Trade) = TradeDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_TRADE, trade)
            }
        }
    }

    private var tradeTitle: TextView? = null
    private var tradeDescription: TextView? = null
    private var tradeDeadline: TextView? = null
    private var tradeStatus: TextView? = null
    private var tasksWidget: TasksWidget? = null
    private var trade: Trade? = null
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
        val view = inflater.inflate(R.layout.trade_detail_fragment, container, false)
        tradeTitle = view.findViewById(R.id.trade_detail_title)
        tradeDescription = view.findViewById(R.id.trade_detail_description)
        tradeDeadline = view.findViewById(R.id.trade_detail_deadline)
        tradeStatus = view.findViewById(R.id.trade_detail_status)
        tasksWidget = view.findViewById(R.id.tasks_widget)

        trade = savedInstanceState?.getParcelable(ARG_TRADE) ?: arguments?.getParcelable(ARG_TRADE)
        title = trade?.title ?: super.title
        initTrade(trade ?: throw Exception("Trade cannot be null"))
        return view
    }

    override fun onStop() {
        tasksWidget?.updatedTasks?.let {
            delegate?.updateTasksCompleted(it)
        }
        super.onStop()
    }

    private fun initTrade(trade: Trade) {
        tradeTitle?.text = trade.title
        setDescription(trade.description)
        setDeadline(trade.deadline)
        setStatus(trade.status)
        setTasks(trade.tasks)
    }

    private fun setDescription(description: String?) {
        if(description != null) {
            tradeDescription?.visibility = View.VISIBLE
            tradeDescription?.text = description
        }
    }

    private fun setDeadline(deadline: Date?) {
        if(deadline != null) {
            tradeDeadline?.visibility = View.VISIBLE
            tradeDeadline?.text = deadline.formatLocal()
        }
    }

    private fun setStatus(status: TradeStatus) {
        tradeStatus?.visibility = View.VISIBLE
        tradeStatus?.text = status.title
        val drawableRes = when(status) {
            TradeStatus.COMPLETED -> R.drawable.task_status_completed
            TradeStatus.IN_WORK -> R.drawable.task_status_in_work
            TradeStatus.REJECTED -> R.drawable.task_status_rejected
            TradeStatus.PAUSED -> R.drawable.task_status_paused
            else -> R.drawable.task_status_default
        }
        tradeStatus?.background = ContextCompat.getDrawable(requireContext(), drawableRes)
    }

    private fun setTasks(tasks: List<Task>) {
        tasksWidget?.setTasks(tasks)
    }

    interface Delegate {
        fun updateTasksCompleted(updatedTasks: List<Task>)
    }
}