package maciej_witkowski.teamlister

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FixWrongTUnitTest {

private fun fixWrongT(text: String):String{
    var isMatch=false;
//    if (text.startsWith('t'))
    var match=""
//if start with t -> replace Ł
//if t surrounded with uppercase -> replace Ł



    val regex = "[A-Z][a-z][A-Z]".toRegex()
    if(regex.containsMatchIn(text)){
        var matchResult = regex.find(text)
        match=matchResult!!.value
        //val(first,second)=matchResult!!.destructured
        val numberedGroupValues = matchResult.destructured.toList()
// destructured group values only contain values of the groups, excluding the zeroth group.
        println(numberedGroupValues) // [John, 9731879]
        isMatch=true
      //  match=first+match+second
    }

return match
}

    @Test
    fun test(){
        val regex = """([\w\s]+)(\d+)""".trimMargin().toRegex()
        val matchResult = regex.find("Jakub StOWIK")
        //val (name, age) = matchResult!!.destructured
        val numberedGroupValues = matchResult!!.destructured.toList()
// destructured group values only contain values of the groups, excluding the zeroth group.
        println(numberedGroupValues) // [John, 9731879]
        //assertEquals("Jakub ", name)
        //assertEquals("IK", age)
    }

    @Test
    fun slowikUnitTest() {
        var actual =fixWrongT("Jakub StOWIK")
        val expected ="Jakub SŁOWIK"
        assertEquals("StO", actual)
    }

    @Test
    fun holowniaUnitTest() {
        var actual =fixWrongT("Mateusz HOtOWNIA")
        val expected ="Mateusz HOŁOWNIA"
        assertEquals("OtO", actual)
    }

    ///Adrian tYSZCZARZ

    //Michat CHRAPEK
    //Michai



}
