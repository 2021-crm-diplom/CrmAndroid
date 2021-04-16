package kz.iitu.diplom.crm.modules.tasks

import kz.iitu.diplom.crm.modules.tasks.models.Task

class State(
    val status: Status,
    val tasks: List<Task> = listOf()
)

enum class Status {
    LOADING,
    LOADED,
    FAILED
}