package maciej_witkowski.teamlister.view


import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.DisplayMetrics
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
import kotlinx.android.synthetic.main.fragment_photo.*

import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel
import android.content.Intent
import android.provider.DocumentsContract
import android.provider.MediaStore

private const val REQUEST_GET_SINGLE_FILE = 1


private val TAG = GalleryFragment::class.java.simpleName

class GalleryFragment : Fragment() {
    private lateinit var viewModel: TeamsViewModel
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
            if (bitmap.height > bitmap.width) {
                iv_photo.adjustViewBounds = true
                iv_photo.scaleType = ImageView.ScaleType.CENTER_CROP
            }
            createPaletteAsync(bitmap)
            iv_photo.setImageBitmap(bitmap)

        }

        override fun onError(e: Throwable) {
            Log.e(TAG, "onError $e")
        }

        override fun onComplete() {}
    }


    private fun rescaleBitmap(bitmap: Bitmap): Bitmap {
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        val width = metrics.widthPixels
        val output: Bitmap
        output = if (bitmap.height > bitmap.width)//portrait
            Bitmap.createScaledBitmap(bitmap, width, width * 4 / 3, false)
        else    //landscape
            Bitmap.createScaledBitmap(bitmap, width, width * 3 / 4, false)
        return output

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), SavedStateVMFactory(requireActivity()))
            .get(TeamsViewModel::class.java)
    }

    fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            // Use generated instance
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
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.imageNew.observe(this, imageObserver)
        viewModel.toastMessage.observe(this, Observer { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })

        if (viewModel.imageNew.value == null) {
            startGallery()
        }
        fab_retry_photo.setOnClickListener { startGallery() }
        fab_accept_photo.setOnClickListener { acceptResult() }
        btn_team_1.setOnClickListener { viewModel.allTeam1() }
        btn_team_auto.setOnClickListener { viewModel.auto() }
        btn_team_2.setOnClickListener { viewModel.allTeam2() }
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GET_SINGLE_FILE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_GET_SINGLE_FILE) {
                    val path=getRealPathFromURI(data)
                    /*val selectedImage = data?.data
                    val wholeId = DocumentsContract.getDocumentId(selectedImage)
                    val id = wholeId.split(":")[1]
                    val column = arrayOf(MediaStore.Images.Media.DATA)
                    val sel = MediaStore.Images.Media._ID + "=?"

                    val cursor = requireContext().contentResolver
                        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, arrayOf(id), null)
                    var path = ""
                    val columnIndex = cursor!!.getColumnIndex (column[0])
                    if (cursor.moveToFirst()) {
                        path = cursor.getString(columnIndex)
                    }
                    cursor.close()*/
                        Log.d(TAG, path)
                        viewModel.setBitmap(path)
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
        val columnIndex = cursor!!.getColumnIndex (column[0])
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
