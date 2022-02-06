package bohdan.sushchak.mywallet.ui.create_order


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.data.model.CategoryProduct
import bohdan.sushchak.mywallet.data.model.CategoryWithListProducts
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.*
import bohdan.sushchak.mywallet.internal.Constants.DETECTION_PRODUCTS_CODE
import bohdan.sushchak.productsdetector.model.AddedProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

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

    val detectedProducts: LiveData<List<AddedProduct>>
        get() = _detectedProducts

    val orderDate: LiveData<Date>
        get() = _orderDate

    var orderSavingType = OrderSavingType.CREATE
    var initOrder: OrderEntity? = null
    //endregion

    private val _totalPrice by lazy { MutableLiveData<Double>() }
    private val _orderDate by lazy { MutableLiveData<Date>() }
    private val _productList by lazy { MutableLiveData<MutableList<ProductEntity>>() }
    private val _recommendCategory by lazy { MutableLiveData<CategoryEntity>() }
    private val _categoryProductList by lazy { MutableLiveData<MutableList<CategoryWithListProducts>>() }
    private val _detectedProducts by lazy { MutableLiveData<List<AddedProduct>>() }
    var selectedCategory = CategoryEntity.emptyCategoryEntity

    init {
        _categoryProductList.value = mutableListOf()
        _totalPrice.value = ZERO
        _orderDate.value = Calendar.getInstance().getOnlyDate()

        myWalletRepository.onActivityResultConsumer { requestCode, intent ->
            if (requestCode != DETECTION_PRODUCTS_CODE) {
                return@onActivityResultConsumer
            }
            intent?.let {
                val detectedProducts = it.extras
                    ?.getParcelableArrayList<AddedProduct>("detectedProducts")?.toList()

                _detectedProducts.postValue(detectedProducts)
            }
        }
    }

    fun initOrder(order: OrderEntity) {
        if (order.orderId == null)
            throw IllegalStateException("Order orderId can't be null")

        orderSavingType = OrderSavingType.UPDATE
        initOrder = order

        GlobalScope.launch(Dispatchers.IO) {
            val categoryProduct = myWalletRepository.getProductCategoryList(order.orderId!!)
            val groupedProducts = groupProductsByCategory(categoryProduct)
            val products = categoryProduct.map { it.productEntity }

            _productList.postValue(products.toMutableList())
            _categoryProductList.postValue(groupedProducts.toMutableList())
            _totalPrice.postValue(order.price)
            _orderDate.postValue(Date(order.date))
        }
    }

    fun searchCategoryCount(productTitle: String) {
        if (productTitle.isEmpty())
            return
        GlobalScope.launch(Dispatchers.IO) {
            val categoryCountList = myWalletRepository.getCategoryCountByProductTitle(productTitle)
            val categoryCount = categoryCountList.maxBy { it.count }

            if (categoryCount?.categoryId != null) {
                val category =
                    let { myWalletRepository.getCategoryById(categoryCount.categoryId!!) }
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
            val categoryProductObj =
                CategoryWithListProducts(selectedCategory, mutableListOf(product))
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
        GlobalScope.launch(Dispatchers.IO) {
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

    fun saveOrder(title: String) {
        if (_orderDate.value == null) throw IllegalArgumentException("Order date can't be null")
        GlobalScope.launch(Dispatchers.IO) {
            val order = OrderEntity(
                orderId = initOrder?.orderId,
                title = title,
                date = _orderDate.value!!.time,
                price = totalPrice.value ?: ZERO
            )

            if (productList.value == null) throw IllegalStateException("Product list can't be empty")

            when (orderSavingType) {
                OrderSavingType.CREATE -> {
                    myWalletRepository.createOrderWithProducts(order, productList.value!!)
                }

                OrderSavingType.UPDATE -> {
                    myWalletRepository.updateOrderWithProducts(order, productList.value!!)
                }
            }
            orderSavingType = OrderSavingType.CREATE
        }
    }

    fun isProductListEmpty(): Boolean {
        if (productList.value != null) {
            return productList.value!!.isEmpty()
        }
        return true
    }

    private fun groupProductsByCategory(productCategoryList: List<CategoryProduct>): List<CategoryWithListProducts> {
        val productsSet = HashSet<ProductEntity>()
        val categorySet = HashSet<CategoryEntity>()

        productCategoryList.forEach {
            productsSet.add(it.productEntity)
            categorySet.add(it.categoryEntity)
        }

        return categorySet.map { categoryEntity ->
            val products =
                productsSet.filter { it.categoryId == categoryEntity.categoryId }.toMutableList()
            CategoryWithListProducts(categoryEntity = categoryEntity, products = products)
        }
    }

    fun removeAddedItemByName(name: String) {
        val products = _detectedProducts.value

        products?.let {
            val productToDelete = products.find { it.product == name }
            if (productToDelete != null) {
                val newProducts = products.toMutableList()
                newProducts.remove(productToDelete)
                _detectedProducts.postValue(newProducts)
            }
        }
    }

    fun setOrderDate(date: Date) {
        _orderDate.postValue(date)
    }
}

enum class OrderSavingType {
    CREATE,
    UPDATE
}
