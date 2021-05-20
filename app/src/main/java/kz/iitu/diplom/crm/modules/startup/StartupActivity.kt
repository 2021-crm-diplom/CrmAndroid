package kz.iitu.diplom.crm.modules.startup

import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.AlertPopup
import kz.iitu.diplom.crm.core.BaseActivity
import kz.iitu.diplom.crm.modules.MainActivity
import kz.iitu.diplom.crm.utils.*

class StartupActivity : BaseActivity(), WelcomeFragment.Delegate, SignInFragment.Delegate, ChangePasswordFragment.Delegate {

    companion object {
        private const val EMPLOYEES = "employees"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(AppPreferences.isLogged) {
            goToMain()
        } else {
            pushFragment(WelcomeFragment())
        }
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

    override fun onPhoneAndPasswordEntered(phone: String, password: String) {
        lock {
           firestoreDb.collection(EMPLOYEES)
               .whereEqualTo("phone", phone)
               .get()
               .onSuccess(this) { documents ->
                   if(documents.isEmpty) {
                       AlertPopup(
                           this,
                           getString(R.string.startup_error_employee_not_found_title),
                           getString(R.string.startup_error_employee_not_found_msg))
                           .setPositiveButton(getString(R.string.OK))
                           .show()
                       return@onSuccess
                   }
                   val doc = documents.documents[0]
                   saveUserInfo(doc)
                   Log.i("StartupActivity", "Document ${doc.id} => ${doc.data}")
                   when {
                       password == doc["password"] && password == getString(R.string.default_password) -> navigateToChangePassword(doc.id)
                       password == doc["password"] -> goToMain()
                       else -> showPasswordIncorrect()
                   }
               }
               .onFailure(this) { exception ->
                   Log.e("StartupActivity", "Error getting documents: $exception", exception)
                   AlertPopup(this, getString(R.string.common_error_title), getString(R.string.common_error_message))
               }
       }
    }

    override fun changePasswordFor(id: String, newPassword: String) {
        lock {
            firestoreDb.collection(EMPLOYEES).document(id)
                .update("password", newPassword)
                .onUpdateSuccess(this) {
                    showPasswordChangedSuccessfully()
                }
                .onUpdateFailure(this) {
                    showPasswordChangedFailure()
                }
        }
    }

    private fun navigateToChangePassword(id: String) {
        AlertPopup(
            this,
            getString(R.string.startup_change_password_alert_title),
            getString(R.string.startup_change_password_alert_msg)
        ).setPositiveButton(getString(R.string.OK)).show()
        pushFragmentBackStack(ChangePasswordFragment.create(id))
    }

    private fun goToMain() {
        AppPreferences.isLogged = true
        MainActivity.start(this)
        finish()
    }

    private fun showPasswordIncorrect() {
        AlertPopup(
            this,
            getString(R.string.startup_error_incorrect_password_title),
            getString(R.string.startup_error_incorrect_password_msg))
            .setPositiveButton(getString(R.string.OK)).show()
    }

    private fun showPasswordChangedSuccessfully() {
        AlertPopup(
            this,
            getString(R.string.startup_change_password_success_title),
            getString(R.string.startup_change_password_success_msg))
            .setPositiveButton(getString(R.string.OK)) { goToMain() }
            .onDismiss { goToMain() }
            .show()
        AppPreferences.isLogged = true
    }

    private fun showPasswordChangedFailure() {
        AlertPopup(
            this,
            getString(R.string.common_error_title),
            getString(R.string.common_error_message)
        ).setPositiveButton(getString(R.string.OK)).show()
    }

    private fun saveUserInfo(document: DocumentSnapshot) {
        AppPreferences.phone = document["phone"].toString()
        AppPreferences.firstName = document["firstName"].toString()
        AppPreferences.lastName = document["lastName"].toString()
    }
}