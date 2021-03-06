package maciej_witkowski.teamlister.vievmodel

import android.app.Application
import android.graphics.*
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.*
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import java.io.IOException
import maciej_witkowski.teamlister.model.*
import maciej_witkowski.teamlister.utils.*


private val TAG = TeamsViewModel::class.java.simpleName

class TeamsViewModel(private val app: Application, private val handle: SavedStateHandle) : AndroidViewModel(app) {
    private val bitmapLoader=BitmapLoader(app)
    @NonNull
    override fun <T : Application> getApplication(): T {
        return super.getApplication()
    }

    var imagePath: String?
        get() = handle.get("path")
        private set(value) {
            handle.set("path", value)
        }

    var isPhotoAvailable: Boolean?
        get() = handle.get("isAvailable")
        private set(value) {
            handle.set("isAvailable", value)
        }
    private var textLines: MutableList<TextLineLight>?
        get() = handle.get("textLines")
        private set(value) {
            handle.set("textLines", value)
        }


    private val rawTeam1Handle: MutableLiveData<List<PlayerData>> =
        handle.getLiveData<List<PlayerData>>("RawTeam1")

    val rawTeam1: LiveData<List<PlayerData>> = rawTeam1Handle

    private val rawTeam2Handle: MutableLiveData<List<PlayerData>> =
        handle.getLiveData<List<PlayerData>>("RawTeam2")
    val rawTeam2: LiveData<List<PlayerData>> = rawTeam2Handle

    private val team1Handle: MutableLiveData<String> = handle.getLiveData<String>("Team1")
    val team1: LiveData<String> = team1Handle

    private val team2Handle: MutableLiveData<String> = handle.getLiveData<String>("Team2")
    val team2: LiveData<String> = team2Handle

    private val _toastMessage = MutableLiveData<Event<String>>()

    val toastMessage: LiveData<Event<String>>
        get() = _toastMessage

    private val imageHandle: MutableLiveData<Bitmap> = getImage(imagePath)
    val image:LiveData<Bitmap> = imageHandle

    private lateinit var teamSplitter: TeamSplitter

    private fun getImage(path: String?): MutableLiveData<Bitmap> {
        val imageLiveData = MutableLiveData<Bitmap>()
        if (!path.isNullOrEmpty()) {
            Log.d(TAG, path)
            val myBitmap = bitmapLoader.getBitmap(path)
            isPhotoAvailable=bitmapLoader.photoExist
            if (!textLines.isNullOrEmpty()) {
                teamSplitter = TeamSplitter(textLines!!, myBitmap, getApplication<Application>().applicationContext)
                imageLiveData.value = teamSplitter.inputImage
                rawTeam1Handle.value = teamSplitter.team1
                rawTeam2Handle.value = teamSplitter.team2
            } else {
                imageLiveData.value = myBitmap
            }
        } else
            Log.d(TAG, "Path Null")
        return imageLiveData
    }

    fun setDefaultEmptyImage(){
        imageHandle.value=bitmapLoader.getDefault()
    }

    fun setPathToImage(path: String) {
        imagePath = path
        val bitmap = bitmapLoader.getBitmap(path)
        isPhotoAvailable=bitmapLoader.photoExist
        imageHandle.value = bitmap
        if (isPhotoAvailable==true) {
            analyzeImage(bitmap)
        }
    }

    fun setImage(bitmap: Bitmap){//TODO tmp for testing
        imageHandle.value = bitmap
        if (isPhotoAvailable==true) {
        analyzeImage(bitmap)
        }
    }

    fun splitToTeam1() {
        if (isDataAvailable()) {
            teamSplitter.splitToTeam1()
            updateSplitValues()
        }
    }

    fun splitToTeam2() {
        if (isDataAvailable()) {
            teamSplitter.splitToTeam2()
            updateSplitValues()
        }
    }

    fun splitAuto() {
        if (isDataAvailable()) {
            teamSplitter.splitAuto()
            updateSplitValues()
        }
    }

    private fun updateSplitValues() {
        imageHandle.value = teamSplitter.inputImage
        rawTeam1Handle.value = teamSplitter.team1
        rawTeam2Handle.value = teamSplitter.team2
    }

    fun updateProcessedTeam(text: String, team: Int) {
        if (team == 1) {
            team1Handle.value = text
        } else if (team == 2) {
            team2Handle.value = text
        }
    }

    fun acceptResult() {
        team1Handle.value = RawToString().rawToTeam(rawTeam1Handle.value, getApplication<Application>().applicationContext)
        team2Handle.value = RawToString().rawToTeam(rawTeam2Handle.value, getApplication<Application>().applicationContext)
    }

    fun saveToFiles() {
        try {
            FileSaver().saveToFiles(team1Handle.value, team2Handle.value, getApplication<Application>().applicationContext)
            _toastMessage.value = Event("Files saved")
        } catch (e: IOException) {
            e.printStackTrace()
            _toastMessage.value = Event("Error, file not saved")
        }
    }


    private fun isDataAvailable(): Boolean {
        val isValid: Boolean
        if (!textLines.isNullOrEmpty() && imageHandle.value != null) {
            isValid = true
        } else if (imageHandle.value == null) {
            isValid = false
            _toastMessage.value = Event("Take inputImag first")
        } else {
            isValid = false
            _toastMessage.value = Event("Text lines not found")
        }
        return isValid
    }

    private fun analyzeImage(bitmap: Bitmap) {
        _toastMessage.value = Event("Analyzing image")
        val firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap)
        val textRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
        //val textRecognizer = FirebaseVision.getInstance().cloudTextRecognizer
        textRecognizer.processImage(firebaseVisionImage)
            .addOnSuccessListener {
                Log.d(TAG, "Success")
                textLines=LineExtractor().getValidTextLines(it,bitmap.width)
                teamSplitter = TeamSplitter(textLines!!, imageHandle.value!!, getApplication<Application>().applicationContext)
                updateSplitValues()
                _toastMessage.value = Event("Image analyzed")
            }
            .addOnFailureListener {
                _toastMessage.value = Event("Error, cannot analyze bitmap")
                Log.d(TAG, "failure")
            }
    }
}
