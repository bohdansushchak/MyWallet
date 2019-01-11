package bohdan.sushchak.mywallet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import bohdan.sushchak.mywallet.R

import bohdan.sushchak.mywallet.data.db.model.OrdersByDate
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.internal.Constants
import bohdan.sushchak.mywallet.internal.formatDate
import java.util.*

class ExpandableListOrderAdapter(private val context: Context,
                                 private val listOrderByDate: List<OrdersByDate>): BaseExpandableListAdapter() {

    var onLongClick: ((view: View, order: Order) -> Unit)? = null
    var onClick: ((order: Order) -> Unit)? = null

    override fun getGroup(groupPosition: Int): bohdan.sushchak.mywallet.data.db.entity.Date {
        return listOrderByDate[groupPosition].date
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }

    override fun hasStableIds(): Boolean {
       return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if(convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.date_item, null)
        }

        val date = Date()
        date.time = getGroup(groupPosition).date

        convertView!!.findViewById<TextView>(R.id.tvDate).text = formatDate(date, Constants.DATE_FORMAT)

        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listOrderByDate[groupPosition].orders.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Order {
        return listOrderByDate[groupPosition].orders[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if(convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.order_item, null)
        }

        convertView!!.findViewById<TextView>(R.id.tvOrderTitle).text = getChild(groupPosition, childPosition).title
        convertView.findViewById<TextView>(R.id.tvOrderPrice).text = getChild(groupPosition, childPosition).price.toString()

        convertView.setOnLongClickListener {
            onLongClick?.invoke(it, getChild(groupPosition, childPosition))
            return@setOnLongClickListener onLongClick == null
        }

        convertView.setOnClickListener {
            onClick?.invoke(getChild(groupPosition, childPosition))
        }

        return convertView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return listOrderByDate.size
    }
}