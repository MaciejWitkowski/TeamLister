package maciej_witkowski.teamlister.vievmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.*
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import maciej_witkowski.teamlister.model.PlayerData
import maciej_witkowski.teamlister.model.TextLineLight
import maciej_witkowski.teamlister.utils.CaseFormat
import maciej_witkowski.teamlister.utils.RemoveBracketFormat
import maciej_witkowski.teamlister.utils.TextUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


private const val TAG = "TeamsViewModel"

class TeamsViewModel(app: Application, handle: SavedStateHandle) : AndroidViewModel(app) {
    @NonNull
    override fun <T : Application> getApplication(): T {
        return super.getApplication()
    }

    private val imageHandle: MutableLiveData<Bitmap> =
        handle.getLiveData<Bitmap>("Image")//TODO image is too big for parcel, also some kind of history can be implemented
    val image: LiveData<Bitmap> = imageHandle

    private val textLinesHandle: MutableLiveData<MutableList<TextLineLight>> =
        handle.getLiveData<MutableList<TextLineLight>>("TextLines")
    val textLines: LiveData<MutableList<TextLineLight>> = textLinesHandle

    private val rawTeam1Handle: MutableLiveData<MutableList<PlayerData>> =
        handle.getLiveData<MutableList<PlayerData>>("RawTeam1")
    val rawTeam1: LiveData<MutableList<PlayerData>> = rawTeam1Handle

    private val rawTeam2Handle: MutableLiveData<MutableList<PlayerData>> =
        handle.getLiveData<MutableList<PlayerData>>("RawTeam2")
    val rawTeam2: LiveData<MutableList<PlayerData>> = rawTeam2Handle

    private val team1Handle: MutableLiveData<String> = handle.getLiveData<String>("Team1")
    val team1: LiveData<String> = team1Handle

    private val team2Handle: MutableLiveData<String> = handle.getLiveData<String>("Team2")
    val team2: LiveData<String> = team2Handle


    fun setBitmap(bitmap: Bitmap) {
        imageHandle.value = bitmap
        analyzeImage(bitmap)

    }


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
        val tmp = mutableListOf<TextLineLight>()
        for (block in result.textBlocks) {
            for (line in block.lines) {
                val rect = line.boundingBox
                if (TextUtils.isValidLine(line.text) && rect != null) {
                    tmp.add(TextLineLight(TextUtils.splitNumbers(line.text), rect))
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
        val teamFirst = mutableListOf<PlayerData>()
        val teamSecond = mutableListOf<PlayerData>()
        if (data.size > 0) {
            val min = data.minBy { it.boundingBox.left }
            data.sortBy { it.boundingBox.top }
            for (line in data) {
                if (line.boundingBox.left < (min!!.boundingBox.left + (imageWidth * 0.25))) {
                    canvas.drawRect(line.boundingBox, team1Paint)
                    teamFirst.add(line.data)
                } else {
                    canvas.drawRect(line.boundingBox, team2Paint)
                    teamSecond.add(line.data)
                }
            }
        }
        rawTeam1Handle.value = teamFirst
        rawTeam2Handle.value = teamSecond
    }

    fun allTeam1() {
        splitToTeam1(textLinesHandle.value, imageHandle.value)
    }

    fun allTeam2() {
        splitToTeam2(textLinesHandle.value, imageHandle.value)
    }

    fun auto() {
        splitAuto(textLinesHandle.value!!, imageHandle.value!!)
    }

    private fun splitToTeam1(data: MutableList<TextLineLight>?, image: Bitmap?) {
        val canvas = Canvas(image)
        val paint = Paint()
        paint.color = Color.parseColor("#80DEEA")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4F
        val teamFirst = mutableListOf<PlayerData>()
        val teamSecond = mutableListOf<PlayerData>()
        if (data != null) {
            for (line in data) {
                canvas.drawRect(line.boundingBox, paint)
                teamFirst.add(line.data)
            }
        }
        rawTeam1Handle.value = teamFirst
        rawTeam2Handle.value = teamSecond
    }

    private fun splitToTeam2(data: MutableList<TextLineLight>?, image: Bitmap?) {
        val canvas = Canvas(image)
        val paint = Paint()
        paint.color = Color.parseColor("#CE93D8")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4F
        val teamFirst = mutableListOf<PlayerData>()
        val teamSecond = mutableListOf<PlayerData>()
        if (data != null) {
            for (line in data) {
                canvas.drawRect(line.boundingBox, paint)
                teamSecond.add(line.data)
            }
        }
        rawTeam1Handle.value = teamFirst
        rawTeam2Handle.value = teamSecond
    }

    fun acceptResult() {
        rawToTeam1(rawTeam1Handle.value, team1Handle)
        rawToTeam2(rawTeam2Handle.value, team2Handle)
        //rawToTeam(rawTeam1Handle.value, team1Handle)
        //rawToTeam(rawTeam2Handle.value, team2Handle)
        //Raw team1 clean //TODO
        //Raw team2 clean
        //Bitmap clean
    }

    private fun rawToTeam1(data: MutableList<PlayerData>?, output: MutableLiveData<String>) {
        val sharedPref = getDefaultSharedPreferences(getApplication<Application>().applicationContext)
        val append = sharedPref.getString("append_t1", null)
        val prepend = sharedPref.getString("prepend_t1", null)
        val numberPosition = sharedPref.getString("number", "start")
        rawTT(data, output, append, prepend, numberPosition)
    }


    private fun rawToTeam2(data: MutableList<PlayerData>?, output: MutableLiveData<String>) {
        val sharedPref = getDefaultSharedPreferences(getApplication<Application>().applicationContext)
        val append = sharedPref.getString("append_t2", null)
        val prepend = sharedPref.getString("prepend_t2", null)
        val numberPosition = sharedPref.getString("number", "start")
        rawTT(data, output, append, prepend, numberPosition)
    }


    private fun rawTT(
        data: MutableList<PlayerData>?,
        output: MutableLiveData<String>,
        append: String?,
        prepend: String?,
        numberPosition: String?
    ) {
        val newSb = StringBuilder()
        if (data != null) {
            for (line in data) {
                var tmp = line.name
                tmp = TextUtils.fixWrongT(tmp)  //TODO to builder based on settings
                tmp = TextUtils.caseFormatting(tmp, CaseFormat.UPPER_LOWER)
                tmp = TextUtils.replaceNonAsciiChars(tmp)
                tmp = TextUtils.removeBrackets(tmp, RemoveBracketFormat.ALL)
                if (numberPosition.equals("start")) {
                    newSb.append(append + line.number + prepend + " " + tmp + "\r\n")
                } else if (numberPosition.equals("end")) {
                    newSb.append(tmp + " " + append + line.number + prepend + "\r\n")
                }
                Log.d(TAG, tmp)
            }
        }
        output.value = newSb.toString()

    }

    private fun rawToTeam(data: MutableList<PlayerData>?, output: MutableLiveData<String>) {
        val newSb = StringBuilder()
        if (data != null) {
            for (line in data) {
                var tmp = line.name
                tmp = TextUtils.fixWrongT(tmp)  //TODO to builder
                tmp = TextUtils.caseFormatting(tmp, CaseFormat.UPPER_LOWER)
                tmp = TextUtils.replaceNonAsciiChars(tmp)
                newSb.append(line.number + " " + tmp + "\r\n") //TODO based on settings
                //TODO number append/preppend
                Log.d(TAG, tmp)
            }
        }
        output.value = newSb.toString()
    }


    fun saveToFiles() {
        val sharedPref = getDefaultSharedPreferences(getApplication<Application>().applicationContext)
        val twoFiles = sharedPref.getBoolean("saveto2", true)
        val folder = sharedPref.getString("folder", "team_lister")
        if (twoFiles) {
            var name1 = sharedPref.getString("name_t1", "team1")
            if (!name1.isNullOrEmpty())
                name1 += ".txt"
            else
                name1 = "team1.txt"
            var name2 = sharedPref.getString("name_t2", "team1")
            if (!name2.isNullOrEmpty())
                name2 += ".txt"
            else
                name2 = "team2.txt"
            saveTeam(team1Handle.value, name1, folder)
            saveTeam(team2Handle.value, name2, folder)
        } else {
            var name = sharedPref.getString("name_t1", "team1")
            if (!name.isNullOrEmpty())
                name += ".txt"
            else
                name = "team1.txt"
            saveTeam(team1Handle.value + team2Handle.value, name, folder)
        }
    }

    private fun saveTeam(team: String?, filename: String, folderName: String?) {
        if (!team.isNullOrEmpty()) {
            var filepath = Environment.getExternalStorageDirectory().absolutePath.toString()
            if (!folderName.isNullOrEmpty()) {
                filepath = filepath + "/" + folderName
                val folder = File((Environment.getExternalStorageDirectory()).toString() + File.separator + folderName)
                if (!folder.exists()) {
                    folder.mkdir()
                }
            }
            Log.d("PATH", "first path: " + filepath)
            val myExternalFile = File(filepath, filename)
            try {
                Log.e("PATH", "final path: " + myExternalFile.absolutePath)
                val fileOutPutStream = FileOutputStream(myExternalFile, false)
                fileOutPutStream.write(team.toByteArray())
                fileOutPutStream.flush()
                fileOutPutStream.close()
                myExternalFile.setExecutable(true)
                myExternalFile.setReadable(true)
                myExternalFile.setWritable(true)
                MediaScannerConnection.scanFile(
                    getApplication<Application>().applicationContext,
                    arrayOf(myExternalFile.toString()),
                    null,
                    null
                )
                Log.d("PATH", "ok")
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d("PATH", "error")
            }
        }
    }

    fun updateProcessedTeam(text: String, team: Int) {
        if (team == 1) {
            team1Handle.value = text
        } else if (team == 2) {
            team2Handle.value = text
        }

    }
}