package bohdan.sushchak.mywallet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import kotlinx.android.synthetic.main.order_item.view.*

class OrderAdapter(private val context: Context,
                   orders: List<OrderEntity>)
    : RecyclerAdapterClick<OrderAdapter.ViewHolder, OrderEntity>(orders) {

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.order_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvOrderTitle = view.tvOrderTitle
        val tvOrderPrice = view.tvOrderPrice

        fun bind(order: OrderEntity) {
            tvOrderTitle.text = order.title
            tvOrderPrice.text = order.price.toString()
        }
    }
}

