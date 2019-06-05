package maciej_witkowski.teamlister.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_photo.*
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProviders
import com.mindorks.paracamera.Camera
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel
import maciej_witkowski.teamlister.R


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
        viewModel = ViewModelProviders.of(requireActivity(), SavedStateVMFactory(requireActivity()))
            .get(TeamsViewModel::class.java)
        //val test = getString(maciej_witkowski.teamlister.R.string.toastAnalyzed)

        Log.d("ORIENT", requireActivity().getResources().getConfiguration().orientation.toString())//1 land, 2 horizontal
        if(viewModel.imageNew.value!=null)
        Log.d( "ORIENT","h: "+ viewModel.imageNew.value?.height.toString()+ " w: "+viewModel.imageNew.value?.width.toString())

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
            it.getContentIfNotHandled()?.let { // Only proceed if the event has never been handled
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Image from camera"
        cameraInit()
        fabRetry.setOnClickListener { takePicture() }
        fabAccept.setOnClickListener { acceptResult() }
        btnTeam1Picker.setOnClickListener { viewModel.allTeam1() }
        btnAuto.setOnClickListener { viewModel.auto() }
        btnTeam2Picker.setOnClickListener { viewModel.allTeam2() }
            if (viewModel.imageNew.value==null){
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
            //.setImageHeight(1500)
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
                val path=camera.cameraBitmapPath
                val bitmap = camera.cameraBitmap
                if (bitmap != null&&path!=null) {
                    viewModel.setBitmap(bitmap,path)
                    Toast.makeText(requireContext(), "Analyzing image", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Picture not taken", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
