package bohdan.sushchak.mywallet.ui.graph

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import bohdan.sushchak.mywallet.R
import com.jjoe64.graphview.LabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.Series
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.graph_item.*
import org.jetbrains.anko.Android
import android.view.animation.LayoutAnimationController




class GraphItem(
    private val moreInfoItems: List<MoreInfoItem>? = null,
    private val legendItems: List<LegendItem>? = null,
    private val seriesList: List<Series<DataPoint>> = listOf(),
    private val titleResId: Int,
    private val minX: Double = 0.0,
    private val minY: Double = 0.0,
    private val maxX: Double = 0.0,
    private val maxY: Double = 0.0,
    private val isXAxisBoundsManual: Boolean = false,
    private val isYAxisBoundsManual: Boolean = false,
    private val labelFormatter: LabelFormatter? = null
) : Item() {

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

        bindLegend(viewHolder)
        bindSubItems(viewHolder)
    }

    override fun getLayout() = R.layout.graph_item

    private fun bindLegend(viewHolder: ViewHolder) {
        if (legendItems != null) {
            val groupAdapter = GroupAdapter<ViewHolder>()
                .apply {
                    addAll(legendItems)
                }

            viewHolder.rcLegend.apply {
                visibility = View.VISIBLE
                adapter = groupAdapter
                layoutManager = LinearLayoutManager(context)
            }
        } else
            viewHolder.rcLegend.visibility = View.GONE
    }

    private fun bindSubItems(viewHolder: ViewHolder) {
        if (moreInfoItems != null) {
            viewHolder.rvMoreInfo.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = GroupAdapter<com.xwray.groupie.ViewHolder>()
                    .apply {
                        addAll(moreInfoItems)
                    }
            }

            viewHolder.ibtnShowMoreInfo.setOnClickListener {
                val ibtn = it as ImageButton
                if (viewHolder.rvMoreInfo.visibility == View.VISIBLE) {
                    val anim = AnimationUtils.loadAnimation(ibtn.context, R.anim.collapse) //TODO: change it
                    ibtn.startAnimation(anim)
                    ibtn.setImageResource(R.drawable.ic_add)

                    viewHolder.rvMoreInfo.visibility = View.GONE
                } else {
                    val anim = AnimationUtils.loadAnimation(ibtn.context, R.anim.expand) //TODO: change it
                    ibtn.startAnimation(anim)
                    ibtn.setImageResource(R.drawable.ic_arrow_down_24dp)

                    viewHolder.rvMoreInfo.visibility = View.VISIBLE
                }
            }
        } else
            viewHolder.ibtnShowMoreInfo.visibility = View.GONE
    }
}


