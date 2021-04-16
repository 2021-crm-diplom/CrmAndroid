package kz.iitu.diplom.crm.modules.tasks

import androidx.databinding.BindingAdapter
import kz.iitu.diplom.crm.modules.tasks.models.Task
import kz.iitu.diplom.crm.modules.tasks.views.TaskWidget

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("task")
    fun TaskWidget.setTask(task: Task) {
        this.setTask(task)
    }

    @JvmStatic
    @BindingAdapter("onStatusClicked")
    fun TaskWidget.onStatusClicked(listener: TaskStatusClickListener) {
        this.setStatusClickListener(listener)
    }

    @JvmStatic
    @BindingAdapter("onTaskClicked")
    fun TaskWidget.onTaskClicked(listener: TaskClickListener) {
        this.setTaskClickListener(listener)
    }
}