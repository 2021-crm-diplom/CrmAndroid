package kz.iitu.diplom.crm.modules.startup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import br.com.sapereaude.maskedEditText.MaskedEditText
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.BaseFragment
import kz.iitu.diplom.crm.utils.hideKeyboard

class SignInFragment : BaseFragment() {

    private var delegate: Delegate? = null

    private lateinit var phoneNumberEditText: MaskedEditText
    private lateinit var passwordEdtText: EditText

    override fun onAttach(context: Context) {
        super.onAttach(context)
        delegate = context as Delegate
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.startup_signin_fragment, container, false)
        delegate?.showActionBar()
        title = getString(R.string.startup_login_title)
        phoneNumberEditText = view.findViewById(R.id.phone_number_edit_text)
        passwordEdtText = view.findViewById(R.id.password_edit_text)
        view.findViewById<Button>(R.id.button_sign_in_continue).setOnClickListener {
            validateFields()
        }

        return view
    }

    private fun validateFields() {
        hideKeyboard()
        val phoneNumber = "8" + phoneNumberEditText.rawText
        val password = passwordEdtText.text
        when {
            phoneNumber.isNullOrBlank() || password.isNullOrBlank() -> {
                showSnackbar(requireView(), R.string.startup_error_empty_field)
            }
            password.trim().length < 8 -> {
                showSnackbar(requireView(), R.string.startup_error_password_length)
            }
            else -> delegate?.onPhoneAndPasswordEntered(phoneNumber.toString(), password.toString())
        }
    }

    interface Delegate {
        fun showActionBar()
        fun onPhoneAndPasswordEntered(phone: String, password: String)
    }
}