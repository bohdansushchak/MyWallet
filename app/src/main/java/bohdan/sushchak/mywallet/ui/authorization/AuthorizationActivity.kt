package bohdan.sushchak.mywallet.ui.authorization

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.internal.SyncType
import bohdan.sushchak.mywallet.ui.MainActivity
import bohdan.sushchak.mywallet.ui.base.BaseActivity
import bohdan.sushchak.mywallet.ui.sync.SyncActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.authorization_activity.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


enum class AuthorizationType {
    SIGN_IN,
    SIGN_UP
}

const val RC_SIGN_IN = 1001

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

        tvClickForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            val email = etEmail.text.toString().trim()
            intent.putExtra("email", email)
            startActivity(intent)
        }
        bindUI()
    }

    private fun bindUI() = launch {
        viewModel.firebaseUser.observe(this@AuthorizationActivity, Observer {
            progressBar_loading.visibility = View.INVISIBLE
            viewModel.databasesCheck()
        })

        viewModel.signInError.observe(this@AuthorizationActivity, Observer { error ->
            showAlert(error)
            progressBar_loading.visibility = View.INVISIBLE
        })

        viewModel.syncType.observe(this@AuthorizationActivity, Observer {
            if (it == SyncType.EQUALS) {
                startActivity<MainActivity>()
            } else {
                startActivity<SyncActivity>()
            }
            finish()
        })
    }

    private fun showAlert(message: String) {
        Log.d("showAlert", message)
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
        btnSendEmail.setOnClickListener {
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

        btnGoogleSignIn.setOnClickListener {
            viewModel.googleSignIn(this)
        }
    }

    private fun initAuthorizationTypeChange() {
        tvClickBackLogin.setOnClickListener {
            if (authorizationType == AuthorizationType.SIGN_IN) {
                authorizationType = AuthorizationType.SIGN_UP
                btnSendEmail.setText(R.string.btn_sign_up)
                tvClickBackLogin.setText(R.string.tv_click_to_log_in)
                etRepeatPasswordLayout.visibility = View.VISIBLE
                btnGoogleSignIn.visibility = View.GONE
            } else {
                authorizationType = AuthorizationType.SIGN_IN
                btnSendEmail.setText(R.string.btn_log_in)
                tvClickBackLogin.setText(R.string.tv_click_to_register)
                etRepeatPasswordLayout.visibility = View.GONE
                btnGoogleSignIn.visibility = View.VISIBLE
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            viewModel.googleSignInResult(task)
        }
    }
}