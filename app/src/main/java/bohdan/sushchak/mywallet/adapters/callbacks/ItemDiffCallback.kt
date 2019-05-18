package bohdan.sushchak.mywallet.adapters.callbacks

import android.util.Log
import androidx.recyclerview.widget.DiffUtil

/**
 * This class is helper for compare lists in adapter
 *
 * @param T some type
 * @property oldList old list to compare
 * @property newList a new list to compare
 */
class ItemDiffCallback<T>(private val oldList: List<T>,
                          private val newList: List<T>): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].hashCode() == newList[newItemPosition].hashCode()
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        Log.d("TAG", "=============================================")
        Log.d("TAG", oldList[oldItemPosition].toString())
        Log.d("TAG", newList[newItemPosition].toString())
        Log.d("TAG", (oldList[oldItemPosition] == newList[newItemPosition]).toString())

        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}