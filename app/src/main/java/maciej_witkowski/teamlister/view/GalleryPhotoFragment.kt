package maciej_witkowski.teamlister.view


import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel
import android.content.Intent
import android.content.res.Configuration
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Rational
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.fragment_photo.btn_team_1
import kotlinx.android.synthetic.main.fragment_photo.btn_team_2
import kotlinx.android.synthetic.main.fragment_photo.btn_team_auto
import kotlinx.android.synthetic.main.fragment_photo.fab_accept_photo
import kotlinx.android.synthetic.main.fragment_photo.fab_retry_photo
import kotlinx.android.synthetic.main.fragment_photo.iv_photo
import maciej_witkowski.teamlister.utils.ImageUtils

private const val REQUEST_GET_SINGLE_FILE = 1


private val TAG = GalleryPhotoFragment::class.java.simpleName

class GalleryPhotoFragment : Fragment() {
    private lateinit var viewModel: TeamsViewModel
    private lateinit var vf: ImageView
    private lateinit var container: ConstraintLayout
    private val compositeDisposable = CompositeDisposable()

    private val imageObserver =
        Observer<Bitmap> { value ->
            value?.let {
                Log.d(TAG, "Bitmap changed")
                val bitmapObservable = Observable.create(ObservableOnSubscribe<Bitmap> { emitter ->
                    emitter.onNext(rescaleBitmap(it))
                    emitter.onComplete()
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                bitmapObservable.subscribe(bitmapObserver)
            }
        }


    private var bitmapObserver: io.reactivex.Observer<Bitmap> = object : io.reactivex.Observer<Bitmap> {

        override fun onSubscribe(d: Disposable) {
            Log.d(TAG, "onSubscribe")
            compositeDisposable.add(d)
        }

        override fun onNext(bitmap: Bitmap) {
                if (Rational(bitmap.height, bitmap.width) == Rational(4, 3)){
                iv_photo.adjustViewBounds = true
                iv_photo.scaleType = ImageView.ScaleType.CENTER_CROP
            }
            else{
                    iv_photo.adjustViewBounds = false
                    iv_photo.scaleType = ImageView.ScaleType.FIT_CENTER
                }
            createPaletteAsync(bitmap)
            iv_photo.setImageBitmap(bitmap)
        }

        override fun onError(e: Throwable) {
            Log.e(TAG, "onError $e")
        }

        override fun onComplete() {
            compositeDisposable.clear()
        }
    }


    private fun rescaleBitmap(bitmap: Bitmap): Bitmap {
        return if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val height = vf.height
            val width = vf.width
            ImageUtils.rescaleBitmapToWidthHeight(bitmap, width, height)
        } else {
            val metrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
            val width=metrics.widthPixels
            ImageUtils.rescaleBitmapToWidth(bitmap, width)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), SavedStateVMFactory(requireActivity()))
            .get(TeamsViewModel::class.java)
    }

    fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            val muted = palette?.mutedSwatch
            val backgroundColor = muted?.rgb
            backgroundColor?.let { iv_photo?.let { iv_photo.setBackgroundColor(backgroundColor) } }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.toastMessage.observe(this, Observer { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })

        if (viewModel.image.value == null) {
            startGallery()
        }
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, "only portrait")
            container = view as ConstraintLayout
            vf = container.findViewById(R.id.iv_photo)
            vf.post {
                viewModel.image.observe(this, imageObserver)
            }
        } else
            viewModel.image.observe(this, imageObserver)

        fab_retry_photo.setOnClickListener { startGallery() }
        fab_accept_photo.setOnClickListener { acceptResult() }
        fab_internal_gallery.setOnClickListener { startInternalGallery() }
        btn_team_1.setOnClickListener { viewModel.splitToTeam1() }
        btn_team_auto.setOnClickListener { viewModel.splitAuto() }
        btn_team_2.setOnClickListener { viewModel.splitToTeam2() }
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GET_SINGLE_FILE)
    }

    private fun startInternalGallery() {
        (activity as MainActivity).loadFragment(InternalGalleryFragment())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_GET_SINGLE_FILE) {
                    val path = getRealPathFromURI(data)
                    Log.d(TAG, path)
                    viewModel.setImagePath(path)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "File select error", e)
        }
    }

    private fun getRealPathFromURI(data: Intent?): String {
        val selectedImage = data?.data
        val wholeId = DocumentsContract.getDocumentId(selectedImage)
        val id = wholeId.split(":")[1]
        val column = arrayOf(MediaStore.Images.Media.DATA)
        val sel = MediaStore.Images.Media._ID + "=?"

        val cursor = requireContext().contentResolver
            .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, arrayOf(id), null)
        var path = ""
        val columnIndex = cursor!!.getColumnIndex(column[0])
        if (cursor.moveToFirst()) {
            path = cursor.getString(columnIndex)
        }
        cursor.close()
        return path
    }


    private fun acceptResult() {
        viewModel.acceptResult()
    }

}
