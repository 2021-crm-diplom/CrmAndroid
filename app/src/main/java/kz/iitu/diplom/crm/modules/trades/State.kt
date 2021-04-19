package kz.iitu.diplom.crm.modules.trades

import kz.iitu.diplom.crm.modules.trades.models.Trade

class State(
    val status: Status,
    val trades: List<Trade> = listOf()
)

enum class Status {
    LOADING,
    LOADED,
    FAILED
}