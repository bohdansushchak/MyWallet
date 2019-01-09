package bohdan.sushchak.mywallet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.db.entity.Category

class MySpinnerAdapter(val context: Context,
                       val layout: Int,
                       val categories: List<Category>)
    : BaseAdapter() {

    override fun getItem(position: Int): Category? {
        return categories.get(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if(convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.category_item_spinner, null)
        }


        convertView!!.findViewById<TextView>(R.id.tvCategory).text = categories[position].title
        convertView!!.findViewById<TextView>(R.id.tvCategory).setTextColor(categories[position].color)
        //convertView.findViewById<CardView>(R.id.boxColor).setBackgroundColor(categories[position].color)

        return convertView
    }

    override fun getCount(): Int {
        return categories.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if(convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.category_item_spinner, null)
        }

        convertView!!.findViewById<TextView>(R.id.tvCategory).text = categories[position].title
        convertView!!.findViewById<TextView>(R.id.tvCategory).setTextColor(categories[position].color)
        //convertView.findViewById<CardView>(R.id.boxColor).setBackgroundColor(categories[position].color)

        return convertView
    }
}