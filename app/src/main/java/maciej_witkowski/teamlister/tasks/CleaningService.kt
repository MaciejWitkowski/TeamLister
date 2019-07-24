package maciej_witkowski.teamlister.tasks

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import maciej_witkowski.teamlister.model.PhotoReportDatabase

private val TAG  = CleaningService::class.java.simpleName
private const val DAY=86400*1000

class CleaningService : Service() {


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.IO).launch {cleanOldFiles()}
        return super.onStartCommand(intent, flags, startId)
    }


    private fun cleanOldFiles(){
        val sharedPref = getDefaultSharedPreferences(baseContext)
        val prefValue = sharedPref.getString("cleanup_time","0")
        val olderThan=prefValue?.toInt()
        if (olderThan!=null&&olderThan!=0){
            val protectedPaths=getReportedPathsFromDb()
            val unixTime = System.currentTimeMillis()
            val path = baseContext.filesDir
            path.walkBottomUp().forEach {
                if (it.toString().endsWith(".jpg")) {
                    if (!protectedPaths.contains(it.absolutePath)) {
                        if (it.lastModified() + olderThan * DAY < unixTime) {
                         //   it.delete()//TODO temporary check
                            Log.d(TAG, it.toString())
                        }
                    }
                }
            }
        }
        stopSelf()
    }

    private fun getReportedPathsFromDb():List<String>{
        val db= PhotoReportDatabase.getInstance(baseContext)
        return db.photoReportDao().getPaths()
    }


}
