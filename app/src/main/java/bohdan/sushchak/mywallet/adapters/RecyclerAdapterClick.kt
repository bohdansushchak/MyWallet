package bohdan.sushchak.mywallet.adapters

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.adapters.callbacks.CategoryDiffCallback
import bohdan.sushchak.mywallet.data.db.entity.BaseEntity

abstract class RecyclerAdapterClick<VH : RecyclerView.ViewHolder, T: BaseEntity>(protected var items: List<T>) : RecyclerView.Adapter<VH>() {

    var onLongClick: ((view: View, position: Int) -> Unit)? = null
    var onClick: ((view: View, position: Int) -> Unit)? = null

    fun update(newItems: List<T>){
        val categoryDiffCallback = CategoryDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(categoryDiffCallback)
        diffResult.dispatchUpdatesTo(this)
        items = newItems
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.itemView.setOnLongClickListener {
            onLongClick?.invoke(it, position)
            return@setOnLongClickListener (onLongClick != null)
        }

        holder.itemView.setOnClickListener{
            onClick?.invoke(it, position)
            return@setOnClickListener
        }
    }
}