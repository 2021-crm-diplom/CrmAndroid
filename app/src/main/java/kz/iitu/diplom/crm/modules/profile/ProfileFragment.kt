package kz.iitu.diplom.crm.modules.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.AlertPopup
import kz.iitu.diplom.crm.core.BaseFragment
import kz.iitu.diplom.crm.modules.profile.widgets.ProfileEditTextWidget
import kz.iitu.diplom.crm.modules.profile.widgets.ProfileGenderWidget
import kz.iitu.diplom.crm.modules.startup.StartupActivity
import kz.iitu.diplom.crm.utils.AppPreferences

class ProfileFragment : BaseFragment() {

    private lateinit var lastNameEditText: ProfileEditTextWidget
    private lateinit var firstNameEditText: ProfileEditTextWidget
    private lateinit var phoneEditText: ProfileEditTextWidget
    private lateinit var genderEditText: ProfileGenderWidget
    private lateinit var buttonSave: Button
    private lateinit var buttonSignOut: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        lastNameEditText = view.findViewById(R.id.edit_text_lastname)
        firstNameEditText = view.findViewById(R.id.edit_text_first_name)
        phoneEditText = view.findViewById(R.id.edit_text_phone)
        genderEditText = view.findViewById(R.id.edit_text_gender)
        buttonSave = view.findViewById(R.id.button_save)
        buttonSignOut = view.findViewById(R.id.button_signout)
        initFields()
        initGenderWidget()
        initButtons()
        return view
    }

    private fun initFields() {
        lastNameEditText.text = AppPreferences.lastName
        firstNameEditText.text = AppPreferences.firstName
        phoneEditText.text = AppPreferences.phone
    }

    private fun initGenderWidget() {
        val genderItems = stringsOf(R.string.profile_gender_male, R.string.profile_gender_female, R.string.profile_gender_not_selected)
        genderEditText.setItems(genderItems)
    }

    private fun initButtons() {
        buttonSave.setOnClickListener {
            saveAndUpdate()
        }
        buttonSignOut.setOnClickListener {
            signOut()
        }
    }

    private fun saveAndUpdate() {
        if(isFieldsValid()) {
            AppPreferences.lastName = lastNameEditText.text
            AppPreferences.firstName = firstNameEditText.text
        } else {
            return
        }
    }

    private fun isFieldsValid(): Boolean {
        return when {
            lastNameEditText.text.isNullOrBlank() || firstNameEditText.text.isNullOrBlank() -> {
                showSnackbar(requireView(), R.string.profile_empty_fields)
                false
            } else -> {
                showSnackbar(requireView(), R.string.profile_fields_updated)
                true
            }
        }
    }

    private fun signOut() {
        AlertPopup(
            requireContext(),
            getString(R.string.profile_sign_out_title),
            getString(R.string.profile_sign_out_description))
            .setPositiveButton(getString(R.string.exit)) { signOutConfirmed() }
            .setNegativeButton(getString(R.string.cancel)).show()
    }

    private fun signOutConfirmed() {
        AppPreferences.clear()
        val intent = Intent(context, StartupActivity::class.java)
        context?.startActivity(intent)
    }

    private fun stringsOf(vararg ids: Int): List<String> {
        val list = mutableListOf<String>()
        for(id in ids) list.add(getString(id))
        return list
    }
}