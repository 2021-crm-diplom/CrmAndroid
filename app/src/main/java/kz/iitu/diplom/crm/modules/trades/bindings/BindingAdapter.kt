package kz.iitu.diplom.crm.modules.trades.bindings

import androidx.databinding.BindingAdapter
import kz.iitu.diplom.crm.modules.trades.models.Trade
import kz.iitu.diplom.crm.modules.trades.views.TradeWidget

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("trades")
    fun TradeWidget.setTrade(trade: Trade) {
        this.setTrade(trade)
    }

    @JvmStatic
    @BindingAdapter("onStatusClicked")
    fun TradeWidget.onStatusClicked(listener: TradeStatusClickListener) {
        this.setStatusClickListener(listener)
    }

    @JvmStatic
    @BindingAdapter("onTradeClicked")
    fun TradeWidget.onTradeClicked(listener: TradeClickListener) {
        this.setTradeClickListener(listener)
    }
}