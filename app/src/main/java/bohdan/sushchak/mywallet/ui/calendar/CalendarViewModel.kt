package bohdan.sushchak.mywallet.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.lazyDeffered
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CalendarViewModel(private val myWalletRepository: MyWalletRepository) : ViewModel() {

    //region public parameters
    val orders: LiveData<List<OrderEntity>>
    get() = _orders

    val totalPrice: LiveData<Double>
    get() = _totalPrice

    val calendarDates by lazyDeffered { myWalletRepository.getEvents() }
    //endregion

    private val _orders by lazy { MutableLiveData<List<OrderEntity>>() }

    private val _totalPrice by lazy { MutableLiveData<Double>() }

    private var lastDate: Long = -1

    init {
        _orders.value = listOf()
    }

    fun updateOrders(date: Long) {
        GlobalScope.launch {
            val orderList = myWalletRepository.getOrdersByDate(date)

            var tPrice = 0.0
            orderList.forEach { order ->
                tPrice += order.price
            }

            _orders.postValue(orderList)
            _totalPrice.postValue(tPrice)

            lastDate = date
        }
    }

    fun removeOrder(order: OrderEntity) {
        GlobalScope.launch {
            myWalletRepository.removeOrder(order)
            updateOrders(lastDate)
        }
    }
}
