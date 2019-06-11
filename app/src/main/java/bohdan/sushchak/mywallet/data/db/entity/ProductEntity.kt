package bohdan.sushchak.mywallet.data.db.entity

import androidx.room.*
import com.google.firebase.firestore.DocumentSnapshot

@Entity(
    tableName = "products",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["categoryId"],
        childColumns = ["category_id"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["orderId"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.RESTRICT
        )]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "productId")
    var productId: Long? = null,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "price")
    var price: Double,

    @ColumnInfo(name = "category_id")
    var categoryId: Long?,

    @ColumnInfo(name = "order_id")
    var orderId: Long?
) {
    override fun toString(): String {
        return "($productId, $title, $price, categoryId:$categoryId, orderId:$orderId)"
    }

    companion object {
        @Ignore
        fun fromDocument(doc: HashMap<String, Any?>): ProductEntity {
            val productId = doc["id"].toString().toLong()
            val title = doc["title"].toString()
            val price = doc["price"].toString().toDouble()
            val categoryId = doc["categoryId"].toString().toLong()
            val orderId = doc["orderId"].toString().toLong()

            return ProductEntity(
                productId = productId,
                title = title,
                price = price,
                categoryId = categoryId,
                orderId = orderId
            )
        }
    }
}