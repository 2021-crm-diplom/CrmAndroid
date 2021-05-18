package kz.iitu.diplom.crm.modules.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.BindingRecyclerAdapter
import kz.iitu.diplom.crm.model.Employee

class ProfileEmployeesDialog(
    private val employees: List<Employee>
) : BottomSheetDialogFragment() {

    private var closeButton: ImageView? = null
    private var recyclerView: RecyclerView? = null
    private val adapter = Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheet_Rounded_Theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_employees_dialog, container, false)
        closeButton = view.findViewById(R.id.close_button)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = adapter

        closeButton?.setOnClickListener {
            dismiss()
        }
        loadEmployees()
        return view
    }

    private fun loadEmployees() {
        adapter.setData(employees)
    }

    private class Adapter : BindingRecyclerAdapter() {

        private var data: List<Employee> = listOf()

        fun setData(data: List<Employee>) {
            this.data = data
            notifyDataSetChanged()
        }

        override fun bindingForPosition(position: Int) = Pair(data[position], null)

        override fun layoutIdForPosition(position: Int) = R.layout.profile_employees_dialog_item

        override fun getItemCount() = data.size
    }
}