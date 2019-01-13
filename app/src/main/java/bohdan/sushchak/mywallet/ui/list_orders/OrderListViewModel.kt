package bohdan.sushchak.mywallet.ui.list_orders

import androidx.lifecycle.ViewModel
import bohdan.sushchak.mywallet.data.db.entity.Order

import bohdan.sushchak.mywallet.data.repository.MyWalletRepository

import bohdan.sushchak.mywallet.internal.lazyDeffered
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class OrderListViewModel(val myWalletRepository: MyWalletRepository) : ViewModel() {

    val orders by lazyDeffered { myWalletRepository.getOrders() }

    val categories by lazyDeffered { myWalletRepository.getCategories() }

    fun removeOrder(order: Order) {
        GlobalScope.launch {
            myWalletRepository.removeOrder(order)
        }
    }
}
