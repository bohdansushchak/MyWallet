package bohdan.sushchak.mywallet.data.db.entity

import androidx.room.*

@Entity(tableName = "orders")
data class Order(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        override var id: Long?,

        @ColumnInfo(name = "title")
        var title: String,

        @ColumnInfo(name = "date")
        var date: Long,

        @ColumnInfo(name = "total_price")
        var price: Double

): BaseEntity() {

    override fun toString(): String {
        return "OrderId: ${id.toString()}"
    }
}