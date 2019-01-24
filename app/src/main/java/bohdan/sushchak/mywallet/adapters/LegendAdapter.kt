package bohdan.sushchak.mywallet.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.model.LegendItem
import kotlinx.android.synthetic.main.legend_item.view.*

class LegendAdapter(private val context: Context,
                    private val items: List<LegendItem>) : RecyclerView.Adapter<LegendAdapter.ViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(context)
               .inflate(R.layout.legend_item, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(context, items[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvColor = view.vColor
        private val tvTitleLegend = view.tvTitleLegend

        fun bind(context: Context, item: LegendItem) {
            //tvColor.setBackgroundColor(item.color)
            //tvTitleLegend.text = item.titleResId
        }
    }
}