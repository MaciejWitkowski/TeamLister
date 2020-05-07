package maciej_witkowski.teamlister

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import maciej_witkowski.teamlister.utils.TextUtils
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DictionaryNameFixUnitTest {
    var list = listOf<String>()
    @Before
    fun setup() {
       val context:Context= ApplicationProvider.getApplicationContext()
        list = context.resources.getStringArray(R.array.names).toList()
    }
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

    @Test
    fun pawetDefaultCaseTest(){
        val actual = TextUtils.dictionaryNameFix("Pawet Bochniewicz", list)
        val expected = "Paweł Bochniewicz"
        assertEquals(expected, actual)
    }

}
