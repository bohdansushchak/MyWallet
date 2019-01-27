package bohdan.sushchak.mywallet.data.model

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import bohdan.sushchak.mywallet.R
import com.jjoe64.graphview.LabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.Series
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.graph_item.*

class GraphItem : Item() {

    var titleResId: Int = 0

    var seriesList: MutableList<Series<DataPoint>> = mutableListOf()

    var isShowLegend: Boolean = false

    var legendItems: MutableList<LegendItem>? = null

    var isXAxisBoundsManual: Boolean = false
    var isYAxisBoundsManual: Boolean = false

    var minX: Double = 0.0
    var minY: Double = 0.0
    var maxX: Double = 0.0
    var maxY: Double = 0.0

    var isHasSubItems = false

    var labelFormatter: LabelFormatter? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        if (titleResId != 0)
            viewHolder.tvGraphTitle.setText(titleResId)

        viewHolder.graph.apply {
            if (seriesList.size == 0)
                removeAllSeries()

            viewport.isXAxisBoundsManual = isXAxisBoundsManual
            viewport.isYAxisBoundsManual = isYAxisBoundsManual

            viewport.setMinX(minX)
            viewport.setMaxX(maxX)
            viewport.setMinY(minY)
            viewport.setMaxY(maxY)

            if (labelFormatter != null)
                gridLabelRenderer.labelFormatter = labelFormatter

            series.addAll(seriesList)
        }

        if (isShowLegend)
            bindLegend(viewHolder)

        if(isHasSubItems)
            bindSubItems(viewHolder)
    }

    override fun getLayout() = R.layout.graph_item

    private fun bindLegend(viewHolder: ViewHolder) {
        if (isShowLegend && legendItems != null) {
            val groupAdapter = GroupAdapter<ViewHolder>()
            groupAdapter.addAll(legendItems!!)

            viewHolder.rcLegend.apply {
                visibility = View.VISIBLE
                adapter = groupAdapter
                layoutManager = LinearLayoutManager(context)
            }
        } else
            viewHolder.rcLegend.visibility = View.GONE
    }

    private fun bindSubItems(viewHolder: ViewHolder){
        viewHolder.itemView.setOnClickListener {
            viewHolder.rcCategoryPrice.visibility = if(viewHolder.rcCategoryPrice.visibility == View.VISIBLE)
                View.GONE
            else View.VISIBLE
        }



    }
}


