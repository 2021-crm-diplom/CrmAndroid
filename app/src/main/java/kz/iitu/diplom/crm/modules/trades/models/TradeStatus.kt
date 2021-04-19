package kz.iitu.diplom.crm.modules.trades.models

import kz.iitu.diplom.crm.R

enum class TradeStatus(val title: String, val color: Int) {
    WAITING("В ожидании", R.color.silver),
    IN_WORK("В работе", R.color.silver),
    COMPLETED("Завершена", R.color.green),
    PAUSED("Приостановлена", R.color.silver),
    REJECTED("Отклонена", R.color.red);

    companion object {
        fun fromStringValue(value: String?) = values().firstOrNull { value == it.title }
    }
}