package bohdan.sushchak.mywallet.ui.create_order

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.adapters.ProductAdapter
import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.data.db.entity.Product
import bohdan.sushchak.mywallet.ui.base.ScoptedFragment
import kotlinx.android.synthetic.main.create_order_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CreateOrderFragment : ScoptedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private lateinit var adapter: ProductAdapter

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

        bindUI()
    }

    private fun bindUI() = launch {

        viewModel.productList.observe(this@CreateOrderFragment, Observer { products ->
            updateCategoryList(products.toList())
        })

        val categoryList = viewModel.categories.await()

        categoryList.observe(this@CreateOrderFragment, Observer { categories ->
            spinnerUpdate(categories)
        })

        viewModel.totalPrice.observe(this@CreateOrderFragment, Observer { totalPrice ->
            tvTotalPrice.text = "Total: $totalPrice"
        })
    }

    private fun updateCategoryList(products: List<Product>) {
        val linearLayout = LinearLayoutManager(context!!)
        linearLayout.orientation = RecyclerView.VERTICAL

        adapter = ProductAdapter(context!!, products)

        adapter.onLongClick = { view, position ->
            showPopup(view, products[position])
        }

        recyclerViewProducts.adapter = adapter
        recyclerViewProducts.layoutManager = linearLayout
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

        val date = System.currentTimeMillis()
        viewModel.addOrder(date)
    }

    private fun spinnerUpdate(categories: List<Category>) {

        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

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

    private fun showPopup(view: View, product: Product) {

        val popupMenu = PopupMenu(context, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.category_popup_menu, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.popupEdit -> {
                    //setDataViewOfProduct(product)
                    return@setOnMenuItemClickListener true
                }

                R.id.popupRemove -> {

                    showDialog("Remove product", "Are sure you to remove product", yes = {
                        viewModel.removeProduct(product)
                    })

                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }
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
    private fun makeToast(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

    private fun cantParsePrice(price: Editable) = try {
        price.toString().toDouble()
        false
    } catch (e: Exception) {
        true
    }
}
