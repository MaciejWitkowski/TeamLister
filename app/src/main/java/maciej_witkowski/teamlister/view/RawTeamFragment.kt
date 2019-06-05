package maciej_witkowski.teamlister.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_raw_team.*
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel


class RawTeamFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_raw_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvRawTeam1.layoutManager = LinearLayoutManager(requireContext())
        viewModel.rawTeam1.observe(this, Observer { team1 ->
            rvRawTeam1.adapter = RawTeamAdapter(team1, requireContext())
            checkTeamAvailability()
        })
        rvRawTeam2.layoutManager = LinearLayoutManager(requireContext())
        viewModel.rawTeam2.observe(this, Observer { team2 ->
            rvRawTeam2.adapter = RawTeamAdapter(team2, requireContext())
            checkTeamAvailability()
        })
    }

    private fun checkTeamAvailability() {
        if (viewModel.rawTeam1.value.isNullOrEmpty() && viewModel.rawTeam2.value.isNullOrEmpty()) {
            tvRawTeam1.isVisible = false
            rvRawTeam1.isVisible = false
            tvRawTeam2.isVisible = false
            rvRawTeam2.isVisible = false
            tvRawTeamNa.isVisible = true
        } else {
            tvRawTeam1.isVisible = !viewModel.rawTeam1.value.isNullOrEmpty()
            rvRawTeam1.isVisible = !viewModel.rawTeam1.value.isNullOrEmpty()
            tvRawTeam2.isVisible = !viewModel.rawTeam2.value.isNullOrEmpty()
            rvRawTeam2.isVisible = !viewModel.rawTeam2.value.isNullOrEmpty()
            tvRawTeamNa.isVisible = false
        }
    }


}
