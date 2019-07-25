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
        viewModel = ViewModelProviders.of(requireActivity(), SavedStateVMFactory(requireActivity())).get(TeamsViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_raw_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rw_team_raw_1.layoutManager = LinearLayoutManager(requireContext())
        viewModel.rawTeam1.observe(this, Observer { team1 ->
            rw_team_raw_1.adapter = RawTeamRecyclerViewAdapter(team1, requireContext())
            checkTeamAvailability()
        })
        rw_team_raw_2.layoutManager = LinearLayoutManager(requireContext())
        viewModel.rawTeam2.observe(this, Observer { team2 ->
            rw_team_raw_2.adapter = RawTeamRecyclerViewAdapter(team2, requireContext())
            checkTeamAvailability()
        })
    }

    private fun checkTeamAvailability() {
        if (viewModel.rawTeam1.value.isNullOrEmpty() && viewModel.rawTeam2.value.isNullOrEmpty()) {
            tv_team_raw_1.isVisible = false
            rw_team_raw_1.isVisible = false
            tv_team_raw_2.isVisible = false
            rw_team_raw_2.isVisible = false
            tv_team_raw_na.isVisible = true
            divider.isVisible = false
        } else {
            tv_team_raw_1.isVisible = !viewModel.rawTeam1.value.isNullOrEmpty()
            rw_team_raw_1.isVisible = !viewModel.rawTeam1.value.isNullOrEmpty()
            tv_team_raw_2.isVisible = !viewModel.rawTeam2.value.isNullOrEmpty()
            rw_team_raw_2.isVisible = !viewModel.rawTeam2.value.isNullOrEmpty()
            tv_team_raw_na.isVisible = false
            divider.isVisible = !viewModel.rawTeam2.value.isNullOrEmpty() && !viewModel.rawTeam1.value.isNullOrEmpty()
        }
    }


}
