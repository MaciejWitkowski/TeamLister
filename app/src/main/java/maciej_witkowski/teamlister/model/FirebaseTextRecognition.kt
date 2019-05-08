package maciej_witkowski.teamlister.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.fragment_camera.*

private const val TAG = "FirebaseTextRecognition"

class FirebaseTextRecognition() {
    private val textRecognitionModels = ArrayList<TextRecognitionModel>()
    private var imageHeight=0
    private var imageWidth=0
    private var teamFirst = mutableListOf<FirebaseVisionText.Line>()
    private var teamSecond= mutableListOf<FirebaseVisionText.Line>()
    //lateinit var imageAnalyzed: Bitmap
//    init {
  //      analyzeImage(image)

    //}

    fun analyzeImage(image:Bitmap):Bitmap? {
        textRecognitionModels.clear()
        var testImage:Bitmap?=null
        val firebaseVisionImage = FirebaseVisionImage.fromBitmap(image)
        val textRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
        textRecognizer.processImage(firebaseVisionImage)
            .addOnSuccessListener {
                val mutableImage = image.copy(Bitmap.Config.ARGB_8888, true)
                imageHeight=mutableImage.height
                imageWidth=mutableImage.width
                Log.d(TAG, "Success")
                Log.d(TAG,"Height: "+mutableImage.height.toString()+ " Width: "+mutableImage.width.toString())
                recognizeText(it, mutableImage)
                testImage=mutableImage
               // imageAnalyzed=mutableImage
               // imageView.setImageBitmap(mutableImage)
            }
            .addOnFailureListener {
                Log.d(TAG, "failure")
                //Toast.makeText(requireContext(), "There was some error", Toast.LENGTH_SHORT).show()
            }
        Log.d(TAG,"Height: "+testImage?.height.toString()+ " Width: "+testImage?.width.toString())
        return testImage
    }

    private fun recognizeText(result: FirebaseVisionText?, image: Bitmap?) {
        if (result == null || image == null) {
           // Toast.makeText(requireContext(), "There was some error", Toast.LENGTH_SHORT).show()
            return
        }

        val canvas = Canvas(image)
        val rectPaint = Paint()
        rectPaint.color = Color.RED
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 4F
        val textPaint = Paint()
        textPaint.color = Color.RED
        textPaint.textSize = 40F

        var index = 0
        val sb = StringBuilder()
        var data = mutableListOf<FirebaseVisionText.Line>()
        for (block in result.textBlocks) {
            for (line in block.lines) {
                if (isValidLine(line)) {
                    canvas.drawRect(line.boundingBox, rectPaint)
                    sb.append(line.text)
                    sb.append("\n")
                    //val lineData = NameData(text = line.text, cord = line.getCornerPoints()!![0].y.toFloat())
                    data.add(line)
                }
                textRecognitionModels.add(TextRecognitionModel(index++, line.text))
            }
        }
        sortLines(data)
    }


    private fun sortLines(data: MutableList<FirebaseVisionText.Line>){//TODO crashes if empty
        val min=data.minBy {it.cornerPoints!![0].x  }
        Log.d(TAG, "Min: "+min?.cornerPoints!![0].x)//NPE TODO
        data.sortBy { it.cornerPoints!![0].y.toFloat() }
        val newSb = StringBuilder()
        for (line in data) {
            if(line.cornerPoints!![0].x<(min.cornerPoints!![0].x+(imageHeight*0.25))) {
                teamFirst.add(line)
                newSb.append(line.text + "\n")
            }
            else{
                teamSecond.add(line)
            }
        }
       // teamToLogD(teamFirst)
      //  Log.d(TAG, "-------------------------------------------")
       // teamToLogD(teamSecond)
        //message.setText(newSb)
    }

    private fun teamToLogD(data: MutableList<FirebaseVisionText.Line>){
        for (line in data) {
            Log.d(TAG, line.text + " " + line.getCornerPoints()!![0].toString())
            Log.d(TAG, line.cornerPoints!![1].toString())
            Log.d(TAG, line.getCornerPoints()!![2].toString())
            Log.d(TAG, line.getCornerPoints()!![3].toString())
        }
    }


    private fun isValidLine(line: FirebaseVisionText.Line):Boolean{
        var isValid=false
        if (line.text[0].isDigit() && line.text.length > 5)
            isValid=true
        return isValid
    }

}
