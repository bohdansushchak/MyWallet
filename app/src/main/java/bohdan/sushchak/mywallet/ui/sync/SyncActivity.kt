package bohdan.sushchak.mywallet.ui.sync

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.internal.SyncType
import bohdan.sushchak.mywallet.ui.MainActivity
import bohdan.sushchak.mywallet.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_sync.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

import org.kodein.di.generic.instance

class SyncActivity : BaseActivity(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val viewModelFactory: SyncViewModelFactory by instance()
    private lateinit var viewModel: SyncViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SyncViewModel::class.java)

        initSyncType()
        bindUI()
    }

    private fun initSyncType() {
        if (intent.hasExtra("syncType")) {
            if (intent.extras!!["syncType"] == null) {
                viewModel.startSynchronization()
            } else {
                viewModel.startSynchronization(intent.extras!!["syncType"] as SyncType)
            }
        } else {
            viewModel.startSynchronization()
        }
    }

    private fun bindUI() = launch {
        viewModel.syncType.observe(this@SyncActivity, Observer {
            when (it) {
                SyncType.EQUALS -> {
                    startActivity<MainActivity>()
                    finish()
                }
                SyncType.FIRESTORE_LESS -> tvSyncType.setText(R.string.tv_sending)
                SyncType.LOCAL_LESS -> tvSyncType.setText(R.string.tv_getting)
                else -> tvSyncType.text = "Hmm, some error"
            }
        })
        viewModel.syncText.observe(this@SyncActivity, Observer {
            loadingText.text = it
        })
    }
}
