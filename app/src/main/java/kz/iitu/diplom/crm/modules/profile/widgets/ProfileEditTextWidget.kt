package kz.iitu.diplom.crm.modules.profile.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kz.iitu.diplom.crm.R

class ProfileEditTextWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttributeSet: Int = 0)
    : FrameLayout(context, attrs, defStyleAttributeSet) {

    val view = inflate(context, R.layout.profile_edit_text_widget, this) as FrameLayout
    private var inputLayout: TextInputLayout
    private var editText: TextInputEditText

    private var hint: String? = null

    private var editable: Boolean = true
        set(value) {
            field = value
            editText.isFocusable = value
            if(!value) editText.keyListener = null
        }

    var text: String? = null
        get() = editText.text.toString()
        set(value) {
            field = value
            editText.setText(value)
        }

    init {
        inputLayout = view.getChildAt(0) as TextInputLayout
        editText = inputLayout.editText as TextInputEditText
        editable = false
        isClickable = false
        context.theme.obtainStyledAttributes(attrs, R.styleable.ProfileEditTextWidget, 0, 0).apply {
            try {
                hint = getString(R.styleable.ProfileEditTextWidget_hint)
                editable = getBoolean(R.styleable.ProfileEditTextWidget_editable, true)
            }
            finally {
                recycle()
            }
        }
        hint?.let { inputLayout.hint = it }
    }
}