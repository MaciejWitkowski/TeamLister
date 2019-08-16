package maciej_witkowski.teamlister.model

import android.graphics.Rect
import android.util.Log
import androidx.core.text.isDigitsOnly
import com.google.firebase.ml.vision.text.FirebaseVisionText
import maciej_witkowski.teamlister.utils.TextUtils

private val TAG = LineExtractor::class.java.simpleName
private const val PERCENT_DIFF = 0.02
private const val MULTIPLIER = 5


class LineExtractor {
    fun getValidTextLines(result: FirebaseVisionText, imageWidth: Int): MutableList<TextLineLight> {
        val textLines = extractValidLines(result)
        //  return textLines}}

        val sideExtractor = SideExtractor(textLines, imageWidth)
        val leftBoxes = sideExtractor.leftBoxes
        val rightBoxes = sideExtractor.rightBoxes
        val rightAvg = sideExtractor.rightAvg
        val leftAvg = sideExtractor.leftAvg


        // different splitting for one team only
        //different list for "suspicious" entries?
        return if (leftBoxes.size == 18 && rightBoxes.size == 18)// Two full teams "out of the box" TODO TMP? Can lead to errors with teams bigger than 18(rugby/af)
            textLines
        else if (leftBoxes.size < 3 || rightBoxes.size < 3)// values need to be checked, if around 0 & around 0 list numbers and corresponding names.
            defaultFixing(textLines, result, leftAvg, rightAvg, imageWidth)
        else if (leftBoxes.size in 3..10 && rightBoxes.size in 3..10)//if >0 && <10 get cords on full lines, get step on partial lines, mby include removed lines like substitutes
            defaultFixing(textLines, result, leftAvg, rightAvg, imageWidth)
        else
            defaultFixing(textLines, result, leftAvg, rightAvg, imageWidth) // if each size >10 limit names to be in boundingBox.height
    }


    private fun singleLinesFixing(textLines: MutableList<TextLineLight>): MutableList<TextLineLight> {
        return textLines
    }

    private fun noLinesFixing(textLines: MutableList<TextLineLight>): MutableList<TextLineLight> {
        return textLines
    }

    private fun extractValidLines(result: FirebaseVisionText): MutableList<TextLineLight> {
        val textLines = mutableListOf<TextLineLight>()
        for (block in result.textBlocks) {
            for (line in block.lines) {
                Log.d(TAG, line.text.toString() + " " + line.boundingBox.toString())
                val rect = line.boundingBox
                if (LineValidator.isValidLine(line.text) && rect != null) {
                    textLines.add(TextLineLight(TextUtils.splitNumbers(line.text), rect!!))
                }
            }
        }
        return textLines
    }


    private fun defaultFixing(textLines: MutableList<TextLineLight>, result: FirebaseVisionText, leftAvg: Int, rightAvg: Int, imageWidth: Int): MutableList<TextLineLight> {
        mergeLines(leftAvg, result, imageWidth, textLines)
        mergeLines(rightAvg, result, imageWidth, textLines)
        Log.d(TAG, "==============================")
        Log.d(TAG, textLines.toString())
        return textLines
    }
    // inverted order needs right side average
    private fun mergeLines(averagePosition: Int, result: FirebaseVisionText, imageWidth: Int, textLines: MutableList<TextLineLight>) {
        if (averagePosition > 0) {
            val namesLeft = mutableListOf<FirebaseVisionText.Line>()
            val numbersLeft = mutableListOf<FirebaseVisionText.Line>()
            for (block in result.textBlocks) {//TODO height limits
                for (line in block.lines) {
                    val rect = line.boundingBox!!
                    if (line.text.isDigitsOnly() && rect.left < averagePosition + imageWidth * PERCENT_DIFF && rect.left > averagePosition - imageWidth * PERCENT_DIFF) {
                        numbersLeft.add(line)
                    } else if (!LineValidator.isValidLine(line.text) && rect.left > averagePosition + imageWidth * PERCENT_DIFF && rect.left < averagePosition + imageWidth * (MULTIPLIER * PERCENT_DIFF)) {
                        namesLeft.add(line)
                    }

                }
            }
            val numberIterator = numbersLeft.iterator()
            var tmp = 0
            if (namesLeft.size > 0 && numbersLeft.size > 0) {
                namesLeft.forEach {
                    tmp += (it.boundingBox!!.bottom - it.boundingBox!!.top)
                }
                val avgHeight = tmp / namesLeft.size
                val heightPercent = (avgHeight * 0.5).toInt()
                for (number in numberIterator) {
                    val nameIterator = namesLeft.iterator()
                    for (name in nameIterator) {
                        if (name.boundingBox!!.centerY() > number.boundingBox!!.centerY() - heightPercent && name.boundingBox!!.centerY() < number.boundingBox!!.centerY() + heightPercent) {
                            textLines.add(mergeNumberName(number, name))
                            numberIterator.remove()
                            nameIterator.remove()
                        }
                    }
                }
                if (namesLeft.size > 0) {
                    namesLeft.forEach { line ->
                        textLines.add(TextLineLight(TextUtils.splitNumbers(line.text), line.boundingBox!!))
                    }
                }
                if (numbersLeft.size > 0) {
                    numbersLeft.forEach { line ->
                        textLines.add(TextLineLight(TextUtils.splitNumbers(line.text), line.boundingBox!!))
                    }
                }
            }
        }
    }

    private fun mergeNumberName(number: FirebaseVisionText.Line, name: FirebaseVisionText.Line): TextLineLight {

        val left = if (number.boundingBox!!.left > name.boundingBox!!.left)
            name.boundingBox!!.left
        else
            number.boundingBox!!.left

        val right = if (number.boundingBox!!.right > name.boundingBox!!.right)
            number.boundingBox!!.right
        else
            name.boundingBox!!.right

        val bottom = if (number.boundingBox!!.bottom > name.boundingBox!!.bottom)
            number.boundingBox!!.bottom
        else
            name.boundingBox!!.bottom

        val top = if (number.boundingBox!!.top > name.boundingBox!!.top)
            name.boundingBox!!.top
        else
            number.boundingBox!!.top

        return TextLineLight(PlayerData(number.text, name.text), Rect(left, top, right, bottom))

    }
}

private class SideExtractor constructor(textLines: MutableList<TextLineLight>, imageWidth: Int) {
    var leftAvg = 0
    var rightAvg = 0
    val leftBoxes = mutableListOf<Rect>()
    val rightBoxes = mutableListOf<Rect>()
    init {
        splitLeftRight(textLines, imageWidth)
    }
    private fun splitLeftRight(textLines: MutableList<TextLineLight>, imageWidth: Int) {
        val minXLine = textLines.minBy { it.boundingBox.left }
        val maxXLine = textLines.maxBy { it.boundingBox.right }
        val minYLine = textLines.minBy { it.boundingBox.top }
        val maxYLine = textLines.maxBy { it.boundingBox.bottom }

        val minX = minXLine?.boundingBox?.left//TODO +5 / avg height/width of box
        val maxX = maxXLine?.boundingBox?.right
        val minY = minYLine?.boundingBox?.top
        val maxY = maxYLine?.boundingBox?.bottom
        if (minX != null && maxX != null && minY != null && maxY != null) {
            if (textLines.size > 0) {
                for (line in textLines) {
                    if (line.boundingBox.left < minX + (imageWidth * 0.25)) {
                        leftBoxes.add(line.boundingBox)
                    } else {
                        rightBoxes.add(line.boundingBox)
                    }
                }
            }
        }
        if (leftBoxes.size > 0) {
            var tmp = 0
            leftBoxes.forEach {
                tmp += it.left
            }
            leftAvg = tmp / leftBoxes.size
        }
        if (rightBoxes.size > 0) {
            var tmp = 0
            rightBoxes.forEach {
                tmp += it.left
            }
            rightAvg = tmp / rightBoxes.size
        }
    }
}
