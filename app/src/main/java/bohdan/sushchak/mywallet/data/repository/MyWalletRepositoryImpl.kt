package bohdan.sushchak.mywallet.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import bohdan.sushchak.mywallet.data.db.dao.CategoryDao
import bohdan.sushchak.mywallet.data.db.dao.OrderDao
import bohdan.sushchak.mywallet.data.db.dao.ProductDao
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.data.model.CategoryCount
import bohdan.sushchak.mywallet.data.model.CategoryPrice
import bohdan.sushchak.mywallet.data.model.OrderWithProducts
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyWalletRepositoryImpl(
        private val categoryDao: CategoryDao,
        private val orderDao: OrderDao,
        private val productDao: ProductDao
) : MyWalletRepository {

    override suspend fun getEvents(): LiveData<List<Event>> {
        return withContext(Dispatchers.IO) {
            return@withContext orderDao.getAllEvents()
        }
    }

    //region categoryEntity
    override suspend fun getCategories(): LiveData<List<CategoryEntity>> {
        return withContext(Dispatchers.IO) {
            return@withContext categoryDao.getAllCategories()
        }
    }

    override suspend fun addCategory(categoryEntity: CategoryEntity) {
        categoryDao.insert(categoryEntity)
    }

    override suspend fun removeCategory(categoryEntity: CategoryEntity) {
        categoryDao.delete(categoryEntity)
    }

    override suspend fun updateCategory(categoryEntity: CategoryEntity) {
        categoryDao.update(categoryEntity)
    }

    override suspend fun getCategoryCountByProductTitle(categoryTitle: String): List<CategoryCount> {
        return withContext(Dispatchers.IO) {
            return@withContext productDao.getCategoriesCountByProductTitle(categoryTitle)
                    ?: listOf()
        }
    }

    override suspend fun getCategoryById(id: Long): CategoryEntity? {
        return categoryDao.getCategoryById(id)
    }

    //endregion

    //region OrderEntity
    override suspend fun createOrderWithProducts(order: OrderEntity, products: List<ProductEntity>) {
        orderDao.insertOrderWithProducts(productDao, order, products)
    }

    override suspend fun getOrdersWithProducts(): LiveData<List<OrderWithProducts>> {
        return withContext(Dispatchers.IO) {
            return@withContext orderDao.getOrdersWithProducts()
        }
    }

    override suspend fun getOrders(): LiveData<List<OrderEntity>> {
        return withContext(Dispatchers.IO) {
            return@withContext orderDao.getOrders()
        }
    }

    override suspend fun removeOrder(order: OrderEntity) {
        withContext(Dispatchers.IO) { orderDao.delete(order) }
    }

    override suspend fun getOrdersByDate(date: Long): List<OrderEntity> {
        return withContext(Dispatchers.IO) {
            return@withContext orderDao.getOrdersByDate(date) ?: listOf()
        }
    }

    override suspend fun getCategoriesPrice(startDate: Long, endDate: Long): List<CategoryPrice> {
        viewDataBase()
        return withContext(Dispatchers.IO) {

            val categoryPriceAllList = mutableListOf<CategoryPrice>()

            val totalPriceForCategories = categoryDao.getTotalPriceCategories(startDate, endDate)
                    ?: listOf()
            val totalPriceCategoryNotSet = categoryDao.getTotalPriceCategoryNotSet(startDate, endDate)
                    ?: listOf()

            categoryPriceAllList.addAll(totalPriceForCategories)
            categoryPriceAllList.addAll(totalPriceCategoryNotSet)

            return@withContext categoryPriceAllList.toList()
        }
    }

    override suspend fun viewDataBase() {
        withContext(Dispatchers.IO) {
            Log.d("TAG", "Products")
            productDao.getProducts()?.forEach {
                Log.d("TAG", it.toString())
            }

            Log.d("TAG", "Orders")
            orderDao.getOrdersNonLive()?.forEach {
                Log.d("TAG", it.toString())
            }

            Log.d("TAG", "Categories")
            categoryDao.getAllCategoriesNonLive()?.forEach {
                Log.d("TAG", it.toString())
            }
        }
    }
    //endregion
}