package maciej_witkowski.teamlister

import maciej_witkowski.teamlister.utils.RemoveBracketFormat
import maciej_witkowski.teamlister.utils.TextUtils
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit line1, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RemoveBracketsUnitTest {

    @Test
    fun closedBracketNoneTest(){
        val input ="Name Surname (K)"
        val expected="Name Surname (K)"
        val actual= TextUtils.removeBrackets(input,RemoveBracketFormat.NONE)
        assertEquals(expected, actual)
    }

    @Test
    fun closedBracketAllTest(){
        val input ="Name Surname (K)"
        val expected="Name Surname"
        val actual= TextUtils.removeBrackets(input,RemoveBracketFormat.ALL)
        assertEquals(expected, actual)
    }

    @Test
    fun closedBracketNotClosedTest(){
        val input ="Name Surname (K)"
        val expected="Name Surname (K)"
        val actual= TextUtils.removeBrackets(input,RemoveBracketFormat.NOT_CLOSED)
        assertEquals(expected, actual)
    }

    @Test
    fun openBracketNoneTest(){
        val input ="Name Surname (K"
        val expected="Name Surname (K"
        val actual= TextUtils.removeBrackets(input,RemoveBracketFormat.NONE)
        assertEquals(expected, actual)
    }

    @Test
    fun openBracketAllTest(){
        val input ="Name Surname (K"
        val expected="Name Surname"
        val actual= TextUtils.removeBrackets(input,RemoveBracketFormat.ALL)
        assertEquals(expected, actual)
    }

    @Test
    fun openBracketNotClosedTest(){
        val input ="Name Surname (K"
        val expected="Name Surname"
        val actual= TextUtils.removeBrackets(input,RemoveBracketFormat.NOT_CLOSED)
        assertEquals(expected, actual)
    }

}