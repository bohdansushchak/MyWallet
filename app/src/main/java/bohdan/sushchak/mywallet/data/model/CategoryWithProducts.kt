package bohdan.sushchak.mywallet.data.model

import androidx.room.Embedded
import androidx.room.Relation
import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.data.db.entity.Product

data class CategoryWithProducts(

        @Embedded
        var category: Category,

        @Relation(parentColumn = "id",
                entityColumn = "category_id")
        var products: MutableList<Product> = mutableListOf()
)

