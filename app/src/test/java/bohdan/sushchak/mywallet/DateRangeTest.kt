package bohdan.sushchak.mywallet

import bohdan.sushchak.mywallet.internal.getDateLimit
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Assert.assertEquals

@RunWith(JUnit4::class)
class DateRangeTest {


//first//1546297200000
//last//1577746800000

    @Test
    fun testRange(){
        val actual = getDateLimit(2019, 0)

        val firstDay = 1546297200000
        val lastDay = 1577746800000

        assertEquals(firstDay, actual.startDate)
    }
}