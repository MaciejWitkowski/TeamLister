package maciej_witkowski.teamlister

import maciej_witkowski.teamlister.utils.TextUtils
import org.junit.Assert.*
import org.junit.Test

class FixWrongILUnitTest {
    //bad l first: lx -> Ix
    //bad: xIx -> xlx
    @Test
    fun lgorTest(){
        val actual = TextUtils.fixWrongIL("lgor Angulo")
        val expected = "Igor Angulo"
        assertEquals(expected, actual)
    }

    @Test
    fun lgorInvertedTest(){
        val placeholder =0
        val actual = TextUtils.fixWrongIL("Angulo lgor")
        val expected = "Angulo Igor"
        assertEquals(expected, actual)
    }

    @Test
    fun lljaSimpleTest(){
        val actual = TextUtils.fixWrongIL("llja")
        val expected = "Ilja"
        assertEquals(expected, actual)
    }
    @Test
    fun lljaOtherSimpleTest(){
        val actual = TextUtils.fixWrongIL("lIja")
        val expected = "Ilja"
        assertEquals(expected, actual)
    }

    @Test
    fun SimpleTest(){
        val actual = TextUtils.fixWrongIL("aIa")
        val expected = "ala"
        assertEquals(expected, actual)
    }
    @Test
    fun SimpleAnotherTest(){
        val actual = TextUtils.fixWrongIL("IIa")
        val expected = "Ila"
        assertEquals(expected, actual)
    }

    @Test
    fun lionTest(){//unchanged
        val actual = TextUtils.fixWrongIL("LION")
        val expected = "LION"
        assertEquals(expected, actual)
    }
    @Test

    fun ilonaTest(){//unchanged
        val actual = TextUtils.fixWrongIL("ILONA")
        val expected = "ILONA"
        assertEquals(expected, actual)
    }

    @Test
    fun lljaTest(){
        val actual = TextUtils.fixWrongIL("llja Iljin")
        val expected = "Ilja Iljin"
        assertEquals(expected, actual)
    }

    @Test
    fun lIjaInvertedTest(){
        val actual = TextUtils.fixWrongIL("Iljin llja")
        val expected = "Iljin Ilja"
        assertEquals(expected, actual)
    }

    @Test
    fun iijaTest(){
        val actual = TextUtils.fixWrongIL("IIja Iljin")
        val expected = "Ilja Iljin"
        assertEquals(expected, actual)
    }

    @Test
    fun iijaInvertedTest(){
        val actual = TextUtils.fixWrongIL("Iljin IIja")
        val expected = "Iljin Ilja"
        assertEquals(expected, actual)
    }

    @Test
    fun wojcicklTest(){
        val actual = TextUtils.fixWrongIL("Jakub WOJCICKl")
        val expected = "Jakub WOJCICKI"
        assertEquals(expected, actual)
    }

    @Test
    fun wojcicklInvertedTest(){
        val actual = TextUtils.fixWrongIL("WOJCICKl Jakub")
        val expected = "WOJCICKI Jakub"
        assertEquals(expected, actual)
    }

    @Test
    fun prikryiTest(){
        val actual = TextUtils.fixWrongIL("Tomas PrikryI")
        val expected = "Tomas Prikryl"
        assertEquals(expected, actual)
    }
    @Test
    fun prikryiInvertedTest(){
        val actual = TextUtils.fixWrongIL("PrikryI Tomas")
        val expected = "Prikryl Tomas"
        assertEquals(expected, actual)
    }



}
