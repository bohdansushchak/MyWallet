package bohdan.sushchak.mywallet.adapters

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.adapters.callbacks.ItemDiffCallback

abstract class BaseRecyclerAdapter<VH : RecyclerView.ViewHolder, T>(private var items: List<T>) : RecyclerView.Adapter<VH>() {

    fun update(newItems: List<T>){

        Log.d("TAG", items.toString())
        Log.d("TAG", newItems.toString())

        val itemDiffCallback = ItemDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(itemDiffCallback)
        diffResult.dispatchUpdatesTo(this)
        items = newItems
    }

    fun getItem(position: Int): T{
        if(position < items.size)
            return items[position]
        else throw ArrayIndexOutOfBoundsException()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

