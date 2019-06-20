package bohdan.sushchak.mywallet.ui.create_order

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.adapters.ExpandableListProductAdapter
import bohdan.sushchak.mywallet.adapters.MySpinnerAdapter
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.data.model.CategoryWithListProducts
import bohdan.sushchak.mywallet.internal.*
import bohdan.sushchak.mywallet.internal.view.startFadeInAnimation
import bohdan.sushchak.mywallet.ui.base.BaseFragment
import bohdan.sushchak.productsdetector.ui.CameraActivity
import kotlinx.android.synthetic.main.create_order_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*

class CreateOrderFragment : BaseFragment(), KodeinAware {

    override val kodein by closestKodein()

    private lateinit var adapter: ExpandableListProductAdapter

    private val viewModelFactory: CreateOrderViewModelFactory by instance()
    private lateinit var viewModel: CreateOrderViewModel

    private val args by navArgs<CreateOrderFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_order_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CreateOrderViewModel::class.java)

        args.order?.let { viewModel.initOrder(it) }

        ibtnAddProduct.setOnClickListener { addProduct() }
        btnClearAll.setOnClickListener { clearProductList() }
        btnSaveOrder.setOnClickListener { saveOrder() }
        tvOrderDate.setOnClickListener {
            pickDate(
                viewModel.orderDate,
                maxDate = Calendar.getInstance().time.time
            ) { setDate(it) }
        }

        context?.let { hideKeyboardIfFocusLost(it, edProductPrice, edProductTitle) }

        bindUI()
    }

    @SuppressLint("SetTextI18n")
    private fun bindUI() = launch {
        val calendar = Calendar.getInstance()
        setDate(calendar.getOnlyDate())

        edProductPrice.filters = arrayOf(DecimalDigitsInputFilter(5, 2))
        initTextWatcher(edProductTitle)

        viewModel.categoryProductList.observe(this@CreateOrderFragment, Observer { categoryProductList ->
            updateCategoryList(categoryProductList)
        })

        val categoryList = viewModel.categories.await()

        categoryList.observe(this@CreateOrderFragment, Observer { categories ->
            spinnerUpdate(categories)
        })

        viewModel.totalPrice.observe(this@CreateOrderFragment, Observer {
            updateTotalPrice(it)
        })

        viewModel.recommendCategory.observe(this@CreateOrderFragment, Observer { category ->
            if (category != null)
                updateSpinner(category)
        })

        ibtnMlKit.setOnClickListener {
            val intent = Intent(context, CameraActivity::class.java)

            startActivity(intent)
        }
    }

    private fun updateTotalPrice(totalPrice: Double) {
        val currency = context?.let {
            getSavedCurrency(it)
        }

        tvTotalPrice.text = "${totalPrice.myToString()} $currency"
        tvTotalPrice.startFadeInAnimation(context)
    }

    private fun initTextWatcher(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val str = s.toString()
                if (str.isNotEmpty())
                    viewModel.searchCategoryCount(s.toString())
            }
        })
    }

    private fun updateCategoryList(categoryWithProduct: MutableList<CategoryWithListProducts>) {
        adapter = ExpandableListProductAdapter(context!!, categoryWithProduct)
        adapter.onLongClick = { view, product ->
            showPopupEditRemove(view,
                edit = {
                    //TODO add removing product
                },
                remove = {
                    showDialog(R.string.d_remove_product, R.string.d_remove_product_are_you_sure, yes = {
                        viewModel.removeProduct(product)
                    })

                })
        }

        expListViewProducts.setAdapter(adapter)
    }

    private fun addProduct() {
        if (!isValid())
            return

        val title = edProductTitle.text.toString().trim()
        val price = try {
            edProductPrice.text.toString().toDouble()
        } catch (e: Exception) {
            ZERO
        }

        val categoryId = viewModel.selectedCategory.categoryId

        val product = ProductEntity(
            productId = null,
            title = title,
            price = price,
            categoryId = categoryId,
            orderId = 0
        )

        viewModel.addProduct(product)

        clearDataInViews()
    }

    private fun updateSpinner(categoryEntity: CategoryEntity) = launch {

        val index = viewModel.categories.await().value?.indexOf(categoryEntity)
        if (index != null)
            spCategory.setSelection(index)
    }

    private fun clearProductList() {
        if (viewModel.productList.value == null)
            makeToast(R.string.t_product_list_is_empty)
        else {
            if (viewModel.productList.value!!.size > 0)
                showDialog(R.string.d_clear_products,
                    R.string.d_clear_products_are_you_sure, yes = {
                        viewModel.clearProductList()
                    })
            else makeToast(R.string.t_product_list_is_empty)
        }
    }

    private fun saveOrder() {
        if (viewModel.isProductListEmpty())
            makeToast(R.string.t_product_list_cant_be_empty)
        else {
            showEntryDialog(R.string.d_order_title, R.string.d_order_title_please_enter, yes = { strMsg ->
                viewModel.saveOrder(strMsg)
                fragmentManager?.popBackStack()
            })
        }
    }

    private fun spinnerUpdate(categoryEntities: List<CategoryEntity>) {
        val adapter = MySpinnerAdapter(context!!, categoryEntities)

        spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                viewModel.selectedCategory = categoryEntities[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                viewModel.selectedCategory = CategoryEntity.emptyCategoryEntity
            }
        }

        spCategory.adapter = adapter
        spCategory.setSelection(categoryEntities.indexOf(viewModel.selectedCategory))
    }

    private fun isValid(): Boolean {
        val title = edProductTitle.text
        val price = edProductPrice.text

        when {
            title.isNullOrEmpty() -> {
                makeToast(R.string.t_please_enter_product_name)
                return false
            }

            price.isNullOrEmpty() -> {
                makeToast(R.string.t_please_enter_product_price)
                return false
            }

            cantParsePrice(price) -> {
                makeToast(R.string.t_price_should_be_a_number)
                return false
            }

            (viewModel.selectedCategory == CategoryEntity.emptyCategoryEntity) -> {
                makeToast(R.string.t_please_select_a_category)
                return false
            }
        }

        return true
    }

    private fun clearDataInViews() {
        edProductPrice.text = null
        edProductTitle.text = null
        //TODO add spCategory nothing selection
        //spCategory.setSelection(-1)
    }

    //TODO edit product ???
    /*
    private fun setDataViewOfProduct(product: ProductEntity){
        edProductTitle.setText(product.titleResId)
        edProductPrice.setText(product.price.toString())
        viewModel.categories.await().value
    }*/

    private fun cantParsePrice(price: Editable) = try {
        price.toString().toDouble()
        false
    } catch (e: Exception) {
        true
    }

    private fun setDate(date: Date) {
        tvOrderDate.text = formatDate(date, Constants.DATE_FORMAT)
        viewModel.orderDate = date.time

    }
}
