package bohdan.sushchak.mywallet.ui.authorization

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class ForgotPasswordActivity : BaseActivity(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: AuthorizationViewModelFactory by instance()

    private lateinit var viewModel: AuthorizationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(AuthorizationViewModel::class.java)

        if (intent.hasExtra("email")) {
            etEmail.setText(intent.getStringExtra("email"))
        }

        bindUI()
    }

    private fun bindUI() = launch {
        btnSendEmail.setOnClickListener {
            val email = etEmail.text.toString().trim()
            if (email.isNotBlank()) {
                progressBar_loading.visibility = View.VISIBLE
                viewModel.forgotPassword(email)
            }
        }

        tvClickBackLogin.setOnClickListener {
            finish()
        }

        viewModel.resetPasswordResult.observe(this@ForgotPasswordActivity, Observer { result ->
            progressBar_loading.visibility = View.INVISIBLE
            if (result["title"] != null && result["msg"] != null)
                showAlertDialog(result["title"]!!, result["msg"]!!)
        })
    }
}
