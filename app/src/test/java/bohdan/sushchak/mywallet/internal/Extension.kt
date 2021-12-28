package bohdan.sushchak.mywallet.internal

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class Extension {

    @Test
    fun dateCheck() {
        val cal = Calendar.getInstance()

        val actual = cal.getOnlyDate()
        cal.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val expected = cal.time

        assertEquals(expected.time, actual.time)
    }

    @Test
    fun timestamp() {
        val cal = Calendar.getInstance()

        val actual = cal.onlyDateInMillis()
        cal.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val expected = cal.timeInMillis

        assertEquals(expected, actual)
    }
}