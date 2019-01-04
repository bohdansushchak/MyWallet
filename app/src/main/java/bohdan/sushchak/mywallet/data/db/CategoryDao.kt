package bohdan.sushchak.mywallet.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import bohdan.sushchak.mywallet.data.db.entity.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategories(category: List<Category>)

    @Update
    fun updateCategory(category: Category)

    @Delete
    fun removeCategory(category: Category)

    @Query("select * from categories")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("select * from categories where id = :id")
    fun getCategoryById(id: Int): LiveData<Category>
}