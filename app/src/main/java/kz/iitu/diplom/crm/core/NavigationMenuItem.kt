package kz.iitu.diplom.crm.core

import kz.iitu.diplom.crm.R

enum class NavigationMenuItem(val id: Int?) {
    PROFILE(null),
    ALL_TASKS(R.id.menu_tasks_all),
    TASKS_WAITING(R.id.menu_tasks_waiting),
    TASKS_INWORK(R.id.menu_tasks_inwork),
    TASKS_COMPLETED(R.id.menu_tasks_completed),
    TASKS_PAUSED(R.id.menu_tasks_paused),
    TASKS_REJECTED(R.id.menu_tasks_rejected),
    TASKS_SETTINGS(R.id.menu_settings)
}