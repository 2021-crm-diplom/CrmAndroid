package kz.iitu.diplom.crm.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

open class BaseFragment : Fragment() {

    open var title: String? = null
    open var subtitle: String? = null
        set(value) {
            field = value
            baseActivity?.setToolbarSubtitle(value)
        }

    var baseActivity: BaseActivity? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subtitle = null
        baseActivity = context as BaseActivity
        baseActivity?.setToolbarTitle(title)
        baseActivity?.setToolbarSubtitle(subtitle)
    }

    protected fun showSnackbar(view: View, @StringRes resId: Int) {
        Snackbar.make(view, resId, Snackbar.LENGTH_SHORT).show()
    }
}