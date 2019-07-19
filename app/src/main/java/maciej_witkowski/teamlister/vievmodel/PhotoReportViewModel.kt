package maciej_witkowski.teamlister.vievmodel

import android.app.Application
import androidx.annotation.NonNull
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.*
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import maciej_witkowski.teamlister.model.PhotoReport
import maciej_witkowski.teamlister.model.PhotoReportDatabase
import maciej_witkowski.teamlister.tasks.UploadWorker

class PhotoReportViewModel(app: Application,handle: SavedStateHandle) : AndroidViewModel(app) {
    @NonNull
    override fun <T : Application> getApplication(): T {
        return super.getApplication()
    }

    private val pathHandle: MutableLiveData<String> =
        handle.getLiveData<String>("path")
    val path: LiveData<String> = pathHandle
    private val isoHandle: MutableLiveData<String> =
        handle.getLiveData<String>("iso")
    val iso: LiveData<String> = isoHandle
    private val shutterHandle: MutableLiveData<String> =
        handle.getLiveData<String>("shutter")
    val shutter: LiveData<String> = shutterHandle

    fun setPath(path: String) {
        pathHandle.value = path
        setExifData(path)
    }

    fun sendReport() {
        val uploadWork = OneTimeWorkRequest.Builder(UploadWorker::class.java)
        val data = Data.Builder()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        data.putString("file_path", pathHandle.value)
        uploadWork.setInputData(data.build())
        uploadWork.setConstraints(constraints)
        WorkManager.getInstance().enqueue(uploadWork.build())
        val uiScope = CoroutineScope(Dispatchers.Main)
        uiScope.launch { updateDb() }
    }

    private fun setExifData(path: String) {
        val exif = ExifInterface(path)
        val iso = exif.getAttribute(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY)
        val exposureTime = exif.getAttributeDouble(ExifInterface.TAG_EXPOSURE_TIME, 0.0)
        isoHandle.value = iso
        shutterHandle.value = timeToFraction(exposureTime)
    }


    private fun timeToFraction(time: Double): String {
        return if (time > 1)
            time.toString()
        else {
            val denominator = 1 / time
            "1/$denominator"
        }
    }

    private suspend fun updateDb() = withContext(Dispatchers.IO) {
        val db = PhotoReportDatabase.getInstance(getApplication())
        val pathCurrent=pathHandle.value
        pathCurrent?.let{
            db.photoReportDao().insertPhotoReport(PhotoReport(null, pathCurrent, false, null))
        }
    }
}



