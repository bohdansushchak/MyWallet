package bohdan.sushchak.mywallet.data.model

import androidx.room.Embedded
import androidx.room.Relation
import bohdan.sushchak.mywallet.data.db.entity.Order

class OrdersByDate(

        @Embedded
        var date: Long,

        @Relation(parentColumn = "id",
                entityColumn = "date_id")
        var orders: MutableList<Order> = mutableListOf()
)