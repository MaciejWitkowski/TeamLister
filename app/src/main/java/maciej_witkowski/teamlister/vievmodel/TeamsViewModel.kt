package maciej_witkowski.teamlister.vievmodel

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import maciej_witkowski.teamlister.model.FirebaseTextRecognition
import maciej_witkowski.teamlister.model.RawDataModel
import maciej_witkowski.teamlister.model.TextRecognitionModel

private const val TAG = "TeamsVievModel"

class TeamsViewModel(handle: SavedStateHandle) : ViewModel(), LifecycleObserver {

    private val imageHandle: MutableLiveData<Bitmap> = handle.getLiveData<Bitmap>("Image")
    val image: LiveData<Bitmap> = imageHandle

    private val rawTeam1Handle: MutableLiveData<MutableList<RawDataModel>> =
        handle.getLiveData<MutableList<RawDataModel>>("RawTeam1")
    val rawTeam1: LiveData<MutableList<RawDataModel>> = rawTeam1Handle

    private val team1Handle: MutableLiveData<String> = handle.getLiveData<String>("Team1")
    val team1: LiveData<String> = team1Handle

    private val rawTeam2Handle: MutableLiveData<MutableList<RawDataModel>> =
        handle.getLiveData<MutableList<RawDataModel>>("RawTeam2")
    val rawTeam2: LiveData<MutableList<RawDataModel>> = rawTeam1Handle

    private val team2Handle: MutableLiveData<String> = handle.getLiveData<String>("Team2")
    val team2: LiveData<String> = team2Handle


    fun setBitmap(bitmap: Bitmap) {
        imageHandle.value = bitmap
        analyzeImage(bitmap)
    }

    fun analyze(bitmap: Bitmap?) {

        if (bitmap != null) {
        }
    }

    fun tmp() {
        Single.fromCallable { analyze(imageHandle.value) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }


    //private fun appendTeam(){}   raw team to team


    private val textRecognitionModels = ArrayList<TextRecognitionModel>()
    private var imageHeight = 0
    private var imageWidth = 0
    private var teamFirst = mutableListOf<FirebaseVisionText.Line>()
    private var teamSecond = mutableListOf<FirebaseVisionText.Line>()

    private fun analyzeImage(image: Bitmap) {
        textRecognitionModels.clear()
        val firebaseVisionImage = FirebaseVisionImage.fromBitmap(image)
        val textRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
        textRecognizer.processImage(firebaseVisionImage)
            .addOnSuccessListener {
                val mutableImage = image.copy(Bitmap.Config.ARGB_8888, true)
                Log.d(TAG, "Success")
                var data=recognizeText(it, mutableImage)
                //sortLines(data)
                imageHandle.value = mutableImage
            }
            .addOnFailureListener {
                Log.d(TAG, "failure")
            }
    }

    private fun recognizeText(result: FirebaseVisionText?, image: Bitmap?): MutableList<FirebaseVisionText.Line>? {
        if (result == null || image == null) {
            // Toast.makeText(requireContext(), "There was some error", Toast.LENGTH_SHORT).show()
            return null
        }
        imageHeight = image.height
        imageWidth = image.width
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
                    sb.append(line.text +"\n")
                    //val lineData = NameData(text = line.text, cord = line.getCornerPoints()!![0].y.toFloat())
                    data.add(line)
                }
                textRecognitionModels.add(TextRecognitionModel(index++, line.text))
            }
        }
        sortLines(data)//TODO out
        return data
    }


    private fun sortLines(data: MutableList<FirebaseVisionText.Line>) {
        Log.d(TAG, data.size.toString())
        if (data.size > 0) {
            val min = data.minBy { it.cornerPoints!![0].x }
            Log.d(TAG, "Min: "+min?.cornerPoints!![0].x)
            data.sortBy { it.cornerPoints!![0].y.toFloat() }
            val newSb = StringBuilder()
            for (line in data) {
                if (line.cornerPoints!![0].x < (min.cornerPoints!![0].x + (imageHeight * 0.25))) {
                    teamFirst.add(line)
                    newSb.append(line.text + "\n")
                } else {
                    teamSecond.add(line)
                }
            }
        }
    }



    private fun teamToLogD(data: MutableList<FirebaseVisionText.Line>) {
        for (line in data) {
            Log.d(TAG, line.text + " " + line.getCornerPoints()!![0].toString())
            Log.d(TAG, line.cornerPoints!![1].toString())
            Log.d(TAG, line.getCornerPoints()!![2].toString())
            Log.d(TAG, line.getCornerPoints()!![3].toString())
        }
    }


    private fun isValidLine(line: FirebaseVisionText.Line): Boolean {
        var isValid = false
        if (line.text[0].isDigit() && line.text.length > 5)
            isValid = true
        return isValid
    }

}