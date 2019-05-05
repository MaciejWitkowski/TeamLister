package maciej_witkowski.teamlister

import maciej_witkowski.teamlister.utils.TextUtils
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ReplaceNonAsciiUnitTest {

    @Test
    fun simpleTest(){
        val input="áéíóůĈ ĉ Ĝ ĝ Ĥ ĥ Ĵ ĵ Ŝ ŝ Ŭ ŭ"
        val expected="aeiouC c G g H h J j S s U u"
        val actual= TextUtils.replaceNonAsciiChars(input)
        assertEquals(expected, actual)
    }

    @Test
    fun polishLowercaseLettersTest(){
        val input="ąćęłńóśźż"
        val expected="acelnoszz"
        val actual= TextUtils.replaceNonAsciiChars(input)
        assertEquals(expected, actual)
    }

    @Test
    fun polishUppercaseLetterTest(){
        val input="Ą Ć Ę Ł Ń Ó Ś Ź Ż"
        val expected="A C E L N O S Z Z"
        val actual= TextUtils.replaceNonAsciiChars(input)
        assertEquals(expected, actual)
    }

    @Test
    fun oTest(){
        val input="Øø"
        val expected="Oo"
        val actual= TextUtils.replaceNonAsciiChars(input)
        assertEquals(expected, actual)
    }

}

