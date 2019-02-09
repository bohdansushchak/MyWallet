package bohdan.sushchak.mywallet.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.data.model.CategoryCount

@Dao
abstract class ProductDao : BaseDao<ProductEntity> {

    @Query("select category_id, count(category_id) as count from products where title like '%' || :productTitle || '%' group by category_id")
    abstract fun getCategoriesCountByProductTitle(productTitle: String): List<CategoryCount>?

    @Query("select * from products")
    abstract fun getProductsNonLive(): List<ProductEntity>?

}
