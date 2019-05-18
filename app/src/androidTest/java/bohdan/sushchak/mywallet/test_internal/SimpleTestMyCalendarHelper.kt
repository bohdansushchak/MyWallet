package bohdan.sushchak.mywallet.test_internal

import bohdan.sushchak.mywallet.internal.dateRangeByYearAndMonth
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

class SimpleTestMyCalendarHelper {

    private lateinit var calendar: Calendar

    @Before
    fun clearCalendar(){
        calendar = Calendar.getInstance()
    }

    @Test
    fun calTestStartDate() {
        calendar.clear()

        val dateLimit = dateRangeByYearAndMonth(2019, Calendar.JANUARY)

        calendar.set(Calendar.YEAR, 2019)
        calendar.set(Calendar.MONTH, Calendar.JANUARY)

        val firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
        calendar.set(Calendar.DAY_OF_MONTH, firstDay)

        Assert.assertEquals(calendar.timeInMillis, dateLimit.startDate)
    }

    @Test
    fun calTestEndDate() {
        calendar.clear()

        val dateLimit = dateRangeByYearAndMonth(2019, Calendar.JANUARY)

        calendar.set(Calendar.YEAR, 2019)
        calendar.set(Calendar.MONTH, Calendar.JANUARY)

        val lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.set(Calendar.DAY_OF_MONTH, lastDay)

        Assert.assertEquals(calendar.timeInMillis, dateLimit.endDate)
    }

    @Test
    fun calTestStartDateForYear() {
        calendar.clear()

        val dateLimit = dateRangeByYearAndMonth(2019)

        calendar.set(Calendar.YEAR, 2019)
        calendar.set(Calendar.MONTH, Calendar.JANUARY)

        val firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
        calendar.set(Calendar.DAY_OF_MONTH, firstDay)

        Assert.assertEquals(calendar.timeInMillis, dateLimit.startDate)
    }

    @Test
    fun calTestEndDateForYear() {
        calendar.clear()

        val dateLimit = dateRangeByYearAndMonth(2019)

        calendar.set(Calendar.YEAR, 2019)
        calendar.set(Calendar.MONTH, Calendar.DECEMBER)

        val lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.set(Calendar.DAY_OF_MONTH, lastDay)

        Assert.assertEquals(calendar.timeInMillis, dateLimit.endDate)
    }
}