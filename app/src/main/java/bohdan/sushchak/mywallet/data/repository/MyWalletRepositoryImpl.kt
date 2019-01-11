package bohdan.sushchak.mywallet.data.repository

import androidx.lifecycle.LiveData
import bohdan.sushchak.mywallet.data.db.dao.CategoryDao
import bohdan.sushchak.mywallet.data.db.dao.DateDao
import bohdan.sushchak.mywallet.data.db.dao.OrderDao
import bohdan.sushchak.mywallet.data.db.dao.ProductDao
import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.data.db.entity.Date
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.db.entity.Product
import bohdan.sushchak.mywallet.data.db.model.CategoryCount
import bohdan.sushchak.mywallet.data.db.model.OrderWithProducts
import bohdan.sushchak.mywallet.data.db.model.OrdersByDate
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyWalletRepositoryImpl(
        private val categoryDao: CategoryDao,
        private val orderDao: OrderDao,
        private val productDao: ProductDao,
        private val dateDao: DateDao
) : MyWalletRepository {

    override suspend fun getDates(): LiveData<List<Event>> {
       return withContext(Dispatchers.IO) {
           return@withContext dateDao.getAllDates()
       }
    }

    //region category
    override suspend fun getCategories(): LiveData<List<Category>> {
        return withContext(Dispatchers.IO) {
            return@withContext categoryDao.getAllCategories()
        }
    }

    override suspend fun addCategory(category: Category) {
        categoryDao.insert(category)
    }

    override suspend fun removeCategory(category: Category) {
        categoryDao.delete(category)
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.update(category)
    }

    override suspend fun getCategoryCountByProductTitle(categoryTitle: String): List<CategoryCount> {
        return withContext(Dispatchers.IO) {
            return@withContext productDao.getCategoriesCountByProductTitle(categoryTitle)
        }
    }

    override suspend fun getCategoryById(id: Long): Category {
        return categoryDao.getCategoryById(id)
    }

    //endregion

    //region Order
    override suspend fun createOrderWithProducts(order: Order, products: List<Product>) {
        orderDao.insertOrderWithProducts(productDao, order, products)
    }

    override suspend fun getOrdersWithProducts(): LiveData<List<OrderWithProducts>> {
        return withContext(Dispatchers.IO) {
            return@withContext orderDao.getOrdersWithProducts()
        }
    }

    override suspend fun getOrders(): LiveData<List<OrdersByDate>> {
        return withContext(Dispatchers.IO) {
            return@withContext dateDao.getOrdersByDate()
        }
    }

    override suspend fun removeOrder(order: Order) {
        orderDao.removeOrder(order, dateDao)
    }

    override suspend fun getOrdersByDate(date: Long?): List<Order> {
        return withContext(Dispatchers.IO) {
            val dateDB = dateDao.getDateById(date)

            return@withContext orderDao.getOrdersByDateId(dateDB?.id)
        }
    }

    //endregion

    //region Date
    override suspend fun getDateId(date: Long): Long? {
        return dateDao.getIdByDate(date).firstOrNull()
    }

    override suspend fun addDate(date: Date): Long {
        return dateDao.insert(date)
    }

    //endregion
}