package bohdan.sushchak.mywallet.data.model

import androidx.room.Embedded
import androidx.room.Relation
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity

data class CategoryProduct(

    @Embedded
    var productEntity: ProductEntity,

    @Embedded
    var categoryEntity: CategoryEntity
) {
    override fun toString(): String {
        return "category:{$categoryEntity} priduct{$productEntity}"
    }
}