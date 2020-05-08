package maciej_witkowski.teamlister

import maciej_witkowski.teamlister.utils.TextUtils.Companion.fixWrongT
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit line1, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FixWrongTUnitTest {

    @Test
    fun slowikUnitTest() {
        val actual = fixWrongT("Jakub StOWIK")
        val expected = "Jakub SŁOWIK"
        assertEquals(expected, actual)
    }

    @Test
    fun holowniaUnitTest() {
        val actual = fixWrongT("Mateusz HOtOWNIA")
        val expected = "Mateusz HOŁOWNIA"
        assertEquals(expected, actual)
    }

    @Test
    fun lyszczarzUnitTest() {
        val actual = fixWrongT("Adrian tYSZCZARZ")
        val expected = "Adrian ŁYSZCZARZ"
        assertEquals(expected, actual)
    }

    @Test
    fun lyszczarzFirstUnitTest() {
        val actual = fixWrongT("tYSZCZARZ")
        val expected = "ŁYSZCZARZ"
        assertEquals(expected, actual)
    }

    @Test
    fun jagielloUnitTest() {
        val actual = fixWrongT("JAGIEtŁO")
        val expected = "JAGIEŁŁO"
        assertEquals(expected, actual)
    }

    @Test
    fun jagielloDoubleUnitTest(){
        val actual = fixWrongT("JAGIEttO")
        val expected = "JAGIEŁŁO"
        assertEquals(expected, actual)
    }

    @Test
    fun unchangedUnitTest() {
        val actual = fixWrongT("Marcin ROBAK")
        val expected = "Marcin ROBAK"
        assertEquals(expected, actual)
    }

    @Test
    fun szczygietTest(){
        val actual = fixWrongT("SZCZYGIEt Imie")
        val expected = "SZCZYGIEŁ Imie"
        assertEquals(expected, actual)
    }
    @Test
    fun szczygietLastTest(){
        val actual = fixWrongT("SZCZYGIEt")
        val expected = "SZCZYGIEŁ"
        assertEquals(expected, actual)
    }


}
