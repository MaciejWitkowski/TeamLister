package maciej_witkowski.teamlister.model

import android.graphics.Rect
import androidx.core.text.isDigitsOnly
import com.google.firebase.ml.vision.text.FirebaseVisionText
import maciej_witkowski.teamlister.utils.TextUtils

private val TAG = LineExtractor::class.java.simpleName
private const val PERCENT_DIFF = 0.03
private const val MAX = 100 + PERCENT_DIFF
private const val MIN = 100 - PERCENT_DIFF


class LineExtractor {

    fun getValidTextLines(result: FirebaseVisionText, imageWidth: Int): MutableList<TextLineLight> {
        val textLines = mutableListOf<TextLineLight>()
        for (block in result.textBlocks) {
            for (line in block.lines) {
                val rect = line.boundingBox
                if (TextUtils.isValidLine(line.text) && rect != null) {
                    textLines.add(TextLineLight(TextUtils.splitNumbers(line.text), rect!!))
                }
            }
        }
   //    return textLines}}

        val minXLine = textLines.minBy { it.boundingBox.left }
        val maxXLine = textLines.maxBy { it.boundingBox.right }
        val minYLine = textLines.minBy { it.boundingBox.top }
        val maxYLine = textLines.maxBy { it.boundingBox.bottom }

        val minX = minXLine?.boundingBox?.left//TODO +5 / avg height/width of box
        val maxX = maxXLine?.boundingBox?.right
        val minY = minYLine?.boundingBox?.top
        val maxY = maxYLine?.boundingBox?.bottom

        if (minX != null && maxX != null && minY != null && maxY != null) {
            val leftBoxes = mutableListOf<Rect>()
            val rightBoxes = mutableListOf<Rect>()
            if (textLines.size > 0) {
                for (line in textLines) {
                    if (line.boundingBox.left < minX + (imageWidth * 0.25)) {
                        leftBoxes.add(line.boundingBox)
                    } else {
                        rightBoxes.add(line.boundingBox)
                    }
                }
            }

            
                var tmp = 0
                leftBoxes.forEach {
                    tmp += it.left
                }
                val leftAvg = tmp / leftBoxes.size


                tmp = 0
                rightBoxes.forEach {
                    tmp += it.left
                }
                val rightAvg = tmp / rightBoxes.size

                val namesLeft = mutableListOf<FirebaseVisionText.Line>()
                val namesRight = mutableListOf<FirebaseVisionText.Line>()
                val numbersLeft = mutableListOf<FirebaseVisionText.Line>()
                val numbersRight = mutableListOf<FirebaseVisionText.Line>()



                for (block in result.textBlocks) {//TODO height limits
                    for (line in block.lines) {
                        val rect = line.boundingBox!!
                        if (line.text.isDigitsOnly() && rect.left < leftAvg + imageWidth * PERCENT_DIFF && rect.left > leftAvg - imageWidth * PERCENT_DIFF) {
                            // textLines.add(TextLineLight(TextUtils.splitNumbers(line.text), rect))
                            numbersLeft.add(line)
                        } else if (line.text.isDigitsOnly() && rect.left < rightAvg + imageWidth * PERCENT_DIFF && rect.left > rightAvg - imageWidth * PERCENT_DIFF) {
                            //  textLines.add(TextLineLight(TextUtils.splitNumbers(line.text), rect))
                            numbersRight.add(line)
                        } else if (!TextUtils.isValidLine(line.text) && rect.left > leftAvg + imageWidth * PERCENT_DIFF && rect.left < leftAvg + imageWidth * (3 * PERCENT_DIFF)) {
                            //   textLines.add(TextLineLight(TextUtils.splitNumbers(line.text), rect))
                            namesLeft.add(line)
                        } else if (!TextUtils.isValidLine(line.text) && rect.left > rightAvg + imageWidth * PERCENT_DIFF && rect.left < rightAvg + imageWidth * (3 * PERCENT_DIFF)) {
                            //    textLines.add(TextLineLight(TextUtils.splitNumbers(line.text), rect))
                            namesRight.add(line)
                        }

                    }
                }

            val rightNumberIterator = numbersRight.iterator()
            if (namesRight.size > 0 && numbersRight.size > 0) {
                tmp = 0
                namesRight.forEach {
                    tmp += (it.boundingBox!!.bottom - it.boundingBox!!.top)
                }
                var avgHeight = tmp / namesRight.size
                val heightPercent = (avgHeight * 0.5).toInt()


                for (number in rightNumberIterator) {
                    val rightNameIterator = namesRight.iterator()
                    for (name in rightNameIterator) {
                        if (name.boundingBox!!.centerY() > number.boundingBox!!.centerY() - heightPercent && name.boundingBox!!.centerY() < number.boundingBox!!.centerY() + heightPercent) {
                            textLines.add(mergeLines(number, name))
                            rightNumberIterator.remove()
                            rightNameIterator.remove()
                        }
                    }
                }
                    if (namesRight.size>0){
                        namesRight.forEach {line->
                            textLines.add(TextLineLight(TextUtils.splitNumbers(line.text), line.boundingBox!!))
                        }
                    }
                if (numbersRight.size>0){
                    numbersRight.forEach {line->
                        textLines.add(TextLineLight(TextUtils.splitNumbers(line.text), line.boundingBox!!))
                    }
                }
                }

            val leftNumberIterator = numbersLeft.iterator()
            if (namesLeft.size > 0 && numbersLeft.size > 0) {
                tmp = 0
                namesLeft.forEach {
                    tmp += (it.boundingBox!!.bottom - it.boundingBox!!.top)
                }
                var avgHeight = tmp / namesLeft.size
                val heightPercent = (avgHeight * 0.5).toInt()


                for (number in leftNumberIterator) {
                    val leftNameIterator = namesLeft.iterator()
                    for (name in leftNameIterator) {
                        if (name.boundingBox!!.centerY() > number.boundingBox!!.centerY() - heightPercent && name.boundingBox!!.centerY() < number.boundingBox!!.centerY() + heightPercent) {
                            textLines.add(mergeLines(number, name))
                            leftNumberIterator.remove()
                            leftNameIterator.remove()
                        }
                    }
                }
                if (namesLeft.size>0){
                    namesLeft.forEach {line->
                        textLines.add(TextLineLight(TextUtils.splitNumbers(line.text), line.boundingBox!!))
                    }
                }
                if (numbersLeft.size>0){
                    numbersLeft.forEach {line->
                        textLines.add(TextLineLight(TextUtils.splitNumbers(line.text), line.boundingBox!!))
                    }
                }
            }

            }
            return textLines

        }

        private fun mergeLines(number: FirebaseVisionText.Line, name: FirebaseVisionText.Line): TextLineLight {

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