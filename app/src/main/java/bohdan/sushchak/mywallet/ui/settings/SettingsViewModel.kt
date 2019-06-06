package bohdan.sushchak.mywallet.ui.settings

import androidx.lifecycle.ViewModel;
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.lazyDeferred
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsViewModel(private val myWalletRepository: MyWalletRepository) : ViewModel() {

    private val mAuth by lazy { FirebaseAuth.getInstance() }

    val categories by lazyDeferred { myWalletRepository.getCategories() }

    fun addCategory(categoryEntity: CategoryEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            myWalletRepository.addCategory(categoryEntity)
        }
    }

    fun removeCategory(categoryEntity: CategoryEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            myWalletRepository.removeCategory(categoryEntity)
        }
    }

    fun updateCategory(categoryEntity: CategoryEntity){
        GlobalScope.launch(Dispatchers.IO) {
            myWalletRepository.updateCategory(categoryEntity)
        }
    }

    fun signOut() {
        mAuth.signOut()
    }

}
