package maciej_witkowski.teamlister.view


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProviders
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_photo_report.*

import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.utils.IOnBackPressed
import maciej_witkowski.teamlister.utils.ImageUtils
import maciej_witkowski.teamlister.vievmodel.PhotoReportViewModel
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel
import java.lang.ref.WeakReference


private val TAG = PhotoReportFragment::class.java.simpleName

class PhotoReportFragment : Fragment(),IOnBackPressed {
    //TODO photo from gallery/files etc
    private lateinit var mainViewModel: TeamsViewModel
    private lateinit var viewModel: PhotoReportViewModel

    private val pathObserver = Observer<String> { path ->
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        ImageUtils.glideDefault(path, metrics, WeakReference(iv_photo_report), requireContext())
    }


    private val isoObserver = Observer<String> { iso ->
        tv_iso.text = getString(R.string.tv_iso, iso)
    }
    private val shutterObserver = Observer<String> { shutter ->
        tv_shutter.text = getString(R.string.tv_shutter, shutter)
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, SavedStateVMFactory(this)).get(PhotoReportViewModel::class.java)
        if (viewModel.path.value == null) {
            mainViewModel = ViewModelProviders.of(requireActivity(), SavedStateVMFactory(requireActivity())).get(TeamsViewModel::class.java)
            if (mainViewModel.isPhotoAvailable==true) {
                val path = mainViewModel.imagePath
                path?.let { viewModel.setPath(path) }
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_report, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.app_title_photo_report)
        btn_report.setOnClickListener {
            if (viewModel.path.value != null) {
                viewModel.sendReport()
                if (checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    loadFragment()
                } else
                    Toast.makeText(requireContext(), "Sending report", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(requireContext(), "There is no inputImag!!", Toast.LENGTH_SHORT).show()
        }
        viewModel.path.observe(this, pathObserver)
        viewModel.iso.observe(this, isoObserver)
        viewModel.shutter.observe(this, shutterObserver)
        sw_report_1.setOnCheckedChangeListener { _, _ -> checkSwitches() }
        sw_report_2.setOnCheckedChangeListener { _, _ -> checkSwitches() }
        sw_report_3.setOnCheckedChangeListener { _, _ -> checkSwitches() }
    }

    private fun loadFragment() {
        val fragment = ReportSummaryFragment()
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.content_frame, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun checkSwitches() {
        btn_report.isEnabled =
            sw_report_1.isChecked == true && sw_report_2.isChecked == true && sw_report_3.isChecked == true
    }

}
