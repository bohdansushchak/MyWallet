package bohdan.sushchak.mywallet.data.repository

import androidx.lifecycle.LiveData
import bohdan.sushchak.mywallet.data.db.OrderWithProducts
import bohdan.sushchak.mywallet.data.db.dao.CategoryDao
import bohdan.sushchak.mywallet.data.db.dao.OrderDao
import bohdan.sushchak.mywallet.data.db.dao.ProductDao
import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.db.entity.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyWalletRepositoryImpl(
        private val categoryDao: CategoryDao,
        private val orderDao: OrderDao,
        private val productDao: ProductDao
) : MyWalletRepository {

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

    override suspend fun getOrders(): LiveData<List<Order>> {
        return withContext(Dispatchers.IO) {
            return@withContext orderDao.getOrders()
        }
    }

    override suspend fun removeOrder(order: Order) {
        orderDao.delete(order)
    }

    //endregion
}