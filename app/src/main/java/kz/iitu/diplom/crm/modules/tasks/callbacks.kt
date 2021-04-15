package kz.iitu.diplom.crm.modules.tasks

import kz.iitu.diplom.crm.modules.tasks.models.Task
import kz.iitu.diplom.crm.modules.tasks.models.TaskStatus

typealias StatusChangedCallback = (taskIs: String, newStatus: TaskStatus) -> Unit
typealias TasksLoadedCallback = (tasks: List<Task>?) -> Unit