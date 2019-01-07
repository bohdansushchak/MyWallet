package bohdan.sushchak.mywallet.ui.list_orders

import androidx.lifecycle.ViewModel;
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.lazyDeffered

class OrderListViewModel(myWalletRepository: MyWalletRepository) : ViewModel() {

    val orderList by lazyDeffered { myWalletRepository.getOrders() }


}
