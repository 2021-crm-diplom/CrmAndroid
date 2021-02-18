package kz.iitu.diplom.crm.modules.startup

import android.content.Intent
import android.os.Bundle
import kz.iitu.diplom.crm.core.BaseActivity
import kz.iitu.diplom.crm.modules.main.MainActivity

class StartupActivity : BaseActivity(), WelcomeFragment.Delegate, SignInFragment.Delegate {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pushFragment(WelcomeFragment())
    }

    override fun hideActionBar() {
        hideToolbar()
    }

    override fun showActionBar() {
        showToolbar()
    }

    override fun onSignInClicked() {
        pushFragmentBackStack(SignInFragment())
    }

    override fun onExitClicked() {
        finish()
    }

    override fun onButtonContinueClicked() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}