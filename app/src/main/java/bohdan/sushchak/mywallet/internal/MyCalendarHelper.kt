package bohdan.sushchak.mywallet.internal

import bohdan.sushchak.mywallet.data.model.DateRange
import java.security.InvalidParameterException
import java.util.*

const val FIRST_DAY_OF_MONTH = 1
const val FIRST_MONTH_OF_YEAR = Calendar.JANUARY
const val LAST_MONTH_OF_YEAR = Calendar.DECEMBER

/***
 * if month is null get dateRange for all year
 */
fun dateRangeByYearAndMonth(year: Int, month: Int? = null): DateRange {
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

    return DateRange(startDate = startDate, endDate = endDate)
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

fun getDayOfTime(dateMilliseconds: Long): Int {
    val cal = Calendar.getInstance()
    val date = Date(dateMilliseconds)
    cal.time = date
    val days = (cal.get(Calendar.YEAR) - 1900) * 366
    var dayOfCurrYear = cal.get(Calendar.DAY_OF_YEAR)
    return days + dayOfCurrYear
}
