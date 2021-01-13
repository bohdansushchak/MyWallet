package bohdan.sushchak.mywallet.ui.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import bohdan.sushchak.mywallet.R
import kotlinx.android.synthetic.main.change_password_dialog.*

class ChangePasswordDialog() : DialogFragment() {

    var onResult: ((newPassword: String) -> Unit)? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.change_password_dialog, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        btnCancel.setOnClickListener { dismiss() }
        btnChange.setOnClickListener {
            if (isValidate()) {
                onResult?.invoke(etPassword.text.toString())
            }
        }
    }

    private fun isValidate(): Boolean {
        val password = etPassword.text?.toString() ?: ""
        val repeatPass = etRepeatPassword.text?.toString() ?: ""

        if (password.isEmpty()) {
            etPassword.error = getString(R.string.password_is_empty)
            etPasswordLayout.isErrorEnabled = true
        } else {
            etPassword.error = null
            etPasswordLayout.isErrorEnabled = false
        }

        when {
            repeatPass.isEmpty() -> {
                etRepeatPassword.error = getString(R.string.password_is_empty)
                etRepeatPasswordLayout.isErrorEnabled = true
            }
            password != repeatPass -> {
                etRepeatPassword.error = getString(R.string.password_mismatch)
                etRepeatPasswordLayout.isErrorEnabled = true
            }
            else -> {
                etRepeatPassword.error = null
                etRepeatPasswordLayout.isErrorEnabled = false
            }
        }

        return password.isNotEmpty() && repeatPass.isNotEmpty() && password == repeatPass
    }
}
