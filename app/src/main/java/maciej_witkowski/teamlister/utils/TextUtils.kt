package maciej_witkowski.teamlister.utils

import maciej_witkowski.teamlister.model.PlayerData
import java.text.Normalizer

private val REGEX_UNACCENTED = "\\p{InCombiningDiacriticalMarks}+".toRegex()

class TextUtils {
    companion object {


        fun isValidLine(line: String): Boolean {
            return when {
                line.length == 3 -> threeCharsValidation(line)
                line.length == 4 -> fourCharsValidation(line)
                line.length in 5..29 -> defaultValidation(line)
                else -> false
            }
        }

        fun removeBrackets(input: String, format: RemoveBracketFormat): String {
            return when (format) {
                RemoveBracketFormat.NONE -> input
                RemoveBracketFormat.ALL -> removeAllBrackets(input)
                RemoveBracketFormat.NOT_CLOSED -> removeNotClosedBrackets(input)
            }
        }

        fun splitNumbers(str: String): PlayerData {//split text into number + name
            var needle = ""
            var result = str.replace("[0-9]{1,3}".toRegex()) { needle = it.value; "" }
            result = result.replace("\\s+".toRegex(), " ")
            while (result.startsWith(" ")) {
                result = result.removePrefix(" ")
            }
            while (result.endsWith(" ")) {
                result = result.removeSuffix(" ")
            }
            return PlayerData(needle, result)
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

private fun threeCharsValidation(line: String): Boolean {
    var isValid = false
    if (line[0].isDigit() && line[1].isLetter() && !line[2].isDigit())
        isValid = true
    return isValid
}

private fun fourCharsValidation(line: String): Boolean {
    var isValid = false
    if (line[0].isDigit() && !line[2].isDigit() && !line[3].isDigit())
        isValid = true
    return isValid
}

private fun defaultValidation(line: String): Boolean {
    var isValid = false
    if (!line[0].isDigit()) {
        isValid = false
    } else {
        var digitCount = 0
        for (i in 2 until line.length) {
            if (line[i].isDigit()) {
                digitCount++
            }
        }
        if (digitCount < 2)
            isValid = true
    }
    return isValid
}

private fun removeAllBrackets(line: String): String {
    var result = line.replace("\\(([^]]+)\\)".toRegex(), "")
    result = removeNotClosedBrackets(result)
    result = result.trimEnd()
    return result
}

private fun removeNotClosedBrackets(line: String): String {
    val regexClosedBracket = "\\(([^]]+)\\)".toRegex()
    var result = line
    if (!regexClosedBracket.containsMatchIn(line)) {
        result = line.replace("\\((.*)".toRegex(), "")
        result = result.trimEnd()
    }
    return result
}

private fun splitKeepDelims(s: String, rx: Regex, keep_empty: Boolean = true): MutableList<String> {
    val res = mutableListOf<String>()
    var start = 0
    rx.findAll(s).forEach {
        val subStrBefore = s.substring(start, it.range.first())
        if (subStrBefore.isNotEmpty() || keep_empty) {
            res.add(subStrBefore)
        }
        res.add(it.value)
        start = it.range.last() + 1
    }
    if (start != s.length) res.add(s.substring(start))
    return res
}


private fun replaceT(input: String, regex: Regex): String {
    val split = splitKeepDelims(input, regex)
    var result = ""
    for (i: Int in 0 until split.size) {
        if (regex.containsMatchIn(split[i])) {
            split[i] = split[i].replace('t', 'Ł')
        }
        result += split[i]
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
