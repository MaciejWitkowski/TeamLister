package maciej_witkowski.teamlister.model

class LineValidator {
    companion object {
        fun isValidLine(input: String): Boolean {
            val line=input.trim()
            return when {
                line.length == 3 -> threeCharsValidation(line)
                line.length == 4 -> fourCharsValidation(line)
                line.length in 5..29 -> defaultValidation(line)
                else -> false
            }
        }
    }
}
    private fun threeCharsValidation(line: String): Boolean {
        var isValid = false
        if (line[0].isDigit() && line[1].isLetter() && !line[2].isDigit())
            isValid = true
        else if (line[2].isDigit() && line[1].isLetter() && !line[0].isDigit())
            isValid = true
        return isValid
    }

    private fun fourCharsValidation(line: String): Boolean {
        var isValid = false
        if (line[0].isDigit() && !line[2].isDigit() && !line[3].isDigit())
            isValid = true
        else if(line[3].isDigit() && !line[1].isDigit() && !line[0].isDigit())
            isValid=true
        return isValid
    }

    private fun defaultValidation(line: String): Boolean {
        var isValid = false


        if (!line[0].isDigit()) {
            if (line[line.lastIndex].isDigit())
                return invertedValidation(line)
                else
                return false
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
        if (line.contains("lotto eks", true))
                isValid = false
        if (line.contains("pko eks",true))
            isValid=false

        return isValid
    }

private fun invertedValidation(line: String):Boolean{
    var isValid = false

    var digitCount = 0
    for (i in line.length-3 downTo 0) {
        if (line[i].isDigit()) {
            digitCount++
        }
    }
    if (digitCount < 2)
        isValid = true

    return isValid
}





