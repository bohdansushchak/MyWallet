package bohdan.sushchak.mywallet.internal.label_formatter

import bohdan.sushchak.mywallet.internal.formatDigitToString
import com.jjoe64.graphview.LabelFormatter
import com.jjoe64.graphview.Viewport

class BarLabelFormatter : LabelFormatter {

    override fun formatLabel(value: Double, isValueX: Boolean) = if (isValueX || value == 0.0) ""
    else
        formatDigitToString(value)

    override fun setViewport(viewport: Viewport?) {}
}