package maciej_witkowski.teamlister.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateVMFactory
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.report_summary_fragment.*

import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.vievmodel.ReportSummaryViewModel

class ReportSummaryFragment : Fragment() {


    private lateinit var viewModel: ReportSummaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ReportSummaryViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.report_summary_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPhotoReports.layoutManager = LinearLayoutManager(requireContext())
        viewModel.reports.observe(this, Observer { reports ->
            rvPhotoReports.adapter = PhotoReportRecyclerviewAdapter(reports, requireContext())
        })
        //rvPhotoReports.layoutManager = LinearLayoutManager(requireContext())
        //rvPhotoReports.adapter = PhotoReportRecyclerviewAdapter(viewModel.raw, requireContext())
    }
}
