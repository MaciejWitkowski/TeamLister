package maciej_witkowski.teamlister.view


import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.camera.core.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_camera.*
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.utils.AutoFitPreviewBuilder
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "CAMERA_FRAGMENT"
private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val PHOTO_EXTENSION = ".jpg"

class CameraFragment : Fragment() {
    private lateinit var container: ConstraintLayout
    private lateinit var viewFinder: TextureView
    private var displayId = -1
    private var preview: Preview? = null
    private var lensFacing = CameraX.LensFacing.BACK
    private var imageCapture: ImageCapture? = null
    private lateinit var viewModel: TeamsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), SavedStateVMFactory(requireActivity()))
            .get(TeamsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        container = view as ConstraintLayout
        viewFinder = container.findViewById(R.id.texturevCamera)

        viewFinder.post {
            displayId = viewFinder.display.displayId
            bindCameraUseCases()
        }

        fabShutter.setOnClickListener {
            imageCapture?.let { imageCapture ->
                val path = Environment.getExternalStorageDirectory()
                Log.d(TAG, "Path ext $path")
                val photoFile = createFile(path, FILENAME, PHOTO_EXTENSION)
                val metadata = ImageCapture.Metadata().apply {
                    // Mirror image when using the front camera
                    isReversedHorizontal = lensFacing == CameraX.LensFacing.FRONT
                }
                imageCapture.takePicture(photoFile, imageSavedListener, metadata)
            }
        }
    }

    private fun createFile(baseFolder: File, format: String, extension: String): File {
        return File(
            baseFolder,
            SimpleDateFormat(format, Locale.US).format(System.currentTimeMillis()) + extension
        )
    }

    private val imageSavedListener = object : ImageCapture.OnImageSavedListener {
        override fun onError(
            error: ImageCapture.UseCaseError, message: String, exc: Throwable?
        ) {
            Log.e(TAG, "Photo capture failed: $message")
            exc?.printStackTrace()
        }

        override fun onImageSaved(photoFile: File) {
            Log.d(TAG, "Photo capture succeeded: ${photoFile.absolutePath}")
            Toast.makeText(requireContext(), "it", Toast.LENGTH_SHORT).show()
            CameraX.unbindAll()
            val path = photoFile.absolutePath
            viewModel.setBitmap(path)


            val fragment = ViewPagerFragment()
            val ft = fragmentManager!!.beginTransaction()
            ft.replace(R.id.contentFrame, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }
    }


    private fun bindCameraUseCases() {

        CameraX.unbindAll()
        val height = viewFinder.height
        val width = viewFinder.width
        val size = if (height > width)
            Size(2448, 3264)
        else
            Size(3264, 2448)
        val aspectRatio = if (height > width)
            Rational(3, 4)
        else
            Rational(4, 3)

        val screenSize = Size(width, height)
        //val aspectRatio = Rational(300, 400) TODO bug report https://groups.google.com/a/android.com/forum/#!forum/camerax-developers corrupted exif when aspect ratio is portrait and camera orient landscape
        val previewAspectRatio = Rational(height, width)
        Log.d(TAG, width.toString())
        Log.d(TAG, previewAspectRatio.toString())
        val viewFinderConfig = PreviewConfig.Builder().apply {
            setLensFacing(lensFacing)
            setTargetResolution(screenSize)
            setTargetAspectRatio(aspectRatio)
            setTargetRotation(viewFinder.display.rotation)
        }.build()

        // Use the auto-fit preview builder to automatically handle size and orientation changes
        val preview = AutoFitPreviewBuilder.build(viewFinderConfig, viewFinder)

        // Set up the capture use case to allow users to take photos
        val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
            setLensFacing(lensFacing)
            setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY)
            setTargetResolution(size)
            setTargetAspectRatio(aspectRatio)
            setTargetRotation(viewFinder.display.rotation)
        }.build()



        imageCapture = ImageCapture(imageCaptureConfig)

        CameraX.bindToLifecycle(this, preview, imageCapture)
    }
}
