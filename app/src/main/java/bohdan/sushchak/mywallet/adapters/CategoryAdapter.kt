package bohdan.sushchak.mywallet.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R

import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import kotlinx.android.synthetic.main.category_item_spinner.view.*
import org.jetbrains.anko.textColor

class CategoryAdapter(private val context: Context,
                      categoryEntities: List<CategoryEntity>): RecyclerAdapterClick<CategoryAdapter.CategoryViewHolder, CategoryEntity>(categoryEntities) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.category_item_spinner, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CategoryAdapter.CategoryViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.tvCategory.text = items[position].title
        holder.tvCategory.textColor = items[position].color

        Log.d("TAG", "${items[position].title} and ${items[position].color}")
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategory = itemView.tvCategory
    }
}