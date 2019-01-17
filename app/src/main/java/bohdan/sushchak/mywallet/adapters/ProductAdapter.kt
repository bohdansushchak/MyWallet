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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val tvProductTitle = view.tvProductTitle
        val tvProductPrice = view.tvProductPrice

        fun bind(item: ProductEntity){
            tvProductTitle.text = item.title
            tvProductPrice.text = item.price.toString()
        }
    }
}