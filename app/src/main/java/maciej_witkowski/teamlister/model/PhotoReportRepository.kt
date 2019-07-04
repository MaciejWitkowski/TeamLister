package maciej_witkowski.teamlister.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room

class PhotoReportRepository(application: Application) {

  //  private val photoReportDao: PhotoReportDao
    private val reportsLiveData: LiveData<List<PhotoReport>>
    private var reportsRaw: List<PhotoReport> = arrayListOf()

    init {
        val db= PhotoReportDatabase.getInstance(application)
        reportsLiveData = db.photoReportDao().getAll()
    }

    fun getAllReports(): LiveData<List<PhotoReport>> {
        return reportsLiveData
    }
/*
    fun getAllRaw():List<PhotoReport>{
        return reportsRaw
    }*/
}