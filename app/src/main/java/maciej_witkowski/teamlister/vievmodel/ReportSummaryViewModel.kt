package maciej_witkowski.teamlister.vievmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import maciej_witkowski.teamlister.model.PhotoReport
import maciej_witkowski.teamlister.model.PhotoReportRepository

class ReportSummaryViewModel(app: Application) : AndroidViewModel(app) {

    private val reportRepository: PhotoReportRepository = PhotoReportRepository(app)
    val reports: LiveData<List<PhotoReport>>

    init {
        reports = reportRepository.getAllReports()
    }



}
