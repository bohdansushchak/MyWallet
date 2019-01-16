package bohdan.sushchak.mywallet.data.model

import androidx.room.Embedded
import androidx.room.Relation
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity

data class CategoryWithProducts(

        @Embedded
        var categoryEntity: CategoryEntity,

        @Relation(parentColumn = "id",
                entityColumn = "category_id")
        var products: MutableList<ProductEntity> = mutableListOf()
)

