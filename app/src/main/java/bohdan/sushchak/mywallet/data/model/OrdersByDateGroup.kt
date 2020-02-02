package bohdan.sushchak.mywallet.data.model

import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.internal.Constants
import bohdan.sushchak.mywallet.internal.formatDate
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

data class OrdersByDateGroup(
    var date: Long,
    var orders: MutableList<OrderEntity> = mutableListOf()

) : ExpandableGroup<OrderEntity>(formatDate(date, Constants.DATE_FORMAT), orders)