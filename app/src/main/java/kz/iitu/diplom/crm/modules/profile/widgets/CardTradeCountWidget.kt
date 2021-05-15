package kz.iitu.diplom.crm.modules.profile.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import kz.iitu.diplom.crm.R

class CardTradeCountWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttributeSet: Int = 0)
    : MaterialCardView(context, attrs, defStyleAttributeSet) {

    private val view = inflate(context, R.layout.card_trade_count_widget, this)
    private val countView = view.findViewById<TextView>(R.id.count)
    private val titleView = view.findViewById<TextView>(R.id.title)
    private val progress = view.findViewById<ProgressBar>(R.id.progress)

    init {
        radius = context.resources.getDimension(R.dimen.profile_card_radius)
        cardElevation = context.resources.getDimension(R.dimen.profile_card_elevation)
    }

    fun setCount(count: Int) {
        progress.visibility = View.GONE
        countView.visibility = View.VISIBLE
        countView.text = count.toString()
    }

    fun setTitle(title: String) {
        titleView.text = title
    }

    fun setTextColor(color: Int) {
        countView.setTextColor(ContextCompat.getColor(context, color))
    }
}