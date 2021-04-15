package kz.iitu.diplom.crm.modules.tasks.models

import kz.iitu.diplom.crm.App
import kz.iitu.diplom.crm.R

enum class TaskStatus(val title: Int, val color: Int) {
    WAITING(R.string.task_waiting, R.color.silver),
    IN_WORK(R.string.task_inwork, R.color.silver),
    COMPLETED(R.string.task_completed, R.color.green),
    PAUSED(R.string.task_paused, R.color.silver),
    REJECTED(R.string.task_rejected, R.color.red);

    companion object {
        fun fromStringValue(value: String?) = values().firstOrNull { value == App.string(it.title) }
    }
}