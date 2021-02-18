package kz.iitu.diplom.crm.modules.startup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.BaseFragment

class WelcomeFragment : BaseFragment() {

    override var title: String? = null

    private lateinit var delegate: Delegate
    private lateinit var welcomePager: ViewPager2
    private lateinit var welcomeTabLayout: TabLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        delegate = context as Delegate
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.startup_welcome_fragment, container, false)
        welcomePager = view.findViewById(R.id.welcome_text_pager)
        welcomeTabLayout = view.findViewById(R.id.welcome_text_tabLayout)
        initViewPager()
        TabLayoutMediator(welcomeTabLayout, welcomePager) { tab, position -> }.attach()

        val signInButton: Button = view.findViewById(R.id.buttonSignin)
        val exitButton: Button = view.findViewById(R.id.buttonExit)
        signInButton.setOnClickListener { delegate.onSignInClicked() }
        exitButton.setOnClickListener { delegate.onExitClicked() }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        delegate.hideActionBar()
    }

    private fun initViewPager() {
        val pagerAdapter = WelcomePagerAdapter()
        pagerAdapter.setItems(getWelcomePagerItems())
        welcomePager.adapter = pagerAdapter
    }

    private fun getWelcomePagerItems(): Array<String> {
        return resources.getStringArray(R.array.startup_welcome_pager_items)
    }

    interface Delegate {
        fun hideActionBar()
        fun onSignInClicked()
        fun onExitClicked()
    }
}