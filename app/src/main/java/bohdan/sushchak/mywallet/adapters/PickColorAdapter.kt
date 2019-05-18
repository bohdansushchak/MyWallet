package bohdan.sushchak.mywallet.adapters

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
import kotlinx.android.synthetic.main.color_item.view.*

/**
 * Adapter to show colors in recycler view
 *
 * @property context Context
 * @property colors list of colors
 * @author Bohdan
 * @version 1.2
 */
class PickColorAdapter(
    private var context: Context,
    private val colors: List<Int>
) : RecyclerView.Adapter<PickColorAdapter.ViewHolder>() {

    var mSelectedItem = -1
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /**
     * Method to create a ViewHolder
     *
     * @param parent
     * @param viewType
     * @return instance of PickColorAdapter.ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.color_item, parent, false)
        )
    }

    /**
     * get count of items
     *
     * @return size of items in list
     */
    override fun getItemCount(): Int {
        return colors.size
    }

    /**
     * Method to bind data in list with elements in recycerView
     *
     * @param holder container of view
     * @param position position of item in list
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(colors[position])

        holder.ivColor.background = if (mSelectedItem == position)
            context.getDrawable(R.drawable.ic_check_circle)
        else null

        holder.itemView.setOnClickListener { setCheck(position) }
    }

    /**
     * method to show stick in view if user choose a color
     *
     * @param position position of item in list
     */
    private fun setCheck(position: Int) {
        mSelectedItem = position
        notifyDataSetChanged()
    }

    /**
     * Class represent a container for views.
     *
     * @constructor
     *
     * @param itemView root view of item
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * ImageView with contain a color
         */
        val ivColor = itemView.ivColor
        /**
         * color view
         */
        val vColor = itemView.vColor

        /**
         * Method to bind color with item
         *
         * @param color color item
         */
        fun bind(color: Int) {
            val background = vColor.background

            if (background is ShapeDrawable)
                background.paint.color = color
            else if (background is GradientDrawable)
                background.setColor(color)

            vColor.background = background
        }
    }
}