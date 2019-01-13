package bohdan.sushchak.mywallet.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import bohdan.sushchak.mywallet.data.model.OrderWithProducts
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.db.entity.Product
import bohdan.sushchak.mywallet.internal.setOrderId
import com.github.sundeepk.compactcalendarview.domain.Event

@Dao
abstract class OrderDao : BaseDao<Order> {

    @Query("select * from orders")
    abstract fun getOnlyOrders(): LiveData<List<Order>>

    @Query("select * from orders where id = :id")
    abstract fun getOrder(id: Int): LiveData<Order>

    @Query("select * from orders")
    abstract fun getOrdersWithProducts(): LiveData<List<OrderWithProducts>>

    @Query("select * from orders")
    abstract fun getOrders(): LiveData<List<Order>>

    @Query("select id from orders where date = :id")
    abstract fun getOrdersIdByDate(id: Long): List<Long>

    @Query("select * from orders where date = :date")
    abstract fun getOrdersByDate(date: Long): List<Order>

    @Query("select * from orders where date between :startDate and :endDate ")
    abstract fun getOrdersOfDateRange(startDate: Long, endDate: Long): LiveData<List<Order>>

    @Query("select date from orders order by date")
    abstract fun getAllDates(): LiveData<List<Event>>

    @Transaction
    open fun insertOrderWithProducts(productDao: ProductDao, order: Order, products: List<Product>){
        val idOrder = insert(order)
        products.setOrderId(idOrder)
        productDao.insert(products)
    }
/*
    @Transaction
    open fun removeOrder(order: Order, dateDao: DateDao){
        val dateId = order.dateId
        delete(order)

        val ordersByDateSize = getOrdersIdByDateId(dateId).size
        if(ordersByDateSize == 0)
            dateDao.removeById(dateId)
    }
    */
}