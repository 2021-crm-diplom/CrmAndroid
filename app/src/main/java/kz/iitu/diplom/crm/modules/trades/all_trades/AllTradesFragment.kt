package kz.iitu.diplom.crm.modules.trades.all_trades

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.firebase.firestore.QuerySnapshot
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.QuerySnapshotCallback
import kz.iitu.diplom.crm.core.BaseFragment
import kz.iitu.diplom.crm.core.BindingRecyclerAdapter
import kz.iitu.diplom.crm.core.SwipeRecycler
import kz.iitu.diplom.crm.modules.trades.*
import kz.iitu.diplom.crm.modules.trades.models.Task
import kz.iitu.diplom.crm.modules.trades.models.Trade
import kz.iitu.diplom.crm.modules.trades.models.TradeStatus
import kz.iitu.diplom.crm.modules.trades.bindings.TradeObj
import kz.iitu.diplom.crm.modules.trades.bindings.StatusChangedCallback
import kz.iitu.diplom.crm.modules.trades.bindings.TradeAct
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
        adapter?.tradesState = TradesState(Status.LOADING)
        delegate?.loadAllTrades(object : QuerySnapshotCallback {
            override fun onSuccess(result: QuerySnapshot) {
                if(result.isEmpty) adapter?.tradesState = TradesState(Status.LOADED)
                else {
                    val trades = mutableListOf<Trade>()
                    result.forEach { tradeDoc ->
                        var trade = Trade(tradeDoc)
                        delegate?.loadTasksForTrade(trade.id, object: QuerySnapshotCallback {
                            override fun onSuccess(result: QuerySnapshot) {
                                val tasks = mutableListOf<Task>()
                                result.forEach { taskDoc ->
                                    tasks.add(Task(taskDoc))
                                }
                                trade = trade.copy(tasks = tasks)
                                trades.add(trade)
                                adapter?.tradesState = TradesState(Status.LOADED, trades)
                                subtitle = getString(R.string.trades_count, trades.size)
                                swipeRecycler?.isRefreshing = false
                            }
                            override fun onFailure(e: Exception) {
                                adapter?.tradesState = TradesState(Status.FAILED)
                                swipeRecycler?.isRefreshing = false
                            }
                        })
                    }
                }
            }
            override fun onFailure(e: Exception) {
                adapter?.tradesState = TradesState(Status.FAILED)
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
        var tradesState: TradesState = TradesState(Status.LOADED)
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun bindingForPosition(position: Int) = when {
            tradesState.status == Status.LOADING -> Pair(null, null)
            tradesState.status == Status.FAILED -> Pair(null, null)
            tradesState.trades.isNullOrEmpty() -> Pair(null, null)
            else -> Pair(TradeObj(tradesState.trades[position], position), this@AllTradesFragment)
        }

        override fun layoutIdForPosition(position: Int) = when {
            tradesState.status == Status.LOADING -> R.layout.trade_list_placeholder_item
            tradesState.status == Status.FAILED -> R.layout.trade_list_failed_item
            tradesState.trades.isNullOrEmpty() -> R.layout.trade_list_empty_item
            else -> R.layout.trade_list_loaded_item
        }

        override fun getItemCount() = when {
            tradesState.status == Status.LOADING -> 1
            tradesState.status == Status.FAILED -> 1
            tradesState.trades.isNullOrEmpty() -> 1
            else -> tradesState.trades.size
        }
    }

    interface Delegate {
        fun onStatusClicked(docId: String, currentStatus: TradeStatus, statusChangedCallback: StatusChangedCallback, position: Int)
        fun onStatusChanged(id: String, newStatus: TradeStatus)
        fun onTradeClicked(trade: Trade)
        fun loadAllTrades(callback: QuerySnapshotCallback)
        fun loadTasksForTrade(tradeId: String, callback: QuerySnapshotCallback)
    }
}