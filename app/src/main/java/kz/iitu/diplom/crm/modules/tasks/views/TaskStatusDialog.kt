package kz.iitu.diplom.crm.modules.tasks.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.modules.tasks.StatusChangedCallback
import kz.iitu.diplom.crm.modules.tasks.models.TaskStatus

class TaskStatusDialog(
    private val taskId: String,
    private val currentStatus: TaskStatus,
    private val statusChangedCallback: StatusChangedCallback
) : BottomSheetDialogFragment() {

    private lateinit var layoutStatusActions: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.task_status_dialog, container, false)
        layoutStatusActions = view.findViewById(R.id.layoutStatusActions)
        initStatuses()
        return view
    }

    private fun initStatuses() {
        val statuses = enumValues<TaskStatus>()
        statuses.forEach { status ->
            if(status != currentStatus) {
                addStatusAction(status)
            }
        }
    }

    private fun addStatusAction(taskStatus: TaskStatus) {
        val view = layoutInflater.inflate(R.layout.task_status_dialog_item, layoutStatusActions, false) as TextView
        if(taskStatus == TaskStatus.COMPLETED || taskStatus == TaskStatus.REJECTED) {
            view.setTextColor(ContextCompat.getColor(requireContext(), taskStatus.color))
        }
        view.setOnClickListener {
            statusChangedCallback.invoke(taskId, taskStatus)
            dismiss()
        }
        view.text = getString(taskStatus.title)
        layoutStatusActions.addView(view)
    }
}