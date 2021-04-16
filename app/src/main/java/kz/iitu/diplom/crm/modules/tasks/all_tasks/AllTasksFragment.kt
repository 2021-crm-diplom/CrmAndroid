package kz.iitu.diplom.crm.modules.tasks.all_tasks

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.QuerySnapshot
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.AsyncCallback
import kz.iitu.diplom.crm.core.BaseFragment
import kz.iitu.diplom.crm.core.BindingRecyclerAdapter
import kz.iitu.diplom.crm.core.SwipeRecycler
import kz.iitu.diplom.crm.modules.tasks.*
import kz.iitu.diplom.crm.modules.tasks.models.Task
import kz.iitu.diplom.crm.modules.tasks.models.TaskStatus
import kz.iitu.diplom.crm.modules.tasks.views.TaskObj
import kz.iitu.diplom.crm.modules.tasks.views.TaskWidget

class AllTasksFragment : BaseFragment(), TaskAct {

    private var adapter: Adapter? = null
    private var delegate: Delegate? = null
    private var swipeRecycler: SwipeRecycler? = null

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
        swipeRecycler = view.findViewById(R.id.swipe_recycler)
        adapter = Adapter()
        swipeRecycler?.adapter = adapter
        swipeRecycler?.onRefresh {
            loadAllTasks()
        }
        loadAllTasks()
        return view
    }

    override fun onTaskClicked() {
        //TODO: Open Task Details
    }

    override fun onTaskStatusClicked(docId: String, status: TaskStatus, position: Int) {
        delegate?.onStatusClicked(docId, status, statusChangedCallback, position)
    }

    override fun onRetryLoadTasks() {
        loadAllTasks()
    }

    private fun loadAllTasks() {
        swipeRecycler?.isRefreshing = true
        adapter?.state = State(Status.LOADING)
        delegate?.loadAllTasks(object : AsyncCallback {
            override fun onSuccess(result: QuerySnapshot) {
                if(result.isEmpty) adapter?.state = State(Status.LOADED)
                else {
                    val tasks = mutableListOf<Task>()
                    result.forEach { doc -> tasks.add(Task(doc)) }
                    adapter?.state = State(Status.LOADED, tasks)
                }
                swipeRecycler?.isRefreshing = false
            }

            override fun onFailure(e: Exception) {
                adapter?.state = State(Status.FAILED)
                swipeRecycler?.isRefreshing = false
            }
        })
    }

    private val statusChangedCallback: StatusChangedCallback = { docId, status, position ->
        val taskWidget = (swipeRecycler?.findItemAt(position) as? FrameLayout)?.getChildAt(0) as? TaskWidget
        taskWidget?.setTaskStatus(status)
        delegate?.onStatusChanged(docId, status)
    }

    private inner class Adapter : BindingRecyclerAdapter() {
        var state: State = State(Status.LOADED)
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun bindingForPosition(position: Int) = when {
            state.status == Status.LOADING -> Pair(null, null)
            state.status == Status.FAILED -> Pair(null, null)
            state.tasks.isNullOrEmpty() -> Pair(null, null)
            else -> Pair(TaskObj(state.tasks[position], position), this@AllTasksFragment)
        }

        override fun layoutIdForPosition(position: Int) = when {
            state.status == Status.LOADING -> R.layout.task_list_placeholder_item
            state.status == Status.FAILED -> R.layout.task_list_failed_item
            state.tasks.isNullOrEmpty() -> R.layout.task_list_empty_item
            else -> R.layout.task_list_loaded_item
        }

        override fun getItemCount() = when {
            state.status == Status.LOADING -> 1
            state.status == Status.FAILED -> 1
            state.tasks.isNullOrEmpty() -> 1
            else -> state.tasks.size
        }
    }

    interface Delegate {
        fun onStatusClicked(docId: String, currentStatus: TaskStatus, statusChangedCallback: StatusChangedCallback, position: Int)
        fun onStatusChanged(id: String, newStatus: TaskStatus)
        fun loadAllTasks(callback: AsyncCallback)
    }
}