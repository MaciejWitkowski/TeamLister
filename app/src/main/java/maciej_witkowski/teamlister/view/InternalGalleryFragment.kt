package maciej_witkowski.teamlister.view


import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_internal_gallery.*

import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel

private val TAG = InternalGalleryFragment::class.java.simpleName

class InternalGalleryFragment : Fragment() {
    private lateinit var viewModel: TeamsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(requireActivity(), SavedStateVMFactory(requireActivity()))
            .get(TeamsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_internal_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.app_title_image_internal_gallery)
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        val width = metrics.widthPixels - rv_internal_gallery.marginLeft - rv_internal_gallery.marginRight
        Log.d(TAG, width.toString())
        val path = requireContext().filesDir
        val fileList: MutableList<String> = ArrayList()
        path.walkBottomUp().forEach {
            if (it.toString().endsWith(".jpg")) {
                fileList.add(it.toString())
            }
        }
        val reversedFileList = fileList.asReversed()//Latest photos on top
        Log.d(TAG, fileList.toString())
        val adapter =InternalGalleryRecyclerViewAdapter(reversedFileList, requireContext(), width)
        rv_internal_gallery.layoutManager = LinearLayoutManager(requireContext())
        rv_internal_gallery.adapter = adapter
        rv_internal_gallery.addItemDecoration(
            DividerItemDecoration(
                rv_internal_gallery.context,
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.onItemClick = { position ->
            viewModel.setImagePath(reversedFileList[position])
            loadFragment()
           // Toast.makeText(requireContext(), reversedFileList[position],Toast.LENGTH_LONG).show()
        }
    }

    private fun loadFragment() {
        val fragment = GalleryPhotoFragment()
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.content_frame, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }
}


