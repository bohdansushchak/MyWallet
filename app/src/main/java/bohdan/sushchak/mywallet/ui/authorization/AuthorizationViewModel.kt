package bohdan.sushchak.mywallet.ui.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class AuthorizationViewModel(private val myWalletRepository: MyWalletRepository) : ViewModel() {

    private val _firebaseUser by lazy { MutableLiveData<FirebaseUser>() }
    private val mAuth by lazy { FirebaseAuth.getInstance() }
    private val _signInError by lazy { MutableLiveData<String>() }

    val firebaseUser: LiveData<FirebaseUser>
        get() = _firebaseUser

    val signInError: LiveData<String>
        get() = _signInError

    fun signIn(email: String, password: String) {
        GlobalScope.launch {
            try{
                val signInResult = Tasks.await(mAuth.signInWithEmailAndPassword(email, password))
                _firebaseUser.postValue(signInResult.user)
            }
            catch (e: Exception){
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
            try{
                val registerResult = Tasks.await(mAuth.createUserWithEmailAndPassword(email, password))
                _firebaseUser.postValue(registerResult.user)

                myWalletRepository.registerNewUser()
            } catch (e: Exception){
                _signInError.postValue(e.message)
            }
        }
    }
}