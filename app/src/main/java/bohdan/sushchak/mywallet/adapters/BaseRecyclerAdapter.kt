package bohdan.sushchak.mywallet.adapters

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.adapters.callbacks.ItemDiffCallback

/**
 * Base class for adapters
 *
 * @param VH ViewHolder
 * @param T items type
 * @property items items list
 */
abstract class BaseRecyclerAdapter<VH : RecyclerView.ViewHolder, T>(private var items: List<T>) : RecyclerView.Adapter<VH>() {

    /**
     * Function to update adapter when new item enter
     *
     * @param newItems list of new items what need to update
     */
    fun update(newItems: List<T>){

        Log.d("TAG", items.toString())
        Log.d("TAG", newItems.toString())

        val itemDiffCallback = ItemDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(itemDiffCallback)
        diffResult.dispatchUpdatesTo(this)
        items = newItems
    }

    /**
     * method to get item from list by position in adapter
     *
     * @param position position in list
     * @return return item from list
     */
    fun getItem(position: Int): T{
        if(position < items.size)
            return items[position]
        else throw ArrayIndexOutOfBoundsException()
    }

    /**
     * Get items count
     *
     * @return count of items
     */
    override fun getItemCount(): Int {
        return items.size
    }
}

