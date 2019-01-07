package bohdan.sushchak.mywallet.ui.settings

import androidx.lifecycle.ViewModel;
import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.lazyDeffered
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsViewModel(private val myWalletRepository: MyWalletRepository) : ViewModel() {

    val categories by lazyDeffered { myWalletRepository.getCategories() }

    fun addCategory(category: Category) {
        GlobalScope.launch {
            myWalletRepository.addCategory(category)
        }
    }

    fun removeCategory(category: Category) {
        GlobalScope.launch {
            myWalletRepository.removeCategory(category)
        }
    }

    fun updateCategory(category: Category){
        GlobalScope.launch {
            myWalletRepository.updateCategory(category)
        }
    }

}
