package bohdan.sushchak.mywallet.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.adapters.CategoryAdapter
import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.ui.base.BaseFragment
import bohdan.sushchak.mywallet.ui.dialogs.CreateCategoryDialogFragment
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class SettingsFragment : BaseFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val viewModelFactory: SettingsViewModelFactory by instance()
    private lateinit var viewModel: SettingsViewModel

    private lateinit var categoryAdapter: CategoryAdapter

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
            val category = Category.emptyCategory
            val fragment = CreateCategoryDialogFragment(category)
            fragment.show(ft, "")

            fragment.onResult = { newCategory -> viewModel.addCategory(newCategory) }
        }

        bindUI()
    }

    private fun bindUI() = launch {
        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(context!!.getDrawable(R.drawable.divider_black)!!)
        recyclerViewCategory.addItemDecoration(dividerItemDecoration)

        val categoryList = viewModel.categories.await()
        categoryList.observe(this@SettingsFragment, Observer { categories ->
            tvNoCategories.visibility = if(categories.isEmpty()) View.VISIBLE else View.GONE
            updateCategory(categories)
        })
    }

    private fun updateCategory(categories: List<Category>) {
        if (::categoryAdapter.isInitialized) {
            categoryAdapter.update(categories)
            initLongClick(categories)
            return
        }

        val linearLayout = LinearLayoutManager(context!!)
        categoryAdapter = CategoryAdapter(context!!, categories)

        recyclerViewCategory.layoutManager = linearLayout
        recyclerViewCategory.adapter = categoryAdapter
        initLongClick(categories)
    }

    private fun editCategory(category: Category) {
        val ft = fragmentManager?.beginTransaction()
        val fragment = CreateCategoryDialogFragment(category)
        fragment.show(ft, "")

        fragment.onResult = { newCategory -> viewModel.updateCategory(newCategory) }
    }

    private fun initLongClick(categories: List<Category>) {
        if (::categoryAdapter.isInitialized)
            categoryAdapter.onLongClick = { view, position ->
                showPopupEditRemove(view,
                        edit = { editCategory(categories[position]) },
                        remove = {
                            showDialog(R.string.d_remove_category, R.string.d_remove_category_are_you_sure,
                                    yes = { viewModel.removeCategory(categories[position]) })
                        })
            }
    }
}
