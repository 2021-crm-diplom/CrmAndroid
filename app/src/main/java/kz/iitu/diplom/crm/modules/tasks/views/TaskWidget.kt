package kz.iitu.diplom.crm.modules.tasks.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.modules.tasks.models.Task
import kz.iitu.diplom.crm.modules.tasks.models.TaskStatus
import kz.iitu.diplom.crm.modules.tasks.views.TaskStatusWidget
import kz.iitu.diplom.crm.utils.isToday
import java.text.SimpleDateFormat
import java.util.*

class TaskWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttributeSet: Int = 0)
    : CardView(context, attrs, defStyleAttributeSet) {

    private val view = inflate(context, R.layout.widget_task, this)
    private var titleView: TextView
    private var taskStartDate: TextView
    private var taskDeadline: TextView
    private var statusWidget: TaskStatusWidget

    private var task: Task? = null

    init {
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
        radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, context.resources.displayMetrics)
        titleView = view.findViewById(R.id.taskTitle)
        taskStartDate = view.findViewById(R.id.taskStartDate)
        taskDeadline = view.findViewById(R.id.taskDeadline)
        statusWidget = view.findViewById(R.id.taskStatus)
    }

    fun setTask(task: Task) {
        setTitle(task.title)
        setStartDate(task.startDate)
        setDeadline(task.deadline)
        setTaskStatus(task.status)
        this.task = task
    }

    fun onStatusClicked(block: (taskId: String, currentStatus: TaskStatus) -> Unit) {
        statusWidget.setOnClickListener {
            val taskId = task?.id ?: throw Exception("Task id cannot be null")
            block.invoke(taskId, statusWidget.status)
        }
    }

    private fun setTitle(title: String) {
        titleView.text = title
    }

    private fun setStartDate(date: Date) {
        if(date.isToday()) {
            taskStartDate.text = context.getString(R.string.task_start_date, context.getString(R.string.task_today))
        } else {
            taskStartDate.text = context.getString(R.string.task_start_date, getFormattedDateString(date))
        }
    }

    private fun setDeadline(date: Date) {
        if(date.isToday()) {
            taskDeadline.text = context.getString(R.string.task_deadline, context.getString(R.string.task_today))
        } else {
            taskDeadline.text = context.getString(R.string.task_deadline, getFormattedDateString(date))
        }
        if(date.before(Date())) {
            taskDeadline.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
    }

    private fun setTaskStatus(status: TaskStatus) {
        statusWidget.status = status
    }

    private fun getFormattedDateString(date: Date): String {
        return SimpleDateFormat("dd MMMM HH:mm", Locale.getDefault()).format(date)
    }
}