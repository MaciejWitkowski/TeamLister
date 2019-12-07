package maciej_witkowski.teamlister


import maciej_witkowski.teamlister.utils.TextUtils
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit line1, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        solution("aabcaabcabda")
        assertEquals(7,         solution("aabcaabcabda"))
    }




    fun solution(S: String): Int {
        // write your code in Kotlin
        var charArray= S.toCharArray()
        for (i in 0.. charArray.size-2){
            // if(charArray[i].toInt()==charArray[i+1].toInt()+1){
            if(charArray[i].inc()==charArray[i+1]){//||charArray[i]==charArray[i+1]){
                for(j in charArray.size-1 downTo 0){
                    if(charArray[j]==charArray[i+1] &&charArray[j-1]==charArray[i]){
                        return j-i-1
                    }
                }
            }
            else if(

        }

        return -1
    }



}
