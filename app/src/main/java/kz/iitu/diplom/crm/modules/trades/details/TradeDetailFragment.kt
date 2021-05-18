package kz.iitu.diplom.crm.modules.trades.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.QuerySnapshotCallback
import kz.iitu.diplom.crm.core.BaseFragment
import kz.iitu.diplom.crm.core.DocumentReferenceCallback
import kz.iitu.diplom.crm.model.Status
import kz.iitu.diplom.crm.modules.trades.models.Comment
import kz.iitu.diplom.crm.modules.trades.models.Task
import kz.iitu.diplom.crm.modules.trades.models.Trade
import kz.iitu.diplom.crm.modules.trades.models.TradeStatus
import kz.iitu.diplom.crm.modules.trades.views.ClientWidget
import kz.iitu.diplom.crm.modules.trades.views.CommentInputWidget
import kz.iitu.diplom.crm.modules.trades.views.CommentsWidget
import kz.iitu.diplom.crm.modules.trades.views.TasksWidget
import kz.iitu.diplom.crm.utils.AppPreferences
import kz.iitu.diplom.crm.utils.format
import kz.iitu.diplom.crm.utils.formatLocal
import java.util.*

class TradeDetailFragment : BaseFragment() {

    companion object {
        private const val ARG_TRADE = "kz.iitu.diplom.crm.modules.trades.details.TradeDetailFragment"
        fun create(trade: Trade) = TradeDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_TRADE, trade)
            }
        }
    }

    private var scrollView: NestedScrollView? = null
    private var tradeTitle: TextView? = null
    private var tradeDescription: TextView? = null
    private var tradeDeadline: TextView? = null
    private var tradeStatus: TextView? = null
    private var tasksWidget: TasksWidget? = null
    private var clientWidget: ClientWidget? = null
    private var commentsWidget: CommentsWidget? = null
    private var commentInputWidget: CommentInputWidget? = null
    private var fab: FloatingActionButton? = null

    private var trade: Trade? = null
    private var delegate: Delegate? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        delegate = context as Delegate
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.trade_detail_fragment, container, false)
        scrollView = view.findViewById(R.id.scroll_view)
        tradeTitle = view.findViewById(R.id.trade_detail_title)
        tradeDescription = view.findViewById(R.id.trade_detail_description)
        tradeDeadline = view.findViewById(R.id.trade_detail_deadline)
        tradeStatus = view.findViewById(R.id.trade_detail_status)
        tasksWidget = view.findViewById(R.id.tasks_widget)
        clientWidget = view.findViewById(R.id.clientWidget)
        commentsWidget = view.findViewById(R.id.comments_widget)
        commentInputWidget = view.findViewById(R.id.comment_input_widget)
        fab = view.findViewById(R.id.fab)

        trade = savedInstanceState?.getParcelable(ARG_TRADE) ?: arguments?.getParcelable(ARG_TRADE)
        title = trade?.title ?: super.title
        initTrade(trade ?: throw Exception("Trade cannot be null"))

        return view
    }

    override fun onStop() {
        tasksWidget?.updatedTasks?.let {
            delegate?.updateTasksCompleted(it)
        }
        super.onStop()
    }

    private fun initTrade(trade: Trade) {
        tradeTitle?.text = trade.title
        setDescription(trade.description)
        setDeadline(trade.deadline)
        setStatus(trade.status)
        setTasks(trade.tasks)
        initClientWidget(trade)
        initTradeComments(trade.id)
        initScrollFab()
    }

    private fun initScrollFab() {
        scrollView?.viewTreeObserver?.addOnScrollChangedListener {
            if(scrollView?.canScrollVertically(1) == true) {
                fab?.show()
            } else fab?.hide()
        }
        fab?.setOnClickListener {
            scrollView?.run {
                val lastChild = getChildAt(childCount - 1)
                val bottom = lastChild.bottom + paddingBottom
                val delta = bottom - (scrollY + height)
                smoothScrollTo(0, delta)
            }
        }
    }

    private fun initTradeComments(tradeId: String) {
        commentsWidget?.state = CommentsState(Status.LOADING)
        delegate?.loadCommentsForTrade(tradeId, object: QuerySnapshotCallback {
            override fun onSuccess(result: QuerySnapshot) {
                val comments = mutableListOf<Comment>()
                result.forEach { document ->
                    comments.add(Comment(document))
                }
                commentsWidget?.state = CommentsState(Status.LOADED, comments)
            }

            override fun onFailure(e: Exception) {
                commentsWidget?.state = CommentsState(Status.FAILED)
            }

        })
        commentInputWidget?.onSendClick { text ->
            addTradeComment(text)
        }
    }

    private fun initClientWidget(trade: Trade) {
        clientWidget?.setClientName(trade.clientFirstName, trade.clientLastName)
        clientWidget?.setClientPhone(trade.client)
    }

    private fun setDescription(description: String?) {
        if(description != null) {
            tradeDescription?.visibility = View.VISIBLE
            tradeDescription?.text = description
        }
    }

    private fun setDeadline(deadline: Date?) {
        if(deadline != null) {
            tradeDeadline?.visibility = View.VISIBLE
            tradeDeadline?.text = deadline.formatLocal()
        }
    }

    private fun setStatus(status: TradeStatus) {
        tradeStatus?.visibility = View.VISIBLE
        tradeStatus?.text = status.title
        val drawableRes = when(status) {
            TradeStatus.COMPLETED -> R.drawable.task_status_completed
            TradeStatus.IN_WORK -> R.drawable.task_status_in_work
            TradeStatus.REJECTED -> R.drawable.task_status_rejected
            TradeStatus.PAUSED -> R.drawable.task_status_paused
            else -> R.drawable.task_status_default
        }
        tradeStatus?.background = ContextCompat.getDrawable(requireContext(), drawableRes)
    }

    private fun setTasks(tasks: List<Task>) {
        tasksWidget?.setTasks(tasks)
    }

    private fun addTradeComment(text: String) {
        val commentMap = hashMapOf(
            "id" to UUID.randomUUID().toString(),
            "tradeId" to trade?.id,
            "text" to text,
            "author" to "${AppPreferences.lastName} ${AppPreferences.firstName}",
            "date" to Date().format()
        )
        delegate?.addTradeComment(commentMap, object: DocumentReferenceCallback {
            override fun onSuccess(result: DocumentReference) {
                val docId = result.id
                val comment = Comment(
                    docId = docId,
                    id = commentMap["id"],
                    tradeId = commentMap["tradeId"],
                    text = commentMap["text"],
                    author = commentMap["author"],
                    date = commentMap["date"]
                )
                commentsWidget?.addAndUpdateComments(comment)
            }

            override fun onFailure(e: Exception) {}
        })
        commentInputWidget?.clear()
    }

    interface Delegate {
        fun loadCommentsForTrade(tradeId: String, callback: QuerySnapshotCallback)
        fun updateTasksCompleted(updatedTasks: List<Task>)
        fun addTradeComment(comment: Map<String, String?>, callback: DocumentReferenceCallback)
    }
}