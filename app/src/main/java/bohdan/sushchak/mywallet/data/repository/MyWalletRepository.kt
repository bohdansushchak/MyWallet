package bohdan.sushchak.mywallet.data.repository

import androidx.lifecycle.LiveData
import bohdan.sushchak.mywallet.data.model.OrderWithProducts
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.data.model.CategoryCount
import bohdan.sushchak.mywallet.data.model.CategoryPrice
import bohdan.sushchak.mywallet.data.model.MoneyByDate
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
    suspend fun createOrderWithProducts(order : OrderEntity, products: List<ProductEntity>)

    suspend fun getOrdersWithProducts(): LiveData<List<OrderWithProducts>>

    suspend fun getOrders(): LiveData<List<OrderEntity>>

    suspend fun removeOrder(order: OrderEntity)

    suspend fun getOrdersByDate(date: Long): List<OrderEntity>

    suspend fun getTotalPriceByDate(startDate: Long, endDate: Long): List<MoneyByDate>
    //endregion

    suspend fun viewDataBase()

}