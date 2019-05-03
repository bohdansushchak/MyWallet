package bohdan.sushchak.mywallet.internal

import java.security.InvalidParameterException
import java.util.*

const val FIRST_DAY_OF_MONTH = 1
const val FIRST_MONTH_OF_YEAR = Calendar.JANUARY
const val LAST_MONTH_OF_YEAR = Calendar.DECEMBER

//first//1546297200000
//last//1577746800000

/***
 * if month is null get dateLimit for all year
 */
fun getDateLimit(year: Int, month: Int? = null): DateLimit {
    val cal = Calendar.getInstance()
    val lastDay = getLastDayOfMonth(month ?: LAST_MONTH_OF_YEAR, year)

    cal.clear()
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month ?: FIRST_MONTH_OF_YEAR)
    cal.set(Calendar.DAY_OF_MONTH, FIRST_DAY_OF_MONTH)

    val startDate = cal.timeInMillis

    cal.set(Calendar.MONTH, month ?: LAST_MONTH_OF_YEAR)
    cal.set(Calendar.DAY_OF_MONTH, lastDay)
    val endDate = cal.timeInMillis

    return DateLimit(startDate = startDate, endDate = endDate)
}

private fun getLastDayOfMonth(month: Int, year: Int): Int {
    val cal = Calendar.getInstance().apply {
        clear()
    }

    if (year !in 1900..2200)
        throw throw InvalidParameterException("wrong year")

    if (month in 0..11) {
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.YEAR, year)

        return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    } else throw InvalidParameterException("wrong mount number")
}
