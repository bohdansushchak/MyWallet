package bohdan.sushchak.mywallet.ui.graph

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.getSavedCurrency

class GraphViewModelFactory(
    private val myWalletRepository: MyWalletRepository,
    private val context: Context
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val currency = getSavedCurrency(context)
        return GraphViewModel(myWalletRepository, currency) as T
    }
}