package maciej_witkowski.teamlister.model


import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.room.Room
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import maciej_witkowski.teamlister.BuildConfig
import java.util.concurrent.CountDownLatch


class UploadWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    val TAG= "UPLOAD_WORKER"
    override fun doWork(): Result {
        val latch = CountDownLatch(1)
        var result =Result.success()
        val path =  inputData.getString("file_path")
        val bucketUrl = BuildConfig.FirebaseBucket
        val string = "file://$path"
        val uri = string.toUri()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReferenceFromUrl(bucketUrl)
        val imagesRef: StorageReference? = storageRef.child("Photo_reports/${uri.lastPathSegment}")
        Log.d(TAG, "Uri $uri")
        val uploadTask = imagesRef?.putFile(uri)
        uploadTask!!.addOnFailureListener {
            Log.d(TAG, "failure$it")
            result= Result.retry()
            latch.countDown()
        }.addOnSuccessListener {
            Log.d(TAG, "image uploaded: $it")
            if (path!=null) {
                CoroutineScope(Dispatchers.Main).launch {updateDb(path, latch)}
                latch.countDown()
            }
        }
        latch.await()
        Log.d(TAG,"worker almost done")
        return result
    }

    private suspend fun updateDb(path: String, latch: CountDownLatch) = withContext(Dispatchers.IO) {
        val db= PhotoReportDatabase.getInstance(applicationContext)
        db.photoReportDao().update(path,true)
        latch.countDown()
    }

}
