package bohdan.sushchak.mywallet.internal

import bohdan.sushchak.mywallet.internal.formatDigitToString
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class HelpersFormatDigits {

    @Test
    fun billionTest() {
        var expected = "1B"
        var actual = formatDigitToString(1000000000.0)
        assertEquals(expected, actual)

        expected = "10B"
        actual = formatDigitToString(10000000000.0)
        assertEquals(expected, actual)

        expected = "100B"
        actual = formatDigitToString(100000000000.0)
        assertEquals(expected, actual)
    }

    @Test
    fun millionTest() {
        var expected = "1M"
        var actual = formatDigitToString(1000000.0)
        assertEquals(expected, actual)

        expected = "10M"
        actual = formatDigitToString(10000000.0)
        assertEquals(expected, actual)

        expected = "100M"
        actual = formatDigitToString(100000000.0)
        assertEquals(expected, actual)
    }

    @Test
    fun thousandTest() {
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
    fun randThousandTest() {
        var expected = "1K"
        var actual = formatDigitToString(1010.0)
        assertEquals(expected, actual)

        expected = "1.1K"
        actual = formatDigitToString(1110.0)
        assertEquals(expected, actual)
    }

    @Test
    fun digitTest1() {
        var expected = "90"
        var actual = formatDigitToString(90.0)
        assertEquals(expected, actual)

        expected = "1"
        actual = formatDigitToString(1.0)
        assertEquals(expected, actual)
    }

    @Test
    fun digitTest2() {
        var expected = "97"
        var actual = formatDigitToString(97.0)
        assertEquals(expected, actual)

        expected = "111"
        actual = formatDigitToString(111.0)
        assertEquals(expected, actual)
    }

}