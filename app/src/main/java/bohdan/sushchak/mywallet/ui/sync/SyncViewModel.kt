package bohdan.sushchak.mywallet.ui.sync

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.SyncType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SyncViewModel(private val repository: MyWalletRepository) : ViewModel() {

    private val _syncText by lazy { MutableLiveData<String>() }
    private val _syncType by lazy { MutableLiveData<SyncType>() }

    val syncText: LiveData<String>
        get() = _syncText

    val syncType: LiveData<SyncType>
    get() = _syncType

    fun startSynchronization(syncType: SyncType? = null) {
        GlobalScope.launch(Dispatchers.IO) {
            val sync = syncType?:repository.databasesCompare()
            _syncType.postValue(sync)
            repository.synchronizeDatabases(sync) { _syncText.postValue(it)}

            _syncType.postValue(SyncType.EQUALS)
        }
    }


}