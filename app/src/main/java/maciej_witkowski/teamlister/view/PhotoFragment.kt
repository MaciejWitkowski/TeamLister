package maciej_witkowski.teamlister.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_photo.*
import maciej_witkowski.teamlister.R
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProviders
import com.mindorks.paracamera.Camera
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class PickResultFragment : Fragment() {
    private lateinit var viewModel: TeamsViewModel
    private lateinit var camera: Camera

    private val imageObserver =
        Observer<Bitmap> { value ->
            value?.let {
                ivPhoto.setImageBitmap(it)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var app = requireActivity().getApplication()
        viewModel = ViewModelProviders.of(requireActivity(), SavedStateVMFactory(requireActivity()))
            .get(TeamsViewModel::class.java)
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
        cameraInit()
        fabRetry.setOnClickListener { takePicture() }
        fabAccept.setOnClickListener { acceptResult() }
        btnTeam1Picker.setOnClickListener { viewModel.allTeam1() }
        btnSeparator.setOnClickListener { viewModel.auto() }
        btnTeam2Picker.setOnClickListener { viewModel.allTeam2() }
        if (viewModel.image.value==null){
            takePicture()
        }
    }


    private fun cameraInit() {
        camera = Camera.Builder()
            .resetToCorrectOrientation(true)
            .setTakePhotoRequestCode(Camera.REQUEST_TAKE_PHOTO)
            .setDirectory("pics")
            .setName("teamLister_${System.currentTimeMillis()}")
            .setImageFormat(Camera.IMAGE_JPEG)
            .setCompression(75)
            .build(this)
    }

    private fun acceptResult() {
        viewModel.acceptResult()
    }

    private fun takePicture() {
        try {
            camera.takePicture()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
                val bitmap = camera.cameraBitmap
                if (bitmap != null) {
                    viewModel.setBitmap(bitmap)
                } else {
                    Toast.makeText(requireContext(), "picutre not taken", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
