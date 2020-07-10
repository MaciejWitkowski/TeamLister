package maciej_witkowski.teamlister.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_view_pager.*

import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.utils.IOnBackPressed
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel

class PhotoViewPagerFragment : Fragment(),IOnBackPressed {
    private lateinit var viewModel: TeamsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), SavedStateVMFactory(requireActivity())).get(TeamsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.app_title_image_camera)
        val fragmentAdapter = PhotoViewPagerAdapter(childFragmentManager)
        vp_main.adapter = fragmentAdapter
    }

    override fun onBackPressed(): Boolean {
        return if (vp_main.currentItem==1){
            vp_main.currentItem = 0
            true
        } else {
            false
        }
    }

}
