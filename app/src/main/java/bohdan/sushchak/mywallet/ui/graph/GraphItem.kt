package bohdan.sushchak.mywallet.ui.graph

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.recyclerview.widget.LinearLayoutManager
import bohdan.sushchak.mywallet.R
import com.jjoe64.graphview.LabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.Series
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.graph_item.*

class GraphItem(
    private val moreInfoItems: List<MoreInfoItem>? = null,
    private val legendItems: List<LegendItem>? = null,
    private val seriesList: List<Series<DataPoint>> = listOf(),
    private val titleResId: Int,
    private val minX: Double = 0.0,
    private val minY: Double = 0.0,
    private val maxX: Double,
    private val maxY: Double,
    private val labelFormatter: LabelFormatter? = null
) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        if (titleResId != 0)
            viewHolder.tvGraphTitle.setText(titleResId)

        viewHolder.graph.apply {
            if (seriesList.isEmpty())
                removeAllSeries()

            viewport.isXAxisBoundsManual = true
            viewport.isYAxisBoundsManual = true

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
            val context = viewHolder.itemView.context
            val legendAnimation = AnimationUtils
                .loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

            val groupAdapter = GroupAdapter<ViewHolder>()
                .apply {
                    addAll(legendItems)
                }

            viewHolder.rcLegend.apply {
                visibility = View.VISIBLE
                adapter = groupAdapter
                layoutManager = LinearLayoutManager(context)
                layoutAnimation = legendAnimation
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
                if (viewHolder.rvMoreInfo.visibility == View.VISIBLE) {
                    startCollapseAnimation(viewHolder)
                } else {
                    startExpandAnimation(viewHolder)
                }
            }
        } else
            viewHolder.ibtnShowMoreInfo.visibility = View.GONE
    }

    @SuppressLint("PrivateResource")
    private fun startExpandAnimation(viewHolder: ViewHolder) {
        val context = viewHolder.itemView.context
        val buttonAnim = AnimationUtils.loadAnimation(context, R.anim.abc_slide_in_top)
        val recyclerAnimation = AnimationUtils
            .loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

        startAnimation(
            viewHolder = viewHolder,
            buttonAnimation = buttonAnim,
            recyclerAnimation = recyclerAnimation,
            recyclerVisibility = View.VISIBLE,
            buttonBackgroundResource = R.drawable.ic_arrow_collapse
        )
    }

    @SuppressLint("PrivateResource")
    private fun startCollapseAnimation(viewHolder: ViewHolder) {
        val context = viewHolder.itemView.context
        val buttonAnim = AnimationUtils.loadAnimation(context, R.anim.abc_slide_in_bottom)

        startAnimation(
            viewHolder = viewHolder,
            buttonAnimation = buttonAnim,
            recyclerVisibility = View.GONE,
            buttonBackgroundResource = R.drawable.ic_arrow_expand
        )
    }

    private fun startAnimation(
        viewHolder: ViewHolder,
        buttonAnimation: Animation,
        recyclerAnimation: LayoutAnimationController? = null,
        recyclerVisibility: Int,
        buttonBackgroundResource: Int
    ) {
        viewHolder.ibtnShowMoreInfo.apply {
            startAnimation(buttonAnimation)
            setBackgroundResource(buttonBackgroundResource)
        }
        viewHolder.rvMoreInfo.apply {
            layoutAnimation = recyclerAnimation
            visibility = recyclerVisibility
        }
    }
}


