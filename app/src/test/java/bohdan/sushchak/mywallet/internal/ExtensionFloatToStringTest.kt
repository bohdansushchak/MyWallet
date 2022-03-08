package bohdan.sushchak.mywallet.internal

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class ExtensionFloatToStringTest {

    @Test
    fun dotZeroTest1() {
        val myNumber = 1.0
        val actual = myNumber.myToString()
        val expected = "1,00"

        assertEquals(expected, actual)
    }

    @Test
    fun dotZeroTest2() {
        val myNumber = 1.01
        val actual = myNumber.myToString()
        val expected = "1,01"

        assertEquals(expected, actual)
    }

    @Test
    fun dotZeroTest3() {
        val myNumber = 0.01
        val actual = myNumber.myToString()
        val expected = "0,01"

        assertEquals(expected, actual)
    }

    @Test
    fun floatCheck() {
        val myNumber = 1.4
        val actual = myNumber.myToString()
        val expected = "1,40"

        assertEquals(expected, actual)
    }

    @Test
    fun numberLastZeroTest() {
        val myNumber = 1.40
        val actual = myNumber.myToString()
        val expected = "1,40"

        assertEquals(expected, actual)
    }

    @Test
    fun numberBigFloatTest1() {
        val myNumber = 1.000005
        val actual = myNumber.myToString()
        val expected = "1,00"

        assertEquals(expected, actual)
    }

    @Test
    fun numberBigFloatTest2() {
        val myNumber = 0.0005
        val actual = myNumber.myToString()
        val expected = "0,00"

        assertEquals(expected, actual)
    }

    @Test
    fun numberBigFloatTest3() {
        val myNumber = 0.005
        val actual = myNumber.myToString()
        val expected = "0,01"

        assertEquals(expected, actual)
    }

    @Test
    fun numberFloatTest() {
        val myNumber = 0.05
        val actual = myNumber.myToString()
        val expected = "0,05"

        assertEquals(expected, actual)
    }

    @Test
    fun zeroTest() {
        val myNumber = 0.0
        val actual = myNumber.myToString()
        val expected = "0,00"

        assertEquals(expected, actual)
    }
}