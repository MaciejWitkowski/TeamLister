package maciej_witkowski.teamlister


import maciej_witkowski.teamlister.utils.TextUtils
import org.junit.Test

import org.junit.Assert.*

class DictionaryNameFixUnitTest {
    private val list = listOf("Barttomiej","Btażej","Btazej","Bogumit","Bogumistaw","Bolestaw","Bronistaw","Czestaw","Jarostaw","tucjan","tukasz","Michat","Mieczystaw",
        "Mikotaj","Mirostaw","Mitosz","Przemystaw","Radostaw","Rafat","Stawomir","Tomistaw","Wactaw","Wiestaw","Wtadystaw","Władystaw","Wtadysław","Wtodzimierz","Zdzistaw")
    //TODO list should be loaded from resources

    @Test
    fun tukaszDefaultCaseTest(){
        val actual = TextUtils.dictionaryNameFix("tukasz Kurek",list)
        val expected ="Łukasz Kurek"
        assertEquals(expected, actual)
    }

    @Test
    fun tukaszInvertedDefaultCaseTest(){
        val actual = TextUtils.dictionaryNameFix("Kurek tukasz",list)
        val expected ="Kurek Łukasz"
        assertEquals(expected, actual)
    }

    @Test
    fun tomistawDefaultCaseTest(){
        val actual = TextUtils.dictionaryNameFix("Tomistaw Kurek",list)
        val expected ="Tomisław Kurek"
        assertEquals(expected, actual)
    }

    @Test
    fun tomistawInvertedDefaultCaseTest(){
        val actual = TextUtils.dictionaryNameFix("Kurek Tomistaw",list)
        val expected ="Kurek Tomisław"
        assertEquals(expected, actual)
    }

    @Test
    fun barttomiejDefaultCaseTest(){
        val actual = TextUtils.dictionaryNameFix("Barttomiej Kurek",list)
        val expected ="Bartłomiej Kurek"
        assertEquals(expected, actual)
    }

    @Test
    fun barttomiejInvertedDefaultCaseTest(){
        val actual = TextUtils.dictionaryNameFix("Kurek Barttomiej",list)
        val expected ="Kurek Bartłomiej"
        assertEquals(expected, actual)
    }

    @Test
    fun tucjanDefaultCaseTest(){
        val actual = TextUtils.dictionaryNameFix("tucjan Kurek",list)
        val expected ="Łucjan Kurek"
        assertEquals(expected, actual)
    }

    @Test
    fun tucjanInvertedDefaultCaseTest(){
        val actual = TextUtils.dictionaryNameFix("Kurek tucjan",list)
        val expected ="Kurek Łucjan"
        assertEquals(expected, actual)
    }

    @Test
    fun wactawDefaultCaseTest(){
        val actual = TextUtils.dictionaryNameFix("Wactaw Kurek",list)
        val expected ="Wacław Kurek"
        assertEquals(expected, actual)
    }

    @Test
    fun wactawInvertedDefaultCaseTest(){
        val actual = TextUtils.dictionaryNameFix("Kurek Wactaw",list)
        val expected ="Kurek Wacław"
        assertEquals(expected, actual)
    }

    @Test
    fun wtadystawDefaultCaseTest(){
        val actual = TextUtils.dictionaryNameFix("Wtadystaw Kurek",list)
        val expected ="Władysław Kurek"
        assertEquals(expected, actual)
    }

    @Test
    fun wtadystawInvertedDefaultCaseTest(){
        val actual = TextUtils.dictionaryNameFix("Kurek Wtadystaw",list)
        val expected ="Kurek Władysław"
        assertEquals(expected, actual)
    }



}
