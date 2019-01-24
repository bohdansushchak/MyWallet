package bohdan.sushchak.mywallet.ui.graph

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.model.CategoryPrice
import bohdan.sushchak.mywallet.data.model.GraphItem
import bohdan.sushchak.mywallet.data.model.LegendItem
import bohdan.sushchak.mywallet.data.model.MoneyByDate
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.Constants
import bohdan.sushchak.mywallet.internal.CustomLabelFormatter
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.Series
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class GraphViewModel(private val myWalletRepository: MyWalletRepository) : ViewModel() {

    val graphItems: MutableLiveData<List<GraphItem>> = MutableLiveData()

    init {
        updateGraphItems(1546297200000, 1548889200000)
    }

    private fun updateGraphItems(startDate: Long, endDate: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            val graphItemsList = mutableListOf<GraphItem>()

            val listCategoryPriceByMonth = myWalletRepository.getCategoriesPrice(startDate, endDate)
            val listMoneyByDateByMonth = myWalletRepository.getTotalPriceByDate(startDate, endDate)

            val itemCategoryPriceByMonth = getBarGraph(R.string.graph_title_category_by_month, listCategoryPriceByMonth)
            val itemMoneyByDateByMonth = getLineGraph(R.string.graph_title_spend_money_for_each_day_in_month, listMoneyByDateByMonth)

            graphItemsList.apply {
                add(itemCategoryPriceByMonth)
                add(itemMoneyByDateByMonth)
            }

            graphItems.postValue(graphItemsList)
        }
    }

    private fun getBarGraph(graphTitleResId: Int, listCategoryPrice: List<CategoryPrice>): GraphItem {
        val graphItem = GraphItem()

        graphItem.apply {
            titleResId = graphTitleResId
            isShowLegend = true
        }

        val listSeries = convertToSeries(listCategoryPrice)
        val legendItemsList = getLegendItemsBarGraph(listCategoryPrice)

        graphItem.apply {
            seriesList.addAll(listSeries)
            legendItems = legendItemsList.toMutableList()
            isXAxisBoundsManual = true
            minX = 0.0
            minY = 0.0
            maxX = (listCategoryPrice.size + 2).toDouble()
            labelFormatter = CustomLabelFormatter()
        }
        return graphItem
    }

    private fun convertToSeries(listCategoryPrice: List<CategoryPrice>): List<Series<DataPoint>> {
        val listSeries = mutableListOf<BarGraphSeries<DataPoint>>()

        listCategoryPrice.forEachIndexed { index, categoryPrice ->
            val dataPoint = DataPoint(index.toDouble() + 1, categoryPrice.totalPrice)
            val series = BarGraphSeries<DataPoint>(arrayOf(dataPoint))

            val categoryColor = categoryPrice.color ?: Constants.DEFAULT_CATEGORY_COLOR
            val categoryTitle = categoryPrice.title ?: ""

            series.apply {
                color = categoryColor
                title = categoryTitle
                isAnimated = true
                spacing = 50 / listCategoryPrice.size
                isDrawValuesOnTop = true
            }

            listSeries.add(series)

        }

        return listSeries
    }
    private fun getLegendItemsBarGraph(listCategoryPrice: List<CategoryPrice>): List<LegendItem>{
        val legendItemsList = mutableListOf<LegendItem>()

        listCategoryPrice.forEachIndexed { index, categoryPrice ->
            val categoryColor = categoryPrice.color ?: Constants.DEFAULT_CATEGORY_COLOR
            val categoryTitle = categoryPrice.title ?: ""

            val legendItem = LegendItem(categoryTitle, categoryColor)

            legendItemsList.add(legendItem)
        }

        return legendItemsList
    }

    private fun getLineGraph(graphTitleResId: Int, listMoneyByDate: List<MoneyByDate>): GraphItem {
        val graphItem = GraphItem()
        val dataPoints: MutableList<DataPoint> = mutableListOf()
        listMoneyByDate.forEach { moneyByDate ->
            val date = Date()
            date.time = moneyByDate.date
            val dataPoint = DataPoint(date, moneyByDate.totalPrice)
            dataPoints.add(dataPoint)
        }
        val lineGraphSeries = LineGraphSeries<DataPoint>(dataPoints.toTypedArray())

        graphItem.apply {
            titleResId = graphTitleResId
            seriesList.add(lineGraphSeries)
        }
        return graphItem
    }
}
