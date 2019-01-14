package bohdan.sushchak.mywallet.ui.list_orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository

class OrderListViewModelFactory(
        private val myWalletRepository: MyWalletRepository)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OrderListViewModel(myWalletRepository) as T
    }
}
