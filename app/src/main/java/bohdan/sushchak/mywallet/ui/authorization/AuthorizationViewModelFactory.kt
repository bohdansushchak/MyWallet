package bohdan.sushchak.mywallet.ui.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository

class AuthorizationViewModelFactory(private val repository: MyWalletRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthorizationViewModel(repository) as T
    }
}