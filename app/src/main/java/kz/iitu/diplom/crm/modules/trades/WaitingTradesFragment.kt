package kz.iitu.diplom.crm.modules.trades

import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.model.Status
import kz.iitu.diplom.crm.modules.trades.models.TradeStatus

class WaitingTradesFragment : BaseTradesFragment() {

    override val fragmentTitle: String get() = getString(R.string.menu_trades_waiting)

    override fun loadTrades() {
        swipeRecycler?.isRefreshing = true
        adapter?.tradesState = TradesState(Status.LOADING)
        delegate?.loadTradesByStatus(TradeStatus.WAITING, tradeLoadCallback)
    }

}