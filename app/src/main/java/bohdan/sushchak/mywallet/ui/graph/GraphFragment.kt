package bohdan.sushchak.mywallet.ui.graph

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.adapters.LegendAdapter
import bohdan.sushchak.mywallet.data.model.CategoryPrice
import bohdan.sushchak.mywallet.internal.Constants
import bohdan.sushchak.mywallet.internal.CustomLabelFormatter
import bohdan.sushchak.mywallet.ui.base.BaseFragment
import com.jjoe64.graphview.series.BarGraphSeries
import kotlinx.coroutines.launch
import com.jjoe64.graphview.series.DataPoint
import kotlinx.android.synthetic.main.graph_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class GraphFragment : BaseFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: GraphViewModelFactory by instance()
    private lateinit var viewModel: GraphViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.graph_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(GraphViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch {

        viewModel.updateCategories(1547334000000, 1547593200000)

        viewModel.categoriesTotalPrice.observe(this@GraphFragment, Observer {

            Log.d("TAG", it.toString())
            updateGraph(it)
        })
    }

    private fun updateGraph(items: List<CategoryPrice>){
        graphCategoryByMonth.removeAllSeries()

        items.forEachIndexed { index, categoryPrice ->
            val dataPoint = DataPoint(index.toDouble() + 1, categoryPrice.totalPrice)
            val series = BarGraphSeries<DataPoint>(arrayOf(dataPoint))

            series.color = categoryPrice.color ?: Constants.DEFAULT_CATEGORY_COLOR
            series.title = categoryPrice.title
            series.isAnimated = true
            graphCategoryByMonth.addSeries(series)
        }

        graphCategoryByMonth.viewport.setMinX(0.0)
        graphCategoryByMonth.viewport.setMinY(0.0)
        graphCategoryByMonth.viewport.setMaxX((items.size + 2).toDouble())

        graphCategoryByMonth.gridLabelRenderer.labelFormatter = CustomLabelFormatter()

        val adapter = LegendAdapter(context!!, items)

        rcLegend.adapter = adapter
        rcLegend.layoutManager = LinearLayoutManager(context)

    }

}
