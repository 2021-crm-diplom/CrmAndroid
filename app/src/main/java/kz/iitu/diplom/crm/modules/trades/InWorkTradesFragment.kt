package kz.iitu.diplom.crm.modules.trades

import kz.iitu.diplom.crm.modules.trades.models.TradeStatus

class InWorkTradesFragment : BaseTradesFragment() {

    override fun loadTrades() {
        swipeRecycler?.isRefreshing = true
        adapter?.tradesState = TradesState(Status.LOADING)
        delegate?.loadTradesByStatus(TradeStatus.IN_WORK, tradeLoadCallback)
    }

}