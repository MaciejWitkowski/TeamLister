package maciej_witkowski.teamlister.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.report_summary_fragment.*

import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.vievmodel.ReportSummaryViewModel
private const val TAG="RSF"
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
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        val width = metrics.widthPixels-rv_photo_reports.marginLeft-rv_photo_reports.marginRight
        Log.d(TAG, width.toString())
        //rvPhotoReports.layoutManager = LinearLayoutManager(requireContext())
        viewModel.reports.observe(this, Observer { reports ->
            rv_photo_reports.layoutManager = LinearLayoutManager(requireContext())
            rv_photo_reports.adapter = ReportSummaryRecyclerViewAdapter(reports, requireContext(), width)
            rv_photo_reports.addItemDecoration(
                DividerItemDecoration(
                    rv_photo_reports.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        })


    }
}
