package bohdan.sushchak.mywallet.ui.settings

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.adapters.CategoryAdapter
import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.ui.base.ScoptedFragment
import bohdan.sushchak.mywallet.ui.dialogs.CreateCategoryDialogFragment
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class SettingsFragment : ScoptedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val viewModelFactory: SettingsViewModelFactory by instance()
    private lateinit var viewModel: SettingsViewModel

    private lateinit var categoryAdapter : CategoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SettingsViewModel::class.java)

        btnAddCategory.setOnClickListener {
            val ft = fragmentManager?.beginTransaction()
            val fragment = CreateCategoryDialogFragment()
            fragment.show(ft,"")

            fragment.onResult = {title, color -> createCategory(title, color) }
        }

        bindUI()
    }

    private fun bindUI() = launch {
        val categoryList = viewModel.categories.await()

        categoryList.observe(this@SettingsFragment, Observer { categories ->
            if (categories == null) return@Observer
            updateCategoryList(categories)
        })
    }

    private fun updateCategoryList(categories: List<Category>) {
        val linearLayout = LinearLayoutManager(context!!)
        linearLayout.orientation = RecyclerView.VERTICAL
        categoryAdapter = CategoryAdapter(context!!, categories)

        recyclerViewCategory.layoutManager = linearLayout
        recyclerViewCategory.adapter = categoryAdapter
    }

    private fun createCategory(title: String, color: String){
        val category = Category(title = title, color = Color.parseColor(color))
        viewModel.addCategory(category)
        
    }
}
