package bohdan.sushchak.mywallet.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.adapters.PickColorAdapter
import kotlinx.android.synthetic.main.create_category_dialog_fragment.*

class CreateCategoryDialogFragment : DialogFragment() {

    private lateinit var adapter : PickColorAdapter

    var onResult: ((title: String, color: String) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.create_category_dialog_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }
    private fun bindUI(){
        val colorList = activity!!.resources.getStringArray(R.array.colorArray).toList()

        adapter = PickColorAdapter(context!!, colorList)

        val gridLayoutManager = GridLayoutManager(context!!,5)
        recyclerViewColors.layoutManager = gridLayoutManager
        recyclerViewColors.adapter = adapter

        btnCancel.setOnClickListener { dismiss() }

        btnSave.setOnClickListener {
            if(!isValidate())
                return@setOnClickListener

            val color = colorList[adapter.mSelectedItem]
            val categoryTitle = edCategoryTitle.text.toString()

            onResult?.invoke(categoryTitle, color)
            dismiss()
        }
    }

    private fun isValidate() : Boolean {
        if(edCategoryTitle.text?.toString().isNullOrBlank()){
            Toast.makeText(context, "Please enter a category", Toast.LENGTH_SHORT).show()
            return false
        }else if(adapter.mSelectedItem < 0){
            Toast.makeText(context, "Please pick a color", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}