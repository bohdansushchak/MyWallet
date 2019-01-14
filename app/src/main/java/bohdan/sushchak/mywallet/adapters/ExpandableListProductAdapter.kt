package bohdan.sushchak.mywallet.adapters

import android.annotation.SuppressLint
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

    @SuppressLint("InflateParams")
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.category_item_spinner, null)
        }

        val tvCategory = view!!.findViewById<TextView>(R.id.tvCategory)
        tvCategory.text = getGroup(groupPosition).title
        return view
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

    @SuppressLint("InflateParams")
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.product_item, null)
        }

        val tvProductName = view!!.findViewById<TextView>(R.id.tvProductTitle)
        val tvProductPrice = view.findViewById<TextView>(R.id.tvProductPrice)

        tvProductName.text = getChild(groupPosition, childPosition).title
        tvProductPrice.text = getChild(groupPosition, childPosition).price.toString()

        view.setOnLongClickListener { v ->

            onLongClick?.invoke(v, getChild(groupPosition, childPosition))

            return@setOnLongClickListener (onLongClick != null)
        }

        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return items.size
    }
}