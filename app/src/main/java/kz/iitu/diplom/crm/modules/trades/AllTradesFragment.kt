package kz.iitu.diplom.crm.modules.trades

class AllTradesFragment : BaseTradesFragment() {

    override fun loadTrades() {
        swipeRecycler?.isRefreshing = true
        adapter?.tradesState = TradesState(Status.LOADING)
        delegate?.loadAllTrades(tradeLoadCallback)
    }

}