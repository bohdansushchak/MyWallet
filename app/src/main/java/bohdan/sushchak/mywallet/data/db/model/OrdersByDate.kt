package bohdan.sushchak.mywallet.data.db.model

import androidx.room.Embedded
import androidx.room.Relation
import bohdan.sushchak.mywallet.data.db.entity.Date
import bohdan.sushchak.mywallet.data.db.entity.Order

data class OrdersByDate(
        @Embedded
        var date: Date,

        @Relation(parentColumn = "id",
                entityColumn = "date_id")
        var orders: MutableList<Order> = mutableListOf()
)