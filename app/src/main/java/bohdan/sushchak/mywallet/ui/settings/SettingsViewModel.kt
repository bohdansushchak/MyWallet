package bohdan.sushchak.mywallet.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.lazyDeferred
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsViewModel(private val myWalletRepository: MyWalletRepository) : ViewModel() {

    private val mAuth by lazy { FirebaseAuth.getInstance() }
    private val _emailVerificationResult by lazy { MutableLiveData<HashMap<String, Int>>() }
    private val _isEmailVerified by lazy {
        MutableLiveData<Boolean>()
            .apply { postValue(mAuth.currentUser?.isEmailVerified) }
    }

    private val _currentUser by lazy {
        MutableLiveData<FirebaseUser>()
            .apply { postValue(mAuth.currentUser) }
    }

    val currentUser: LiveData<FirebaseUser>
        get() = _currentUser

    val emailVerificationResult: LiveData<HashMap<String, Int>>
        get() = _emailVerificationResult

    val isEmailVerified: LiveData<Boolean>
        get() = _isEmailVerified

    val categories by lazyDeferred { myWalletRepository.getCategories() }

    fun addCategory(categoryEntity: CategoryEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            myWalletRepository.addCategory(categoryEntity)
        }
    }

    fun removeCategory(categoryEntity: CategoryEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            myWalletRepository.removeCategory(categoryEntity)
        }
    }

    fun updateCategory(categoryEntity: CategoryEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            myWalletRepository.updateCategory(categoryEntity)
        }
    }

    fun signOut() {
        mAuth.signOut()
    }

    fun sendEmailVerification() {
        mAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
            val result = hashMapOf(
                "title" to R.string.d_title_email_verification,
                "msg" to R.string.d_msg_email_verification
            )
            _emailVerificationResult.postValue(result)
        }?.addOnFailureListener {
            val result = hashMapOf(
                "title" to R.string.d_title_error_email_verification,
                "msg" to R.string.d_msg_error_email_verification
            )
            _emailVerificationResult.postValue(result)
        }
    }

    fun changePassword(password: String, repeatPassword: String) {
        GlobalScope.launch(Dispatchers.IO) {
            if (password == repeatPassword)
                mAuth.currentUser?.updatePassword(password)

        }
    }
}
