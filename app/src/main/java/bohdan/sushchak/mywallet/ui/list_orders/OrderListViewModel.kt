package bohdan.sushchak.mywallet.ui.list_orders

import androidx.lifecycle.ViewModel
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity

import bohdan.sushchak.mywallet.data.repository.MyWalletRepository

import bohdan.sushchak.mywallet.internal.lazyDeffered
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class OrderListViewModel(val myWalletRepository: MyWalletRepository) : ViewModel() {

    //region public parameters
    val orders by lazyDeffered { myWalletRepository.getOrders() }

    val categories by lazyDeffered { myWalletRepository.getCategories() }

    //endregion


    fun removeOrder(order: OrderEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            myWalletRepository.removeOrder(order)
        }
    }
}
