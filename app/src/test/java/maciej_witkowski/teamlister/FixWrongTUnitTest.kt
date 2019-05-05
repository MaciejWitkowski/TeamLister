package maciej_witkowski.teamlister

import maciej_witkowski.teamlister.utils.TextUtils.Companion.fixWrongT
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FixWrongTUnitTest {

    @Test
    fun slowikUnitTest() {
        val actual =fixWrongT("Jakub StOWIK")
        val expected ="Jakub SŁOWIK"
        assertEquals(expected, actual)
    }

    @Test
    fun holowniaUnitTest() {
        val actual =fixWrongT("Mateusz HOtOWNIA")
        val expected ="Mateusz HOŁOWNIA"
        assertEquals(expected, actual)
    }

    @Test
    fun lyszczarzUnitTest() {
        val actual =fixWrongT("Adrian tYSZCZARZ")
        val expected ="Adrian ŁYSZCZARZ"
        assertEquals(expected, actual)
    }
    @Test
    fun lyszczarz1UnitTest() {
        val actual =fixWrongT("tYSZCZARZ")
        val expected ="ŁYSZCZARZ"
        assertEquals(expected, actual)
    }

    @Test
    fun unchangedUnitTest(){
        val actual =fixWrongT("Marcin ROBAK")
        val expected ="Marcin ROBAK"
        assertEquals(expected, actual)
    }
    //Michat CHRAPEK
    //Michai



}
