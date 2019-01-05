package bohdan.sushchak.mywallet.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import bohdan.sushchak.mywallet.data.db.entity.Order

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addOrder(order: Order)

    @Query("select * from orders")
    fun getOrders(): LiveData<List<Order>>

    @Query("select * from orders where id = :id")
    fun getOrder(id: Int): LiveData<Order>

    @Delete
    fun removeOrder(order: Order)
}