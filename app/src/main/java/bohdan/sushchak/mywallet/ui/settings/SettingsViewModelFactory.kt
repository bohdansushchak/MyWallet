package bohdan.sushchak.mywallet.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository

class SettingsViewModelFactory(
    private val myWalletRepository: MyWalletRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(myWalletRepository) as T
    }
}