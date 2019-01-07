package bohdan.sushchak.mywallet.ui.settings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
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

        categoryAdapter.onLongClick = { view, position ->
            showPopup(view, categories[position])
        }
    }

    private fun showPopup(view: View, category: Category) {
        val popupMenu = PopupMenu(context, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.category_popup_menu, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.popupEdit -> {
                    editCategory(category)
                    return@setOnMenuItemClickListener true
                }

                R.id.popupRemove -> {

                    showDialog("Remove category", "Are you sure to remove category", yes = {
                        viewModel.removeCategory(category)
                    })

                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }
    }

    private fun editCategory(category: Category) {
        val ft = fragmentManager?.beginTransaction()
        val fragment = CreateCategoryDialogFragment(category)
        fragment.show(ft, "")

        fragment.onResult = { newCategory -> viewModel.updateCategory(newCategory) }
    }

}
