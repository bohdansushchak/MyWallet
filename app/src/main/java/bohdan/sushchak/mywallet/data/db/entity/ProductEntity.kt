package bohdan.sushchak.mywallet.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "products", foreignKeys = [ForeignKey(entity = CategoryEntity::class, parentColumns = ["id"], childColumns = ["category_id"], onDelete = ForeignKey.SET_NULL, onUpdate = ForeignKey.CASCADE),
ForeignKey(entity = OrderEntity::class, parentColumns = ["id"], childColumns = ["order_id"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.RESTRICT)])
data class ProductEntity (
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        override var id: Long? = null,

        @ColumnInfo(name = "title")
        var title: String,

        @ColumnInfo(name = "price")
        var price: Double,

        @ColumnInfo(name = "category_id")
        var categoryId: Long?,

        @ColumnInfo(name = "order_id")
        var orderId: Long?
): BaseEntity()
{
        override fun toString(): String {
                return "(id:$id, title:$title, price:$price, categoryId:$categoryId, orderId:$orderId)"
        }
}