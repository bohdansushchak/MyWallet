package bohdan.sushchak.mywallet.internal

import java.util.concurrent.TimeUnit


data class DateLimit(
    var startDate: Long,
    var endDate: Long
) {

    fun getCountOfDays(): Long {
        if(endDate < startDate)
            throw IllegalStateException("Should be endDate: $endDate greater than startDate: $startDate")

        val diff = endDate - startDate
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }
}