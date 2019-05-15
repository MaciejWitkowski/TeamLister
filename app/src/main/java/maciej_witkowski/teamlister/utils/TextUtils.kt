package maciej_witkowski.teamlister.utils

import android.util.Log
import java.text.Normalizer

private val REGEX_UNACCENTED = "\\p{InCombiningDiacriticalMarks}+".toRegex()

class TextUtils {
    companion object {

        fun isValidLine(line: String): Boolean {//TODO needs to be improved, need UT
            var isValid = false
            if (line[0].isDigit() && line.length > 5)
                isValid = true
            return isValid
        }

        fun splitNumbers(str: String): List<String> {//split text into number + name
            var needle = ""
            var result = str.replace("[0-9]{1,3}".toRegex()) { needle = it.value; "" }
            result = result.replace("\\s+".toRegex(), " ")
            while (result.startsWith(" ")) {
                result = result.removePrefix(" ")
            }
            while (result.endsWith(" ")) {
                result = result.removeSuffix(" ")
            }
            return listOf(needle, result)
        }

        fun replaceNonAsciiChars(input: String): String {
            var str = input
            //Chars not replaced by normalize
            str = str.replace("ł", "l")
            str = str.replace("Ł", "L")
            str = str.replace("Ø", "O")
            str = str.replace("ø", "o")
            val temp = Normalizer.normalize(str, Normalizer.Form.NFD)
            return REGEX_UNACCENTED.replace(temp, "")
        }

        fun caseFormatting(input: String, format: CaseFormat): String {
            return when (format) {
                CaseFormat.DEFAULT -> input
                CaseFormat.UPPER_LOWER -> upperLower(input)
                CaseFormat.UPPER -> input.toUpperCase()
                CaseFormat.LOWER -> input.toLowerCase()
            }
        }

        fun fixWrongT(input: String): String {
            val regex = "[A-Z][t][A-Z]".toRegex()
            val regexStart = "^[t][A-Z]".toRegex()
            val regexSpace = "[\\s][t][A-Z]".toRegex()
            return when {
                regex.containsMatchIn(input) -> replaceT(input, regex)
                regexSpace.containsMatchIn(input) -> replaceT(input, regexSpace)
                regexStart.containsMatchIn(input) -> replaceT(input, regexStart)
                else -> input
            }
        }
    }
}


private fun splitKeepDelims(s: String, rx: Regex, keep_empty: Boolean = true): MutableList<String> {
    val rx = "[A-Z][t][A-Z]".toRegex()
    val res = mutableListOf<String>() // Declare the mutable list var
    var start = 0                     // Define var for substring start pos
    rx.findAll(s).forEach {
        // Looking for matches
        val subStrBefore = s.substring(start, it.range.first()) // // Substring before match start
        if (subStrBefore.length > 0 || keep_empty) {
            res.add(subStrBefore)      // Adding substring before match start
        }
        res.add(it.value)               // Adding match
        start = it.range.last() + 1       // Updating start pos of next substring before match
    }
    if (start != s.length) res.add(s.substring(start))  // Adding text after last match if any
    Log.e("TAG", res.toString())
    return res
}


private fun replaceT(input: String, regex: Regex): String {
    val splited = splitKeepDelims(input, regex)
    var result = ""
    for (i: Int in 0 until splited.size) {
        if (regex.containsMatchIn(splited[i])) {
            splited[i] = splited[i].replace('t', 'Ł')
        }
        result += splited[i]
    }
    return result
}

private fun upperLower(str: String): String {
    val input = str.toLowerCase()
    val words = input.split(" ").toMutableList()
    var output = ""
    for (word in words) {
        output += word.capitalize() + " "
    }
    output = output.trim()
    return output
}
