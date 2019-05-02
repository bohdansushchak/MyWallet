package bohdan.sushchak.mywallet.ui.graph

import android.annotation.SuppressLint
import android.graphics.Typeface
import bohdan.sushchak.mywallet.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.more_info_item.*

class MoreInfoItem(
    private val title: String = "",
    private val titleResId: Int = -1,
    private val data: String,
    private val currency: String,
    private val isTextBold: Boolean = false
) : Item() {

    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: ViewHolder, position: Int) {
        if (titleResId != -1)
            viewHolder.tvTitle.setText(titleResId)
        else
            viewHolder.tvTitle.text = title

        viewHolder.tvData.text = "$data $currency"

        if (isTextBold) {
            viewHolder.tvData.setTypeface(viewHolder.tvData.typeface, Typeface.BOLD)
            viewHolder.tvTitle.setTypeface(viewHolder.tvTitle.typeface, Typeface.BOLD)
        }
    }

    override fun getLayout() = R.layout.more_info_item
}