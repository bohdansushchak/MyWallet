package bohdan.sushchak.mywallet.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.data.model.CategoryPrice
import bohdan.sushchak.mywallet.data.model.CategoryWithProducts

@Dao
abstract class CategoryDao : BaseDao<Category> {

    @Query("select * from categories")
    abstract fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM categories WHERE id = :id")
    abstract fun getCategoryById(id: Long): Category?

    @Transaction
    @Query("select * from categories")
    abstract fun getCategoriesWithProduct(): LiveData<List<CategoryWithProducts>>

    @Transaction
    @Query("select [categories].[title] as 'title', [categories].[color] as 'color'," +
            " sum([products].[price]) as 'total_price' from categories, products, orders where " +
            "[products].[category_id] = [categories].[id] and [products].[order_id] = [orders].[id] " +
            "and [orders].[date] between :startDate and :endDate group by category_id")
    abstract fun getTotalPriceCategories(startDate: Long, endDate: Long): List<CategoryPrice>?
}