package bohdan.sushchak.mywallet.ui.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthorizationViewModel : ViewModel() {

    private val _firebaseUser by lazy { MutableLiveData<FirebaseUser>() }
    private val mAuth by lazy { FirebaseAuth.getInstance() }
    private val _signInError by lazy { MutableLiveData<String>()}

    val firebaseUser: LiveData<FirebaseUser>
        get() = _firebaseUser

    val signInError: LiveData<String>
    get() = _signInError

    fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val fireUser = mAuth.currentUser
                    _firebaseUser.postValue(fireUser)
                } else {
                    if(task.isCanceled){
                        _signInError.postValue("Sign in was canceled")
                    } else{
                        task.exception?.let { _signInError.postValue(it.message) }
                    }
                }
            }
            .addOnFailureListener { exception ->
                _signInError.postValue(exception.message)
            }
    }

    fun signUp(email: String, password: String, repeatPassword: String) {
        if(!password.equals(repeatPassword))
        {
            _signInError.postValue("Password is mismatching")
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val fireUser = mAuth.currentUser
                    _firebaseUser.postValue(fireUser)
                } else {
                    if(task.isCanceled){
                        _signInError.postValue("Sign in was canceled")
                    } else{
                        task.exception?.let { _signInError.postValue(it.message) }
                    }
                }
            }
            .addOnFailureListener { exception ->
                _signInError.postValue(exception.message)
            }
    }
}