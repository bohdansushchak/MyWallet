package bohdan.sushchak.mywallet.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.model.CategoryPrice
import bohdan.sushchak.mywallet.data.model.CategoryWithListProducts

@Dao
abstract class CategoryDao : BaseDao<CategoryEntity> {

    @Query("select * from categories")
    abstract fun getAllCategories(): LiveData<List<CategoryEntity>>

    @Query("select * from categories")
    abstract fun getAllCategoriesNonLive(): List<CategoryEntity>?

    @Query("SELECT * FROM categories WHERE categoryId = :id")
    abstract fun getCategoryById(id: Long): CategoryEntity?
/*
    @Transaction
    @Query("select * from products, categories where [products].[order_id] = :orderId")
    abstract fun getCategoriesWithProductByOrderIdNonLive(orderId: Long): List<CategoryWithListProducts>?
*/
    @Transaction
    @Query("select [categories].[category_title] as 'category_title', [categories].[color] as 'color', sum([products].[price]) as 'total_price' from categories, products, orders where [products].[category_id] = [categories].[categoryId] and [products].[order_id] = [orders].[orderId] and [orders].[date] between :startDate and :endDate group by category_id")
    abstract fun getTotalPriceCategories(startDate: Long, endDate: Long): List<CategoryPrice>?

    @Transaction
    @Query("select sum([products].[price]) as 'total_price' from  products, orders where [products].[category_id] is null and [products].[order_id] = [orders].[orderId] and [orders].[date] between :startDate and :endDate group by category_id")
    abstract fun getTotalPriceCategoryNotSet(startDate: Long, endDate: Long): List<CategoryPrice>?

    @Transaction
    open fun replaceAll(categories: List<CategoryEntity>) {
        clearTable()
        insert(categories)
    }

    @Query("DELETE FROM categories")
    abstract fun clearTable()

}