package kz.iitu.diplom.crm.modules.trades.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.utils.hideKeyboard

class CommentInputWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttributeSet: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttributeSet) {

    private val sendButton: ImageView
    private var commentEditText: EditText

    init {
        val view = inflate(context, R.layout.comment_input_widget, this)
        sendButton = view.findViewById(R.id.send)
        commentEditText = view.findViewById(R.id.comment_edit_text)
        setBackgroundColor(ContextCompat.getColor(context, R.color.white))

        commentEditText.addTextChangedListener(object: TextWatcher {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s?.isNotBlank() == true) {
                    sendButton.visibility = View.VISIBLE
                } else {
                    sendButton.visibility = View.GONE
                }
            }
            override fun afterTextChanged(s: Editable?) { }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
        })
    }

    fun onSendClick(action: (text: String) -> Unit) {
        sendButton.setOnClickListener {
            if(commentEditText.text.trim().isNotBlank()) {
                action.invoke(commentEditText.text.toString())
            }
        }
    }

    fun clear() {
        commentEditText.text = null
        commentEditText.clearFocus()
        hideKeyboard()
    }
}