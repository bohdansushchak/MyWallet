package bohdan.sushchak.mywallet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.db.entity.Product
import kotlinx.android.synthetic.main.product_item.view.*

class ProductAdapter(private val context: Context,
                     private val products: List<Product>)
    : RecyclerAdapterClick<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ProductAdapter.ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.product_item, parent, false))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.tvProductTitle.text = products[position].title
        holder.tvProductPrice.text = products[position].price.toString()

    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)


    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val tvProductTitle = view.tvProductTitle
        val tvProductPrice = view.tvProductPrice
    }
}