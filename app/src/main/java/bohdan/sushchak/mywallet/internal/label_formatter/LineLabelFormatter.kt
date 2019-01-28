package bohdan.sushchak.mywallet.internal.label_formatter

import bohdan.sushchak.mywallet.internal.formatDigitToString
import com.jjoe64.graphview.LabelFormatter
import com.jjoe64.graphview.Viewport
import java.util.*

class LineLabelFormatter : LabelFormatter {
    override fun formatLabel(value: Double, isValueX: Boolean) = if (isValueX || value == 0.0)
        getNumberDay(value).toString()
    else formatDigitToString(value)

    override fun setViewport(viewport: Viewport?) {}

    private fun getNumberDay(value: Double): Int {
        val date = Date(value.toLong())
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.DAY_OF_MONTH)
    }
}