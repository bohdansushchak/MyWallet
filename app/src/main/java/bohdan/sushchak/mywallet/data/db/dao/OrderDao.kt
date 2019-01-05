package bohdan.sushchak.mywallet.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import bohdan.sushchak.mywallet.data.db.OrderWithProducts
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.db.entity.Product

@Dao
abstract class OrderDao : BaseDao<Order> {

    @Query("select * from orders")
    abstract fun getOnlyOrders(): LiveData<List<Order>>

    @Query("select * from orders where id = :id")
    abstract fun getOrder(id: Int): LiveData<Order>

    @Query("select * from orders")
    abstract fun getOrders(): LiveData<OrderWithProducts>

    @Transaction
    open fun insertOrderWithProducts(order: Order, products: List<Product>){
        //val idOrder = addOrder(order)
        //products.setOrderId(idOrder.)
       // productDao.insert(products)
    }
}