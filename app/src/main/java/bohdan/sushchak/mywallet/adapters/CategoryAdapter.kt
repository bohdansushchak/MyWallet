package bohdan.sushchak.mywallet.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.db.entity.Category
import kotlinx.android.synthetic.main.category_item.view.*
import org.jetbrains.anko.textColor

class CategoryAdapter(
        private val context: Context,
        private val items: List<Category>
) : RecyclerAdapterClick<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.category_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.tvCategory.text = items[position].title
        holder.tvCategory.textColor = items[position].color

        Log.d("TAG", "${items[position].title} and ${items[position].color}")

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategory = itemView.tvCategory
    }
}