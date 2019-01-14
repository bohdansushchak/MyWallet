package bohdan.sushchak.mywallet.internal

import com.jjoe64.graphview.LabelFormatter
import com.jjoe64.graphview.Viewport
import kotlin.math.roundToInt

class CustomLabelFormatter : LabelFormatter {

    override fun formatLabel(value: Double, isValueX: Boolean): String {
        var str: String = value.roundToInt().toString()

        if (isValueX || value == 0.0)
            str = ""
        else
            if (value.div(1000) >= 1 && value.div(1000) < 1000) {
                str = "${value.div(1000).roundToInt()}K"
            } else if (value.div(1000000) >= 1 && value.div(1000000) < 1000) {
                str = "${value.div(1000000).roundToInt()}M"
            }

        return str
    }

    override fun setViewport(viewport: Viewport?) {

    }
}