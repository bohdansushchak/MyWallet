package bohdan.sushchak.mywallet.ui.splash


import android.content.Intent
import android.os.Bundle
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.internal.SyncType
import bohdan.sushchak.mywallet.ui.MainActivity
import bohdan.sushchak.mywallet.ui.authorization.AuthorizationActivity
import bohdan.sushchak.mywallet.ui.base.BaseActivity
import bohdan.sushchak.mywallet.ui.sync.SyncActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


class SplashActivity : BaseActivity(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    val repository: MyWalletRepository by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            startActivity<AuthorizationActivity>()
            finish()
        } else {
            fetchSync()
        }
    }

    private fun fetchSync() = launch {
        val syncType = repository.databasesCompare()

        if (syncType == SyncType.EQUALS) {
            this@SplashActivity.runOnUiThread(Runnable {
                startActivity<MainActivity>()
                finish()
            })
            return@launch
        }

        this@SplashActivity.runOnUiThread(Runnable {
            val intent = Intent(this@SplashActivity, SyncActivity::class.java)
            intent.putExtra("syncType", syncType)
            startActivity(intent)
            finish()
        })
    }
}