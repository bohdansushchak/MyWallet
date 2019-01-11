package bohdan.sushchak.mywallet.ui.calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.data.db.entity.Order
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CalendarViewModel(private val myWalletRepository: MyWalletRepository) : ViewModel() {

    val orders: MutableLiveData<List<Order>> = MutableLiveData()

    init {
        orders.postValue(listOf())
    }

    fun updateOrders(date: Long){
        GlobalScope.launch {
            val dateId = myWalletRepository.getDateId(date)
            val orderList = myWalletRepository.getOrderByDate(dateId)

            orders.postValue(orderList)
        }
    }

    fun removeOrder(order: Order) {
        GlobalScope.launch {
            myWalletRepository.removeOrder(order)
        }
    }

}
