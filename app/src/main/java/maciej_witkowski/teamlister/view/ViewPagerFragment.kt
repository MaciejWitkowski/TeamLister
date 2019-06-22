package maciej_witkowski.teamlister.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.android.synthetic.main.fragment_view_pager.*

import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel

class ViewPagerFragment : Fragment() {
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
        if(viewModel.imageNew.value==null){
            startCamera()
        }
        val fragmentAdapter = MyPagerAdapter(childFragmentManager)
        vp_main.adapter = fragmentAdapter
    }

    private fun startCamera(){
        val fragment=CameraFragment()
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(maciej_witkowski.teamlister.R.id.contentFrame, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }
}
