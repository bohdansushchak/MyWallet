package bohdan.sushchak.mywallet

import bohdan.sushchak.mywallet.internal.myToString
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Assert.assertEquals


@RunWith(JUnit4::class)
class DoubleConvertToStringTest {

    @Test
    fun dotZeroTest(){
        val myNumber = 1.0
        val actual = myNumber.myToString()
        val expected = "1"

        assertEquals(expected, actual)
    }

    @Test
    fun numberTest() {
        val myNumber = 1.4
        val actual = myNumber.myToString()
        val expected = "1.4"

        assertEquals(expected, actual)
    }

    @Test
    fun numberLastZeroTest(){
        val myNumber = 1.40
        val actual = myNumber.myToString()
        val expected = "1.4"

        assertEquals(expected, actual)
    }

    @Test
    fun numberBigFloatTest() {
        val myNumber = 1.05
        val actual = myNumber.myToString()
        val expected = "1.05"

        assertEquals(expected, actual)
    }

    @Test
    fun numberFloatTest() {
        val myNumber = 0.05
        val actual = myNumber.myToString()
        val expected = "0.05"

        assertEquals(expected, actual)
    }

    @Test
    fun zeroTest() {
        val myNumber = 0.0
        val actual = myNumber.myToString()
        val expected = "0"

        assertEquals(expected, actual)
    }
}