package kz.iitu.diplom.crm.modules.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.BaseFragment
import kz.iitu.diplom.crm.modules.profile.widgets.ProfileEditTextWidget
import kz.iitu.diplom.crm.modules.profile.widgets.ProfileGenderWidget

class ProfileFragment : BaseFragment() {

    private lateinit var lastNameEditText: ProfileEditTextWidget
    private lateinit var firstNameEditText: ProfileEditTextWidget
    private lateinit var phoneEditText: ProfileEditTextWidget
    private lateinit var genderEditText: ProfileGenderWidget

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
        initGenderWidget()
        return view
    }

    private fun initGenderWidget() {
        val genderItems = stringsOf(R.string.profile_gender_male, R.string.profile_gender_female, R.string.profile_gender_not_selected)
        genderEditText.setItems(genderItems)
    }

    private fun stringsOf(vararg ids: Int): List<String> {
        val list = mutableListOf<String>()
        for(id in ids) list.add(getString(id))
        return list
    }
}