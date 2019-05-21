package maciej_witkowski.teamlister.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_processed_team.*

import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel

private const val TAG= "ListFragment"
class ListFragment : Fragment() {
    private lateinit var viewModel: TeamsViewModel

    private val teamObserver =
        Observer<String> { value -> value?.let {
            tvTeam.setText(value)
        } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "on create")
        viewModel = ViewModelProviders.of(requireActivity(), SavedStateVMFactory(requireActivity())).get(TeamsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "on create view")
        return inflater.inflate(R.layout.fragment_processed_team, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "on view created")
        viewModel.team1.observe(this, teamObserver)
        btnTeam1.setOnClickListener {
            viewModel.team2.removeObserver(teamObserver)
            viewModel.team1.observe(this,teamObserver) }
        btnTeam2.setOnClickListener {
            viewModel.team1.removeObserver(teamObserver)
            viewModel.team2.observe(this,teamObserver) }
        btnSave.setOnClickListener { viewModel.saveToFiles() }
    }


}
