package bohdan.sushchak.mywallet

import bohdan.sushchak.mywallet.internal.myMinus
import bohdan.sushchak.mywallet.internal.myPlus
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DoublePlus {

    @Test
    fun doubleSum() {
        val a = 0.01
        val b = 0.09

        val actual = a.myPlus(b)
        val expected = 0.1

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun doubleDiff1() {
        val a = 0.1
        val b = 0.09

        val actual = a.myMinus(b)
        val expected = 0.01

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun doubleDiff2() {
        val a = 3.99
        val b = 0.09

        val actual = a.myMinus(b)
        val expected = 3.9

        assertEquals(expected, actual, 0.0)
    }
}