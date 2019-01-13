package bohdan.sushchak.mywallet.data.repository

import androidx.lifecycle.LiveData
import bohdan.sushchak.mywallet.data.model.OrderWithProducts
import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.db.entity.Product
import bohdan.sushchak.mywallet.data.model.CategoryCount
import bohdan.sushchak.mywallet.data.model.OrdersByDate
import com.github.sundeepk.compactcalendarview.domain.Event


interface MyWalletRepository {

    //region Categories
    suspend fun getCategories(): LiveData<List<Category>>

    suspend fun addCategory(category: Category)

    suspend fun removeCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun getCategoryCountByProductTitle(categoryTitle: String): List<CategoryCount>

    suspend fun getCategoryById(id: Long): Category

    //endregion

    //region Date
    suspend fun getDates(): LiveData<List<Event>>
    //endregion


    //region Order
    suspend fun createOrderWithProducts(order : Order, products: List<Product>)

    suspend fun getOrdersWithProducts(): LiveData<List<OrderWithProducts>>

    suspend fun getOrders(): LiveData<List<Order>>

    suspend fun removeOrder(order: Order)

    suspend fun getOrdersByDate(date: Long): List<Order>
    //endregion

}