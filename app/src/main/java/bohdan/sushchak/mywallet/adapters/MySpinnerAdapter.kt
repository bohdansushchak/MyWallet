package bohdan.sushchak.mywallet.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity

/**
 * Spinner adapter to make a list for spinner. This using to show categories when user add a new product
 *
 * @property context context need to have a access to layouts
 * @property categoryEntities categories items
 */
class MySpinnerAdapter(
    private val context: Context,
    private val categoryEntities: List<CategoryEntity>
) : BaseAdapter() {

    override fun getItem(position: Int): CategoryEntity? {
        return categoryEntities[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.category_item_spinner, null)
        view.findViewById<TextView>(R.id.tvCategory).text = categoryEntities[position].categoryTitle
        view.findViewById<TextView>(R.id.tvCategory).setTextColor(categoryEntities[position].color)

        return view
    }

    /**
     * method to get a size of items in list
     *
     * @return
     */
    override fun getCount(): Int {
        return categoryEntities.size
    }

    /**
     * method to get a id of item
     *
     * @param position position item in list
     * @return return a id of item
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("InflateParams")
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /*       var view = convertView
               if (view == null) {
                   view = LayoutInflater.from(context)
                       .inflate(R.layout.category_item_spinner, null)
               }

               view!!.findViewById<TextView>(R.id.tvCategory).text = categoryEntities[position].categoryTitle
               view.findViewById<TextView>(R.id.tvCategory).setTextColor(categoryEntities[position].color)
       */
        return getView(position, convertView, parent)
    }
}