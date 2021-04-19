package kz.iitu.diplom.crm.core

import kz.iitu.diplom.crm.R

enum class NavigationMenuItem(val id: Int?) {
    PROFILE(null),
    ALL_TRADES(R.id.menu_trades_all),
    TRADES_WAITING(R.id.menu_trades_waiting),
    TRADES_INWORK(R.id.menu_trades_inwork),
    TRADES_COMPLETED(R.id.menu_trades_completed),
    TRADES_PAUSED(R.id.menu_trades_paused),
    TRADES_REJECTED(R.id.menu_trades_rejected),
    SETTINGS(R.id.menu_settings)
}