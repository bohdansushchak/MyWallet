package bohdan.sushchak.mywallet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.db.entity.Order
import kotlinx.android.synthetic.main.order_item.view.*

class OrderAdapter(private val context: Context,
                   orders: List<Order>)
    : RecyclerAdapterClick<OrderAdapter.ViewHolder, Order>(orders) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.order_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.bind(items[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvOrderTitle = view.tvOrderTitle
        val tvOrderPrice = view.tvOrderPrice

        fun bind(order: Order) {
            tvOrderTitle.text = order.title
            tvOrderPrice.text = order.price.toString()
        }
    }
}

