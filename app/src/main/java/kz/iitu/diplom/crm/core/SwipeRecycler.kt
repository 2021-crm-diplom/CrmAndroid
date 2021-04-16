package kz.iitu.diplom.crm.core

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kz.iitu.diplom.crm.R

class SwipeRecycler @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : SwipeRefreshLayout(context, attrs) {

    private var recyclerView: RecyclerView

    var adapter: BindingRecyclerAdapter? = null
        set(value) {
            field = value
            recyclerView.adapter = field
        }

    init {
        val view: View = inflate(context, R.layout.swipe_recycler, this)
        recyclerView = view.findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    fun onRefresh(action: () -> Unit) {
        setOnRefreshListener { action.invoke() }
    }

    fun findByTag(tag: String): View {
        Log.i("ASDF", " findHolder = ${recyclerView.findViewHolderForAdapterPosition(0)?.itemView}")
        return recyclerView.findViewWithTag(tag)
    }

    fun findItemAt(position: Int): View? {
        return recyclerView.findViewHolderForAdapterPosition(position)?.itemView
    }
}
