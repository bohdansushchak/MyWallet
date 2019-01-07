package bohdan.sushchak.mywallet.data.repository

import androidx.lifecycle.LiveData
import bohdan.sushchak.mywallet.data.db.dao.CategoryDao
import bohdan.sushchak.mywallet.data.db.dao.OrderDao
import bohdan.sushchak.mywallet.data.db.dao.ProductDao
import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.db.entity.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyWalletRepositoryImpl(
        private val categoryDao: CategoryDao,
        private val orderDao: OrderDao,
        private val productDao: ProductDao
) : MyWalletRepository {

    override suspend fun createOrderWithProducts(order: Order, products: List<Product>) {
        GlobalScope.launch {
            orderDao.insertOrderWithProducts(order, products)
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
    //endregion
}