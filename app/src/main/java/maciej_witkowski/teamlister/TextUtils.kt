package maciej_witkowski.teamlister

import java.text.Normalizer

private val REGEX_UNACCENTED = "\\p{InCombiningDiacriticalMarks}+".toRegex()

class TextUtils {
    companion object {
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
                CaseFormat.UPPERLOWER -> upperLower(input)
                CaseFormat.UPPER -> input.toUpperCase()
                CaseFormat.LOWER -> input.toLowerCase()
            }
        }

    }

}

private fun upperLower(str:String):String {
    val input=str.toLowerCase()
    val words = input.split(" ").toMutableList()
    var output = ""
    for (word in words) {
        output += word.capitalize() + " "
    }
    output = output.trim()
    return output
}
