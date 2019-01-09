package bohdan.sushchak.mywallet.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import bohdan.sushchak.mywallet.data.db.model.OrderWithProducts
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.db.entity.Product
import bohdan.sushchak.mywallet.internal.setOrderId

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

    @Transaction
    open fun insertOrderWithProducts(productDao: ProductDao, order: Order, products: List<Product>){
        val idOrder = insert(order)
        products.setOrderId(idOrder)
        productDao.insert(products)
    }
}