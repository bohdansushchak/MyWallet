package bohdan.sushchak.mywallet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import kotlinx.android.synthetic.main.product_item.view.*

class ProductAdapter(private val context: Context,
                     products: List<ProductEntity>)
    : RecyclerAdapterClick<ProductAdapter.ViewHolder, ProductEntity>(products) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ProductAdapter.ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.product_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.tvProductTitle.text = items[position].title
        holder.tvProductPrice.text = items[position].price.toString()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val tvProductTitle = view.tvProductTitle
        val tvProductPrice = view.tvProductPrice
    }
}