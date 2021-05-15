package kz.iitu.diplom.crm.modules.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.BaseFragment

class SettingsFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.settings_fragment, container, false)
        return view
    }
}