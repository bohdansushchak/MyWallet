package bohdan.sushchak.mywallet.ui.create_order


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.data.model.CategoryWithProducts
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val ZERO = 0.0

class CreateOrderViewModel(private val myWalletRepository: MyWalletRepository)
    : ViewModel() {

    var productList: MutableLiveData<MutableList<ProductEntity>> = MutableLiveData()
    val categories by lazyDeffered { myWalletRepository.getCategories() }
    val totalPrice: MutableLiveData<Double> = MutableLiveData()
    val foundedCategoryEntity: MutableLiveData<CategoryEntity> = MutableLiveData()

    val categoryProductList: MutableLiveData<MutableList<CategoryWithProducts>> = MutableLiveData()

    var selectedCategory = CategoryEntity.emptyCategoryEntity

    init {
        productList.value = mutableListOf()
        categoryProductList.value = mutableListOf()
        totalPrice.value = ZERO
    }

    fun searchCategoryCount(productTitle: String) {
        GlobalScope.launch {
            if (productTitle.isEmpty())
                return@launch

            val categoryCountList = myWalletRepository.getCategoryCountByProductTitle(productTitle)

            val categoryCount = categoryCountList.maxBy { it.count }

            if (categoryCount?.categoryId != null) {
                val category = let { myWalletRepository.getCategoryById(categoryCount.categoryId!!) }
                foundedCategoryEntity.postValue(category)
            }
        }
    }

    fun addProduct(product: ProductEntity) {
        val list = mutableListOf<ProductEntity>()
        list.addAll(productList.value?.toList() ?: listOf())
        list.add(product)

        val price = totalPrice.value!! + product.price

        val newCategoryProductList = categoryProductList.value!!

        if (!newCategoryProductList.containCategory(selectedCategory)) {
            val categoryProductObj = CategoryWithProducts(selectedCategory, mutableListOf(product))
            newCategoryProductList.add(categoryProductObj)
        } else {
            val index = newCategoryProductList.indexOfCategory(selectedCategory)
            newCategoryProductList[index].products.add(product)
        }

        productList.postValue(list)
        totalPrice.postValue(price)
        categoryProductList.postValue(newCategoryProductList)
    }

    fun removeProduct(product: ProductEntity) {
        GlobalScope.launch {
            val list = productList.value
            list?.remove(product)

            val price = totalPrice.value!! - product.price

            val newCategoryProductList = categoryProductList.value!!
            newCategoryProductList.removeProduct(product)

            productList.postValue(list)
            totalPrice.postValue(price)
            categoryProductList.postValue(newCategoryProductList)
        }
    }

    fun clearProductList() {
        productList.postValue(mutableListOf())
        totalPrice.postValue(ZERO)
    }

    fun addOrder(date: Long, title: String) {
        if (isProductListEmpty())
            throw EmptyProductListException()

        GlobalScope.launch {
            val order = OrderEntity(id = null,
                    title = title,
                    date = date,
                    price = totalPrice.value ?: ZERO)

            myWalletRepository.createOrderWithProducts(order, productList.value?.toList()
                    ?: listOf())
        }
    }

    fun isProductListEmpty() = productList.value!!.isEmpty()
}
