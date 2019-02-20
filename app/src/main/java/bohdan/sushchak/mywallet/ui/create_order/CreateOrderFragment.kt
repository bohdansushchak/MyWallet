package bohdan.sushchak.mywallet.ui.create_order

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable

import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.adapters.ExpandableListProductAdapter
import bohdan.sushchak.mywallet.adapters.MySpinnerAdapter

import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.data.model.CategoryWithProducts
import bohdan.sushchak.mywallet.internal.Constants
import bohdan.sushchak.mywallet.internal.DecimalDigitsInputFilter
import bohdan.sushchak.mywallet.internal.formatDate
import bohdan.sushchak.mywallet.internal.parseDate
import bohdan.sushchak.mywallet.ui.base.BaseFragment
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.create_order_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(CreateOrderViewModel::class.java)

        ibtnAddProduct.setOnClickListener { addProduct() }
        btnClearAll.setOnClickListener { clearProductList() }
        btnSaveOrder.setOnClickListener { saveOrder() }
        tvOrderDate.setOnClickListener {
            val date = tvOrderDate.text
            pickDate(date)
        }

        bindUI()
    }

    @SuppressLint("SetTextI18n")
    private fun bindUI() = launch {

        tvOrderDate.text = formatDate(Date(), Constants.DATE_FORMAT)

        edProductPrice.filters = arrayOf(DecimalDigitsInputFilter(5,2))
        initTextWatcher(edProductTitle)

        viewModel.categoryProductList.observe(this@CreateOrderFragment, Observer { categoryProductList ->
            updateCategoryList(categoryProductList)
        })

        val categoryList = viewModel.categories.await()

        categoryList.observe(this@CreateOrderFragment, Observer { categories ->
            spinnerUpdate(categories)
        })

        viewModel.totalPrice.observe(this@CreateOrderFragment, Observer { totalPrice ->
            tvTotalPrice.text = "${getString(R.string.total)} $totalPrice"
        })

        viewModel.foundedCategoryEntity.observe(this@CreateOrderFragment, Observer { category ->
            if (category != null)
                updateSpinner(category)
        })
    }

    private fun initTextWatcher(editText: EditText){

        editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val str = s.toString()
                if(str.isNotEmpty())
                    viewModel.searchCategoryCount(s.toString())
            }
        })
    }

    private fun updateCategoryList(categoryWithProduct: MutableList<CategoryWithProducts>){

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

                }) }

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

        val categoryId = viewModel.selectedCategory.id

        val product = ProductEntity(
                id = null,
                title = title,
                price = price,
                categoryId = categoryId,
                orderId = 0
        )

        viewModel.addProduct(product)

        clearDataInViews()
    }

    private fun updateSpinner(categoryEntity: CategoryEntity) = launch{

        val index = viewModel.categories.await().value?.indexOf(categoryEntity)
        if(index != null)
            spCategory.setSelection(index)
    }

    private fun clearProductList() {
        if (viewModel.productList.value!!.size > 0)
            showDialog(R.string.d_clear_products,
                    R.string.d_clear_products_are_you_sure, yes = {
                viewModel.clearProductList()
            })
        else makeToast(R.string.t_product_list_is_empty)
    }

    private fun saveOrder() {

        val date = parseDate(tvOrderDate.text.toString(), Constants.DATE_FORMAT)

        if (viewModel.isProductListEmpty())
            makeToast(R.string.t_product_list_cant_be_empty)
        else {
            showEntryDialog(R.string.d_order_title, R.string.d_order_title_please_enter, yes = { strMsg ->

                Log.d("DATE", date.time.toString())

                viewModel.addOrder(date.time, strMsg)
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

    private fun pickDate(dateCharSequence: CharSequence) {
        val calendar = Calendar.getInstance()

        if (dateCharSequence.isNotEmpty()) {
            val date = parseDate(dateCharSequence.toString(), Constants.DATE_FORMAT)
            calendar.clear()
            calendar.time = date
        }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { _, year_, monthOfYear, dayOfMonth ->

            calendar.clear()
            calendar.set(Calendar.YEAR, year_)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            tvOrderDate.text = formatDate(calendar.time, Constants.DATE_FORMAT)

            Log.d("DATE", calendar.time.time.toString())

        }, year, month, day)
        dpd.show()
    }
}
