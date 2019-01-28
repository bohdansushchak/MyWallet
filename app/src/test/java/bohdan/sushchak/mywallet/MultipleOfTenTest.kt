package bohdan.sushchak.mywallet

import bohdan.sushchak.mywallet.internal.multipleOfTen
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MultipleOfTenTest {

    @Test
    fun thousandTest(){
        val expected = 3
        val actual = multipleOfTen(1000.0)

        assertEquals(expected, actual)
    }

    @Test
    fun randNumberTest(){
        val expected = 3
        val actual = multipleOfTen(1124.455)

        assertEquals(expected, actual)
    }

    @Test
    fun digitTest(){
        val expected = 0
        val actual = multipleOfTen(5.2)

        assertEquals(expected, actual)
    }

    @Test
    fun numberWithZeroTest(){
        val expected = 4
        val actual = multipleOfTen(10101.2)

        assertEquals(expected, actual)
    }

    @Test
    fun negativeDigitTest(){
        val expected = 2
        val actual = multipleOfTen(-101.2)

        assertEquals(expected, actual)
    }

    @Test
    fun zeroTest(){
        val expected = 0
        val actual = multipleOfTen(0.0)

        assertEquals(expected, actual)
    }
}