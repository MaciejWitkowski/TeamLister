package maciej_witkowski.teamlister.view


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProviders
import androidx.work.*
import com.bumptech.glide.Glide
import android.util.DisplayMetrics
import com.bumptech.glide.request.RequestOptions
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_photo_report.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.model.BitmapWithExif
import maciej_witkowski.teamlister.model.PhotoReport
import maciej_witkowski.teamlister.model.PhotoReportDatabase
import maciej_witkowski.teamlister.model.UploadWorker
import maciej_witkowski.teamlister.utils.ImageUtils
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel


private const val TAG = "PHOTO_REPORT_FRAGMENT"


class PhotoReportFragment : Fragment() {
    //TODO photo from gallery/files etc
    private lateinit var viewModel: TeamsViewModel
    private lateinit var path: String

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(requireActivity(), SavedStateVMFactory(requireActivity()))
            .get(TeamsViewModel::class.java)
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
        btnReport.setOnClickListener {
            sendReport()
            loadFragment()
        }

        swReport1.setOnCheckedChangeListener { _, _ -> checkSwitches() }
        swReport2.setOnCheckedChangeListener { _, _ -> checkSwitches() }
        swReport3.setOnCheckedChangeListener { _, _ -> checkSwitches() }

        val tmp = viewModel.imagePathHandle.value
        if (tmp != null) {
            path=tmp
            Log.d(TAG, tmp)
            val metrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
            val exif = ExifInterface(path)
            val iso = exif.getAttribute(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY)
            tvIso.text = getString(R.string.iso, iso)
            val exposureTime = exif.getAttributeDouble(ExifInterface.TAG_EXPOSURE_TIME, 0.0)
            tvShutter.text = getString(R.string.shutter, timeToFraction(exposureTime))

            val width = metrics.widthPixels
            path = tmp
            Glide.with(requireContext())
                .load(path)
                .apply( RequestOptions().override(width, (width*4/3)))
                .into(ivPhotoReport);
        }
    }

    private fun loadFragment(){
        val fragment=ReportSummaryFragment()
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.contentFrame, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun checkSwitches() {
        btnReport.isEnabled = swReport1.isChecked == true && swReport2.isChecked == true && swReport3.isChecked == true
    }

    private fun timeToFraction(time: Double): String {
        return if (time > 1)
            time.toString()
        else {
            val denominator = 1 / time
            "1/$denominator"
        }
    }


    private suspend fun updateDb() = withContext(Dispatchers.IO) {
        val db= PhotoReportDatabase.getInstance(activity!!.applicationContext)
        //db.photoReportDao().insertPhotoReport(PhotoReport(null,path,false,null))
        db.photoReportDao().insertPhotoReport(PhotoReport(null,path,false))
        }

private fun sendReport() {
        if (::path.isInitialized) {
            val uploadWork = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            val data = Data.Builder()
            val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
            data.putString("file_path", path)
            uploadWork.setInputData(data.build())
            uploadWork.setConstraints(constraints)
            WorkManager.getInstance().enqueue(uploadWork.build())
            val uiScope = CoroutineScope(Dispatchers.Main)
            uiScope.launch {updateDb()}
        } else {
            Toast.makeText(requireContext(), "There is no image!!", Toast.LENGTH_SHORT).show()
        }
    }

}
