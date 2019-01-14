package bohdan.sushchak.mywallet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.model.OrdersByDateGroup
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.date_item.view.*
import kotlinx.android.synthetic.main.order_item.view.*


class OrdersByDateAdapter(private val context: Context,
                          items: List<OrdersByDateGroup>)
    : ExpandableRecyclerViewAdapter<OrdersByDateAdapter.DateViewHolder, OrdersByDateAdapter.OrderViewHolder>(items) {
    /*
        fun bind(newItems: List<OrdersByDateGroup>){
            val categoryDiffCallback = CategoryDiffCallback(items, newItems)
            val diffResult = DiffUtil.calculateDiff(categoryDiffCallback)
            diffResult.dispatchUpdatesTo(this)

            items = newItems
        }
    */
    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.date_item, parent, false)
        return DateViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindChildViewHolder(holder: OrderViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?, childIndex: Int) {
        val ordersByDateGroup = group as OrdersByDateGroup
        holder?.bind(ordersByDateGroup.orders[childIndex])
    }

    override fun onBindGroupViewHolder(holder: DateViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?) {
        val ordersByDateGroup = group as OrdersByDateGroup
        holder?.bind(ordersByDateGroup)
    }

    class DateViewHolder(view: View) : GroupViewHolder(view) {

        val tvDate = view.tvDate

        fun bind(item: OrdersByDateGroup): Unit {
            tvDate.text = item.date
        }
    }

    class OrderViewHolder(view: View) : ChildViewHolder(view) {

        val tvOrderTitle = view.tvOrderTitle
        val tvOrderPrice = view.tvOrderPrice

        fun bind(order: Order): Unit {
            tvOrderTitle.text = order.title
            tvOrderPrice.text = order.price.toString()
        }
    }
}