package maciej_witkowski.teamlister.vievmodel

import android.app.Application
import android.graphics.*
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
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.utils.*


private val TAG = TeamsViewModel::class.java.simpleName

class TeamsViewModel(app: Application, handle: SavedStateHandle) : AndroidViewModel(app) {
    @NonNull
    override fun <T : Application> getApplication(): T {
        return super.getApplication()
    }

    val imagePathHandle: MutableLiveData<String> = handle.getLiveData<String>("path")

    private val textLinesHandle: MutableLiveData<MutableList<TextLineLight>> =
        handle.getLiveData<MutableList<TextLineLight>>("TextLines")

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

    private val _toastMessage = MutableLiveData<Event<String>>()

    val toastMessage: LiveData<Event<String>>
        get() = _toastMessage

    val imageNew: MutableLiveData<Bitmap> = getImage(imagePathHandle.value)

    private fun getImage(path: String?): MutableLiveData<Bitmap> {
        val imageLiveData = MutableLiveData<Bitmap>()
        if (!path.isNullOrEmpty()) {
            Log.d(TAG, path)
            val myBitmap = getBitmapWithOrient(path)
            val mutableImage = myBitmap.copy(Bitmap.Config.ARGB_8888, true)
            if (!textLinesHandle.value.isNullOrEmpty()) {
                splitAuto(textLinesHandle.value!!, mutableImage)
            }
            imageLiveData.value = mutableImage

        } else
            Log.d(TAG, "Path Null")
        return imageLiveData
    }


    private fun getBitmapWithOrient(path: String): Bitmap {//check size
        Log.d(TAG, path)
        val exif = ExifInterface(path)
        val rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val rotationInDegrees = ImageUtils.exifToDegrees(rotation)
        val myBitmap = BitmapFactory.decodeFile(path)
        val matrix = Matrix()
        var height = myBitmap.height
        var width = myBitmap.width
        if (height > 4096)
            height = 4096
        if (width > 4096)
            width = 4096
        if (rotation != 0) {
            matrix.preRotate(rotationInDegrees)
            return Bitmap.createBitmap(myBitmap, 0, 0, width, height, matrix, true)
        } else if (myBitmap.height > 4096 || myBitmap.width > 4096) {
            return Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.width, myBitmap.height)
        }
        return myBitmap
    }


    fun setImagePath(path: String) {
        Log.d(TAG, "set bitmap")
        imagePathHandle.value = path
        val bitmap = getBitmapWithOrient(path)
        imageNew.value = bitmap
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
                imageNew.value = mutableImage
                _toastMessage.value = Event("Image analyzed")
            }
            .addOnFailureListener {
                _toastMessage.value = Event("Error, cannot analyze image")
                Log.d(TAG, "failure")
            }
    }

    private fun recognizeText(result: FirebaseVisionText, image: Bitmap) {
        val tmp = mutableListOf<TextLineLight>()
        for (block in result.textBlocks) {
            for (line in block.lines) {
                val rect = line.boundingBox
                if (TextUtils.isValidLine(line.text) && rect != null) {
                    Log.d(TAG, line.text)
                    tmp.add(TextLineLight(TextUtils.splitNumbers(line.text), rect))
                }
            }
        }
        textLinesHandle.value = tmp
        splitAuto(tmp, image)
    }


    fun allTeam1() {
        Log.d(TAG, "allTeam1")
        if (isDataAvailable())
            splitToTeam1(textLinesHandle.value, imageNew.value)
    }

    fun allTeam2() {
        Log.d(TAG, "allTeam2")
        if (isDataAvailable())
            splitToTeam2(textLinesHandle.value, imageNew.value)
    }

    fun auto() {
        Log.d(TAG, "auto")
        if (isDataAvailable())
            splitAuto(textLinesHandle.value!!, imageNew.value!!)
    }


    private fun isDataAvailable(): Boolean {
        val isValid: Boolean
        if (!textLinesHandle.value.isNullOrEmpty() && imageNew.value != null) {
            isValid = true
        } else if (imageNew.value == null) {
            isValid = false
            _toastMessage.value = Event("Take image first")
        } else {
            isValid = false
            _toastMessage.value = Event("Text lines not found")
        }
        return isValid
    }


    private fun splitAuto(data: MutableList<TextLineLight>, image: Bitmap) {
        val imageHeight = image.height
        val imageWidth = image.width
        val canvas = Canvas(image)
        val team1Paint = Paint()
        team1Paint.color = ContextCompat.getColor(getApplication<Application>().applicationContext, R.color.team_1)
        team1Paint.style = Paint.Style.STROKE
        team1Paint.strokeWidth = 6F
        val team2Paint = Paint()
        team2Paint.color = ContextCompat.getColor(getApplication<Application>().applicationContext, maciej_witkowski.teamlister.R.color.team_2)
        team2Paint.style = Paint.Style.STROKE
        team2Paint.strokeWidth = 6F
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
        imageNew.value = imageNew.value
    }

    private fun splitToTeam1(data: MutableList<TextLineLight>?, image: Bitmap?) {
        val canvas = Canvas(image)
        val paint = Paint()
        paint.color = ContextCompat.getColor(getApplication<Application>().applicationContext, R.color.team_1)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 6F
        val teamFirst = mutableListOf<PlayerData>()
        val teamSecond = mutableListOf<PlayerData>()
        if (data != null) {
            for (line in data) {
                Log.d(TAG, "Team 1 drawing")
                canvas.drawRect(line.boundingBox, paint)
                teamFirst.add(line.data)
            }
        }
        imageNew.value = imageNew.value//to refresh bitmap
        rawTeam1Handle.value = teamFirst
        rawTeam2Handle.value = teamSecond
    }

    private fun splitToTeam2(data: MutableList<TextLineLight>?, image: Bitmap?) {
        val canvas = Canvas(image)
        val paint = Paint()
        paint.color = ContextCompat.getColor(getApplication<Application>().applicationContext,R.color.team_2)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 6F
        val teamFirst = mutableListOf<PlayerData>()
        val teamSecond = mutableListOf<PlayerData>()
        if (data != null) {
            for (line in data) {
                Log.d(TAG, "Team 2 drawing")
                canvas.drawRect(line.boundingBox, paint)
                teamSecond.add(line.data)
            }
        }
        imageNew.value = imageNew.value
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
        rawToTeam(data, output, append, prepend, numberPosition)
    }


    private fun rawToTeam2(data: MutableList<PlayerData>?, output: MutableLiveData<String>) {
        val sharedPref = getDefaultSharedPreferences(getApplication<Application>().applicationContext)
        val append = sharedPref.getString("append_t2", null)
        val prepend = sharedPref.getString("prepend_t2", null)
        val numberPosition = sharedPref.getString("number", "start")
        rawToTeam(data, output, append, prepend, numberPosition)
    }


    private fun rawToTeam(
        data: MutableList<PlayerData>?,
        output: MutableLiveData<String>,
        append: String?,
        prepend: String?,
        numberPosition: String?
    ) {
        val newSb = StringBuilder()
        val sharedPref = getDefaultSharedPreferences(getApplication<Application>().applicationContext)
        val fixT = sharedPref.getBoolean("fixt", true)
        val case = sharedPref.getString("case", "UPPER_LOWER")
        val replaceAscii = sharedPref.getBoolean("replace_ascii", true)
        val brackets = sharedPref.getString("brackets", "NONE")
        val caseEnum: CaseFormat
        caseEnum = try {
            CaseFormat.valueOf(case!!)
        } catch (e: IllegalArgumentException) {
            Log.d(TAG, "INVALID CaseFormat value: $case $e")
            CaseFormat.UPPER_LOWER
        }
        val bracketsEnum: RemoveBracketFormat
        bracketsEnum = try {
            RemoveBracketFormat.valueOf(brackets!!)
        } catch (e: IllegalArgumentException) {
            Log.d(TAG, "INVALID RemoveBracketFormat value: $case $e")
            RemoveBracketFormat.NONE
        }
        if (data != null) {
            val names =
                getApplication<Application>().applicationContext.resources.getStringArray(R.array.names).toList()
            Log.d(TAG, "Names size: " + names.size.toString())
            for (line in data) {
                var tmp = line.name
                tmp = TextUtils.fixDollarSign(tmp)
                if (fixT) {
                    tmp = TextUtils.dictionaryNameFix(tmp, names)
                    tmp = TextUtils.fixWrongT(tmp)  //TODO to builder, now needs to be called in particular order
                }
                tmp = TextUtils.caseFormatting(tmp, caseEnum)
                if (replaceAscii)
                    tmp = TextUtils.replaceNonAsciiChars(tmp)
                tmp = TextUtils.removeBrackets(tmp, bracketsEnum)
                if (numberPosition.equals("start")) {
                    newSb.append(append + line.number + prepend + " " + tmp + "\r\n")
                } else if (numberPosition.equals("end")) {
                    newSb.append(tmp + " " + append + line.number + prepend + "\r\n")
                }

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
                filepath = "$filepath/$folderName"
                val folder = File((Environment.getExternalStorageDirectory()).toString() + File.separator + folderName)
                if (!folder.exists()) {
                    folder.mkdir()
                }
            }
            Log.d(TAG, "first path: $filepath")
            val myExternalFile = File(filepath, filename)
            try {
                Log.d(TAG, "final path: " + myExternalFile.absolutePath)
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
                Log.d(TAG, "ok")
            } catch (e: IOException) {
                e.printStackTrace()
                _toastMessage.value = Event("Error, file not saved")
                Log.d(TAG, "error")
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