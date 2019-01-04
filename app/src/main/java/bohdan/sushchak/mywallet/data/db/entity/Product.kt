package bohdan.sushchak.mywallet.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "products", foreignKeys = [ForeignKey(entity = Category::class, parentColumns = ["id"], childColumns = ["category_id"], onDelete = ForeignKey.SET_NULL),
ForeignKey(entity = Order::class, parentColumns = ["id"], childColumns = ["order_id"], onDelete = ForeignKey.CASCADE)])
data class Product (
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        val title: String,
        val price: Double,

        @ColumnInfo(name = "category_id")
        val categoryId: Int?,

        @ColumnInfo(name = "order_id")
        val orderId: Int
)
