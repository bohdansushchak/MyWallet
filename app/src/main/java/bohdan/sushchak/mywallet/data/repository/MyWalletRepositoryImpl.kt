package bohdan.sushchak.mywallet.data.repository

import androidx.lifecycle.LiveData
import bohdan.sushchak.mywallet.data.db.MyWalletDatabase
import bohdan.sushchak.mywallet.data.db.dao.CategoryDao
import bohdan.sushchak.mywallet.data.db.dao.MetaDataDao
import bohdan.sushchak.mywallet.data.db.dao.OrderDao
import bohdan.sushchak.mywallet.data.db.dao.ProductDao
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.MetaDataEntity
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.data.firebase.ApiDatabase
import bohdan.sushchak.mywallet.data.model.*
import bohdan.sushchak.mywallet.internal.NonAuthorizedExeption
import bohdan.sushchak.mywallet.internal.SyncType
import bohdan.sushchak.mywallet.internal.dateRangeByYearAndMonth
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    private val apiDatabase: ApiDatabase,
    private val myWalletDatabase: MyWalletDatabase
) : MyWalletRepository {

    private val categoryDao: CategoryDao
        get() = myWalletDatabase.categoryDao()

    private val orderDao: OrderDao
        get() = myWalletDatabase.orderDao()

    private val productDao: ProductDao
        get() = myWalletDatabase.productDao()

    private val metaDataDao: MetaDataDao
        get() = myWalletDatabase.metaDataDao()

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
        withContext(Dispatchers.IO) {
            val id = categoryDao.insert(categoryEntity)
            val category = categoryEntity.copy(categoryId = id)
            apiDatabase.addCategory(category)
            increaseVersion()
        }
    }

    override suspend fun removeCategory(categoryEntity: CategoryEntity) {
        withContext(Dispatchers.IO) {
            categoryDao.delete(categoryEntity)
            apiDatabase.removeCategory(categoryEntity)
            increaseVersion()
        }
    }

    override suspend fun updateCategory(categoryEntity: CategoryEntity) {
        withContext(Dispatchers.IO) {
            categoryDao.update(categoryEntity)
            //TODO: add updating category
        }
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
    override suspend fun createOrderWithProducts(orderEntity: OrderEntity, productsEntity: List<ProductEntity>) {
        withContext(Dispatchers.IO) {
            val idsMap = orderDao.insertOrderWithProducts(productDao, orderEntity, productsEntity)

            val order = orderEntity.copy(orderId = idsMap["orderId"].toString().toLong())
            val products = mutableListOf<ProductEntity>()
            productsEntity.forEachIndexed { index, productEntity ->
                val id = (idsMap["productIds"] as List<*>)[index]
                products.add(productEntity.copy(productId = id.toString().toLong()))
            }

            apiDatabase.addOrder(order, products)
            increaseVersion()
        }
    }

    override suspend fun updateOrderWithProducts(orderEntity: OrderEntity, productsEntity: List<ProductEntity>) {
        withContext(Dispatchers.IO) {
            apiDatabase.removeOrder(orderEntity)
            orderDao.removeById(orderEntity.orderId!!)

            val idsMap = orderDao.editOrderWithProducts(productDao, orderEntity, productsEntity)

            val order = orderEntity.copy(orderId = idsMap["orderId"].toString().toLong())
            val products = mutableListOf<ProductEntity>()
            productsEntity.forEachIndexed { index, productEntity ->
                val id = (idsMap["productIds"] as List<*>)[index]
                products.add(productEntity.copy(productId = id.toString().toLong()))
            }

            apiDatabase.addOrder(order, products)
            increaseVersion()
        }
    }

    override suspend fun getOrdersWithProducts(): LiveData<List<OrderWithProducts>> {
        return withContext(Dispatchers.IO) {
            return@withContext orderDao.getOrdersWithProducts()
        }
    }

    override suspend fun getOrders(): LiveData<List<OrderEntity>> {
        return withContext(Dispatchers.IO) {
            //Log.d("version: ", apiDatabase.getVersionOfDatabase().toString())
            return@withContext orderDao.getOrders()
        }
    }

    override suspend fun removeOrder(order: OrderEntity) {
        withContext(Dispatchers.IO) {
            orderDao.delete(order)
            apiDatabase.removeOrder(order)
            increaseVersion()
        }
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
            return@withContext orderDao.getSpendMoneyByDateNonLive(startDate, endDate)
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

    override suspend fun registerNewUser(uid: String) {
        withContext(Dispatchers.IO) {
            val currentMetaData = metaDataDao.getMetaData()

            if (!currentMetaData?.userFirebaseId.isNullOrEmpty())
                myWalletDatabase.clearAllTables()

            val metaDataEntity = MetaDataEntity(databaseVersion = 1, userFirebaseId = uid)
            apiDatabase.setMetaData(metaDataEntity)
            metaDataDao.upsert(metaDataEntity)
        }
    }

    override suspend fun synchronizeDatabases(syncType: SyncType, observer: (text: String) -> Unit) {
        return withContext(Dispatchers.IO) {
            if (syncType == SyncType.LOCAL_LESS) {
                pullDatabase(observer)
                return@withContext
            }

            if (syncType == SyncType.FIRESTORE_LESS) {
                pushDatabase(observer)
                return@withContext
            }
        }
    }

    private suspend fun pullDatabase(observer: (text: String) -> Unit) {
        withContext(Dispatchers.IO) {
            val categories = apiDatabase.getCategories()
            categories.forEach {
                observer.invoke("Category: ${it.categoryTitle}")
                delay(30L)
            }
            categoryDao.replaceAll(categories)

            val orders = apiDatabase.getOrders()
            orders.forEach{
                observer.invoke("Order: ${it.order.title} products: ${it.products.
                    joinToString(separator = ", ", prefix = "[", postfix = "]"){product -> product.title}}")
                delay(30L)
            }

            orderDao.replaceAll(productDao = productDao, orderWithProducts = orders)

            val newVersionForApi = apiDatabase.getVersionOfDatabase()
            increaseVersion(newVersionForApi)
        }
    }

    private fun pushDatabase(observer: (text: String) -> Unit) {

    }

    override suspend fun databasesCompare(): SyncType {
        return withContext(Dispatchers.IO) {
            val myWalletRepositoryVersion = metaDataDao.getMetaData()?.databaseVersion ?: 1L
            val firestoreVersion = apiDatabase.getVersionOfDatabase()

            var syncEnum = SyncType.EQUALS
            if (myWalletRepositoryVersion > firestoreVersion) syncEnum = SyncType.FIRESTORE_LESS
            if (myWalletRepositoryVersion < firestoreVersion) syncEnum = SyncType.LOCAL_LESS

            return@withContext syncEnum
        }
    }

    private suspend fun increaseVersion(version: Long = 1L): Void? {
        return withContext(Dispatchers.IO) {
            val currentMetaData = metaDataDao.getMetaData()

            if (FirebaseAuth.getInstance().currentUser == null) {
                throw NonAuthorizedExeption()
            }

            val newMetaDataEntity = currentMetaData?.let {
                it.copy(databaseVersion = it.databaseVersion!! + 1)
            } ?: MetaDataEntity(databaseVersion = 1, userFirebaseId = FirebaseAuth.getInstance().currentUser!!.uid)

            if(version > 1){
                newMetaDataEntity.databaseVersion = version
            }

            metaDataDao.upsert(newMetaDataEntity)
            return@withContext apiDatabase.increaseVersion(newMetaDataEntity.databaseVersion)
        }
    }
}