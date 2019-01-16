package bohdan.sushchak.mywallet.data.model

import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class OrdersByDateGroup(
        var date: String,
        var orders: MutableList<OrderEntity> = mutableListOf()
) : ExpandableGroup<OrderEntity>(date, orders)