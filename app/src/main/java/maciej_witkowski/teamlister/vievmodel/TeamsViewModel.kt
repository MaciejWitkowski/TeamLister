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
import maciej_witkowski.teamlister.model.TextLineLight
import maciej_witkowski.teamlister.utils.CaseFormat
import maciej_witkowski.teamlister.utils.TextUtils

private const val TAG = "TeamsViewModel"

class TeamsViewModel(handle: SavedStateHandle) : ViewModel(), LifecycleObserver {

    private val imageHandle: MutableLiveData<Bitmap> = handle.getLiveData<Bitmap>("Image")//TODO image is too big for parcel
    val image: LiveData<Bitmap> = imageHandle

    private val textLinesHandle: MutableLiveData<MutableList<TextLineLight>> =
        handle.getLiveData<MutableList<TextLineLight>>("TextLines")
    val textLines: LiveData<MutableList<TextLineLight>> = textLinesHandle

    private val rawTeam1Handle: MutableLiveData<MutableList<TextLineLight>> =
        handle.getLiveData<MutableList<TextLineLight>>("RawTeam1")
    val rawTeam1: LiveData<MutableList<TextLineLight>> = rawTeam1Handle

    private val rawTeam2Handle: MutableLiveData<MutableList<TextLineLight>> =
        handle.getLiveData<MutableList<TextLineLight>>("RawTeam2")
    val rawTeam2: LiveData<MutableList<TextLineLight>> = rawTeam2Handle

    private val team1Handle: MutableLiveData<String> = handle.getLiveData<String>("Team1")
    val team1: LiveData<String> = team1Handle

    private val team2Handle: MutableLiveData<String> = handle.getLiveData<String>("Team2")
    val team2: LiveData<String> = team2Handle


    fun setBitmap(bitmap: Bitmap) {
        imageHandle.value = bitmap
        analyzeImage(bitmap)
    }


    private fun rawToTeam(data: MutableList<TextLineLight>?, team: Int) {
        val newSb = StringBuilder()
        if (data != null) {
            for (line in data) {
                var tmp = line.text
                tmp = TextUtils.fixWrongT(tmp)
                tmp = TextUtils.caseFormatting(tmp, CaseFormat.UPPER_LOWER)
                tmp = TextUtils.replaceNonAsciiChars(tmp)
                newSb.append(tmp + "\n")
            }
        }
        if (team == 1)
            team1Handle.value = newSb.toString()
        else if (team == 2)
            team2Handle.value = newSb.toString()
    }


//    private var imageHeight = 0
//    private var imageWidth = 0


    private fun analyzeImage(image: Bitmap) {
        val firebaseVisionImage = FirebaseVisionImage.fromBitmap(image)
        val textRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
        textRecognizer.processImage(firebaseVisionImage)
            .addOnSuccessListener {
                val mutableImage = image.copy(Bitmap.Config.ARGB_8888, true)
                Log.d(TAG, "Success")
                recognizeText(it, mutableImage)
                imageHandle.value = mutableImage
            }
            .addOnFailureListener {
                Log.d(TAG, "failure")
            }
    }

    private fun recognizeText(result: FirebaseVisionText, image: Bitmap) {
        val data = mutableListOf<FirebaseVisionText.Line>()
        val tmp = mutableListOf<TextLineLight>()
        for (block in result.textBlocks) {
            for (line in block.lines) {
                val rect = line.boundingBox
                if (TextUtils.isValidLine(line.text) && rect != null) {
                    data.add(line)
                    tmp.add(TextLineLight(line.text, rect))
                }
            }
        }
        textLinesHandle.value = tmp
        splitAuto(tmp, image)
    }

    private fun splitAuto(data: MutableList<TextLineLight>, image: Bitmap) {
        val imageHeight = image.height
        val imageWidth = image.width
        val canvas = Canvas(image)
        val team1Paint = Paint()
        team1Paint.color = Color.parseColor("#80DEEA")
        team1Paint.style = Paint.Style.STROKE
        team1Paint.strokeWidth = 4F
        val team2Paint = Paint()
        team2Paint.color = Color.parseColor("#CE93D8")
        team2Paint.style = Paint.Style.STROKE
        team2Paint.strokeWidth = 4F
        val teamFirst = mutableListOf<TextLineLight>()
        val teamSecond = mutableListOf<TextLineLight>()
        if (data.size > 0) {
            val min = data.minBy { it.boundingBox.left }
            data.sortBy { it.boundingBox.top }
            for (line in data) {
                if (line.boundingBox.left < (min!!.boundingBox.left + (imageWidth * 0.25))) {
                    canvas.drawRect(line.boundingBox, team1Paint)
                    teamFirst.add(line)
                } else {
                    canvas.drawRect(line.boundingBox, team2Paint)
                    teamSecond.add(line)
                }
            }
        }
        rawTeam1Handle.value = teamFirst
        rawTeam2Handle.value = teamSecond
    }

    fun allTeam1(){
        splitToTeam1(textLinesHandle.value,imageHandle.value)
    }

    fun allTeam2(){
        splitToTeam2(textLinesHandle.value,imageHandle.value)
    }

    fun auto(){
        splitAuto(textLinesHandle.value!!,imageHandle.value!!)//TODO NEED NULL CHECKS
    }

    private fun splitToTeam1(data: MutableList<TextLineLight>?, image: Bitmap?){
        val canvas = Canvas(image)
        val paint = Paint()
        paint.color = Color.parseColor("#80DEEA")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4F
        val teamFirst = mutableListOf<TextLineLight>()
        val teamSecond = mutableListOf<TextLineLight>()
        if (data!=null) {
            for (line in data) {
                    canvas.drawRect(line.boundingBox, paint)
                    teamFirst.add(line)
                }
        }
        rawTeam1Handle.value = teamFirst
        rawTeam2Handle.value = teamSecond
    }
    private fun splitToTeam2(data: MutableList<TextLineLight>?, image: Bitmap?){
        val canvas = Canvas(image)
        val paint = Paint()
        paint.color = Color.parseColor("#CE93D8")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4F
        val teamFirst = mutableListOf<TextLineLight>()
        val teamSecond = mutableListOf<TextLineLight>()
        if (data!=null) {
            for (line in data) {
                canvas.drawRect(line.boundingBox, paint)
                teamSecond.add(line)
            }
        }
        rawTeam1Handle.value = teamFirst
        rawTeam2Handle.value = teamSecond
    }
    
    fun acceptResult() {
        rawToTeam(rawTeam1Handle.value, 1)
        rawToTeam(rawTeam2Handle.value, 2)
        //Raw team1 clean
        //Raw team2 clean
        //Bitmap clean
    }

}