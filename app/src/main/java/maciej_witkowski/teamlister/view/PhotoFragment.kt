package maciej_witkowski.teamlister.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.graphics.Bitmap
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_photo.*
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel

private const val TAG = "PHOTO_FRAGMENT"
class PhotoFragment : Fragment() {
    private lateinit var viewModel: TeamsViewModel

    private val imageObserver =
        Observer<Bitmap> { value ->
            value?.let {
                ivPhoto.setImageBitmap(it)


            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), SavedStateVMFactory(requireActivity())).get(TeamsViewModel::class.java)
    }

    private fun startCamera(){
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
        viewModel.imageNew.observe(this, imageObserver)
        viewModel.toastMessage.observe(this, Observer { it ->
            it.getContentIfNotHandled()?.let { // Only proceed if the event has never been handled
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Image from camera"

        if(viewModel.imageNew.value==null){
            startCamera()
        }
        fabRetry.setOnClickListener { startCamera()}
        fabAccept.setOnClickListener { acceptResult() }
        btnTeam1Picker.setOnClickListener { viewModel.allTeam1() }
        btnAuto.setOnClickListener { viewModel.auto() }
        btnTeam2Picker.setOnClickListener { viewModel.allTeam2() }
    }
        private fun acceptResult() {
            viewModel.acceptResult()
        }
    }


