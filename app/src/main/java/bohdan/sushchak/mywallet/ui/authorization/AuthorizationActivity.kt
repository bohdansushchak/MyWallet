package bohdan.sushchak.mywallet.ui.authorization

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.ui.MainActivity
import bohdan.sushchak.mywallet.ui.base.BaseActivity
import bohdan.sushchak.mywallet.ui.calendar.CalendarViewModelFactory
import kotlinx.android.synthetic.main.authorization_activity.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

enum class AuthorizationType {
    SIGN_IN,
    SIGN_UP
}

class AuthorizationActivity : BaseActivity(), KodeinAware {
    override val kodein by closestKodein()

    private val viewModelFactory: AuthorizationViewModelFactory by instance()
    var authorizationType = AuthorizationType.SIGN_IN

    private lateinit var viewModel: AuthorizationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authorization_activity)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(AuthorizationViewModel::class.java)

        initAuthorizationTypeChange()
        initSignInButton()
        bindUI()
    }

    private fun bindUI() = launch {
        viewModel.firebaseUser.observe(this@AuthorizationActivity, Observer {
            startActivity<MainActivity>()
            finish()
            progressBar_loading.visibility = View.GONE
        })

        viewModel.signInError.observe(this@AuthorizationActivity, Observer { error ->
            showAlert(error)
            progressBar_loading.visibility = View.GONE
        })
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this@AuthorizationActivity)
        builder.setMessage(message)
        builder.setCancelable(true)

        builder.setPositiveButton(
            "OK"
        ) { dialog, _ -> dialog.cancel() }

        val alert = builder.create()
        alert.show()
    }

    private fun initSignInButton() {
        btnAuthorization.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            etEmail.setText(email)
            etPassword.setText(password)

            if (!isValid()) return@setOnClickListener

            when (authorizationType) {
                AuthorizationType.SIGN_IN -> {
                    viewModel.signIn(email, password)
                }
                AuthorizationType.SIGN_UP -> {
                    val repeatPassword = etRepeatPassword.text.toString().trim()
                    etRepeatPassword.setText(repeatPassword)
                    viewModel.signUp(email, password, repeatPassword)
                }
            }
            progressBar_loading.visibility = View.VISIBLE
        }
    }

    private fun initAuthorizationTypeChange() {
        tvClickRegister.setOnClickListener {
            if (authorizationType == AuthorizationType.SIGN_IN) {
                authorizationType = AuthorizationType.SIGN_UP
                btnAuthorization.setText(R.string.btn_sign_up)
                tvClickRegister.setText(R.string.tv_click_to_log_in)
                etRepeatPasswordLayout.visibility = View.VISIBLE
            } else {
                authorizationType = AuthorizationType.SIGN_IN
                btnAuthorization.setText(R.string.btn_log_in)
                tvClickRegister.setText(R.string.tv_click_to_register)
                etRepeatPasswordLayout.visibility = View.GONE
            }
        }
    }

    private fun isValid(): Boolean {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (authorizationType == AuthorizationType.SIGN_IN) {
            return email.isNotEmpty() && password.isNotEmpty()
        }

        if (authorizationType == AuthorizationType.SIGN_UP) {
            val repeatPassword = etRepeatPassword.text.toString().trim()
            return email.isNotEmpty() && password.isNotEmpty() && repeatPassword.isNotEmpty()
        }

        return false
    }
}