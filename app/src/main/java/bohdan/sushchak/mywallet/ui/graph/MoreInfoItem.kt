package bohdan.sushchak.mywallet.ui.graph

import bohdan.sushchak.mywallet.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.more_info_item.*

class MoreInfoItem(
    private val title: String,
    private val data: String
) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvTitle.text = title
        viewHolder.tvData.text = data
    }

    override fun getLayout() = R.layout.more_info_item
}