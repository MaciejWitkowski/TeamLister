package maciej_witkowski.teamlister

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SplitUnitTest {


    @Test
    fun oneDigitTest() {
        val str="1Name Surname"
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("1","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }
    @Test
    fun twoDigitTest() {
        val str="19Name Surname"
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("19","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }

    @Test
    fun threeDigitTest() {
        val str="191Name Surname"
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("191","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }

    @Test
    fun oneDigitSpaceTest() {
        val str="1 Name Surname"
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("1","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }
    @Test
    fun twoDigitSpaceTest() {
        val str="19 Name Surname"
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("19","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }

    @Test
    fun threeDigitSpaceTest() {
        val str="191 Name Surname"
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("191","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }
    @Test
    fun oneDigitSpaceSpaceTest() {
        val str="1  Name Surname"
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("1","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }
    @Test
    fun twoDigitSpaceSpaceTest() {
        val str="19  Name Surname"
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("19","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }

    @Test
    fun threeDigitSpaceSpaceTest() {
        val str="191  Name Surname"
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("191","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }
    @Test
    fun spaceOneDigitSpaceTest() {
        val str=" 1 Name Surname"
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("1","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }
    @Test
    fun spaceTwoDigitSpaceTest() {
        val str=" 19 Name Surname"
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("19","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }

    @Test
    fun spaceThreeDigitSpaceNameSpaceTest() {
        val str=" 191 Name  Surname"
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("191","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }
    @Test
    fun spaceOneDigitSpaceNameSpaceTest() {
        val str=" 1 Name  Surname"
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("1","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }
    @Test
    fun spaceTwoDigitSpaceNameSpaceTest() {
        val str=" 19 Name  Surname"
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("19","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }

    @Test
    fun spaceThreeDigitSpaceTest() {
        val str=" 191 Name Surname"
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("191","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }
    @Test
    fun spaceOneDigitSpaceLastSpaceTest() {
        val str=" 1 Name Surname "
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("1","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }
    @Test
    fun spaceTwoDigitSpaceLastSpaceTest() {
        val str=" 19 Name Surname "
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("19","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }

    @Test
    fun spaceThreeDigitSpaceLastSpaceTest() {
        val str=" 191 Name Surname "
        val actualArray=TextUtils.splitNumbers(str)

        val expectedArray= listOf("191","Name Surname")
        assertEquals(expectedArray[0],actualArray[0])
        assertEquals(expectedArray[1],actualArray[1])
    }

}
