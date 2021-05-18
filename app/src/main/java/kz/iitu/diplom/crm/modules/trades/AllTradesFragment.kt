package kz.iitu.diplom.crm.modules.trades

import kz.iitu.diplom.crm.model.Status

class AllTradesFragment : BaseTradesFragment() {

    override fun loadTrades() {
        swipeRecycler?.isRefreshing = true
        adapter?.tradesState = TradesState(Status.LOADING)
        delegate?.loadAllTrades(tradeLoadCallback)
    }

}