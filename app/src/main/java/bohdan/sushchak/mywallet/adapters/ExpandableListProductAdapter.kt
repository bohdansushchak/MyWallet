package bohdan.sushchak.mywallet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.data.db.entity.Product
import bohdan.sushchak.mywallet.data.model.CategoryWithProducts

class ExpandableListProductAdapter(
        private val context: Context,
        private val items: MutableList<CategoryWithProducts>)
    : BaseExpandableListAdapter() {

    var onLongClick: ((view: View, product: Product) -> Unit)? = null

    override fun getGroup(groupPosition: Int): Category {
        return items[groupPosition].category
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.category_item_spinner, null)
        }

        val tvCategory = convertView!!.findViewById<TextView>(R.id.tvCategory)
        tvCategory.text = getGroup(groupPosition).title
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return items[groupPosition].products.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Product {
        return items[groupPosition].products[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.product_item, null)
        }

        val tvProductName = convertView!!.findViewById<TextView>(R.id.tvProductTitle)
        val tvProductPrice = convertView!!.findViewById<TextView>(R.id.tvProductPrice)

        tvProductName.text = getChild(groupPosition, childPosition).title
        tvProductPrice.text = getChild(groupPosition, childPosition).price.toString()

        convertView.setOnLongClickListener { view ->

            onLongClick?.invoke(view, getChild(groupPosition, childPosition))

            return@setOnLongClickListener (onLongClick != null)
        }

        return convertView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return items.size
    }
}