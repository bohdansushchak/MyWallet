package bohdan.sushchak.mywallet.ui.base

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import bohdan.sushchak.mywallet.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(), CoroutineScope {

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

    protected fun showDialog(title: String, msg: String, yes: (() -> Unit)? = null, cancel: (() -> Unit)? = null) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(title)
        alertDialog.setMessage(msg)
        alertDialog.setPositiveButton(R.string.btn_text_yes) { _, _ ->
            yes?.invoke()
        }

        if (cancel != null)
            alertDialog.setNegativeButton(R.string.btn_text_cancel) { _, _ ->
                cancel.invoke()
            }

        val dialog = alertDialog.create()
        dialog.show()
    }

    protected fun showDialog(title: Int, msg: Int, yes: (() -> Unit)? = null, cancel: (() -> Unit)? = null) {
        showDialog(getString(title), getString(msg), yes, cancel)
    }

    protected fun showEntryDialog(title: String, msg: String, yes: ((str: String) -> Unit)? = null, cancel: (() -> Unit)? = null) {
        val entryDialogBuilder = AlertDialog.Builder(context)
        entryDialogBuilder.setTitle(title)
        entryDialogBuilder.setMessage(msg)

        val input = EditText(context)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        input.layoutParams = lp
        lp.marginStart = 15
        lp.marginEnd = 15

        entryDialogBuilder.setView(input)

        entryDialogBuilder.setPositiveButton(R.string.btn_text_yes) { _, _ ->
            val inputStr = input.text.toString().trim()
            if (inputStr.isNotEmpty() && inputStr.isNotBlank())
                yes?.invoke(inputStr)
        }
        entryDialogBuilder.setNegativeButton(R.string.btn_text_cancel) { _, _ ->
            cancel?.invoke()
        }

        val entryDialog = entryDialogBuilder.create()
        entryDialog.show()
    }

    protected fun showEntryDialog(title: Int, msg: Int, yes: ((str: String) -> Unit)? = null, cancel: (() -> Unit)? = null) {
        showEntryDialog(getString(title), getString(msg), yes, cancel)
    }

    protected fun showPopupEditRemove(view: View, edit: (() -> Unit)? = null, remove: (() -> Unit)? = null) {
        val popupMenu = PopupMenu(context, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.category_popup_menu, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.popupEdit -> {
                    edit?.invoke()
                    return@setOnMenuItemClickListener true
                }

                R.id.popupRemove -> {

                    remove?.invoke()
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }
    }

    protected fun makeToast(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

    protected fun makeToast(msg: Int) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}