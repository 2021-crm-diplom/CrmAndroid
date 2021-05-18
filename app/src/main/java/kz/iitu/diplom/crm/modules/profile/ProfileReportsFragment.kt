package kz.iitu.diplom.crm.modules.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.BaseFragment

class ProfileReportsFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_reports_fragments, container, false)
        return view
    }
}