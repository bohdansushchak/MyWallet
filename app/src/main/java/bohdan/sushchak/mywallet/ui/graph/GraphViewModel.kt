package bohdan.sushchak.mywallet.ui.graph

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.model.CategoryPrice
import bohdan.sushchak.mywallet.data.model.DateLimit
import bohdan.sushchak.mywallet.data.model.DateRange
import bohdan.sushchak.mywallet.data.model.MoneyByDate
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.Constants
import bohdan.sushchak.mywallet.internal.getDayOfTime
import bohdan.sushchak.mywallet.internal.label_formatter.BarLabelFormatter
import bohdan.sushchak.mywallet.internal.label_formatter.LineLabelFormatter
import bohdan.sushchak.mywallet.internal.myPlus
import bohdan.sushchak.mywallet.internal.myToString
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.Series
import kotlinx.coroutines.*

class GraphViewModel(
    private val myWalletRepository: MyWalletRepository,
    private val currency: String
) : ViewModel() {

    //region public parameters
    val graphItems: LiveData<List<GraphItem>>
        get() = _graphItems

    val dateLimit: LiveData<DateLimit>
        get() = _dateLimitLive

    //endregion

    private val _graphItems by lazy { MutableLiveData<List<GraphItem>>() }
    private val _dateLimitLive by lazy { MutableLiveData<DateLimit>() }
    private lateinit var _dateLimit: DateLimit

    init {
        GlobalScope.launch(Dispatchers.Main) {
            _dateLimit = myWalletRepository.getDateLimit()
            _dateLimitLive.postValue(_dateLimit)
            updateGraphItems(_dateLimit)
        }
    }

    fun updateDateLimit(startDate: Long? = null, endDate: Long? = null) {
        if (startDate == null && endDate == null)
            return
        GlobalScope.launch(Dispatchers.Main) {
            val newDateLimit = _dateLimit
            startDate?.let {
                newDateLimit.startDate = it
            }
            endDate?.let {
                newDateLimit.endDate = it
            }

            _dateLimitLive.postValue(newDateLimit)
            _dateLimit = newDateLimit
            updateGraphItems(_dateLimit)
        }
    }

    private fun updateGraphItems(dateRange: DateRange?) {
        if (dateRange == null)
            return
        GlobalScope.launch(Dispatchers.IO) {
            val startDate = dateRange.startDate
            val endDate = dateRange.endDate
            val graphItemsList = mutableListOf<GraphItem>()

            val listCategoryPriceByMonth = myWalletRepository.getCategoriesPrice(startDate, endDate)
            val listMoneyByDateByMonth = myWalletRepository.getTotalPriceByDate(startDate, endDate)

            val itemCategoryPriceByMonth = getBarGraphAsync(listCategoryPriceByMonth)
            val itemMoneyByDateByMonth = getLineGraphAsync(listMoneyByDateByMonth)
            val itemGrowingLineGraph = getGrowingLineGraphAsync(listMoneyByDateByMonth)

            graphItemsList.apply {
                add(itemCategoryPriceByMonth.await())
                add(itemMoneyByDateByMonth.await())
                add(itemGrowingLineGraph.await())
            }

            _graphItems.postValue(graphItemsList)
        }
    }

    private fun getBarGraphAsync(listCategoryPrice: List<CategoryPrice>): Deferred<GraphItem> {
        return GlobalScope.async {
            val legendItemsList = getLegendItemsBarGraph(listCategoryPrice)
            val listSeries = convertToSeries(listCategoryPrice)
            val moreInfoItemList = getBarGraphSubItems(listSeries)

            val graphItem = GraphItem(
                seriesList = listSeries,
                legendItems = legendItemsList,
                titleResId = R.string.graph_title_category_by_month,
                maxX = (listCategoryPrice.size + 2).toDouble(),
                maxY = listCategoryPrice.maxBy { it.totalPrice }?.totalPrice?.times(1.1) ?: 0.0,
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

    private fun getGrowingLineGraphAsync(listMoneyByDate: List<MoneyByDate>): Deferred<GraphItem> {
        return GlobalScope.async {
            val dataPoints: MutableList<DataPoint> = mutableListOf()
            var sumOfTotalPrices = 0.0
            val minDateInList = listMoneyByDate.minBy { it.date }?.date ?: 0
            val dayOfYearForMinDate = getDayOfTime(minDateInList)
            val countOfDays = _dateLimit.getCountOfDays().toDouble()

            listMoneyByDate.forEach { moneyByDate ->
                val day = getDayOfTime(moneyByDate.date) - dayOfYearForMinDate + 2
                sumOfTotalPrices = sumOfTotalPrices.myPlus(moneyByDate.totalPrice)
                val dataPoint = DataPoint(day.toDouble(), sumOfTotalPrices)
                dataPoints.add(dataPoint)
            }

            val lineGraphSeries = getLineGraphSeries(dataPoints)
            val graphItem = GraphItem(
                titleResId = R.string.graph_title_growing_line,
                seriesList = listOf(lineGraphSeries),
                maxX = if (countOfDays + 2 >= 5) countOfDays + 2 else 5.0,
                maxY = sumOfTotalPrices.times(1.1),
                labelFormatter = LineLabelFormatter()
            )
            return@async graphItem
        }
    }

    private fun getLineGraphAsync(listMoneyByDate: List<MoneyByDate>): Deferred<GraphItem> {
        return GlobalScope.async {
            val dataPoints: MutableList<DataPoint> = mutableListOf()
            val minDateInList = listMoneyByDate.minBy { it.date }?.date ?: 0
            val dayOfYearForMinDate = getDayOfTime(minDateInList)
            val countOfDays = _dateLimit.getCountOfDays().toDouble()

            listMoneyByDate.forEach { moneyByDate ->
                val day = getDayOfTime(moneyByDate.date) - dayOfYearForMinDate + 2
                val dataPoint = DataPoint(day.toDouble(), moneyByDate.totalPrice)
                dataPoints.add(dataPoint)
            }

            val lineGraphSeries = getLineGraphSeries(dataPoints)
            val graphItem = GraphItem(
                titleResId = R.string.graph_title_spend_money_for_each_day_in_month,
                seriesList = listOf(lineGraphSeries),
                maxX = if (countOfDays + 2 >= 5) countOfDays + 2 else 5.0,
                maxY = listMoneyByDate.maxBy { it.totalPrice }?.totalPrice?.times(1.1) ?: 0.0,
                labelFormatter = LineLabelFormatter()
            )
            return@async graphItem
        }
    }

    private fun getLineGraphSeries(listDataPoint: List<DataPoint>): LineGraphSeries<DataPoint> {
        val sorted = listDataPoint.toTypedArray()
        sorted.sortedBy {
            it.x
        }

        val lineGraphSeries = LineGraphSeries<DataPoint>(sorted)
        lineGraphSeries.setAnimated(true)
        lineGraphSeries.isDrawDataPoints = true
        lineGraphSeries.dataPointsRadius = 5f
        return lineGraphSeries
    }
}
