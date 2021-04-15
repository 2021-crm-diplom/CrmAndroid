package kz.iitu.diplom.crm.modules.tasks.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.modules.tasks.models.TaskStatus

class TaskStatusWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttributeSet: Int = 0)
    : AppCompatTextView(context, attrs, defStyleAttributeSet) {

    var status: TaskStatus = TaskStatus.WAITING
        set(value) {
            field = value
            text = context.getString(status.title)
            setTextColor(ContextCompat.getColor(context, status.color))
        }

    init {
        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand, 0)
        compoundDrawablePadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6f, context.resources.displayMetrics).toInt()
    }
}