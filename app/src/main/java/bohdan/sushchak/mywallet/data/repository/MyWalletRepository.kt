package bohdan.sushchak.mywallet.data.repository

import androidx.lifecycle.LiveData
import bohdan.sushchak.mywallet.data.db.OrderWithProducts
import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.db.entity.Product

interface MyWalletRepository {

    //region Categories
    suspend fun getCategories(): LiveData<List<Category>>

    suspend fun addCategory(category: Category)

    suspend fun removeCategory(category: Category)

    suspend fun updateCategory(category: Category)
    //endregion

    //region Order
    suspend fun createOrderWithProducts(order : Order, products: List<Product>)

    suspend fun getOrdersWithProducts(): LiveData<List<OrderWithProducts>>

    suspend fun getOrders(): LiveData<List<Order>>

    suspend fun removeOrder(order: Order)
    //endregion
}