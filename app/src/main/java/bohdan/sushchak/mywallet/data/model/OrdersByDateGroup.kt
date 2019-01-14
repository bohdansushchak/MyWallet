package bohdan.sushchak.mywallet.data.model

import androidx.recyclerview.widget.DiffUtil
import bohdan.sushchak.mywallet.data.db.entity.Order
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class OrdersByDateGroup(
        var date: String,
        var orders: MutableList<Order> = mutableListOf()
) : ExpandableGroup<Order>(date, orders)