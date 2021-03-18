package kz.iitu.diplom.crm.modules.all_tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.BaseFragment
import kz.iitu.diplom.crm.models.TaskStatus
import kz.iitu.diplom.crm.widgets.TaskWidget
import java.util.*

class AllTasksFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.all_tasks_fragment, container ,false)
        val task = view.findViewById<TaskWidget>(R.id.task_widget)
        task.setTaskStatus(TaskStatus.REJECTED)
        task.setTitle("Начать")
        task.setStartDate(Date())
        task.setDeadline(Date(System.currentTimeMillis() - 86400000))
        return view
    }
}