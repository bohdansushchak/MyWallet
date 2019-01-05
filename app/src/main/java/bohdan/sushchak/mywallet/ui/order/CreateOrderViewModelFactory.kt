package bohdan.sushchak.mywallet.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository

class CreateOrderViewModelFactory(
        private val myWalletRepository: MyWalletRepository)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CreateOrderViewModel(myWalletRepository) as T
    }
}