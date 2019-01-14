package bohdan.sushchak.mywallet.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.db.entity.Category

class MySpinnerAdapter(private val context: Context,
                       private val categories: List<Category>)
    : BaseAdapter() {

    override fun getItem(position: Int): Category? {
        return categories.get(position)
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if(view == null){
            view = LayoutInflater.from(context)
                    .inflate(R.layout.category_item_spinner, null)
        }


        view!!.findViewById<TextView>(R.id.tvCategory).text = categories[position].title
        view!!.findViewById<TextView>(R.id.tvCategory).setTextColor(categories[position].color)
        //convertView.findViewById<CardView>(R.id.boxColor).setBackgroundColor(categories[position].color)

        return view
    }

    override fun getCount(): Int {
        return categories.size
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

        view!!.findViewById<TextView>(R.id.tvCategory).text = categories[position].title
        view.findViewById<TextView>(R.id.tvCategory).setTextColor(categories[position].color)
        //convertView.findViewById<CardView>(R.id.boxColor).setBackgroundColor(categories[position].color)

        return view
    }
}