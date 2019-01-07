package bohdan.sushchak.mywallet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
import kotlinx.android.synthetic.main.color_item.view.*

class PickColorAdapter(private var context: Context,
                       private val colors : List<Int>)
: RecyclerView.Adapter<PickColorAdapter.ViewHolder>() {

    var mSelectedItem = -1
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.color_item, parent, false))
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardViewBackground.setCardBackgroundColor(colors[position])
        holder.ivColor.background = if(mSelectedItem == position)
            context.getDrawable(R.drawable.ic_check_circle)
        else null

        holder.itemView.setOnClickListener{ setCheck(position)}
    }

    private fun setCheck(position: Int){
        mSelectedItem = position
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val cardViewBackground = itemView.cardViewBackground
        val ivColor = itemView.ivColor
    }
}