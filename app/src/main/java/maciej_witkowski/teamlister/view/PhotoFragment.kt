package maciej_witkowski.teamlister.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProviders
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_photo.*
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel

import androidx.palette.graphics.Palette
import io.reactivex.disposables.CompositeDisposable

private val TAG  = PhotoFragment::class.java.simpleName

class PhotoFragment : Fragment() {
    private lateinit var viewModel: TeamsViewModel
    private val compositeDisposable = CompositeDisposable()

    private val imageObserver =
        Observer<Bitmap> { value ->
            value?.let {
                val bitmapObservable = Observable.create(ObservableOnSubscribe<Bitmap> { emitter ->
                    emitter.onNext(rescaleBitmap(it))
                    emitter.onComplete()
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                bitmapObservable.subscribe(bitmapObserver)
            }
        }


    private var bitmapObserver: io.reactivex.Observer<Bitmap> = object : io.reactivex.Observer<Bitmap> {

        override fun onSubscribe(d: Disposable) {
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

        override fun onComplete() {
            compositeDisposable.clear()
        }
    }

    fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            // Use generated instance
            val muted = palette?.mutedSwatch
            val backgroundColor = muted?.rgb
            backgroundColor?.let { iv_photo?.let{iv_photo.setBackgroundColor(backgroundColor)}}

        }
    }


    private fun rescaleBitmap(bitmap: Bitmap): Bitmap {
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        val width = metrics.widthPixels
        val output: Bitmap
        if (bitmap.height > bitmap.width)//portrait
            output = Bitmap.createScaledBitmap(bitmap, width, width * 4 / 3, false)
        else    //landscape
            output = Bitmap.createScaledBitmap(bitmap, width, width * 3 / 4, false)
        return output

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), SavedStateVMFactory(requireActivity()))
            .get(TeamsViewModel::class.java)
    }

    private fun startCamera() {
        (activity as MainActivity).loadFragment(CameraFragment())
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
        viewModel.image.observe(this, imageObserver)
        viewModel.toastMessage.observe(this, Observer { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })

        if (viewModel.image.value == null) {
            startCamera()
        }
        fab_retry_photo.setOnClickListener { startCamera() }
        fab_accept_photo.setOnClickListener { acceptResult() }
        btn_team_1.setOnClickListener { viewModel.splitToTeam1() }
        btn_team_auto.setOnClickListener { viewModel.splitAuto() }
        btn_team_2.setOnClickListener { viewModel.splitToTeam2() }
    }

    private fun acceptResult() {
        viewModel.acceptResult()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}


