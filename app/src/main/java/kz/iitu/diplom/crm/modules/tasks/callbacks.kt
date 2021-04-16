package kz.iitu.diplom.crm.modules.tasks

import kz.iitu.diplom.crm.modules.tasks.models.Task
import kz.iitu.diplom.crm.modules.tasks.models.TaskStatus

typealias StatusChangedCallback = (docId: String, newStatus: TaskStatus, position: Int) -> Unit

interface TaskStatusClickListener {
    fun onClick(docId: String, currentStatus: TaskStatus)
}

interface TaskClickListener {
    fun onClick(task: Task)
}