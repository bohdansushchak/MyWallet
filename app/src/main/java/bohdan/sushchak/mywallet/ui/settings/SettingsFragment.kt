package bohdan.sushchak.mywallet.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
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
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.internal.Constants
import bohdan.sushchak.mywallet.internal.Constants.CURRENCY_KEY_PREF
import bohdan.sushchak.mywallet.internal.getSavedCurrency
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
    private lateinit var preferences: SharedPreferences

    private val viewModelFactory: SettingsViewModelFactory by instance()
    private lateinit var viewModel: SettingsViewModel

    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SettingsViewModel::class.java)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        bindUI()
    }

    private fun bindUI() = launch {
        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)

        dividerItemDecoration.setDrawable(context!!.getDrawable(R.drawable.divider_black)!!)
        recyclerViewCategory.addItemDecoration(dividerItemDecoration)

        val categoryList = viewModel.categories.await()
        categoryList.observe(this@SettingsFragment, Observer { categories ->
            tvNoCategories.visibility = if (categories.isEmpty()) View.VISIBLE else View.GONE
            updateCategory(categories)
        })

        initViews()
    }

    private fun initViews() {
        context?.let { edCurrency.setText(getSavedCurrency(it)) }
        edCurrency.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                preferences.edit().apply {
                    putString(CURRENCY_KEY_PREF, edCurrency.text.toString().trim())
                }.apply()
                context?.let { hideKeyboard(it, v) }
            }
        }
    }

    private fun addNewCategory(list: List<CategoryEntity>) {
        if (list.size < Constants.MAX_CATEGORIES) {
            val ft = fragmentManager?.beginTransaction()
            val category = CategoryEntity.emptyCategoryEntity
            val fragment = CreateCategoryDialogFragment(category)
            fragment.show(ft, "")

            fragment.onResult = { newCategory -> viewModel.addCategory(newCategory) }
        } else {
            showDialog(R.string.d_title_max_categories, R.string.d_msg_max_categories)
        }
    }


    private fun updateCategory(categoryEntities: List<CategoryEntity>) {
        if (::categoryAdapter.isInitialized) {
            categoryAdapter.update(categoryEntities)
            categoryAdapter.notifyDataSetChanged()
            initLongClick(categoryEntities)
            initButton(categoryEntities)
            return
        }

        categoryAdapter = CategoryAdapter(context!!, categoryEntities)
        val linearLayout = LinearLayoutManager(context!!)

        recyclerViewCategory.layoutManager = linearLayout
        recyclerViewCategory.adapter = categoryAdapter
        initLongClick(categoryEntities)
        initButton(categoryEntities)
    }

    private fun initButton(list: List<CategoryEntity>) {
        btnAddCategory.setOnClickListener { addNewCategory(list) }
    }

    private fun editCategory(categoryEntity: CategoryEntity) {
        val ft = fragmentManager?.beginTransaction()
        val fragment = CreateCategoryDialogFragment(categoryEntity)
        fragment.show(ft, "")

        fragment.onResult = { newCategory -> viewModel.updateCategory(newCategory) }
    }

    private fun initLongClick(categoryEntities: List<CategoryEntity>) {
        if (::categoryAdapter.isInitialized)
            categoryAdapter.onLongClick = { view, position ->
                showPopupEditRemove(view,
                    edit = { editCategory(categoryEntities[position]) },
                    remove = {
                        showDialog(R.string.d_remove_category, R.string.d_remove_category_are_you_sure,
                            yes = { viewModel.removeCategory(categoryEntities[position]) })
                    })
            }
    }
}
