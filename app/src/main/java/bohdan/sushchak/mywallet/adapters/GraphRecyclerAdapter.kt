package bohdan.sushchak.mywallet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.model.GraphItem
import bohdan.sushchak.mywallet.data.model.LegendItem
import kotlinx.android.synthetic.main.graph_item.view.*

class GraphRecyclerAdapter(val context: Context, items: List<GraphItem>)
    : BaseRecyclerAdapter<GraphRecyclerAdapter.ViewHolder, GraphItem>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : GraphRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.graph_item, null)
        return ViewHolder(context, view)
    }

    override fun onBindViewHolder(holder: GraphRecyclerAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(val context: Context, view: View) : RecyclerView.ViewHolder(view) {
        val tvGraphTitle = view.tvGraphTitle
        val graph = view.graph
        val rcLegend = view.rcLegend

        fun bind(graphItem: GraphItem) {
            tvGraphTitle.text = graphItem.title

            bindGraph(graphItem)

            if (graphItem.isShowLegend && graphItem.legendItems != null)
                bindLegend(graphItem.legendItems!!)
        }

        private fun bindLegend(items: List<LegendItem>) {
            rcLegend.adapter = LegendAdapter(context, items.toList())
            rcLegend.layoutManager = LinearLayoutManager(context)
        }

        private fun bindGraph(item: GraphItem) {
            graph.removeAllSeries()

            item.seriesList.forEach {
                graph.addSeries(it)
            }

            if (item.maxX != null)
                graph.viewport.setMaxX(item.maxX!!)

            if (item.maxY != null)
                graph.viewport.setMaxY(item.maxY!!)

            if (item.minX != null)
                graph.viewport.setMinX(item.minX!!)

            if (item.minY != null)
                graph.viewport.setMinY(item.minY!!)

            graph.viewport.isXAxisBoundsManual = item.isXAxisBoundsManual
            if (item.labelFormatter != null)
                graph.gridLabelRenderer.labelFormatter = item.labelFormatter

        }
    }
}