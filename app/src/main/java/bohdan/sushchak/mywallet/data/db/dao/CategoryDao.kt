package bohdan.sushchak.mywallet.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.data.db.model.CategoryWithProducts

@Dao
abstract class CategoryDao : BaseDao<Category> {

    @Query("select * from categories")
    abstract fun getAllCategories(): LiveData<List<Category>>

    @Query("select * from categories where id = :id")
    abstract fun getCategoryById(id: Int): LiveData<Category>

    @Query("select * from categories")
    abstract fun getCategoriesWithProduct(): LiveData<List<CategoryWithProducts>>
}