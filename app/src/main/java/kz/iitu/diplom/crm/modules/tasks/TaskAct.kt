package kz.iitu.diplom.crm.modules.tasks

import kz.iitu.diplom.crm.modules.tasks.models.TaskStatus

interface TaskAct {
    fun onTaskClicked()
    fun onTaskStatusClicked(docId: String, status: TaskStatus, position: Int)
    fun onRetryLoadTasks()
}