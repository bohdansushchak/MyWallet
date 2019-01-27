package bohdan.sushchak.mywallet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
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

    var onLongClick: ((view: View, order: OrderEntity) -> Unit)? = null
    var onClick: ((order: OrderEntity) -> Unit)? = null

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
        val order = ordersByDateGroup.orders[childIndex]

        holder?.bind(order)

        holder?.itemView?.setOnLongClickListener {
            onLongClick?.invoke(it, order)
            return@setOnLongClickListener (onLongClick != null)
        }

        holder?.itemView?.setOnClickListener{
            onClick?.invoke(order)
            return@setOnClickListener
        }
    }

    override fun onBindGroupViewHolder(holder: DateViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?) {
        val ordersByDateGroup = group as OrdersByDateGroup
        holder?.bind(ordersByDateGroup)
    }

    class DateViewHolder(view: View) : GroupViewHolder(view) {

        private val tvDate = view.tvDate

        fun bind(item: OrdersByDateGroup): Unit {
            tvDate.text = item.date
        }
    }

    class OrderViewHolder(view: View) : ChildViewHolder(view) {

        private val tvOrderTitle = view.tvOrderTitle
        private val tvOrderPrice = view.tvOrderPrice

        fun bind(order: OrderEntity): Unit {
            tvOrderTitle.text = order.title
            tvOrderPrice.text = order.price.toString()
        }
    }
}