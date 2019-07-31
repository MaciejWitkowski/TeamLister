package maciej_witkowski.teamlister


import maciej_witkowski.teamlister.utils.TextUtils
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun randomTest(){
        val expected ="0 0\n" +
                "1 1\n" +
                "2 2\n" +
                "3 3"

        val newSb = StringBuilder()
        for (x in 0..3) {
                newSb.append(x.toString() + "  " + x.toString()+ "\n")
        }
        var string=newSb.toString()

        //if ( newSb.endsWith("\n")){
         var string1 =string.substring(0,string.lastIndex)

        assertEquals(expected,string1 )

    }

}
