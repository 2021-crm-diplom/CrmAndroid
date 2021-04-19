package kz.iitu.diplom.crm.modules.trades.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.modules.trades.models.Task

class TasksWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttributeSet: Int = 0)
    : LinearLayout(context, attrs, defStyleAttributeSet) {

    private val header: TextView
    private val layoutTasks: LinearLayout
    private val tasks = mutableListOf<Task>()
    val updatedTasks = mutableListOf<Task>()

    init {
        val view = inflate(context, R.layout.tasks_widget, this)
        header = view.findViewById(R.id.header)
        layoutTasks = view.findViewById(R.id.layout_tasks)
        orientation = VERTICAL
    }

    fun setTasks(tasks: List<Task>) {
        if(tasks.isNullOrEmpty()) {
            header.text = context.getString(R.string.trade_tasks_empty)
            layoutTasks.visibility = View.GONE
            return
        }
        this.tasks.clear()
        this.tasks.addAll(tasks)
        tasks.forEach { task ->
            addTask(task)
        }
    }

    private fun addTask(task: Task) {
        val view = LayoutInflater.from(context).inflate(R.layout.task_item, layoutTasks, false)
        val iconView = view.findViewById<ImageView>(R.id.task_icon)
        val title = view.findViewById<TextView>(R.id.title)
        val icon = getTaskIcon(task.isCompleted)
        iconView.setImageDrawable(icon)
        title.text = task.title
        iconView.setOnClickListener {
            updateTaskCompleted(task, iconView)
        }
        layoutTasks.addView(view)
    }

    private fun updateTaskCompleted(task: Task, icon: ImageView) {
        val task = this.tasks.firstOrNull { it.id == task.id }
        val index = this.tasks.indexOf(task)
        task?.let {
            val reversed = task.isCompleted != true
            this.tasks[index] = task.copy(isCompleted = reversed)
            updatedTasks.add(task.copy(isCompleted = reversed))
            icon.setImageDrawable(getTaskIcon(reversed))

        }
    }

    private fun getTaskIcon(isCompleted: Boolean?): Drawable? {
        return when (isCompleted == true) {
            true -> ContextCompat.getDrawable(context, R.drawable.ic_task_completed)
            else -> ContextCompat.getDrawable(context, R.drawable.ic_task_not_completed)
        }
    }
}