package bohdan.sushchak.productsdetector.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.productsdetector.R
import bohdan.sushchak.productsdetector.model.AddedProduct
import kotlinx.android.synthetic.main.added_item.view.*

class ProductAdapter(val context: Context, val products: List<AddedProduct>): RecyclerView.Adapter<ViewHolder>() {
    var onRemoveListener: ((item: AddedProduct) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =  LayoutInflater.from(context).inflate(R.layout.added_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])

        holder.setOnRemoveListener(View.OnClickListener {
            onRemoveListener?.invoke(products[position])
        })
    }
}

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val tvTitle = view.tvItemName
    private val tvCount = view.tvItemsCount
    private val flCountContainer = view.flCount
    private val btnRemove = view.iBtnRemoveItem

    fun bind(item: AddedProduct) {
        tvTitle.text = item.product

        if(item.count > 1){
            tvCount.text = item.count.toString()
            flCountContainer.visibility = View.VISIBLE
        } else {
            flCountContainer.visibility = View.GONE
        }
    }

    fun setOnRemoveListener(listener: View.OnClickListener) {
        btnRemove.setOnClickListener(listener)
    }
}