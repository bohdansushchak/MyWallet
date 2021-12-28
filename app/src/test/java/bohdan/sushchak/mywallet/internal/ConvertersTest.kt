package bohdan.sushchak.mywallet.internal


import android.graphics.Color
import com.github.sundeepk.compactcalendarview.domain.Event
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ConvertersTest {

    @Test
    fun timeStampCheck() {
        val timestamp = 1640715926L
        val actual = Converters().dateToEvent(timestamp)
        val expected = Event(Color.BLACK, timestamp)

        assertEquals(expected.timeInMillis, actual.timeInMillis)
    }

    @Test
    fun timeStampZeroCheck() {
        val timestamp = 0L
        val actual = Converters().dateToEvent(timestamp)
        val expected = Event(Color.BLACK, timestamp)

        assertEquals(expected.timeInMillis, actual.timeInMillis)
    }

    @Test
    fun dataCheck() {
        val timestamp = 1640715926L
        val actual = Converters().dateToEvent(timestamp)
        val expected = null

        assertEquals(expected, actual.data)
    }

    @Test
    fun colorCheck() {
        val timestamp = 1640715926L
        val actual = Converters().dateToEvent(timestamp)
        val expected = Event(Color.BLACK, timestamp)

        assertEquals(expected.color, actual.color)
    }
}