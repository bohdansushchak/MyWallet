package bohdan.sushchak.mywallet.data.model

import androidx.room.Embedded
import androidx.room.Relation
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity

data class OrderWithProducts(
    @Embedded
    var order: OrderEntity,

    @Relation(
        parentColumn = "orderId",
        entityColumn = "order_id"
    )
    var products: List<ProductEntity> = listOf()
) {
    override fun toString(): String {
        return "${order} :{ $products }"

    }
}