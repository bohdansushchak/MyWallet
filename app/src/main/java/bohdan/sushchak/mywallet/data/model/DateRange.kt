package bohdan.sushchak.mywallet.data.model

import java.util.concurrent.TimeUnit


open class DateRange(
    var startDate: Long,
    var endDate: Long
) {

    fun getCountOfDays(): Long {
        if (endDate < startDate)
            throw IllegalStateException("Should be endDate: $endDate greater than startDate: $startDate")

        val diff = endDate - startDate
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }
}

class DateLimit(
    val leastDate: Long,
    val biggestDate: Long,
    startDate: Long,
    endDate: Long
) : DateRange(startDate, endDate)