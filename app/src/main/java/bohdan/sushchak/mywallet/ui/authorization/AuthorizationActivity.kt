package bohdan.sushchak.mywallet.ui.authorization

import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.internal.SyncType
import bohdan.sushchak.mywallet.internal.ValidBasic
import bohdan.sushchak.mywallet.internal.ValidPassword
import bohdan.sushchak.mywallet.internal.Validators
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
    private val validators = Validators()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authorization_activity)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(AuthorizationViewModel::class.java)

        initTextFields()
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

                etPasswordLayout.error = null
                etEmailLayout.error = null
            }
        }
    }

    private fun initTextFields() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            etPasswordLayout.setEndIconTintList(ColorStateList.valueOf(getColor(R.color.colorAccent)))
            etRepeatPasswordLayout.setEndIconTintList(ColorStateList.valueOf(getColor(R.color.colorAccent)))
        }

        etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                validateEmail(s.toString())
            }
        })

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                validatePassword(s.toString())
            }
        })

        etRepeatPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                validateRepeat(s.toString())
            }
        })
    }

    private fun validateEmail(email: String) {
        val validation = validators.email(email)

        etEmailLayout.error = when (validation) {
            ValidBasic.empty -> getString(R.string.email_is_empty)
            ValidBasic.invalid -> getString(R.string.email_is_invalid)
            else -> null
        }
    }

    private fun validatePassword(pass: String) {
        val validation = validators.password(pass)

        if (authorizationType == AuthorizationType.SIGN_IN) {
            if (validation == ValidPassword.empty) {
                etPasswordLayout.error = getString(R.string.password_is_empty)
            } else {
                etPasswordLayout.error = null
            }

            return
        }

        etPasswordLayout.error = when (validation) {
            ValidPassword.empty -> getString(R.string.password_is_empty)
            ValidPassword.noNumbers -> getString(R.string.password_should_number)
            ValidPassword.noLowerChar -> getString(R.string.password_should_lowercase)
            ValidPassword.noUpperChar -> getString(R.string.password_should_uppercase)
            ValidPassword.minLength -> getString(R.string.password_is_short)
            else -> null
        }
    }

    private fun validateRepeat(repeatPass: String) {
        etRepeatPasswordLayout.error =
            if (repeatPass == etPassword.text.toString()) null else getString(R.string.password_mismatch)
    }

    private fun isValid(): Boolean {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        val emailValid = validators.email(email)
        val passValid = validators.password(password)

        validateEmail(email)
        validatePassword(password)

        if (authorizationType == AuthorizationType.SIGN_IN) {
            return emailValid == ValidBasic.valid && passValid != ValidPassword.empty
        }

        if (authorizationType == AuthorizationType.SIGN_UP) {
            val repeatPassword = etRepeatPassword.text.toString().trim()
            val repPassValid = repeatPassword == password

            validateRepeat(repeatPassword)
            return emailValid == ValidBasic.valid && passValid == ValidPassword.valid && repPassValid
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