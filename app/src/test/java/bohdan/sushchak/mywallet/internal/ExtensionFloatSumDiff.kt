package bohdan.sushchak.mywallet.internal

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ExtensionFloatSumDiff {

    @Test
    fun floatTwoSum1() {
        val a = 0.01
        val b = 0.09

        val actual = a.myPlus(b)
        val expected = 0.1

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatTwoSum2() {
        val a = 0.03
        val b = 0.07

        val actual = a.myPlus(b)
        val expected = 0.1

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatTwoSum3() {
        val a = 0.11
        val b = 0.09

        val actual = a.myPlus(b)
        val expected = 0.2

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatTwoSum4() {
        val a = 2.11
        val b = 0.09

        val actual = a.myPlus(b)
        val expected = 2.2

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatTwoSum5() {
        val a = 0.0
        val b = 0.0

        val actual = a.myPlus(b)
        val expected = 0.0

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatThreeSum1() {
        val a = 2.111
        val b = 0.09

        val actual = a.myPlus(b)
        val expected = 2.2

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatThreeSum2() {
        val a = 2.111
        val b = 0.091

        val actual = a.myPlus(b)
        val expected = 2.2

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatFourSum1() {
        val a = 1.1112
        val b = 1.091

        val actual = a.myPlus(b)
        val expected = 2.2

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatFourSum2() {
        val a = 1.0002
        val b = 1.091

        val actual = a.myPlus(b)
        val expected = 2.09

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatFourSum3() {
        val a = 0.0001
        val b = 0.0001

        val actual = a.myPlus(b)
        val expected = 0.0

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatTwoDiff1() {
        val a = 0.11
        val b = 0.9

        val actual = a.myMinus(b)
        val expected = -0.79

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatTwoDiff2() {
        val a = 3.99
        val b = 0.09

        val actual = a.myMinus(b)
        val expected = 3.9

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatTwoDiff3() {
        val a = 0.0
        val b = 0.09

        val actual = a.myMinus(b)
        val expected = -0.09

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatTwoDiff4() {
        val a = 0.0
        val b = 0.0

        val actual = a.myMinus(b)
        val expected = 0.0

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatThreeDiff1() {
        val a = 0.001
        val b = 0.001

        val actual = a.myMinus(b)
        val expected = 0.0

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatThreeDiff2() {
        val a = 0.009
        val b = 0.001

        val actual = a.myMinus(b)
        val expected = 0.0

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatThreeDiff3() {
        val a = 0.222
        val b = 0.111

        val actual = a.myMinus(b)
        val expected = 0.11

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatThreeDiff4() {
        val a = 0.999
        val b = 0.001

        val actual = a.myMinus(b)
        val expected = 0.99

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatFourDiff1() {
        val a = 0.9991
        val b = 0.0011

        val actual = a.myMinus(b)
        val expected = 0.99

        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun floatFourDiff2() {
        val a = 0.0001
        val b = 0.0001

        val actual = a.myMinus(b)
        val expected = 0.0

        assertEquals(expected, actual, 0.0)
    }
}