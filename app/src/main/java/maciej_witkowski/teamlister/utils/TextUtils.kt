package maciej_witkowski.teamlister.utils

import maciej_witkowski.teamlister.model.PlayerData
import java.text.Normalizer

private val REGEX_UNACCENTED = "\\p{InCombiningDiacriticalMarks}+".toRegex()

class TextUtils {
            companion object {

        fun removeBrackets(input: String, format: RemoveBracketFormat): String {
            return when (format) {
                RemoveBracketFormat.NONE -> input
                RemoveBracketFormat.ALL -> removeAllBrackets(input)
                RemoveBracketFormat.NOT_CLOSED -> removeNotClosedBrackets(input)
            }
        }

        fun splitNumbers(str: String): PlayerData {//split text into number + name
            var number = ""
            var result = str.replace("[0-9]{1,3}".toRegex()) { number = it.value; "" }
            result = result.replace("\\s+".toRegex(), " ")
            result = result.replace(".", "")
            result = result.trim()
            return PlayerData(number, result)
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
            val regex = "[A-Z-Ł][t][A-Z-Ł]".toRegex()
            val regexStart = "^[t][A-Z-Ł]".toRegex()
            val regexSpace = "[\\s][t][A-Z-Ł]".toRegex()
            return when {
                regex.containsMatchIn(input) -> replaceWrong(input, regex,"t","Ł")
                regexSpace.containsMatchIn(input) -> replaceWrong(input, regexSpace,"t","Ł")
                regexStart.containsMatchIn(input) -> replaceWrong(input, regexStart,"t","Ł")
                else -> input
            }
        }


        fun fixRandomWrongSigns(input: String): String {
           return input.replace("$", "S").replace("&", "Ł").replace(" @","").replace(" @ ","")
        }

        /** "t"  surrounded by uppercase letters are fixed in @fixWrongT method **/
        fun dictionaryNameFix(input: String, names: List<String>): String {
            var corrected = ""
            val split = input.split(" ").toMutableList()
            split.forEachIndexed { index, string ->
                names.forEach { dictName ->
                    if (string.contains(dictName, true)) {
                        if (dictName.equals("tomistaw", true))
                            split[index] = split[index].replaceRange(5, 6, "ł")
                        else if (dictName.equals("barttomiej", true)) {
                            split[index] = split[index].replaceRange(4, 5, "ł")
                        } else {
                            if (dictName[0] == 't')
                                split[index] = split[index].replaceRange(0, 1, "Ł")
                            else
                                split[index] = split[index].replace("t", "ł")

                        }

                    }
                }
            }

            split.forEach {
                corrected = "$corrected $it"
            }
            return corrected.trim()
        }

    }
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

private fun splitKeepDelimiters(s: String, rx: Regex, keep_empty: Boolean = true): MutableList<String> {
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


private fun replaceWrong(input: String, regex: Regex, old: String, new: String): String {
    val split = splitKeepDelimiters(input, regex)
    var result = ""
    for (i: Int in 0 until split.size) {
        if (regex.containsMatchIn(split[i])) {
            split[i] = split[i].replace(old, new)
        }
        result += split[i]
    }
    return result
}

private fun replaceT(input: String, regex: Regex): String {
    val split = splitKeepDelimiters(input, regex)
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
