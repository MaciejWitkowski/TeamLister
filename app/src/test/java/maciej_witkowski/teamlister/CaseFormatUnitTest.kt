package maciej_witkowski.teamlister


import maciej_witkowski.teamlister.utils.CaseFormat
import maciej_witkowski.teamlister.utils.TextUtils
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CaseFormatUnitTest {

    @Test
    fun abcdDefaultTest(){
        val input ="abcd"
        val expected="abcd"
        val actual= TextUtils.caseFormatting(input, CaseFormat.DEFAULT)
        assertEquals(expected, actual)
    }

    @Test
    fun abcdUpperLowerTest(){
        val input ="abcd"
        val expected="Abcd"
        val actual= TextUtils.caseFormatting(input, CaseFormat.UPPER_LOWER)
        assertEquals(expected, actual)
    }

    @Test
    fun abcdUpperTest(){
        val input ="abcd"
        val expected="ABCD"
        val actual= TextUtils.caseFormatting(input, CaseFormat.UPPER)
        assertEquals(expected, actual)
    }

    @Test
    fun abcdLowerTest(){
        val input ="abcd"
        val expected="abcd"
        val actual= TextUtils.caseFormatting(input, CaseFormat.LOWER)
        assertEquals(expected, actual)
    }

    @Test
    fun nameSurnameDefaultTest(){
        val input ="name SURNAME"
        val expected="name SURNAME"
        val actual= TextUtils.caseFormatting(input, CaseFormat.DEFAULT)
        assertEquals(expected, actual)
    }

    @Test
    fun nameSurnameUpperLowerTest(){
        val input ="name SURNAME"
        val expected="Name Surname"
        val actual= TextUtils.caseFormatting(input, CaseFormat.UPPER_LOWER)
        assertEquals(expected, actual)
    }
    @Test
    fun nameSurnameUpperTest(){
        val input ="name SURNAME"
        val expected="NAME SURNAME"
        val actual= TextUtils.caseFormatting(input, CaseFormat.UPPER)
        assertEquals(expected, actual)
    }
    @Test
    fun nameSurnameLowerTest(){
        val input ="name SURNAME"
        val expected="name surname"
        val actual= TextUtils.caseFormatting(input, CaseFormat.LOWER)
        assertEquals(expected, actual)
    }
}