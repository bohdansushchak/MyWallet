package bohdan.sushchak.mywallet.ui.graph

import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.ui.base.BaseFragment
import com.jjoe64.graphview.series.BarGraphSeries
import kotlinx.coroutines.launch
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.DataPoint
import kotlinx.android.synthetic.main.graph_fragment.*


class GraphFragment : BaseFragment() {

    private lateinit var viewModel: GraphViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.graph_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GraphViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch {

        val series1 = BarGraphSeries<DataPoint>(arrayOf(DataPoint(1.0, 1.0)))
        val series2 = BarGraphSeries<DataPoint>(arrayOf(DataPoint(2.0, 5.0)))
        val series3 = BarGraphSeries<DataPoint>(arrayOf(DataPoint(3.0, 3.0)))
        val series4 = BarGraphSeries<DataPoint>(arrayOf(DataPoint(4.0, 6.0)))


        series1.color = Color.RED
        series2.color = Color.BLUE
        series3.color = Color.GREEN
        series4.color = Color.YELLOW

        series1.title = "Produkty"
        series2.title = "Paliwo"
        series3.title = "Zabawy"
        series4.title = "Rachunki"

        graphCategoryByMonth.addSeries(series1)
        graphCategoryByMonth.addSeries(series2)
        graphCategoryByMonth.addSeries(series3)
        graphCategoryByMonth.addSeries(series4)

        graphCategoryByMonth.viewport.setMinX(0.0)
        graphCategoryByMonth.viewport.setMaxX(7.0)
        graphCategoryByMonth.viewport.isXAxisBoundsManual = true
        graphCategoryByMonth.legendRenderer.isVisible = true
        graphCategoryByMonth.legendRenderer.spacing = 42

    }

}
