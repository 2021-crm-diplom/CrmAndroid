package kz.iitu.diplom.crm.core

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import kz.iitu.diplom.crm.R

class AlertPopup(
    context: Context?,
    title: String,
    message: String,
) {

    private var builder: AlertDialog.Builder? = null

    init {
        builder = context?.let { AlertDialog.Builder(it, R.style.AlertPopup) }
        builder?.setMessage(message)?.setTitle(title)
    }

    fun setPositiveButton(text: String, onClick: (() -> Unit)? = null): AlertPopup {
        val onClickListener = DialogInterface.OnClickListener { _, _ -> onClick?.invoke() }
        builder?.setPositiveButton(text, onClickListener)
        return this
    }

    fun setNegativeButton(text: String, onClick: (() -> Unit)? = null): AlertPopup {
        val onClickListener = DialogInterface.OnClickListener { _, _ -> onClick?.invoke()}
        builder?.setNegativeButton(text, onClickListener)
        return this
    }

    fun onDismiss(callback: (() -> Unit)?): AlertPopup {
        builder?.setOnDismissListener {
            callback?.invoke()
        }
        return this
    }

    fun show() {
        builder?.create()
        builder?.show()
    }
}