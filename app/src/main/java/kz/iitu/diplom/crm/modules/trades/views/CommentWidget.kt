package kz.iitu.diplom.crm.modules.trades.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import kz.iitu.diplom.crm.R

class CommentWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttributeSet: Int = 0)
    : LinearLayout(context, attrs, defStyleAttributeSet) {

    private val view = inflate(context, R.layout.comment_widget, this)

    init {

    }
}