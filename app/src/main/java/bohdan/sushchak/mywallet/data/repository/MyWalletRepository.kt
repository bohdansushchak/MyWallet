package bohdan.sushchak.mywallet.data.repository

import androidx.lifecycle.LiveData
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.data.model.*
import bohdan.sushchak.mywallet.internal.SyncType
import com.github.sundeepk.compactcalendarview.domain.Event


interface MyWalletRepository {

    //region Categories
    suspend fun getCategories(): LiveData<List<CategoryEntity>>

    suspend fun addCategory(categoryEntity: CategoryEntity)

    suspend fun removeCategory(categoryEntity: CategoryEntity)

    suspend fun updateCategory(categoryEntity: CategoryEntity)

    suspend fun getCategoryCountByProductTitle(categoryTitle: String): List<CategoryCount>

    suspend fun getCategoryById(id: Long): CategoryEntity?

    suspend fun getCategoriesPrice(startDate: Long, endDate: Long): List<CategoryPrice>
    //endregion

    //region Date
    suspend fun getEvents(): LiveData<List<Event>>
    //endregion


    //region OrderEntity
    suspend fun createOrderWithProducts(orderEntity : OrderEntity, productsEntity: List<ProductEntity>)

    suspend fun getOrdersWithProducts(): LiveData<List<OrderWithProducts>>

    suspend fun getOrders(): LiveData<List<OrderEntity>>

    suspend fun removeOrder(order: OrderEntity)

    suspend fun getOrdersByDate(date: Long): List<OrderEntity>

    suspend fun getTotalPriceByDate(startDate: Long, endDate: Long): List<MoneyByDate>
    //endregion

    suspend fun getDateLimit(): DateLimit

    suspend fun getProductCategoryList(orderId: Long): List<CategoryProduct>

    suspend fun registerNewUser(uid: String)

    suspend fun synchronizeDatabases(syncType: SyncType)

    suspend fun databasesCompare(): SyncType
}