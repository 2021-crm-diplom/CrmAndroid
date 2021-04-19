package kz.iitu.diplom.crm.modules.trades.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.modules.trades.StatusChangedCallback
import kz.iitu.diplom.crm.modules.trades.models.TradeStatus

class TradeStatusDialog(
    private val docId: String,
    private val currentStatus: TradeStatus,
    private val statusChangedCallback: StatusChangedCallback,
    private val position: Int
) : BottomSheetDialogFragment() {

    private lateinit var layoutStatusActions: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.trade_status_dialog, container, false)
        layoutStatusActions = view.findViewById(R.id.layoutStatusActions)
        initStatuses()
        return view
    }

    private fun initStatuses() {
        val statuses = enumValues<TradeStatus>()
        statuses.forEach { status ->
            if(status != currentStatus) {
                addStatusAction(status)
            }
        }
    }

    private fun addStatusAction(tradeStatus: TradeStatus) {
        val view = layoutInflater.inflate(R.layout.trade_status_dialog_item, layoutStatusActions, false) as TextView
        if(tradeStatus == TradeStatus.COMPLETED || tradeStatus == TradeStatus.REJECTED) {
            view.setTextColor(ContextCompat.getColor(requireContext(), tradeStatus.color))
        }
        view.setOnClickListener {
            statusChangedCallback.invoke(docId, tradeStatus, position)
            dismiss()
        }
        view.text = tradeStatus.title
        layoutStatusActions.addView(view)
    }
}