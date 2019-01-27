package bohdan.sushchak.mywallet.adapters.callbacks

import androidx.recyclerview.widget.DiffUtil

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
        return oldList[oldItemPosition] == (newList[newItemPosition])
    }

}