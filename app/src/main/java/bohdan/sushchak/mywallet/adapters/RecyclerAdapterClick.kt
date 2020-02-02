package bohdan.sushchak.mywallet.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerAdapterClick<VH : RecyclerView.ViewHolder, T>(items: List<T>) :
    BaseRecyclerAdapter<VH, T>(items) {
    var onLongClick: ((view: View, position: Int) -> Unit)? = null
    var onClick: ((view: View, position: Int) -> Unit)? = null

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.itemView.setOnLongClickListener {
            onLongClick?.invoke(it, position)
            return@setOnLongClickListener (onLongClick != null)
        }

        holder.itemView.setOnClickListener {
            onClick?.invoke(it, position)
            return@setOnClickListener
        }
    }
}