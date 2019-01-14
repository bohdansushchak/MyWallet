package bohdan.sushchak.mywallet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.model.CategoryPrice
import kotlinx.android.synthetic.main.legend_item.view.*

class LegendAdapter(val context: Context, var items: List<CategoryPrice>) : RecyclerView.Adapter<LegendAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(context)
               .inflate(R.layout.legend_item, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val vColor = view.vColor
        val tvTitleLegend = view.tvTitleLegend

        fun bind(item: CategoryPrice) {
            vColor.setBackgroundColor(item.color)
            tvTitleLegend.text = item.title
        }
    }
}