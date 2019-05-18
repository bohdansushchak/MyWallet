package bohdan.sushchak.mywallet.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import bohdan.sushchak.mywallet.data.db.dao.CategoryDao
import bohdan.sushchak.mywallet.data.db.dao.OrderDao
import bohdan.sushchak.mywallet.data.db.dao.ProductDao
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.data.model.*
import bohdan.sushchak.mywallet.internal.dateRangeByYearAndMonth
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Repository is main class to get and change data in whole application.
 *
 * @property categoryDao abstract class to manipulate table of categories
 * @property orderDao abstract class to change data in order table
 * @property productDao abstract class to make a CRUD methods on  product table
 */
class MyWalletRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val orderDao: OrderDao,
    private val productDao: ProductDao
) : MyWalletRepository {

    /**
     * TODO
     *
     * @return a list with contain a date witch need to show event in grapch
     */
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
        withContext(Dispatchers.IO) { categoryDao.insert(categoryEntity) }
    }

    override suspend fun removeCategory(categoryEntity: CategoryEntity) {
        withContext(Dispatchers.IO) { categoryDao.delete(categoryEntity) }
    }

    override suspend fun updateCategory(categoryEntity: CategoryEntity) {
        withContext(Dispatchers.IO) { categoryDao.update(categoryEntity) }
    }

    override suspend fun getCategoryCountByProductTitle(categoryTitle: String): List<CategoryCount> {
        return withContext(Dispatchers.IO) {
            return@withContext productDao.getCategoriesCountByProductTitle(categoryTitle)
                ?: listOf()
        }
    }

    override suspend fun getCategoryById(id: Long): CategoryEntity? {
        return withContext(Dispatchers.IO) { return@withContext categoryDao.getCategoryById(id) }
    }

    //endregion

    //region OrderEntity
    override suspend fun createOrderWithProducts(order: OrderEntity, products: List<ProductEntity>) {
        withContext(Dispatchers.IO) { orderDao.insertOrderWithProducts(productDao, order, products) }
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

    override suspend fun getTotalPriceByDate(startDate: Long, endDate: Long): List<MoneyByDate> {
        return withContext(Dispatchers.IO) {
            viewDataBase()
            return@withContext orderDao.getSpendMoneyByDateNonLive(startDate, endDate)
        }
    }

    override suspend fun viewDataBase() {
        withContext(Dispatchers.Default) {
            Log.d("TAG", "==============================================")
            Log.d("TAG", "Products")
            productDao.getProductsNonLive()?.forEach {
                Log.d("TAG", it.toString())
            }
            Log.d("TAG", "################################################")

            Log.d("TAG", "Orders")
            orderDao.getOrdersNonLive()?.forEach {
                Log.d("TAG", it.toString())
            }

            Log.d("TAG", "################################################")

            Log.d("TAG", "Categories")
            categoryDao.getAllCategoriesNonLive()?.forEach {
                Log.d("TAG", it.toString())
            }
            Log.d("TAG", "==============================================")
        }
    }

    override suspend fun getDateLimit(): DateLimit {
        return withContext(Dispatchers.IO) {
            val leastDate = orderDao.getLeastDateInDb() ?: 0L
            val biggestDate = orderDao.getBiggestDateInDb() ?: 0L
            val actualDateRange = getActualDateRange()

            return@withContext DateLimit(
                leastDate = leastDate,
                biggestDate = biggestDate,
                startDate = actualDateRange.startDate,
                endDate = actualDateRange.endDate
            )
        }
    }

    private fun getActualDateRange(): DateRange {
        val c = Calendar.getInstance()
        val currentMonth = c.get(Calendar.MONTH)
        val currentYear = c.get(Calendar.YEAR)

        return dateRangeByYearAndMonth(month = currentMonth, year = currentYear)
    }
    //endregion

    override suspend fun getProductCategoryList(orderId: Long): List<CategoryProduct> {
        return withContext(Dispatchers.IO) {
            return@withContext productDao.getCategoryProductNonLive(orderId) ?: listOf()
        }
    }
}