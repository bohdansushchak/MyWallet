package bohdan.sushchak.mywallet.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var date: Long,
        var price: Double
)