package bohdan.sushchak.mywallet.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.internal.getSavedCurrency
import bohdan.sushchak.mywallet.internal.myToString
import kotlinx.android.synthetic.main.order_item.view.*

/**
 * Adapter to show orders in recycler view
 *
 * @property context activity context to get layout from resources
 * @constructor
 * make a instance of OrderAdapter
 *
 * @param orders list of orders
 */
class OrderAdapter(private val context: Context,
                   orders: List<OrderEntity>)
    : RecyclerAdapterClick<OrderAdapter.ViewHolder, OrderEntity>(orders) {

    /**
     * method to create view holder of views
     *
     * @param parent
     * @param viewType
     * @return a view holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.order_item, parent, false))
    }

    /**
     * bind data to items in list
     *
     * @param holder this is a holder witch contains a views for list item
     * @param position position of item in list
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val item = getItem(position)
        val currency = getSavedCurrency(context)
        holder.bind(item, currency)
    }

    /**
     * Class ViewHolder contains a views for item
     *
     * @constructor
     *
     * @param view root view of item in list
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvOrderTitle = view.tvOrderTitle
        private val tvOrderPrice = view.tvOrderPrice

        @SuppressLint("SetTextI18n")
        fun bind(order: OrderEntity, currency: String) {
            tvOrderTitle.text = order.title
            val priceStr = order
                .price
                .myToString()
            tvOrderPrice.text = "$priceStr $currency"
        }
    }
}

