package kz.iitu.diplom.crm.modules.trades.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.BindingRecyclerAdapter
import kz.iitu.diplom.crm.modules.trades.details.CommentsState
import kz.iitu.diplom.crm.modules.trades.Status
import kz.iitu.diplom.crm.modules.trades.models.Comment

class CommentsWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttributeSet: Int = 0)
    : LinearLayout(context, attrs, defStyleAttributeSet) {

    private var recyclerView: RecyclerView
    private var adapter: CommentsAdapter? = null
    var state: CommentsState = CommentsState(Status.LOADED)
        set(value) {
            adapter?.state = value
            adapter?.notifyDataSetChanged()
            field = value
        }

    init {
        val view = inflate(context, R.layout.comments_widget, this)
        adapter = CommentsAdapter()
        recyclerView = view.findViewById(R.id.comments_recycler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.isNestedScrollingEnabled = false
        orientation = VERTICAL
    }

    fun addAndUpdateComments(comment: Comment) {
        state = CommentsState(Status.LOADED, state.comments.plus(comment))
    }

    class CommentsAdapter : BindingRecyclerAdapter() {
        var state = CommentsState(Status.LOADED)

        override fun bindingForPosition(position: Int) = when {
            state.status == Status.LOADING -> Pair(null, null)
            state.status == Status.FAILED -> Pair(null, null)
            state.comments.isNullOrEmpty() -> Pair(null, null)
            else -> Pair(state.comments[position], null)
        }

        override fun layoutIdForPosition(position: Int) = when {
            state.status == Status.LOADING -> R.layout.comments_loading_item
            state.status == Status.FAILED -> R.layout.comments_failed_item
            state.comments.isNullOrEmpty() -> R.layout.comments_empty_item
            else -> R.layout.comments_loaded_item
        }

        override fun getItemCount() = when {
            state.status == Status.LOADING -> 1
            state.status == Status.FAILED -> 1
            state.comments.isNullOrEmpty() -> 1
            else -> state.comments.size
        }
    }
}