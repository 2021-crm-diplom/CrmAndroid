package kz.iitu.diplom.crm.modules.trades.bindings

import kz.iitu.diplom.crm.modules.trades.models.Trade
import kz.iitu.diplom.crm.modules.trades.models.TradeStatus

typealias StatusChangedCallback = (docId: String, newStatus: TradeStatus, position: Int) -> Unit

interface TradeStatusClickListener {
    fun onClick(docId: String, currentStatus: TradeStatus)
}

interface TradeClickListener {
    fun onClick(trade: Trade)
}