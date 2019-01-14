package bohdan.sushchak.mywallet.ui.graph

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository

class GraphViewModelFactory(private val myWalletRepository: MyWalletRepository)
    : ViewModelProvider.NewInstanceFactory()  {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GraphViewModel(myWalletRepository) as T
    }
}