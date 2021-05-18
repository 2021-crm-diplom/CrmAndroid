package kz.iitu.diplom.crm.modules.trades

import kz.iitu.diplom.crm.model.Status
import kz.iitu.diplom.crm.modules.trades.models.Trade

class TradesState(
    val status: Status,
    val trades: List<Trade> = listOf()
)