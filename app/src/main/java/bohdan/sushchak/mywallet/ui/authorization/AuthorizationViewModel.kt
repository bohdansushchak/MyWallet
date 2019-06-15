package bohdan.sushchak.mywallet.ui.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.SyncType
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AuthorizationViewModel(private val myWalletRepository: MyWalletRepository) : ViewModel() {

    private val _firebaseUser by lazy { MutableLiveData<FirebaseUser>() }
    private val mAuth by lazy { FirebaseAuth.getInstance() }
    private val _signInError by lazy { MutableLiveData<String>() }
    private val _syncEnum by lazy { MutableLiveData<SyncType>() }
    private val _resetPasswordResult by lazy { MutableLiveData<HashMap<String, Int>>() }

    val firebaseUser: LiveData<FirebaseUser>
        get() = _firebaseUser

    val signInError: LiveData<String>
        get() = _signInError

    val syncType: LiveData<SyncType>
        get() = _syncEnum

    val resetPasswordResult: LiveData<HashMap<String, Int>>
    get() = _resetPasswordResult

    fun signIn(email: String, password: String) {
        GlobalScope.launch {
            try {
                val signInResult = Tasks.await(mAuth.signInWithEmailAndPassword(email, password))

                _firebaseUser.postValue(signInResult.user)
            } catch (e: Exception) {
                _signInError.postValue(e.message)
            }
        }
    }

    fun signUp(email: String, password: String, repeatPassword: String) {
        if (password != repeatPassword) {
            _signInError.postValue("Password is mismatching")
            return
        }
        GlobalScope.launch {
            try {
                val registerResult = Tasks.await(mAuth.createUserWithEmailAndPassword(email, password))
                registerResult.user.sendEmailVerification()
                _firebaseUser.postValue(registerResult.user)

                myWalletRepository.registerNewUser(registerResult.user.uid)
            } catch (e: Exception) {
                _signInError.postValue(e.message)
            }
        }
    }

    fun databasesCheck() {
        GlobalScope.launch {
            val sync = myWalletRepository.databasesCompare()
            _syncEnum.postValue(sync)
        }
    }

    fun forgotPassword(email: String) {
        GlobalScope.launch(Dispatchers.IO) {
            mAuth.sendPasswordResetEmail(email).addOnSuccessListener {
               val result = hashMapOf(
                   "title" to R.string.d_title_check_your_email,
                   "msg" to R.string.d_msg_check_your_email
               )
                _resetPasswordResult.postValue(result)
            }.addOnFailureListener {
                val result = hashMapOf(
                    "title" to R.string.d_title_error_reset,
                    "msg" to R.string.d_msg_error_reset
                )
                _resetPasswordResult.postValue(result)
            }
        }
    }
}