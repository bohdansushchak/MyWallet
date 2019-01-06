package bohdan.sushchak.mywallet.data.repository

import androidx.lifecycle.LiveData
import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.db.entity.Product

interface MyWalletRepository {

    suspend fun getCategories(): LiveData<List<Category>>

    suspend fun addCategory(category: Category)

    suspend fun removeCategory(category: Category)

    suspend fun createOrderWithProducts(order : Order, products: List<Product>)
}