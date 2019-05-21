package maciej_witkowski.teamlister

import maciej_witkowski.teamlister.model.PlayerData
import maciej_witkowski.teamlister.utils.TextUtils
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
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("1","Name Surname")
        assertEquals(expected,actual)
    }

    @Test
    fun twoDigitTest() {
        val str="19Name Surname"
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("19","Name Surname")
        assertEquals(expected,actual)
    }

    @Test
    fun threeDigitTest() {
        val str="191Name Surname"
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("191","Name Surname")
        assertEquals(expected,actual)
    }

    @Test
    fun oneDigitSpaceTest() {
        val str="1 Name Surname"
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("1","Name Surname")
        assertEquals(expected,actual)
    }
    @Test
    fun twoDigitSpaceTest() {
        val str="19 Name Surname"
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("19","Name Surname")
        assertEquals(expected,actual)
    }

    @Test
    fun threeDigitSpaceTest() {
        val str="191 Name Surname"
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("191","Name Surname")
        assertEquals(expected,actual)
    }
    @Test
    fun oneDigitSpaceSpaceTest() {
        val str="1  Name Surname"
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("1","Name Surname")
        assertEquals(expected,actual)
    }
    @Test
    fun twoDigitSpaceSpaceTest() {
        val str="19  Name Surname"
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("19","Name Surname")
        assertEquals(expected,actual)
    }

    @Test
    fun threeDigitSpaceSpaceTest() {
        val str="191  Name Surname"
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("191","Name Surname")
        assertEquals(expected,actual)
    }
    @Test
    fun spaceOneDigitSpaceTest() {
        val str=" 1 Name Surname"
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("1","Name Surname")
        assertEquals(expected,actual)
    }
    @Test
    fun spaceTwoDigitSpaceTest() {
        val str=" 19 Name Surname"
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("19","Name Surname")
        assertEquals(expected,actual)
    }

    @Test
    fun spaceThreeDigitSpaceNameSpaceTest() {
        val str=" 191 Name  Surname"
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("191","Name Surname")
        assertEquals(expected,actual)
    }
    @Test
    fun spaceOneDigitSpaceNameSpaceTest() {
        val str=" 1 Name  Surname"
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("1","Name Surname")
        assertEquals(expected,actual)
    }
    @Test
    fun spaceTwoDigitSpaceNameSpaceTest() {
        val str=" 19 Name  Surname"
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("19","Name Surname")
        assertEquals(expected,actual)
    }

    @Test
    fun spaceThreeDigitSpaceTest() {
        val str=" 191 Name Surname"
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("191","Name Surname")
        assertEquals(expected,actual)
    }
    @Test
    fun spaceOneDigitSpaceLastSpaceTest() {
        val str=" 1 Name Surname "
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("1","Name Surname")
        assertEquals(expected,actual)
    }
    @Test
    fun spaceTwoDigitSpaceLastSpaceTest() {
        val str=" 19 Name Surname "
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("19","Name Surname")
        assertEquals(expected,actual)
    }

    @Test
    fun spaceThreeDigitSpaceLastSpaceTest() {
        val str=" 191 Name Surname "
        val actual= TextUtils.splitNumbers(str)
        val expected= PlayerData("191","Name Surname")
        assertEquals(expected,actual)
    }

}
