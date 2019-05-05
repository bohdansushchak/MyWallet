package bohdan.sushchak.mywallet.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.data.model.CategoryCount
import bohdan.sushchak.mywallet.data.model.CategoryProduct
import bohdan.sushchak.mywallet.data.model.CategoryWithListProducts

@Dao
abstract class ProductDao : BaseDao<ProductEntity> {
    @Query("select category_id, count(category_id) as count from products where title like '%' || :productTitle || '%' group by category_id")
    abstract fun getCategoriesCountByProductTitle(productTitle: String): List<CategoryCount>?

    @Query("select * from products")
    abstract fun getProductsNonLive(): List<ProductEntity>?

    @Query("select * from products where order_id = :orderId")
    abstract fun getProductsByOrderIdNonLive(orderId: Long): List<ProductEntity>?

    @Query("select * from categories inner join products on products.category_id = categories.categoryId where [products].[order_id] = :orderId") // where [products].[order_id] = :orderId group by category_id
    abstract fun getCategoryProductNonLive(orderId: Long): List<CategoryProduct>?

}
