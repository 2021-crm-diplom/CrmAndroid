package kz.iitu.diplom.crm.modules.trades

import kz.iitu.diplom.crm.modules.trades.models.Trade
import kz.iitu.diplom.crm.modules.trades.models.TradeStatus

interface TradeAct {
    fun onTradeClicked(trade: Trade)
    fun onTradeStatusClicked(docId: String, status: TradeStatus, position: Int)
    fun onRetryLoadTrades()
}