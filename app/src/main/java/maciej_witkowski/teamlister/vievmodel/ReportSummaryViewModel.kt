package maciej_witkowski.teamlister.vievmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel;
import maciej_witkowski.teamlister.model.PhotoReport
import maciej_witkowski.teamlister.model.PhotoReportRepository

class ReportSummaryViewModel(app: Application) : AndroidViewModel(app) {

    private val reportRepository: PhotoReportRepository = PhotoReportRepository(app)
    val reports: LiveData<List<PhotoReport>>
    //val raw: List<PhotoReport>

    init {
        reports = reportRepository.getAllReports()
        //raw=reportRepository.getAllRaw()
        //Log.d("RSVM", raw.size.toString())
        //Log.d("RSVM",reports.value!!.size.toString())
    }



}
