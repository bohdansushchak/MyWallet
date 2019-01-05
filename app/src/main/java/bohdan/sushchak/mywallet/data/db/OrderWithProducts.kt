package bohdan.sushchak.mywallet.data.db

import androidx.room.Embedded
import androidx.room.Relation
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.db.entity.Product

class OrderWithProducts {

    @Embedded
    var order: Order? = null

    @Relation(parentColumn = "id",
            entityColumn = "order_id")
    var products: List<Product> = ArrayList()
}