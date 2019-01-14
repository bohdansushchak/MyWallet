package bohdan.sushchak.mywallet.ui.graph

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import bohdan.sushchak.mywallet.data.model.CategoryPrice
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class GraphViewModel(private val myWalletRepository: MyWalletRepository) : ViewModel() {

     var categoriesTotalPrice: MutableLiveData<List<CategoryPrice>> = MutableLiveData()
     //val categoriesTotalPrice by lazyDeffered{myWalletRepository.getCategoriesPrice(1547334000000, 1547506800000)}

    init {
        categoriesTotalPrice.value = listOf()
    }

    fun updateCategories(startDate: Long, endDate: Long ): Unit {
        GlobalScope.launch(Dispatchers.IO) {

            val categories = myWalletRepository.getCategoriesPrice(startDate, endDate)
            categoriesTotalPrice.postValue(categories)
        }
    }
}
