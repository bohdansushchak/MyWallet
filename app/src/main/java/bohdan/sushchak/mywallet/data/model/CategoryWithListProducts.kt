package bohdan.sushchak.mywallet.data.model

import androidx.room.Embedded
import androidx.room.Relation
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity

data class CategoryWithListProducts(

        @Embedded
        var categoryEntity: CategoryEntity,

        @Relation(parentColumn = "categoryId",
                entityColumn = "category_id")
        var products: MutableList<ProductEntity> = mutableListOf()
)
{
        override fun toString(): String {
                return "$categoryEntity ${products.joinToString(
                        prefix = "{",
                        postfix = "}",
                        separator = ", "
                ) { it.toString() }}"
        }
}

