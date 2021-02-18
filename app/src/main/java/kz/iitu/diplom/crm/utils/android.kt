package kz.iitu.diplom.crm.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = activity?.currentFocus
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
}