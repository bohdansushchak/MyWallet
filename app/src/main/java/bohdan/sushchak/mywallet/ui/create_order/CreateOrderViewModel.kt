package bohdan.sushchak.mywallet.ui.create_order


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.data.model.CategoryWithListProducts
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

const val ZERO = 0.0

class CreateOrderViewModel(private val myWalletRepository: MyWalletRepository) : ViewModel() {

    //region public parameters
    val categories by lazyDeferred { myWalletRepository.getCategories() }

    val productList: LiveData<MutableList<ProductEntity>>
        get() = _productList

    val totalPrice: LiveData<Double>
        get() = _totalPrice

    val recommendCategory: LiveData<CategoryEntity>
        get() = _recommendCategory

    val categoryProductList: LiveData<MutableList<CategoryWithListProducts>>
    get() = _categoryProductList

    var orderDate: Long = 0
    //endregion

    private val _totalPrice by lazy { MutableLiveData<Double>() }
    private val _productList by lazy { MutableLiveData<MutableList<ProductEntity>>() }
    private val _recommendCategory by lazy { MutableLiveData<CategoryEntity>() }
    private val _categoryProductList by lazy { MutableLiveData<MutableList<CategoryWithListProducts>>() }
    var selectedCategory = CategoryEntity.emptyCategoryEntity

    init {
        //productList.value = mutableListOf()
        _categoryProductList.value = mutableListOf()
        _totalPrice.value = ZERO
    }

    fun initOrder(order: OrderEntity){
        if(order.orderId == null)
            throw IllegalStateException("Order categoryId can't be null")

        GlobalScope.launch(Dispatchers.IO) {
           val list = myWalletRepository.getProductCategoryList(order.orderId!!)

            list.forEach {
                Log.d("Product", it.toString())
            }

            /*
            val productList = list.map {
                ProductEntity(
                    categoryId = it.categoryId,
                    categoryTitle = it.productTitle,
                    price = it.price,
                    categoryId = it.
                )
            }*/
            
            //_categoryProductList.postValue(list.toMutableList())
            _totalPrice.postValue(order.price)
        }
    }

    fun searchCategoryCount(productTitle: String) {
        GlobalScope.launch {
            if (productTitle.isEmpty())
                return@launch

            val categoryCountList = myWalletRepository.getCategoryCountByProductTitle(productTitle)

            val categoryCount = categoryCountList.maxBy { it.count }

            if (categoryCount?.categoryId != null) {
                val category = let { myWalletRepository.getCategoryById(categoryCount.categoryId!!) }
                _recommendCategory.postValue(category)
            }
        }
    }

    fun addProduct(product: ProductEntity) {
        val list = mutableListOf<ProductEntity>().apply {
            addAll(productList.value?.toList() ?: listOf())
            add(product)
        }

        val price = totalPrice.value!!.myPlus(product.price)

        val newCategoryProductList = _categoryProductList.value ?: mutableListOf()

        if (!newCategoryProductList.containCategory(selectedCategory)) {
            val categoryProductObj = CategoryWithListProducts(selectedCategory, mutableListOf(product))
            newCategoryProductList.add(categoryProductObj)
        } else {
            val index = newCategoryProductList.indexOfCategory(selectedCategory)
            newCategoryProductList[index].products.add(product)
        }

        _productList.postValue(list)
        _totalPrice.postValue(price)
        _categoryProductList.postValue(newCategoryProductList)
    }

    fun removeProduct(product: ProductEntity) {
        GlobalScope.launch {
            val list = productList.value
            list?.remove(product)

            val price = totalPrice.value!!.myMinus(product.price)

            val newCategoryProductList = _categoryProductList.value ?: mutableListOf()
            newCategoryProductList.removeProduct(product)

            _productList.postValue(list)
            _totalPrice.postValue(price)
            _categoryProductList.postValue(newCategoryProductList)
        }
    }

    fun clearProductList() {
        _productList.postValue(mutableListOf())
        _categoryProductList.postValue(mutableListOf())
        _totalPrice.postValue(ZERO)
    }

    fun addOrder(title: String) {
        GlobalScope.launch {
            if(orderDate == 0L) throw IllegalArgumentException("Order date can't be 0")

            val order = OrderEntity(
                orderId = null,
                title = title,
                date = orderDate,
                price = totalPrice.value ?: ZERO
            )

            myWalletRepository.createOrderWithProducts(
                order, productList.value?.toList()
                    ?: listOf()
            )
        }
    }

    fun isProductListEmpty(): Boolean  {
        if (productList.value != null){
           return productList.value!!.isEmpty()
        }
        return true
    }
}
