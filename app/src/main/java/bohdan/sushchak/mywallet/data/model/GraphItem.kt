package bohdan.sushchak.mywallet.data.model

import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.Series

class GraphItem {

    var title: String = ""

    var series: MutableList<Series<DataPoint>> = mutableListOf()

    var isShowLegend: Boolean = false

    var legendItems: MutableList<LegendItem>? = null
}


