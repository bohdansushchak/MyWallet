package bohdan.sushchak.mywallet.data.model

import com.jjoe64.graphview.LabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.Series

class GraphItem {

    var title: String = ""

    var series: MutableList<Series<DataPoint>> = mutableListOf()

    var isShowLegend: Boolean = false

    var legendItems: List<LegendItem>? = null

    var isXAxisBoundsManual: Boolean = false

    var minX: Double? = null
    var minY: Double? = null
    var maxX: Double? = null
    var maxY: Double? = null

    var labelFormatter: LabelFormatter? = null
}


