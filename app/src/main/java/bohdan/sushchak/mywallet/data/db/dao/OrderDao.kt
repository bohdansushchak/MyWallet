package bohdan.sushchak.mywallet.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.data.model.MoneyByDate
import bohdan.sushchak.mywallet.data.model.OrderWithProducts
import bohdan.sushchak.mywallet.internal.setOrderId
import com.github.sundeepk.compactcalendarview.domain.Event

@Dao
abstract class OrderDao : BaseDao<OrderEntity> {

    @Query("select * from orders")
    abstract fun getOnlyOrders(): LiveData<List<OrderEntity>>

    @Query("select * from orders where orderId = :id")
    abstract fun getOrder(id: Int): LiveData<OrderEntity>

    @Query("select * from orders")
    abstract fun getOrdersWithProducts(): LiveData<List<OrderWithProducts>>

    @Query("select * from orders order by date desc")
    abstract fun getOrders(): LiveData<List<OrderEntity>>

    @Query("select orderId from orders where date = :id")
    abstract fun getOrdersIdByDate(id: Long): List<Long>?

    @Query("select * from orders")
    abstract fun getOrdersNonLive(): List<OrderEntity>?

    @Query("select * from orders where date = :date")
    abstract fun getOrdersByDate(date: Long): List<OrderEntity>?

    @Query("select * from orders where date between :startDate and :endDate ")
    abstract fun getOrdersOfDateRange(startDate: Long, endDate: Long): LiveData<List<OrderEntity>>

    @Query("select date from orders group by date")
    abstract fun getAllEvents(): LiveData<List<Event>>

    @Query("select [orders].[date], sum(total_price) as 'totalPrice' from orders where date between :startDate and :endDate group by date")
    abstract fun getSpendMoneyByDateNonLive(startDate: Long, endDate: Long): List<MoneyByDate>

    @Query("select min(date) from orders")
    abstract fun getLeastDateInDb(): Long?

    @Query("select max(date) from orders")
    abstract fun getBiggestDateInDb(): Long?

    @Query("DELETE FROM orders WHERE orderId = :id")
    abstract fun removeById(id: Long)

    @Transaction
    open fun insertOrderWithProducts(
        productDao: ProductDao,
        order: OrderEntity,
        products: List<ProductEntity>
    ): HashMap<String, Any?> {
        val idOrder = insert(order)
        products.setOrderId(idOrder)
        val productIds = productDao.insertList(products)

        return hashMapOf(
            "orderId" to idOrder,
            "productIds" to productIds
        )
    }

    @Query("DELETE FROM orders")
    abstract fun clearTable()


    @Transaction
    open fun replaceAll(
        productDao: ProductDao,
        orderWithProducts: List<OrderWithProducts>
    ) {
        clearTable()
        productDao.clearTable()

        orderWithProducts.forEach {
            insert(it.order)
            it.products.forEach { product ->
                try {
                    productDao.insert(product)
                } catch (e: Exception) {
                    val withoutCategoryId = product.copy(categoryId = null)
                    productDao.insert(withoutCategoryId)
                }
            }
        }
    }

    @Transaction
    open fun editOrderWithProducts(
        productDao: ProductDao,
        order: OrderEntity,
        products: List<ProductEntity>
    ): HashMap<String, Any?> {
        delete(order)
        return insertOrderWithProducts(productDao, order, products)
    }
}