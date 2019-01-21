package bohdan.sushchak.mywallet.data.model

import androidx.annotation.ColorInt
import bohdan.sushchak.mywallet.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.legend_item.*

data class LegendItem(private val title: String,
                      @ColorInt private val color: Int) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.vColor.setBackgroundColor(color)
        viewHolder.tvTitleLegend.text = title
    }

    override fun getLayout() = R.layout.legend_item
}