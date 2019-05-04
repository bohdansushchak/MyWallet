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

class MySpinnerAdapter(private val context: Context,
                       private val categoryEntities: List<CategoryEntity>)
    : BaseAdapter() {

    override fun getItem(position: Int): CategoryEntity? {
        return categoryEntities.get(position)
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if(view == null){
            view = LayoutInflater.from(context)
                    .inflate(R.layout.category_item_spinner, null)
        }


        view!!.findViewById<TextView>(R.id.tvCategory).text = categoryEntities[position].categoryTitle
        view!!.findViewById<TextView>(R.id.tvCategory).setTextColor(categoryEntities[position].color)
        //convertView.findViewById<CardView>(R.categoryId.boxColor).setBackgroundColor(categoryEntities[position].color)

        return view
    }

    override fun getCount(): Int {
        return categoryEntities.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("InflateParams")
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if(view == null){
            view = LayoutInflater.from(context)
                    .inflate(R.layout.category_item_spinner, null)
        }

        view!!.findViewById<TextView>(R.id.tvCategory).text = categoryEntities[position].categoryTitle
        view.findViewById<TextView>(R.id.tvCategory).setTextColor(categoryEntities[position].color)
        //convertView.findViewById<CardView>(R.categoryId.boxColor).setBackgroundColor(categoryEntities[position].color)

        return view
    }
}