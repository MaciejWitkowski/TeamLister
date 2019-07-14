package maciej_witkowski.teamlister.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room

class PhotoReportRepository(application: Application) {

    private val reportsLiveData: LiveData<List<PhotoReport>>

    init {
        val db= PhotoReportDatabase.getInstance(application)
        reportsLiveData = db.photoReportDao().getAll()
    }

    fun getAllReports(): LiveData<List<PhotoReport>> {
        return reportsLiveData
    }
}