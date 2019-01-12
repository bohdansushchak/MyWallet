package bohdan.sushchak.mywallet.adapters

import bohdan.sushchak.mywallet.data.db.entity.Order
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
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

        holder.tvOrderTitle.text = items[position].title
        holder.tvOrderPrice.text = items[position].price.toString()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val tvOrderTitle = view.tvOrderTitle
        val tvOrderPrice = view.tvOrderPrice
    }
}