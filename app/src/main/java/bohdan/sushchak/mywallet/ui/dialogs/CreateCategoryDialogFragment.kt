package bohdan.sushchak.mywallet.ui.dialogs

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.adapters.PickColorAdapter
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import kotlinx.android.synthetic.main.create_category_dialog_fragment.*

class CreateCategoryDialogFragment(var categoryEntity: CategoryEntity?) : DialogFragment() {

    private lateinit var adapter: PickColorAdapter

    var onResult: ((categoryEntity: CategoryEntity) -> Unit)? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.create_category_dialog_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {

        val colorList = convertStringToIntList(activity!!.resources.getStringArray(R.array.colorCategoriesArray))
        adapter = PickColorAdapter(context!!, colorList)

        if (categoryEntity != null) {
            edCategoryTitle.setText(categoryEntity!!.title)
            val index = colorList.indexOf(categoryEntity!!.color)
            adapter.mSelectedItem = index

        } else {
            categoryEntity = CategoryEntity(null, "", 0)
        }

        val gridLayoutManager = GridLayoutManager(context!!, 5)
        recyclerViewColors.layoutManager = gridLayoutManager
        recyclerViewColors.adapter = adapter

        btnCancel.setOnClickListener { dismiss() }
        btnSave.setOnClickListener {
            if (!isValidate())
                return@setOnClickListener

            categoryEntity!!.color = colorList[adapter.mSelectedItem]
            categoryEntity!!.title = edCategoryTitle.text.toString().trim()

            onResult?.invoke(categoryEntity!!)
            dismiss()
        }
    }

    private fun isValidate(): Boolean {
        if (edCategoryTitle.text?.toString().isNullOrBlank()) {
            Toast.makeText(context, "Please enter a categoryEntity", Toast.LENGTH_SHORT).show()
            return false
        } else if (adapter.mSelectedItem < 0) {
            Toast.makeText(context, "Please pick a color", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun convertStringToIntList(colorsString: Array<String>): List<Int> {
        val colorsInt = mutableListOf<Int>()
        for (color in colorsString) {
            colorsInt.add(Color.parseColor(color))
        }

        return colorsInt.toList()
    }
}