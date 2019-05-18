package bohdan.sushchak.mywallet.ui.graph

import android.annotation.SuppressLint
import android.graphics.Typeface
import bohdan.sushchak.mywallet.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.more_info_item.*
import java.lang.Double.parseDouble
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * This class represent a additional information in graph item
 *
 * @property title title
 * @property titleResId id of string
 * @property data data
 * @property currency currency from settings
 * @property isTextBold if true text will be bold
 */
class MoreInfoItem(
    private val title: String = "",
    private val titleResId: Int = -1,
    private val data: String,
    private val currency: String,
    private val isTextBold: Boolean = false
) : Item() {

    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: ViewHolder, position: Int) {
        when {
            titleResId != -1 -> viewHolder.tvTitle.setText(titleResId)
            title.isNotEmpty() -> viewHolder.tvTitle.text = title
            else -> viewHolder.tvTitle.setText(R.string.non_set_category)
        }

        val value = parseData(data)
        viewHolder.tvData.text = value

        if (isTextBold) {
            viewHolder.tvData.setTypeface(viewHolder.tvData.typeface, Typeface.BOLD)
            viewHolder.tvTitle.setTypeface(viewHolder.tvTitle.typeface, Typeface.BOLD)
        }
    }

    private fun parseData(data: String): String {
        lateinit var value: String
        try {
            val num = parseDouble(data)
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            value = "${df.format(num)} $currency"
        } catch (e: NumberFormatException) {
           value = "$data $currency"
        }
        return value
    }

    override fun getLayout() = R.layout.more_info_item
}