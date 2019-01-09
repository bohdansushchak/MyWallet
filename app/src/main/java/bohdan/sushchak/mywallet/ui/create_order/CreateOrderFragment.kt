package bohdan.sushchak.mywallet.ui.create_order

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.adapters.ExpandableListProductAdapter
import bohdan.sushchak.mywallet.adapters.MySpinnerAdapter

import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.data.db.entity.Product
import bohdan.sushchak.mywallet.data.db.model.CategoryWithProducts
import bohdan.sushchak.mywallet.internal.Constants
import bohdan.sushchak.mywallet.internal.parseDate
import bohdan.sushchak.mywallet.ui.base.BaseFragment
import kotlinx.android.synthetic.main.create_order_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
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

    private fun bindUI() = launch {

        setDate(Date()) //set current date

        viewModel.categoryProductList.observe(this@CreateOrderFragment, Observer { categoryProductList ->
            updateCategoryList(categoryProductList)
        })

        val categoryList = viewModel.categories.await()

        categoryList.observe(this@CreateOrderFragment, Observer { categories ->
            spinnerUpdate(categories)
        })

        viewModel.totalPrice.observe(this@CreateOrderFragment, Observer { totalPrice ->
            tvTotalPrice.text = "Total: $totalPrice"
        })
    }
/*
    private fun updateCategoryList(products: List<Product>) {
        val linearLayout = LinearLayoutManager(context!!)
        linearLayout.orientation = RecyclerView.VERTICAL

        adapter = ProductAdapter(context!!, products)

        adapter.onLongClick = { view, position ->
            showPopupEditRemove(view,
                    edit = {
                        //TODO add removing product
                    },
                    remove = {
                        showDialog("Remove product", "Are sure you to remove product", yes = {
                            viewModel.removeProduct(products[position])
                        })

                    })
        }

        expListViewProducts.adapter = adapter
        expListViewProducts.layoutManager = linearLayout
    }
*/

    private fun updateCategoryList(categoryWithProduct: MutableList<CategoryWithProducts>){

        adapter = ExpandableListProductAdapter(context!!, categoryWithProduct)

        adapter.onLongClick = { view, product ->
            showPopupEditRemove(view,
                edit = {
                    //TODO add removing product
                },
                remove = {
                    showDialog("Remove product", "Are sure you to remove product", yes = {
                        viewModel.removeProduct(product)
                    })

                }) }

        expListViewProducts.setAdapter(adapter)
    }
    private fun addProduct() {
        if (!isValid())
            return

        val title = edProductTitle.text.toString()
        val price = try {
            edProductPrice.text.toString().toDouble()
        } catch (e: Exception) {
            ZERO
        }

        val categoryId = viewModel.selectedCategory.id

        val product = Product(
                id = null,
                title = title,
                price = price,
                categoryId = categoryId,
                orderId = 0
        )

        viewModel.addProduct(product)

        clearDataInViews()
    }

    private fun clearProductList() {
        if (viewModel.productList.value!!.size > 0)
            showDialog("Clear all products",
                    "Are you sure to clear all product from list", yes = {
                viewModel.clearProductList()
            })
        else makeToast("Product list is empty")
    }

    private fun saveOrder() {

        val date = parseDate(tvOrderDate.text.toString())

        if (viewModel.isProductListEmpty())
            makeToast("Product list can't be empty")
        else {
            showEntryDialog("Order title", "Enter order title", yes = { strMsg ->
                viewModel.addOrder(date.time, strMsg)
                fragmentManager?.popBackStack()
            })
        }
    }

    private fun spinnerUpdate(categories: List<Category>) {

        val adapter = MySpinnerAdapter(context!!, R.layout.category_item_spinner, categories)
        //adapter.setDropDownViewResource(R.layout.category_item_spinner)

        spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                viewModel.selectedCategory = categories[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                viewModel.selectedCategory = Category.emptyCategory
            }
        }

        spCategory.adapter = adapter
        spCategory.setSelection(categories.indexOf(viewModel.selectedCategory))
    }

    private fun isValid(): Boolean {
        val title = edProductTitle.text
        val price = edProductPrice.text

        when {
            title.isNullOrEmpty() -> {
                makeToast("Please enter product name")
                return false
            }

            price.isNullOrEmpty() -> {
                makeToast("Please enter product price")
                return false
            }

            cantParsePrice(price) -> {
                makeToast("Price should be a number")
                return false
            }

            (viewModel.selectedCategory == Category.emptyCategory) -> {
                makeToast("Please select a category")
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
    private fun setDataViewOfProduct(product: Product){
        edProductTitle.setText(product.title)
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
            val date = parseDate(dateCharSequence.toString())
            calendar.time = date
        }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { _, year_, monthOfYear, dayOfMonth ->

            calendar.set(Calendar.YEAR, year_)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            setDate(calendar.time)

        }, year, month, day)
        dpd.show()
    }

    private fun setDate(date: Date) {
        val sdf = SimpleDateFormat(Constants.DATE_FORMAT)
        val formattedDate = sdf.format(date)

        tvOrderDate.text = formattedDate
    }
}
