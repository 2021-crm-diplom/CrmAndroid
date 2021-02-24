package kz.iitu.diplom.crm.modules.profile.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import com.google.android.material.textfield.TextInputLayout
import kz.iitu.diplom.crm.R

class ProfileGenderWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttributeSet: Int = 0)
    : FrameLayout(context, attrs, defStyleAttributeSet) {

    val view = inflate(context, R.layout.profile_gender_widget, this) as FrameLayout

    private var inputLayout: TextInputLayout
    private var editText: AutoCompleteTextView


    init {
        inputLayout = view.getChildAt(0) as TextInputLayout
        editText = inputLayout.editText as AutoCompleteTextView
    }

    fun setItems(items: List<String>) {
        val adapter = ArrayAdapter(context, R.layout.profile_gender_list_item, items)
        editText.setAdapter(adapter)
    }

}