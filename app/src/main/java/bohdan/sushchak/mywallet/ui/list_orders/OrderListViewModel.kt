package bohdan.sushchak.mywallet.ui.list_orders

import androidx.lifecycle.ViewModel
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity

import bohdan.sushchak.mywallet.data.repository.MyWalletRepository

import bohdan.sushchak.mywallet.internal.lazyDeffered
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class OrderListViewModel(val myWalletRepository: MyWalletRepository) : ViewModel() {

    val orders by lazyDeffered { myWalletRepository.getOrders() }

    val categories by lazyDeffered { myWalletRepository.getCategories() }

    fun removeOrder(order: OrderEntity) {
        GlobalScope.launch {
            myWalletRepository.removeOrder(order)
        }
    }

    /*
(id:1, title:Ех, date:1550354400000, price:892.0)
(id:2, title:мяско, date:1550354400000, price:50.0)
(id:3, title:жд, date:1550268000000, price:135.0)
(id:4, title:льж, date:1550617200000, price:275.0)
 */
}
