package bohdan.sushchak.mywallet.internal

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import bohdan.sushchak.mywallet.R

fun showAlertDialog(context: Context?, title: String, msg: String, yes: (() -> Unit)? = null) {
    val alertDialog = AlertDialog.Builder(context)
    alertDialog.setTitle(title)
    alertDialog.setMessage(msg)
    alertDialog.setPositiveButton(R.string.btn_text_yes) { _, _ ->
        yes?.invoke()
    }

    val dialog = alertDialog.create()
    dialog.show()
}

fun showDialog(
    context: Context?,
    title: String,
    msg: String,
    yes: (() -> Unit)? = null,
    cancel: (() -> Unit)? = null
) {
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

fun showEntryDialog(
    context: Context?,
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