package bohdan.sushchak.mywallet.ui.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository

class AuthorizationViewModel(repository: MyWalletRepository): ViewModel() {

    private val _isLoggedIn by lazy {MutableLiveData<Boolean>()}

    val iaLoggedIn: LiveData<Boolean>
    get() = _isLoggedIn

    fun logIn(email: String, password: String){

    }

    fun signIn(email: String, password: String, repeatPassword: String ) {

    }
}