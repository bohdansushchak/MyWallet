package bohdan.sushchak.mywallet.ui.graph

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.model.CategoryPrice
import bohdan.sushchak.mywallet.data.model.MoneyByDate
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.Constants
import bohdan.sushchak.mywallet.internal.getDateLimit
import bohdan.sushchak.mywallet.internal.label_formatter.BarLabelFormatter
import bohdan.sushchak.mywallet.internal.label_formatter.LineLabelFormatter
import bohdan.sushchak.mywallet.internal.myPlus
import bohdan.sushchak.mywallet.internal.myToString
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.Series
import kotlinx.coroutines.*
import java.util.*

class GraphViewModel(
    private val myWalletRepository: MyWalletRepository,
    private val currency: String
) : ViewModel() {

    //region public parameters
    val graphItems: LiveData<List<GraphItem>>
        get() = _graphItems
    //endregion

    private val _graphItems by lazy { MutableLiveData<List<GraphItem>>() }

    init {
        val dateLimit = getDateLimit(2019)
        updateGraphItems(dateLimit.startDate, dateLimit.endDate)
    }

    private fun updateGraphItems(startDate: Long, endDate: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            val graphItemsList = mutableListOf<GraphItem>()

            val listCategoryPriceByMonth = myWalletRepository.getCategoriesPrice(startDate, endDate)
            val listMoneyByDateByMonth = myWalletRepository.getTotalPriceByDate(startDate, endDate)

            val itemCategoryPriceByMonth =
                getBarGraphAsync(R.string.graph_title_category_by_month, listCategoryPriceByMonth)
            val itemMoneyByDateByMonth =
                getLineGraphAsync(R.string.graph_title_spend_money_for_each_day_in_month, listMoneyByDateByMonth)

            graphItemsList.apply {
                add(itemCategoryPriceByMonth.await())
                add(itemMoneyByDateByMonth.await())
            }

            _graphItems.postValue(graphItemsList)
        }
    }

    private fun getBarGraphAsync(graphTitleResId: Int, listCategoryPrice: List<CategoryPrice>): Deferred<GraphItem> {
        return GlobalScope.async {
            val legendItemsList = getLegendItemsBarGraph(listCategoryPrice)
            val listSeries = convertToSeries(listCategoryPrice)

            val moreInfoItemList = getBarGraphSubItems(listSeries)

            val graphItem = GraphItem(
                seriesList = listSeries,
                legendItems = legendItemsList,
                titleResId = graphTitleResId,
                maxX = (listCategoryPrice.size + 2).toDouble(),
                isXAxisBoundsManual = true,
                labelFormatter = BarLabelFormatter(),
                moreInfoItems = moreInfoItemList
            )
            return@async graphItem
        }
    }

    private fun getBarGraphSubItems(listSeries: List<Series<DataPoint>>): List<MoreInfoItem> {
        val moreInfoItemList = mutableListOf<MoreInfoItem>()
        var totalPrice = 0.0
        listSeries.forEach {
            totalPrice = totalPrice.myPlus(it.highestValueY)
            val title = it.title
            val valueStr = it.highestValueY.myToString()
            moreInfoItemList.add(
                MoreInfoItem(
                    title = title,
                    data = valueStr,
                    currency = currency
                )
            )
        }

        moreInfoItemList.add(
            MoreInfoItem(
                titleResId = R.string.total,
                data = totalPrice.myToString(),
                currency = currency,
                isTextBold = true
            )
        )
        return moreInfoItemList
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

    private fun getLegendItemsBarGraph(listCategoryPrice: List<CategoryPrice>): List<LegendItem> {
        val legendItemsList = mutableListOf<LegendItem>()

        listCategoryPrice.forEach { categoryPrice ->
            val categoryColor = categoryPrice.color ?: Constants.DEFAULT_CATEGORY_COLOR
            val categoryTitle = categoryPrice.title ?: ""

            val legendItem = LegendItem(categoryTitle, categoryColor)

            legendItemsList.add(legendItem)
        }

        return legendItemsList
    }

    private fun getLineGraphAsync(graphTitleResId: Int, listMoneyByDate: List<MoneyByDate>): Deferred<GraphItem> {
        return GlobalScope.async {
            val dataPoints: MutableList<DataPoint> = mutableListOf()
            listMoneyByDate.forEach { moneyByDate ->
                val date = Date(moneyByDate.date)
                val cal = Calendar.getInstance()
                cal.time = date
                val day = cal.get(Calendar.DAY_OF_MONTH).toDouble()
                val dataPoint = DataPoint(day, moneyByDate.totalPrice)
                dataPoints.add(dataPoint)
            }
            dataPoints.sortBy { it.x }

            val lineGraphSeries = LineGraphSeries<DataPoint>(dataPoints.toTypedArray())
            lineGraphSeries.setAnimated(true)

            val graphItem = GraphItem(
                titleResId = graphTitleResId,
                seriesList = listOf(lineGraphSeries),
                maxX = 32.0,
                isXAxisBoundsManual = true,
                labelFormatter = LineLabelFormatter()
            )

            graphItem.apply {

            }
            return@async graphItem
        }
    }
}
