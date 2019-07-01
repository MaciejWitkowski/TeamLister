package maciej_witkowski.teamlister.view


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_photo_report.*
import maciej_witkowski.teamlister.BuildConfig

import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.model.BitmapWithExif
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
        }

        swReport1.setOnCheckedChangeListener { _, _ -> checkSwitches() }
        swReport2.setOnCheckedChangeListener { _, _ -> checkSwitches() }
        swReport3.setOnCheckedChangeListener { _, _ -> checkSwitches() }

        val tmp = viewModel.imagePathHandle.value
        if (tmp != null) {
            path = tmp
            bitmapObservable.subscribe(bitmapObserver)
        }
    }

    private fun checkSwitches() {
        btnReport.isEnabled = swReport1.isChecked == true && swReport2.isChecked == true && swReport3.isChecked == true
    }

    private var bitmapObservable = Observable.create(ObservableOnSubscribe<BitmapWithExif> { emitter ->
        emitter.onNext(getBitmapWithExif(path))
        emitter.onComplete()
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    private var bitmapObserver: io.reactivex.Observer<BitmapWithExif> = object :
        Observer<BitmapWithExif> {

        override fun onSubscribe(d: Disposable) {
            Log.e(TAG, "onSubscribe" + Thread.currentThread().name)
        }

        override fun onNext(bitmapWithExif: BitmapWithExif) {
            ivPhotoReport?.let {ivPhotoReport.setImageBitmap(bitmapWithExif.bitmap)}
            ivPhotoReport?.let{tvIso.text = getString(R.string.iso, bitmapWithExif.iso)}
            tvShutter?.let{tvShutter.text = getString(R.string.shutter, bitmapWithExif.shutter)}
        }

        override fun onError(e: Throwable) {
            Log.e(TAG, "onError complete: ")
        }

        override fun onComplete() {}
    }


    private fun getBitmapWithExif(path: String): BitmapWithExif {
        val exif = ExifInterface(path)
        val rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val rotationInDegrees = ImageUtils.exifToDegrees(rotation)
        var myBitmap = BitmapFactory.decodeFile(path)
        val matrix = Matrix()
        var height = myBitmap.height
        var width = myBitmap.width
        if (height > 4096)
            height = 4096
        if (width > 4096)
            width = 4096
        if (rotation != 0) {
            matrix.preRotate(rotationInDegrees)
            myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, width, height, matrix, true)
        } else if (myBitmap.height > 4096 || myBitmap.width > 4096) {
            myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.width, myBitmap.height)
        }
        val iso = exif.getAttribute(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY)
        val exposureTime = exif.getAttributeDouble(ExifInterface.TAG_EXPOSURE_TIME, 0.0)
        return if (iso != null)
            BitmapWithExif(myBitmap, iso, timeToFraction(exposureTime))
        else
            BitmapWithExif(myBitmap, "error", timeToFraction(exposureTime))
    }

    private fun timeToFraction(time: Double): String {
        return if (time > 1)
            time.toString()
        else {
            val denominator = 1 / time
            "1/$denominator"
        }
    }


    private fun sendReport() {//TODO should be moved to external service and logged on device at success
        if (::path.isInitialized) {
            val bucketUrl= BuildConfig.FirebaseBucket
            val string = "file://$path"
            val uri = string.toUri()
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.getReferenceFromUrl(bucketUrl)
            val imagesRef: StorageReference? = storageRef.child("Photo_reports/${uri.lastPathSegment}")
            Log.d(TAG, "Uri $uri")
            val uploadTask = imagesRef?.putFile(uri)
            uploadTask!!.addOnFailureListener {
                // Handle unsuccessful uploads
                Log.d(TAG, "failure$it")
            }.addOnSuccessListener {
                Log.d(TAG, "image uploaded$it")
            }
        }
    }

}
