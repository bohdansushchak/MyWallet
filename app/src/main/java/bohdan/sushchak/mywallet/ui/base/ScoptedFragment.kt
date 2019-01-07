package bohdan.sushchak.mywallet.ui.base

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class ScoptedFragment : Fragment(), CoroutineScope {

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

    protected fun showDialog (title: String, msg: String, yes: (() -> Unit)? = null, cancel: (() -> Unit)? = null) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(title)
        alertDialog.setMessage(msg)
        alertDialog.setPositiveButton("Yes") { _, _ ->
            yes?.invoke()
        }
        alertDialog.setNegativeButton("Cancel") { _, _ ->
            cancel?.invoke()
        }

        val dialog = alertDialog.create()
        dialog.show()
    }

    protected fun showDialog(title: Int, msg: Int, yes: (() -> Unit)? = null, cancel: (() -> Unit)? = null) {
        showDialog(getString(title), getString(msg), yes, cancel)
    }
}