package maciej_witkowski.teamlister.model

import android.util.Log
import com.google.firebase.ml.vision.text.FirebaseVisionText
import maciej_witkowski.teamlister.utils.TextUtils

private val TAG = LineExtractor::class.java.simpleName
class LineExtractor {

     fun getValidTextLines(result: FirebaseVisionText):MutableList<TextLineLight> {
        val textLines = mutableListOf<TextLineLight>()
        for (block in result.textBlocks) {
            for (line in block.lines) {
                val rect = line.boundingBox
               if (TextUtils.isValidLine(line.text) && rect != null) {
                    textLines.add(TextLineLight(TextUtils.splitNumbers(line.text), rect))
                }
            }
        }
        return textLines
    }
}