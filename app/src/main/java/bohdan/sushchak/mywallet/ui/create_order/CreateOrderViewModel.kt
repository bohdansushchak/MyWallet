package bohdan.sushchak.mywallet.ui.create_order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.db.entity.Product
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.EmptyProductListException
import bohdan.sushchak.mywallet.internal.lazyDeffered
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val ZERO = 0.0

class CreateOrderViewModel(private val myWalletRepository: MyWalletRepository)
    : ViewModel() {

    var productList: MutableLiveData<MutableList<Product>> = MutableLiveData()
    val categories by lazyDeffered { myWalletRepository.getCategories() }
    val totalPrice: MutableLiveData<Double> = MutableLiveData()

    var selectedCategory = Category.emptyCategory

    init {
        productList.value = mutableListOf()
        totalPrice.value = ZERO
    }

    fun addProduct(product: Product) {
        val list = mutableListOf<Product>()
        list.addAll(productList.value?.toList() ?: listOf())
        list.add(product)

        val price = totalPrice.value!! + product.price

        productList.postValue(list)
        totalPrice.postValue(price)
    }

    fun removeProduct(product: Product) {
        val list = productList.value
        list?.remove(product)

        val price = totalPrice.value!! - product.price

        productList.postValue(list)
        totalPrice.postValue(price)
    }

    fun clearProductList() {
        productList.postValue(mutableListOf())
        totalPrice.postValue(ZERO)
    }

    fun addOrder(date: Long, title: String) {
        if (isProductListEmpty())
            throw EmptyProductListException()

            GlobalScope.launch {
                val order = Order(id = null,
                        title = title,
                        date = date,
                        price = totalPrice.value ?: ZERO)

                myWalletRepository.createOrderWithProducts(order, productList.value?.toList()
                        ?: listOf())
            }
    }

    fun isProductListEmpty() = productList.value!!.isEmpty()

}
