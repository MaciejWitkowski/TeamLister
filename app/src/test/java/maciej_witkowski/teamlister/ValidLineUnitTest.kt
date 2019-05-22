package maciej_witkowski.teamlister

import org.junit.Assert.*
import maciej_witkowski.teamlister.utils.TextUtils
import org.junit.Test

class ValidLineUnitTest {
    //POSITIVE TESTS

    @Test
    fun oneDigitTest() {
        val str="1Name Surname"
        val actual= TextUtils.isValidLine(str)
        val expected= true
        assertEquals(expected,actual)
    }

    @Test
    fun twoDigitTest() {
        val str="19Name Surname"
        val actual= TextUtils.isValidLine(str)
        val expected= true
        assertEquals(expected,actual)
    }

    @Test
    fun threeDigitTest() {
        val str="191Name Surname"
        val actual= TextUtils.isValidLine(str)
        val expected= true
        assertEquals(expected,actual)
    }

    @Test
    fun oneDigitSpaceTest() {
        val str="1 Name Surname"
        val actual= TextUtils.isValidLine(str)
        val expected= true
        assertEquals(expected,actual)
    }
    @Test
    fun twoDigitSpaceTest() {
        val str="19 Name Surname"
        val actual= TextUtils.isValidLine(str)
        val expected= true
        assertEquals(expected,actual)
    }

    @Test
    fun threeDigitSpaceTest() {
        val str="191 Name Surname"
        val actual= TextUtils.isValidLine(str)
        val expected= true
        assertEquals(expected,actual)
    }
    @Test
    fun oneDigitSpaceSpaceTest() {
        val str="1  Name Surname"
        val actual= TextUtils.isValidLine(str)
        val expected= true
        assertEquals(expected,actual)
    }
    @Test
    fun twoDigitSpaceSpaceTest() {
        val str="19  Name Surname"
        val actual= TextUtils.isValidLine(str)
        val expected= true
        assertEquals(expected,actual)
    }

    @Test
    fun threeDigitSpaceSpaceTest() {
        val str="191  Name Surname"
        val actual= TextUtils.isValidLine(str)
        val expected= true
        assertEquals(expected,actual)
    }

    @Test
    fun twoLetterNameTest(){
        val str="1 TJ"
        val actual= TextUtils.isValidLine(str)
        val expected= true
        assertEquals(expected,actual)
    }
    @Test
    fun oneDigitTwoLetterNameTest(){
        val str="1TJ"
        val actual= TextUtils.isValidLine(str)
        val expected= true
        assertEquals(expected,actual)
    }
    @Test
    fun oneNumberTest(){
        val str="14 ADAM DZW1GALA"
        val actual= TextUtils.isValidLine(str)
        val expected= true
        assertEquals(expected,actual)
    }



    //NEGATIVE TESTS
    @Test
    fun twoCharsTest(){
        val str ="1A"
        val actual= TextUtils.isValidLine(str)
        val expected= false
        assertEquals(expected,actual)
    }

    @Test
    fun plainTextTest(){
        val str ="Name Surname"
        val actual= TextUtils.isValidLine(str)
        val expected= false
        assertEquals(expected,actual)
    }

    @Test
    fun dateTest(){
        val str ="19.05.2019"
        val actual= TextUtils.isValidLine(str)
        val expected= false
        assertEquals(expected,actual)
    }

    @Test
    fun dateTextTest(){
        val str ="08.06.2018, INEA STADIUM, P0ZNAN, Kick-off 20.45"
        val actual= TextUtils.isValidLine(str)
        val expected= false
        assertEquals(expected,actual)
    }
    @Test
    fun numbersOnlyTest(){
        val str="17500075"
        val actual= TextUtils.isValidLine(str)
        val expected= false
        assertEquals(expected,actual)
    }

    @Test
    fun numberTextNumbersTest(){
        val str="65 Fax: 48 71 750 00 74"
        val actual= TextUtils.isValidLine(str)
        val expected= false
        assertEquals(expected,actual)
    }

    @Test
    fun longNumberTextNumbersTest(){
        val str="2019 roku, godzina 18"
        val actual= TextUtils.isValidLine(str)
        val expected= false
        assertEquals(expected,actual)
    }
}

