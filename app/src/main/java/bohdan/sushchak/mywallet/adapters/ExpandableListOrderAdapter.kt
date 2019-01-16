package bohdan.sushchak.mywallet.adapters

/*
class ExpandableListOrderAdapter(private val context: Context,
                                 private val listOrderByDate: List<OrdersByDateGroup>): BaseExpandableListAdapter() {

    var onLongClick: ((view: View, order: OrderEntity) -> Unit)? = null
    var onClick: ((order: OrderEntity) -> Unit)? = null

    override fun getGroup(groupPosition: Int): Long {
        return listOrderByDate[groupPosition].date
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }

    override fun hasStableIds(): Boolean {
       return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if(view == null){
            view = LayoutInflater.from(context)
                    .inflate(R.layout.date_item, null)
        }

        val date = Date()
        date.time = getGroup(groupPosition)

        view!!.findViewById<TextView>(R.id.tvDate).text = formatDate(date, Constants.DATE_FORMAT)

        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listOrderByDate[groupPosition].orders.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): OrderEntity {
        return listOrderByDate[groupPosition].orders[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if(view == null){
            view = LayoutInflater.from(context)
                    .inflate(R.layout.order_item, null)
        }

        view!!.findViewById<TextView>(R.id.tvOrderTitle).text = getChild(groupPosition, childPosition).title
        view.findViewById<TextView>(R.id.tvOrderPrice).text = getChild(groupPosition, childPosition).price.toString()

        view.setOnLongClickListener {
            onLongClick?.invoke(it, getChild(groupPosition, childPosition))
            return@setOnLongClickListener onLongClick == null
        }

        view.setOnClickListener {
            onClick?.invoke(getChild(groupPosition, childPosition))
        }

        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return listOrderByDate.size
    }
}*/