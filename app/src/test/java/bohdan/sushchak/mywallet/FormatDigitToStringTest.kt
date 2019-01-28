package bohdan.sushchak.mywallet

import bohdan.sushchak.mywallet.internal.formatDigitToString
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class FormatDigitToStringTest {

    @Test
    fun thousandTest(){
        var expected = "1K"
        var actual = formatDigitToString(1000.0)
        assertEquals(expected, actual)

        expected = "10K"
        actual = formatDigitToString(10000.0)
        assertEquals(expected, actual)

        expected = "100K"
        actual = formatDigitToString(100000.0)
        assertEquals(expected, actual)
    }

    @Test
    fun randThousandTest(){
        var expected = "1K"
        var actual = formatDigitToString(1010.0)
        assertEquals(expected, actual)

        expected = "1.1K"
        actual = formatDigitToString(1110.0)
        assertEquals(expected, actual)
    }

    @Test
    fun digitTest(){
        var expected = "90"
        var actual = formatDigitToString(90.0)
        assertEquals(expected, actual)

        expected = "1"
        actual = formatDigitToString(1.0)
        assertEquals(expected, actual)
    }

}