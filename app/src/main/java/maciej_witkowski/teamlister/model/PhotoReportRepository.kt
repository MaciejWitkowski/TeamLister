package maciej_witkowski.teamlister.model

import android.app.Application
import androidx.lifecycle.LiveData

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