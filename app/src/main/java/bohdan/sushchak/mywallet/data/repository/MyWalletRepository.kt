package bohdan.sushchak.mywallet.data.repository

import androidx.lifecycle.LiveData
import bohdan.sushchak.mywallet.data.db.entity.Category

interface MyWalletRepository {

    suspend fun getCategories(): LiveData<List<Category>>
}