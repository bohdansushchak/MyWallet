package bohdan.sushchak.mywallet.ui.base

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.internal.getOnlyDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job

    private lateinit var mToast: Toast

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
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(title)
        alertDialog.setMessage(msg)
        alertDialog.setPositiveButton(R.string.btn_text_yes) { _, _ ->
            yes?.invoke()
        }

        val dialog = alertDialog.create()
        dialog.show()
    }

    protected fun showDialog(title: String, msg: String, yes: (() -> Unit)? = null, cancel: (() -> Unit)? = null) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(title)
        alertDialog.setMessage(msg)
        alertDialog.setPositiveButton(R.string.btn_text_yes) { _, _ ->
            yes?.invoke()
        }

        alertDialog.setNegativeButton(R.string.btn_text_cancel) { _, _ ->
            cancel?.invoke()
        }

        val dialog = alertDialog.create()
        dialog.show()
    }

    protected fun showDialog(title: Int, msg: Int, yes: (() -> Unit)? = null, cancel: (() -> Unit)? = null) {
        showDialog(getString(title), getString(msg), yes, cancel)
    }

    protected fun showAlertDialog(title: Int, msg: Int, yes: (() -> Unit)? = null) {
        showAlertDialog(getString(title), getString(msg), yes)
    }

    protected fun showEntryDialog(
        title: String,
        msg: String,
        yes: ((str: String) -> Unit)? = null,
        cancel: (() -> Unit)? = null
    ) {
        val entryDialogBuilder = AlertDialog.Builder(context)
        entryDialogBuilder.setTitle(title)
        entryDialogBuilder.setMessage(msg)

        val input = EditText(context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
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

    protected fun showEntryDialog(
        title: Int,
        msg: Int,
        yes: ((str: String) -> Unit)? = null,
        cancel: (() -> Unit)? = null
    ) {
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

    protected fun makeToast(msg: Int) {
        fun toastShow() {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
            mToast.show()
        }

        if (!::mToast.isInitialized) {
            toastShow()
            return
        }

        if (mToast.view.isShown)
            mToast.cancel()

        toastShow()
    }

    protected fun hideKeyboard(context: Context, view: View) {
        val imm = with(context) {
            getSystemService(Activity.INPUT_METHOD_SERVICE)
        } as InputMethodManager

        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected fun hideKeyboardIfFocusLost(context: Context, vararg view: TextView) {
        val imm = with(context) {
            getSystemService(Activity.INPUT_METHOD_SERVICE)
        } as InputMethodManager
        view.forEach {
            it.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus)
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }

    protected fun pickDate(actualTime: Long,
                           minDate: Long? = null,
                           maxDate: Long? = null,
                           callBack: ((date: Date) -> Unit)) {
        val calendar = Calendar.getInstance()

        if (0L != actualTime) {
            calendar.clear()
            calendar.time = Date(actualTime)
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        activity?.let { activity ->
            val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { _, year_, monthOfYear, dayOfMonth ->
                calendar.apply {
                    clear()
                    set(Calendar.YEAR, year_)
                    set(Calendar.MONTH, monthOfYear)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                callBack.invoke(calendar.getOnlyDate())
            }, year, month, day)
            minDate?.let{dpd.datePicker.minDate = it }
            maxDate?.let { dpd.datePicker.maxDate = it }
            dpd.show()
        }
    }

    protected fun getNavController(view: View): NavController {
        val navController = Navigation.findNavController(view)
        navController.setGraph(R.navigation.app_navigation)
        return navController
    }
}