package bohdan.sushchak.mywallet.data.db.model

import androidx.room.Embedded
import androidx.room.Relation
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.db.entity.Product

data class OrderWithProducts(
        @Embedded
        var order: Order,

        @Relation(parentColumn = "id",
                entityColumn = "order_id")
        var products: List<Product> = listOf()
) {
    override fun toString(): String {
        return "${order.toString()} :{ $products }"

    }
}