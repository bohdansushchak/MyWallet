package bohdan.sushchak.mywallet.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "orders", foreignKeys = [ForeignKey(entity = Date::class, parentColumns = ["id"], childColumns = ["date_id"], onDelete = ForeignKey.CASCADE)])
data class Order(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        override var id: Long?,

        @ColumnInfo(name = "title")
        var title: String,

        @ColumnInfo(name = "date_id")
        var dateId: Long?,

        @ColumnInfo(name = "price")
        var price: Double
): BaseEntity() {
    override fun toString(): String {
        return "OrderId: ${id.toString()}"
    }
}