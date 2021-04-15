package kz.iitu.diplom.crm.modules.tasks.all_tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.BaseFragment
import kz.iitu.diplom.crm.modules.tasks.StatusChangedCallback
import kz.iitu.diplom.crm.modules.tasks.TasksLoadedCallback
import kz.iitu.diplom.crm.modules.tasks.models.TaskStatus
import kz.iitu.diplom.crm.modules.tasks.views.TaskWidget

class AllTasksFragment : BaseFragment() {

    private var delegate: Delegate? = null
    private lateinit var taskView: TaskWidget

    override fun onAttach(context: Context) {
        super.onAttach(context)
        delegate = context as Delegate
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.all_tasks_fragment, container ,false)
        taskView = view.findViewById(R.id.task_widget)
        taskView.onStatusClicked { id, currentStatus ->
            delegate?.onStatusClicked(id, currentStatus, statusChangedCallback)
        }
        loadAllTasks()
        return view
    }

    private fun loadAllTasks() {
        delegate?.loadAllTasks { tasks ->
            if(tasks.isNullOrEmpty()) {
                //TODO: Handle null tasks
            } else {
                //TODO: Handle Not null tasks
            }
        }
    }

    private val statusChangedCallback: StatusChangedCallback = { id, status ->
        //TODO: Change status in Firebase
    }

    interface Delegate {
        fun onStatusClicked(taskId: String, currentStatus: TaskStatus, statusChangedCallback: StatusChangedCallback)
        fun loadAllTasks(tasksLoadedCallback: TasksLoadedCallback)
    }
}