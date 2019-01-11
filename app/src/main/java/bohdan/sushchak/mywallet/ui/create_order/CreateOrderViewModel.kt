package bohdan.sushchak.mywallet.ui.create_order


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.data.db.entity.Date
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.db.entity.Product
import bohdan.sushchak.mywallet.data.db.model.CategoryWithProducts
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val ZERO = 0.0

class CreateOrderViewModel(private val myWalletRepository: MyWalletRepository)
    : ViewModel() {

    var productList: MutableLiveData<MutableList<Product>> = MutableLiveData()
    val categories by lazyDeffered { myWalletRepository.getCategories() }
    val totalPrice: MutableLiveData<Double> = MutableLiveData()
    val foundedCategory: MutableLiveData<Category> = MutableLiveData()

    val categoryProductList: MutableLiveData<MutableList<CategoryWithProducts>> = MutableLiveData()

    var selectedCategory = Category.emptyCategory

    init {
        productList.value = mutableListOf()
        categoryProductList.value = mutableListOf()
        totalPrice.value = ZERO
    }

    fun searchCategoryCount(productTitle: String){
        GlobalScope.launch {
            if(productTitle.length <= 0)
                return@launch

            val categoryCountList = myWalletRepository.getCategoryCountByProductTitle(productTitle)

            val categoryCount = categoryCountList.maxBy { it -> it.count }

            if(categoryCount != null){
                val category = myWalletRepository.getCategoryById(categoryCount.categoryId!!)
                foundedCategory.postValue(category)
            }
        }
    }

    fun addProduct(product: Product) {
        val list = mutableListOf<Product>()
        list.addAll(productList.value?.toList() ?: listOf())
        list.add(product)

        val price = totalPrice.value!! + product.price

        val newCategoryProductList = categoryProductList.value!!

        if (!newCategoryProductList.containCategory(selectedCategory)){
            val categoryProductObj = CategoryWithProducts(selectedCategory, mutableListOf(product))
            newCategoryProductList.add(categoryProductObj)
        }
        else{
            val index = newCategoryProductList.indexOfCategory(selectedCategory)
            newCategoryProductList[index].products.add(product)
        }

        productList.postValue(list)
        totalPrice.postValue(price)
        categoryProductList.postValue(newCategoryProductList)
    }

    fun removeProduct(product: Product) {
        GlobalScope.launch {
            val list = productList.value
            list?.remove(product)

            val price = totalPrice.value!! - product.price

            val newCategoryProductList = categoryProductList.value!!
            newCategoryProductList.removeProduct(product)

            productList.postValue(list)
            totalPrice.postValue(price)
            categoryProductList.postValue(newCategoryProductList)  }
    }

    fun clearProductList() {
        productList.postValue(mutableListOf())
        totalPrice.postValue(ZERO)
    }

    fun addOrder(date: Long, title: String) {
        if (isProductListEmpty())
            throw EmptyProductListException()

            GlobalScope.launch {
                var dateId = myWalletRepository.getDateId(date = date)
                if(dateId == null){
                    dateId = myWalletRepository.addDate(Date(null, date))
                }

                val order = Order(id = null,
                        title = title,
                        dateId = dateId,
                        price = totalPrice.value ?: ZERO)

                myWalletRepository.createOrderWithProducts(order, productList.value?.toList()
                        ?: listOf())
            }
    }

    fun isProductListEmpty() = productList.value!!.isEmpty()

}
