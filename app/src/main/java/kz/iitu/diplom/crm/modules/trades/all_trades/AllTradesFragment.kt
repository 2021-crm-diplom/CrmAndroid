package kz.iitu.diplom.crm.modules.trades.all_trades

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.firebase.firestore.QuerySnapshot
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.AsyncCallback
import kz.iitu.diplom.crm.core.BaseFragment
import kz.iitu.diplom.crm.core.BindingRecyclerAdapter
import kz.iitu.diplom.crm.core.SwipeRecycler
import kz.iitu.diplom.crm.modules.trades.*
import kz.iitu.diplom.crm.modules.trades.models.Task
import kz.iitu.diplom.crm.modules.trades.models.Trade
import kz.iitu.diplom.crm.modules.trades.models.TradeStatus
import kz.iitu.diplom.crm.modules.trades.views.TradeObj
import kz.iitu.diplom.crm.modules.trades.views.TradeWidget

class AllTradesFragment : BaseFragment(), TradeAct {

    private var adapter: Adapter? = null
    private var delegate: Delegate? = null
    private var swipeRecycler: SwipeRecycler? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        delegate = context as Delegate
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.all_trades_fragment, container ,false)
        title = getString(R.string.menu_trades_all)
        swipeRecycler = view.findViewById(R.id.swipe_recycler)
        adapter = Adapter()
        swipeRecycler?.adapter = adapter
        swipeRecycler?.onRefresh {
            loadAllTrades()
        }
        loadAllTrades()
        return view
    }

    override fun onTradeClicked(trade: Trade) {
        delegate?.onTradeClicked(trade)
    }

    override fun onTradeStatusClicked(docId: String, status: TradeStatus, position: Int) {
        delegate?.onStatusClicked(docId, status, statusChangedCallback, position)
    }

    override fun onRetryLoadTrades() {
        loadAllTrades()
    }

    private fun loadAllTrades() {
        swipeRecycler?.isRefreshing = true
        adapter?.state = State(Status.LOADING)
        delegate?.loadAllTrades(object : AsyncCallback {
            override fun onSuccess(result: QuerySnapshot) {
                if(result.isEmpty) adapter?.state = State(Status.LOADED)
                else {
                    val trades = mutableListOf<Trade>()
                    result.forEach { tradeDoc ->
                        var trade = Trade(tradeDoc)
                        delegate?.loadTasksForTrade(trade.id, object: AsyncCallback {
                            override fun onSuccess(result: QuerySnapshot) {
                                val tasks = mutableListOf<Task>()
                                result.forEach { taskDoc ->
                                    tasks.add(Task(taskDoc))
                                }
                                trade = trade.copy(tasks = tasks)
                                trades.add(trade)
                                adapter?.state = State(Status.LOADED, trades)
                                swipeRecycler?.isRefreshing = false
                            }
                            override fun onFailure(e: Exception) {
                                adapter?.state = State(Status.FAILED)
                                swipeRecycler?.isRefreshing = false
                            }
                        })
                    }
                }
            }
            override fun onFailure(e: Exception) {
                adapter?.state = State(Status.FAILED)
                swipeRecycler?.isRefreshing = false
            }
        })
    }

    private val statusChangedCallback: StatusChangedCallback = { docId, status, position ->
        val tradeWidget = (swipeRecycler?.findItemAt(position) as? FrameLayout)?.getChildAt(0) as? TradeWidget
        tradeWidget?.setTradeStatus(status)
        delegate?.onStatusChanged(docId, status)
    }

    private inner class Adapter : BindingRecyclerAdapter() {
        var state: State = State(Status.LOADED)
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun bindingForPosition(position: Int) = when {
            state.status == Status.LOADING -> Pair(null, null)
            state.status == Status.FAILED -> Pair(null, null)
            state.trades.isNullOrEmpty() -> Pair(null, null)
            else -> Pair(TradeObj(state.trades[position], position), this@AllTradesFragment)
        }

        override fun layoutIdForPosition(position: Int) = when {
            state.status == Status.LOADING -> R.layout.trade_list_placeholder_item
            state.status == Status.FAILED -> R.layout.trade_list_failed_item
            state.trades.isNullOrEmpty() -> R.layout.trade_list_empty_item
            else -> R.layout.trade_list_loaded_item
        }

        override fun getItemCount() = when {
            state.status == Status.LOADING -> 1
            state.status == Status.FAILED -> 1
            state.trades.isNullOrEmpty() -> 1
            else -> state.trades.size
        }
    }

    interface Delegate {
        fun onStatusClicked(docId: String, currentStatus: TradeStatus, statusChangedCallback: StatusChangedCallback, position: Int)
        fun onStatusChanged(id: String, newStatus: TradeStatus)
        fun onTradeClicked(trade: Trade)
        fun loadAllTrades(callback: AsyncCallback)
        fun loadTasksForTrade(tradeId: String, callback: AsyncCallback)
    }
}