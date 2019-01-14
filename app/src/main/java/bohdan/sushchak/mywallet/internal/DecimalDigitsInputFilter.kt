package bohdan.sushchak.mywallet.internal

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern


class DecimalDigitsInputFilter(digitsBeforeDot: Int, digitsAfterDot: Int) : InputFilter {

    private var pattern: Pattern = Pattern.compile("(([1-9]{1})([0-9]{0," + (digitsBeforeDot - 1) + "})?)?(\\.[0-9]{0," + digitsAfterDot + "})?")

    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {

        val builder = StringBuilder(dest!!)
        builder.replace(dstart, dend, source?.subSequence(start, end).toString())
        return if (!builder.toString().matches((pattern).toRegex())) {
            if (source.isNullOrEmpty()) dest.subSequence(dstart, dend) else ""
        } else null

    }
}