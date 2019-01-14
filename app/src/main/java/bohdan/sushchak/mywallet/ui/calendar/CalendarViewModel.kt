package bohdan.sushchak.mywallet.ui.calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.internal.lazyDeffered
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CalendarViewModel(private val myWalletRepository: MyWalletRepository) : ViewModel() {

    val orders:MutableLiveData<List<Order>> = MutableLiveData()

    val calendarDates by lazyDeffered { myWalletRepository.getEvents() }
    val totalPrice: MutableLiveData<Double> = MutableLiveData()
    private var lastDate: Long = -1

    init {
        orders.postValue(listOf())
    }

    fun updateOrders(date: Long){
        GlobalScope.launch {
            val orderList = myWalletRepository.getOrdersByDate(date)

            var tPrice = 0.0
            orderList.forEach { order ->
                tPrice += order.price
            }

            orders.postValue(orderList)
            totalPrice.postValue(tPrice)

            lastDate = date
        }
    }

    fun removeOrder(order: Order) {
        GlobalScope.launch {
            myWalletRepository.removeOrder(order)
            updateOrders(lastDate)
        }
    }

}
