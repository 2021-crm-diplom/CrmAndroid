package kz.iitu.diplom.crm.modules.trades.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.SpannedString
import android.text.style.StrikethroughSpan
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
        val titleView = view.findViewById<TextView>(R.id.title)
        titleView.text = SpannableString(task.title)
        updateTaskIcon(iconView, task.isCompleted)
        updateTaskTitle(titleView, task.isCompleted)
        iconView.setOnClickListener { updateTask(task, iconView, titleView) }
        layoutTasks.addView(view)
    }

    private fun updateTask(task: Task, iconView: ImageView, titleView: TextView) {
        val task = this.tasks.firstOrNull { it.id == task.id }
        val index = this.tasks.indexOf(task)
        task?.let {
            val completed = task.isCompleted != true
            this.tasks[index] = task.copy(isCompleted = completed)
            updatedTasks.add(task.copy(isCompleted = completed))
            updateTaskIcon(iconView, completed)
            updateTaskTitle(titleView, completed)
        }
    }

    private fun updateTaskIcon(iconView: ImageView, isCompleted: Boolean?) {
        val icon = when (isCompleted) {
            true -> ContextCompat.getDrawable(context, R.drawable.ic_task_completed)
            else -> ContextCompat.getDrawable(context, R.drawable.ic_task_not_completed)
        }
        iconView.setImageDrawable(icon)
    }

    private fun updateTaskTitle(titleView: TextView, isCompleted: Boolean?) {
        if(isCompleted == true) {
            val spannable = SpannableString(titleView.text)
            spannable.setSpan(StrikethroughSpan(), 0, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            titleView.setTextColor(ContextCompat.getColor(context, R.color.gray))
            titleView.text = spannable
        } else {
            val spannable = SpannableString.valueOf((titleView.text as SpannedString))
            val spans = spannable.getSpans(0, spannable.length, StrikethroughSpan::class.java)
            spans.forEach { spannable.removeSpan(it) }
            titleView.setTextColor(ContextCompat.getColor(context, R.color.black))
            titleView.text = spannable
        }
    }
}