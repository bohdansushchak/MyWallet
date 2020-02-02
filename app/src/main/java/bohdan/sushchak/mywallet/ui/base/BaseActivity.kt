package bohdan.sushchak.mywallet.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    protected fun showAlertDialog(title: String, msg: String, yes: (() -> Unit)? = null) {
        bohdan.sushchak.mywallet.internal.showAlertDialog(this, title, msg, yes)
    }

    protected fun showAlertDialog(title: Int, msg: Int, yes: (() -> Unit)? = null) {
        showAlertDialog(getString(title), getString(msg), yes)
    }
}