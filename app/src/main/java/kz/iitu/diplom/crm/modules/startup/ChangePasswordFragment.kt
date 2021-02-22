package kz.iitu.diplom.crm.modules.startup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.BaseFragment
import java.lang.NullPointerException

class ChangePasswordFragment : BaseFragment() {

    companion object {
        private const val ARG_ID = "kz.iitu.diplom.crm.modules.startup.ChangePasswordFragment#id"

        fun create(id: String): ChangePasswordFragment {
            val bundle = Bundle()
            bundle.putString(ARG_ID, id)
            return ChangePasswordFragment().apply {
                arguments = bundle
            }
        }
    }

    private lateinit var newPasswordView: EditText
    private lateinit var confirmNewPasswordView: EditText
    private lateinit var confirmButton: Button
    private lateinit var delegate: Delegate

    private lateinit var id: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        delegate = context as Delegate
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.startup_change_password_fragment, container, false)
        title = getString(R.string.startup_change_password_title)
        id = (if(savedInstanceState == null) arguments?.getString(ARG_ID) else savedInstanceState.getString(ARG_ID))
            ?: throw NullPointerException("Phone number cannot be null")
        newPasswordView = view.findViewById(R.id.new_password_edit_text)
        confirmNewPasswordView = view.findViewById(R.id.confirmed_password_edit_text)
        confirmButton = view.findViewById(R.id.button_confirm_continue)
        setConfirmButton()
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARG_ID, id)
    }

    private fun setConfirmButton() {
        confirmButton.setOnClickListener {
            validatePassword()
        }
    }

    private fun validatePassword() {
        val password = newPasswordView.text
        val confirmedPassword = confirmNewPasswordView.text

        when {
            password.isNullOrBlank() || confirmedPassword.isNullOrEmpty() -> showSnackbar(requireView(), R.string.startup_error_empty_password)
            password.length < 8 || confirmedPassword.length < 8 -> showSnackbar(requireView(), R.string.startup_error_password_length)
            password.toString() != confirmedPassword.toString() -> showSnackbar(requireView(), R.string.startup_error_password_not_equal)
            else -> changePassword(password.toString())
        }
    }

    private fun changePassword(newPassword: String) {
        delegate.changePasswordFor(id, newPassword)
    }

    interface Delegate {
        fun changePasswordFor(id: String, newPassword: String)
    }

}